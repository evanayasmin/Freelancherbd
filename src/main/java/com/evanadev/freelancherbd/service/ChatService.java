package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.dto.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final List<ChatMessage> messages = new ArrayList<>();

    public void saveMessage(ChatMessage message) {
        messages.add(message);
    }

    public List<ChatMessage> getChatHistory(String user1, String user2) {
        List<ChatMessage> history = new ArrayList<>();
        for (ChatMessage m : messages) {
            if (
                    (m.getSender().equals(user1) && m.getReceiver().equals(user2)) ||
                            (m.getSender().equals(user2) && m.getReceiver().equals(user1))
            ) {
                history.add(m);
            }
        }
        return history;
    }
}
