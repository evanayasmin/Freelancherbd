package com.evanadev.freelancherbd.websocket;

import com.evanadev.freelancherbd.service.OnlineUserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WebSocketEventListener {
    private final OnlineUserService onlineUserService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(OnlineUserService onlineUserService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.onlineUserService = onlineUserService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            onlineUserService.userConnected(user.getName());
            broadcastOnlineUsers();
        }
    }
    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            onlineUserService.userDisconnected(user.getName());
            broadcastOnlineUsers();
        }
    }


    private void broadcastOnlineUsers() {

        Set<String> users = onlineUserService.getOnlineUsers();

        users.forEach(user -> {
            Set<String> others = users.stream()
                    .filter(u -> !u.equals(user))
                    .collect(Collectors.toSet());

            messagingTemplate.convertAndSendToUser(
                    user,
                    "/queue/online-users",
                    others
            );
        });
    }

}
