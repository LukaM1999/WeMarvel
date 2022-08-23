package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    @Query("SELECT n.userId FROM NotificationSettings n " +
            "WHERE n.topics = true AND n.userId <> ?1")
    List<Long> getUsersWithEnabledTopics(Long excludedUserId);
}
