package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.TopicNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n " +
            "WHERE n.recipientId = ?1 " +
            "ORDER BY n.read, n.receivedAt DESC")
    List<Notification> getAllNotifications(Long recipientId);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.recipientId = ?1 AND n.read = false " +
            "ORDER BY n.receivedAt DESC")
    List<Notification> getAllUnreadNotifications(Long recipientId);
}
