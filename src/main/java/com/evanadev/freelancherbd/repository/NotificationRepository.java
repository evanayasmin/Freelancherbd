package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    long countByRecipient_IdAndIsReadFalse(Long userId);
    List<Notification> findByRecipient_IdOrderByCreatedAtDesc(Long userId);

    // Mark all unread as read
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.id = :userId AND n.isRead = false")
    void markAllAsReadByReceiptId(@Param("userId") Long userId);
}
