package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.dto.NotificationSettingsDTO;
import com.wemarvel.wemarvel.repository.NotificationSettingsRepository;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Override
    public NotificationSettings getNotificationSettings() {
        return notificationSettingsRepository.findById(Objects.requireNonNull(getSignedInUser()).getId()).orElse(null);
    }

    @Override
    public NotificationSettings getByUserId(Long userId) {
        return notificationSettingsRepository.findById(userId).orElse(null);
    }

    @Override
    public List<Long> getUsersWithEnabledTopics(Long excludedUserId) {
        return notificationSettingsRepository.getUsersWithEnabledTopics(excludedUserId);
    }

    @Override
    public void updateNotificationSettings(NotificationSettingsDTO notificationSettings) {
        String username = Objects.requireNonNull(getSignedInUser()).getUsername();
        if(!username.equals(notificationSettings.getUsername())) {
            throw new IllegalArgumentException("Username in notification settings does not match username in security context");
        }
        notificationSettingsRepository.save(new NotificationSettings(notificationSettings.getUserId(),
                notificationSettings.isTopics(), notificationSettings.isMessages(),
                notificationSettings.isFriendRequests()));
    }
}
