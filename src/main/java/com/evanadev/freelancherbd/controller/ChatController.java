package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessageRequest;
import com.evanadev.freelancherbd.dto.ChatMessageResponse;
import com.evanadev.freelancherbd.model.ChatMessageEntity;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.repository.ChatMessageRepository;
import com.evanadev.freelancherbd.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final AESUtil aesUtil;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository chatMessageRepository,
                          AESUtil aesUtil) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.aesUtil = aesUtil;
    }

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(ChatMessageRequest request, Principal principal) {

        // Sender from session (SECURE)
        String senderUsername = principal.getName();

        //  Receiver from encrypted key
        Long receiverId = aesUtil.decryptId(request.getReceiverKey());

        // Save entity
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderUsername(senderUsername);
        entity.setSenderId(null); // optional if you resolve later
        entity.setReceiverId(receiverId);
        entity.setContent(request.getContent());

        chatMessageRepository.save(entity);

        // Build response DTO
        ChatMessageResponse response = toResponse(entity);

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                getUsernameById(receiverId),
                "/queue/notifications",
                response
        );

        // Echo to sender
        messagingTemplate.convertAndSendToUser(
                senderUsername,
                "/queue/notifications",
                response
        );
    }

    @GetMapping("/chat/history/{receiverKey}")
    @ResponseBody
    public List<ChatMessageResponse> loadChatHistory(
            @PathVariable String receiverKey,
            @AuthenticationPrincipal CustomUserDetail loggedUser) {

        Long receiverId = aesUtil.decryptId(receiverKey);

        List<ChatMessageEntity> messages =
                chatMessageRepository.findChatHistory(
                        loggedUser.getId(),
                        receiverId
                );

        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    // Helper mapper
    private ChatMessageResponse toResponse(ChatMessageEntity entity) {

        ChatMessageResponse dto = new ChatMessageResponse();
        dto.setSender(entity.getSenderUsername());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getCreatedAt().toString());

        return dto;
    }

    // Resolve username safely (example)
    private String getUsernameById(Long userId) {
        // fetch from UserRepository
        return "receiverUsername";
    }
}

