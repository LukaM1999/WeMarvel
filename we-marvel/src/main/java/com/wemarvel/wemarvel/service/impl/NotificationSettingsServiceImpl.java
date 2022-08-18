package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.repository.NotificationSettingsRepository;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Override
    public NotificationSettings getNotificationSettings(String username) {
        return notificationSettingsRepository.findById(username).orElse(null);
    }

    @Override
    public List<String> getUsersWithEnabledTopics(String excludedUsername) {
        return notificationSettingsRepository.getUsersWithEnabledTopics(excludedUsername);
    }
}
