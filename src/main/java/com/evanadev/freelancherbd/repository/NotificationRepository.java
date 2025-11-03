package com.evanadev.freelancherbd.repository;

import com.evanadev.freelancherbd.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {

}
