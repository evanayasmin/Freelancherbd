package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.dto.ChatMessage;
import com.evanadev.freelancherbd.model.ChatMessageEntity;
import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.repository.ChatMessageRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRestController {

    private final ChatMessageRepository chatMessageRepository;

    public ChatRestController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/chat/history/{userId}")
    @ResponseBody
    public List<ChatMessage> loadChatHistory(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetail loggedUser) {

        List<ChatMessageEntity> messages =
                chatMessageRepository.findChatHistory(
                        loggedUser.getId(),
                        userId
                );

        return messages.stream().map(m -> {
            ChatMessage dto = new ChatMessage();
            dto.setSenderId(m.getSenderId());
            dto.setReceiverId(m.getReceiverId());
            dto.setSender(m.getSenderUsername());
            dto.setReceiver(m.getReceiverUsername());
            dto.setContent(m.getContent());
            return dto;
        }).toList();
    }

}
