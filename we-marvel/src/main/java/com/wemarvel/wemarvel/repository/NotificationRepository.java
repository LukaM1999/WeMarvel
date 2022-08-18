package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.FriendNotification;
import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.TopicNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM TopicNotification n " +
            "WHERE n.recipientUsername = ?1")
    List<TopicNotification> getAllTopicNotifications(String username);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.recipientUsername = ?1 " +
            "ORDER BY n.receivedAt DESC")
    List<Notification> getAllNotifications(String username);

    @Query("SELECT n FROM Notification n " +
            "WHERE n.recipientUsername = ?1 AND n.read = false " +
            "ORDER BY n.receivedAt DESC")
    List<Notification> getAllUnreadNotifications(String username);

    @Query("SELECT n FROM FriendNotification n " +
            "WHERE n.recipientUsername = ?1 " +
            "ORDER BY n.receivedAt DESC")
    List<FriendNotification> getAllFriendNotifications(String username);

}
