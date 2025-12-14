package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessage;
import com.evanadev.freelancherbd.model.ChatMessageEntity;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.repository.ChatMessageRepository;
import com.evanadev.freelancherbd.service.ChatService;
import com.evanadev.freelancherbd.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final AESUtil aesUtil;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, ChatMessageRepository chatMessageRepository, AESUtil aesUtil) {
        this.simpMessagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.chatMessageRepository = chatMessageRepository;
        this.aesUtil = aesUtil;
    }

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(ChatMessage message, Principal principal) {

        ChatMessageEntity entity = new ChatMessageEntity();

        entity.setSenderId(message.getSenderId());
        entity.setSenderUsername(message.getSender());
        entity.setReceiverId(message.getReceiverId());
        entity.setReceiverUsername(message.getReceiver());
        entity.setContent(message.getContent());
        entity.setCreatedAt(LocalDateTime.now());

        chatMessageRepository.save(entity);

        // Set sender from logged-in user
        message.setSender(principal.getName());

        // Save message to DB
        chatService.saveMessage(message);

        // Send to receiver only
        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/notifications",
                message
        );

        simpMessagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/notifications",
                message
        );
    }
    @GetMapping("/chat/history")
    public List<ChatMessage> getHistory(Long user1, Long user2) {

        return chatMessageRepository.findConversation(user1, user2)
                .stream()
                .map(e -> {
                    ChatMessage dto = new ChatMessage();
                    dto.setSenderId(e.getSenderId());
                    dto.setSender(e.getSenderUsername());
                    dto.setReceiverId(e.getReceiverId());
                    dto.setReceiver(e.getReceiverUsername());
                    dto.setContent(e.getContent());
                    dto.setTimestamp(e.getCreatedAt().toString());
                    return dto;
                })
                .toList();
    }
}
