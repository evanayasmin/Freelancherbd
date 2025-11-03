package com.evanadev.freelancherbd.dto;

import java.time.LocalDateTime;

public class NotificationDTO {

    private String title;
    private String message;
    private String type;
    private String sender;
    private Long receiver;
    private LocalDateTime createdAt;

    public NotificationDTO() {
    }

    public NotificationDTO(String title, String message, String type, String sender, Long receiver, LocalDateTime createdAt) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
