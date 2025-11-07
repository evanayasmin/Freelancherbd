package com.evanadev.freelancherbd.controller;

import com.evanadev.freelancherbd.model.CustomUserDetail;
import com.evanadev.freelancherbd.repository.NotificationRepository;
import com.evanadev.freelancherbd.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/count")
    public long getUnreadCount(@ModelAttribute("loggedUser") CustomUserDetail loggedUser) {
        return notificationRepository.countByRecipient_IdAndIsReadFalse(loggedUser.getId());
    }

    @GetMapping("/list")
    public List<Map<String, String>> getNotifications(@ModelAttribute("loggedUser") CustomUserDetail loggedUser) {
        return notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(loggedUser.getId())
                .stream()
                .map(n -> Map.of("message", n.getMessage()))
                .toList();
    }

    @PostMapping("/mark-as-read")
    @ResponseBody
    public ResponseEntity<Map<String, String>> markNotificationsAsRead(@ModelAttribute("loggedUser") CustomUserDetail loggedUser) {
        notificationService.markAllAsRead(loggedUser.getId());
        return ResponseEntity.ok(Map.of("status", "success"));
    }
}
