package com.evanadev.freelancherbd.dto;

public class ChatMessageRequest {
    private String receiverKey;   // encrypted user id
    private String content;

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
