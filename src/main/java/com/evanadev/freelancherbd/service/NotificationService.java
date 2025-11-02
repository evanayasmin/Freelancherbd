package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.model.Notification;
import com.evanadev.freelancherbd.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }
    public void sendNotification(Notification notification) {
        // Save to DB
        notificationRepository.save(notification);

        // Send real-time message to admin dashboard (topic: /topic/admin)
        messagingTemplate.convertAndSend("/topic/admin", notification);
    }
}
