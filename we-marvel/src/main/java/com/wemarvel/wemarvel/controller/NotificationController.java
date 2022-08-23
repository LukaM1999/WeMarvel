package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Notification;
import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.NotificationDTO;
import com.wemarvel.wemarvel.model.dto.NotificationSettingsDTO;
import com.wemarvel.wemarvel.service.NotificationService;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/settings")
    public ResponseEntity<NotificationSettings> getNotificationSettings() {
        return ResponseEntity.ok(notificationSettingsService.getNotificationSettings());
    }

    @PutMapping("/settings")
    public void updateNotificationSettings(@RequestBody NotificationSettingsDTO notificationSettings) {
        notificationSettingsService.updateNotificationSettings(notificationSettings);
    }

    @PostMapping("/topic")
    public ResponseEntity<String> sendTopicNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.sendTopicNotification(notificationDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getAllUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getAllUnreadNotifications());
    }

    @PatchMapping("/read")
    public ResponseEntity<String> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok().build();
    }
}
