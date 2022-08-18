package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationDTO notificationDTO);
    void sendTopicNotification(NotificationDTO notificationDTO);
    void sendFriendNotification(NotificationDTO notificationDTO);
    List<Notification> getAllNotifications();
    List<Notification> getAllUnreadNotifications();
    void markAllAsRead();
}
