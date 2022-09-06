package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.NotificationSettingsDTO;
import com.wemarvel.wemarvel.repository.NotificationSettingsRepository;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public NotificationSettings createNotificationSettings(Long userId) {
        return notificationSettingsRepository.save(new NotificationSettings(userId));
    }

    @Override
    public List<Long> getUsersWithEnabledTopics(Long excludedUserId) {
        return notificationSettingsRepository.getUsersWithEnabledTopics(excludedUserId);
    }

    @Override
    public void updateNotificationSettings(NotificationSettingsDTO notificationSettings) {
        RegisteredUser user = getSignedInUser();
        if(user == null) {
            throw new UsernameNotFoundException("User not logged in");
        }
        notificationSettingsRepository.save(new NotificationSettings(user.getId(),
                notificationSettings.isTopics(), notificationSettings.isMessages(),
                notificationSettings.isFriendRequests()));
    }
}
