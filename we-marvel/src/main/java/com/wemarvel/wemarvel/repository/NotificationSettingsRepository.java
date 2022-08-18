package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, String> {

    @Query("SELECT n.username FROM NotificationSettings n " +
            "WHERE n.topics = true AND n.username <> ?1")
    List<String> getUsersWithEnabledTopics(String excludedUsername);
}
