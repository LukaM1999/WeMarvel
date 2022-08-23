package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.dto.NotificationSettingsDTO;

import java.util.List;

public interface NotificationSettingsService {
    NotificationSettings getNotificationSettings();
    List<Long> getUsersWithEnabledTopics(Long excludedUserId);
    void updateNotificationSettings(NotificationSettingsDTO notificationSettings);
}
