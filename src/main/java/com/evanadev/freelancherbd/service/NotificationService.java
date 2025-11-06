package com.evanadev.freelancherbd.service;

import com.evanadev.freelancherbd.dto.NotificationDTO;
import com.evanadev.freelancherbd.model.Notification;
import com.evanadev.freelancherbd.model.User;
import com.evanadev.freelancherbd.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }
    public void sendNotification(Notification notification, User adminUser) {
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
        System.out.println("Principal: " + notification.getRecipient().getUsername());
       // System.out.println("Admin Principal: " + SecurityContextHolder.getContext().getAuthentication().getName());

        messagingTemplate.convertAndSendToUser(
                notification.getRecipient().getUsername(), // must match Spring Security principal
                "/queue/notifications",                    // frontend will subscribe to this
                dto
        );



    }
}
