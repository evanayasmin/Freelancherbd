package com.evanadev.freelancherbd.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {

    private String sender;        // username
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageResponse(String sender, String content, LocalDateTime createdAt) {
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }

    public ChatMessageResponse() {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
