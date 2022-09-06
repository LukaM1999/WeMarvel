package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {
    @Id
    @Getter
    private Long userId;
    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default true")
    private boolean topics;
    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default true")
    private boolean messages;
    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default true")
    private boolean friendRequests;

    public NotificationSettings(Long userId) {
        this.userId = userId;
    }
}
