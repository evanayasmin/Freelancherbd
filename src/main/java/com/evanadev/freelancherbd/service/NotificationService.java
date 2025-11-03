package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.dto.NotificationDTO;
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

        NotificationDTO dto = new NotificationDTO(
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getSender().getUsername(),
                notification.getRecipient().getId(),
                notification.getCreatedAt()
        );

        // Send real-time message to admin dashboard (topic: /topic/admin)
        messagingTemplate.convertAndSend("/topic/admin", dto);

    }
}
