package com.wemarvel.wemarvel.service.impl;

import com.pusher.rest.Pusher;
import com.wemarvel.wemarvel.config.PusherConfig;
import com.wemarvel.wemarvel.model.*;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;
import com.wemarvel.wemarvel.repository.NotificationRepository;
import com.wemarvel.wemarvel.service.*;
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

    @Autowired
    private FriendService friendService;

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
    public List<NotificationDTO> getAllNotifications() {
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        List<Notification> notifications = notificationRepository.getAllNotifications(Objects.requireNonNull(getSignedInUser()).getId());
        for(Notification notification : notifications) {
            RegisteredUser sender = registeredUserService.getUserById(notification.getSenderId());
            NotificationDTO notificationDTO = new NotificationDTO(notification.getId(), notification.getType(), notification.getRecipientId(),
                    sender.getId(), notification.getReceivedAt());
            notificationDTO.setSenderUsername(sender.getUsername());
            notificationDTO.setSenderImageUrl(sender.getImageUrl());
            notificationDTO.setRead(notification.isRead());
            if(notification instanceof TopicNotification) {
                TopicNotification topicNotification = (TopicNotification) notification;
                notificationDTO.setBoardId(topicNotification.getBoardId());
                notificationDTO.setTopicId(topicNotification.getTopicId());
                notificationDTO.setTopicTitle(topicNotification.getTopicTitle());
            } else if(notification instanceof MessageNotification){
                MessageNotification messageNotification = (MessageNotification) notification;
                notificationDTO.setMessage(messageNotification.getMessage());
            }
            notificationDTOs.add(notificationDTO);
        }
        return notificationDTOs;
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
            } else if(notification instanceof MessageNotification) {
                MessageNotification messageNotification = (MessageNotification) notification;
                notificationDTO.setMessage(messageNotification.getMessage());
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

    @Override
    public void sendMessageNotification(NotificationDTO notificationDTO) {
        Long excludedUserId = Objects.requireNonNull(getSignedInUser()).getId();
        FriendRequest friendRequest = friendService.getAcceptedFriendRequest(excludedUserId, notificationDTO.getRecipientId());
        if(friendRequest == null) {
            throw new IllegalArgumentException("User not friends with recipient");
        }
        NotificationSettings notificationSettings = notificationSettingsService.getByUserId(notificationDTO.getRecipientId());
        if(notificationSettings == null || !notificationSettings.isMessages()) {
            throw new IllegalArgumentException("User does not have message notifications enabled");
        }
        MessageNotification messageNotification = new MessageNotification(notificationDTO.getType(),
                excludedUserId, notificationDTO.getRecipientId(), LocalDateTime.now(), notificationDTO.getMessage());
        notificationRepository.save(messageNotification);
        notificationDTO.setSenderImageUrl(getSignedInUser().getImageUrl());
        Pusher pusher = pusherConfig.getPusher();
        pusher.trigger("messages", notificationDTO.getType(), notificationDTO, notificationDTO.getSocketId());
    }

    @Override
    public void markAsRead(NotificationDTO notificationDTO) {
        for(Long notificationId : notificationDTO.getToMarkAsRead()){
            Notification notification = notificationRepository.findById(notificationId).orElse(null);
            if(notification == null) continue;
            if(!notification.getRecipientId().equals(Objects.requireNonNull(getSignedInUser()).getId())) {
                throw new IllegalArgumentException("User does not have permission to mark this notification as read");
            }
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
}
