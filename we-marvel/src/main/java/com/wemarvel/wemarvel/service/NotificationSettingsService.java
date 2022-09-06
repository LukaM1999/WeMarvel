package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.dto.NotificationSettingsDTO;

import java.util.List;

public interface NotificationSettingsService {
    NotificationSettings getNotificationSettings();
    NotificationSettings getByUserId(Long userId);
    NotificationSettings createNotificationSettings(Long userId);
    List<Long> getUsersWithEnabledTopics(Long excludedUserId);
    void updateNotificationSettings(NotificationSettingsDTO notificationSettings);
}
