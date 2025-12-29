package com.evanadev.freelancherbd.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PresenceEventListener {

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        System.out.println("A user connected: " + event.getUser());
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("A user disconnected: " + header.getUser());
    }
}
