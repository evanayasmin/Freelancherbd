package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessageRequest;
import com.evanadev.freelancherbd.dto.ChatMessageResponse;
import com.evanadev.freelancherbd.model.Category;
import com.evanadev.freelancherbd.model.ChatMessageEntity;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.ChatMessageRepository;
import com.evanadev.freelancherbd.repository.UserRepository;
import com.evanadev.freelancherbd.service.UserService;
import com.evanadev.freelancherbd.util.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final AESUtil aesUtil;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository chatMessageRepository, UserService userService,
                          AESUtil aesUtil) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
        this.aesUtil = aesUtil;
    }

    @GetMapping("/chat")
    public String chat_open(HttpServletRequest request, Model model){
        model.addAttribute("currentPath", request.getRequestURI());
        return "chat_page";
    }


    @MessageMapping("/chat.send")
    public void sendPrivateMessage(ChatMessageRequest request, Principal principal) {

        // Sender from session (SECURE)
        String senderUsername = principal.getName();
        String receiverUsername = request.getReceiverUsername();
        User sender = userService.findByUsername(senderUsername);

        //  Receiver from encrypted key
        User receiver = userService.findByUsername(receiverUsername);

        //System.out.println("Sender principal: " + senderUsername);
        //System.out.println("Receiver username: " + receiver.getUsername());

        // Save entity
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderUsername(senderUsername);
        entity.setSenderId(sender.getId()); // optional if you resolve later
        entity.setReceiverId(receiver.getId());
        entity.setReceiverUsername(receiverUsername);
        entity.setContent(request.getContent());

        chatMessageRepository.save(entity);

        // Build response DTO
        ChatMessageResponse response = toResponse(entity);

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                receiverUsername,
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

    @GetMapping("/chat/history/{username}")
    @ResponseBody
    public List<ChatMessageResponse> loadChatHistory(@PathVariable String username, Principal principal) {

        String me = principal.getName();

        return chatMessageRepository
                .findConversation(me, username)
                .stream()
                .map(m -> new ChatMessageResponse(
                        m.getSenderUsername(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .toList();
    }

    // Helper mapper
    private ChatMessageResponse toResponse(ChatMessageEntity entity) {

        ChatMessageResponse dto = new ChatMessageResponse();
        dto.setSender(entity.getSenderUsername());
        dto.setContent(entity.getContent());
        //dto.setTimestamp(entity.getCreatedAt().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // Resolve username safely
    private String getUsernameById(Long userId) {
        // fetch from UserRepository
        User user = userService.findUserDetailById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUsername();
    }
}

