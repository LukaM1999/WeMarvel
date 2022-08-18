package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.NotificationSettings;

import java.util.List;

public interface NotificationSettingsService {
    NotificationSettings getNotificationSettings(String username);
    List<String> getUsersWithEnabledTopics(String excludedUsername);
}
