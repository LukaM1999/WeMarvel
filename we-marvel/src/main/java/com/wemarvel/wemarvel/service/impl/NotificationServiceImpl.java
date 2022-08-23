package com.wemarvel.wemarvel.service.impl;

import com.pusher.rest.Pusher;
import com.wemarvel.wemarvel.config.PusherConfig;
import com.wemarvel.wemarvel.model.FriendNotification;
import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.TopicNotification;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;
import com.wemarvel.wemarvel.repository.NotificationRepository;
import com.wemarvel.wemarvel.service.NotificationService;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.WatchedTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @Autowired
    private WatchedTopicService watchedTopicService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private PusherConfig pusherConfig;

    @Override
    public void sendNotification(NotificationDTO notificationDTO) {

    }

    @Override
    public void sendTopicNotification(NotificationDTO notificationDTO) {
        Long excludedUserId = Objects.requireNonNull(getSignedInUser()).getId();
        for(Long userId : notificationSettingsService.getUsersWithEnabledTopics(excludedUserId)) {
            if(watchedTopicService.getWatchedTopic(notificationDTO.getTopicId(), userId) == null) continue;
            TopicNotification topicNotification = new TopicNotification(notificationDTO.getType(),
                    userId, LocalDateTime.now(), notificationDTO.getBoardId(),
                    notificationDTO.getTopicId(), notificationDTO.getTopicTitle(), excludedUserId);
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
        return notificationRepository.getAllNotifications(getSignedInUser().getId());
    }

    @Override
    public List<NotificationDTO> getAllUnreadNotifications() {
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        List<Notification> notifications = notificationRepository.getAllUnreadNotifications(Objects.requireNonNull(getSignedInUser()).getId());
        for(Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO(notification.getId(), notification.getRecipientId(), notification.getType(), notification.getReceivedAt());
            RegisteredUser recipient = registeredUserService.getUserById(notification.getRecipientId());
            notificationDTO.setRecipientUsername(recipient.getUsername());
            if(notification instanceof TopicNotification) {
               TopicNotification topicNotification = (TopicNotification) notification;
                notificationDTO.setBoardId(topicNotification.getBoardId());
                notificationDTO.setTopicId(topicNotification.getTopicId());
                notificationDTO.setTopicTitle(topicNotification.getTopicTitle());
                notificationDTO.setPosterId(topicNotification.getPosterId());
                RegisteredUser poster = registeredUserService.getUserById(topicNotification.getPosterId());
                notificationDTO.setPosterUsername(poster.getUsername());
            } else if(notification instanceof FriendNotification) {
                notification.setType("friend");
            }
            notificationDTOs.add(notificationDTO);
        }
        return notificationDTOs;
    }

    @Override
    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.getAllUnreadNotifications(getSignedInUser().getId());
        for(Notification notification : unread) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
