package com.wemarvel.wemarvel.service.impl;

import com.pusher.rest.Pusher;
import com.wemarvel.wemarvel.config.PusherConfig;
import com.wemarvel.wemarvel.model.*;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;
import com.wemarvel.wemarvel.repository.NotificationRepository;
import com.wemarvel.wemarvel.service.NotificationService;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.WatchedTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                    userId, excludedUserId, LocalDateTime.now(), notificationDTO.getBoardId(),
                    notificationDTO.getTopicId(), notificationDTO.getTopicTitle());
            notificationRepository.save(topicNotification);
        }
        notificationDTO.setSenderImageUrl(getSignedInUser().getImageUrl());
        Pusher pusher = pusherConfig.getPusher();
        pusher.trigger("topics", notificationDTO.getType(), notificationDTO, notificationDTO.getSocketId());
    }

    @Override
    public void sendFriendNotification(NotificationDTO notificationDTO) {
        RegisteredUser excludedUser = getSignedInUser();
        if(excludedUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        NotificationSettings notificationSettings = notificationSettingsService.getByUserId(notificationDTO.getRecipientId());
        if(notificationSettings == null || !notificationSettings.isFriendRequests()) {
            throw new IllegalArgumentException("User does not have friend notifications enabled");
        }
        Notification friendNotification = new Notification(notificationDTO.getType(),
                notificationDTO.getRecipientId(), excludedUser.getId(), LocalDateTime.now());
        notificationRepository.save(friendNotification);
        notificationDTO.setSenderId(excludedUser.getId());
        notificationDTO.setSenderUsername(excludedUser.getUsername());
        notificationDTO.setSenderImageUrl(excludedUser.getImageUrl());
        notificationDTO.setReceivedAt(LocalDateTime.now());
        Pusher pusher = pusherConfig.getPusher();
        pusher.trigger("friends", notificationDTO.getType(), notificationDTO, notificationDTO.getSocketId());
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.getAllNotifications(Objects.requireNonNull(getSignedInUser()).getId());
    }

    @Override
    public List<NotificationDTO> getAllUnreadNotifications() {
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        List<Notification> notifications = notificationRepository.getAllUnreadNotifications(Objects.requireNonNull(getSignedInUser()).getId());
        for(Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO(notification.getId(), notification.getType(),
                    notification.getRecipientId(), notification.getSenderId(), notification.getReceivedAt());
            RegisteredUser recipient = registeredUserService.getUserById(notification.getRecipientId());
            notificationDTO.setRecipientUsername(recipient.getUsername());
            RegisteredUser sender = registeredUserService.getUserById(notification.getSenderId());
            notificationDTO.setSenderUsername(sender.getUsername());
            notificationDTO.setSenderImageUrl(sender.getImageUrl());
            if(notification instanceof TopicNotification) {
               TopicNotification topicNotification = (TopicNotification) notification;
                notificationDTO.setBoardId(topicNotification.getBoardId());
                notificationDTO.setTopicId(topicNotification.getTopicId());
                notificationDTO.setTopicTitle(topicNotification.getTopicTitle());
            }
            notificationDTOs.add(notificationDTO);
        }
        return notificationDTOs;
    }

    @Override
    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.
                getAllUnreadNotifications(Objects.requireNonNull(getSignedInUser()).getId());
        for(Notification notification : unread) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
