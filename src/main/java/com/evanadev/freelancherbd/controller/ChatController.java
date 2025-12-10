package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessage;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void sendPrivateMessage(ChatMessage message, Principal principal) {

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
}
