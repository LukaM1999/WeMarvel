package com.wemarvel.wemarvel.service.impl;

import com.pusher.rest.Pusher;
import com.wemarvel.wemarvel.config.PusherConfig;
import com.wemarvel.wemarvel.model.FriendNotification;
import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.TopicNotification;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;
import com.wemarvel.wemarvel.repository.NotificationRepository;
import com.wemarvel.wemarvel.service.NotificationService;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUsername;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @Autowired
    private WatchedTopicServiceImpl watchedTopicService;

    @Autowired
    private PusherConfig pusherConfig;

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {

    }

    @Override
    public void sendTopicNotification(NotificationDTO notificationDTO) {
        String excludedUsername = getSignedInUsername();
        for(String username : notificationSettingsService.getUsersWithEnabledTopics(excludedUsername)) {
            if(watchedTopicService.getWatchedTopic(notificationDTO.getTopicId(), username) == null) continue;
            TopicNotification topicNotification = new TopicNotification(notificationDTO.getType(),
                    username, LocalDateTime.now(), notificationDTO.getBoardId(),
                    notificationDTO.getTopicId(), notificationDTO.getTopicTitle(), notificationDTO.getPosterUsername());
            notificationRepository.save(topicNotification);
        }
        Pusher pusher = pusherConfig.getPusher();
        pusher.trigger("topics", notificationDTO.getType(), notificationDTO, notificationDTO.getSocketId());
    }

    @Override
    public void sendFriendNotification(NotificationDTO notificationDTO) {

    }

    @Override
    public List<Notification> getAllNotifications() {
        String username = getSignedInUsername();
        if(username == null) throw new IllegalStateException("User is not signed in");
        return notificationRepository.getAllNotifications(username);
    }

    @Override
    public List<Notification> getAllUnreadNotifications() {
        String username = getSignedInUsername();
        if(username == null) throw new IllegalStateException("User is not signed in");
        return notificationRepository.getAllUnreadNotifications(username);
    }

    @Override
    public void markAllAsRead() {
        String username = getSignedInUsername();
        if(username == null) throw new IllegalStateException("User is not signed in");
        List<Notification> unread = notificationRepository.getAllUnreadNotifications(username);
        for(Notification notification : unread) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
