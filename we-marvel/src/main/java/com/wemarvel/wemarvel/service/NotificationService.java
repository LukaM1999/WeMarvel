package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationDTO notificationDTO);
    void sendTopicNotification(NotificationDTO notificationDTO);
    void sendFriendNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getAllUnreadNotifications();
    void markAllAsRead();
    void sendMessageNotification(NotificationDTO notificationDTO);
    void markAsRead(NotificationDTO notificationDTO);
}
