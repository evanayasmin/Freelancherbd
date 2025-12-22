package com.evanadev.freelancherbd.dto;

public class ChatMessageResponse {

    private String sender;        // username
    private String content;
    private String timestamp;

    public ChatMessageResponse(String sender, String content) {
        this.sender = sender;
        this.content = content;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
