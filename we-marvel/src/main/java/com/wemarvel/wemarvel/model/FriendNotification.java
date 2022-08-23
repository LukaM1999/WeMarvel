package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FriendNotification extends Notification {
    @Getter
    @Setter
    private String senderUsername;
    @Getter
    @Setter
    private String message;

    public FriendNotification(String type, Long recipientId, LocalDateTime receivedAt,
                              String senderUsername, String message) {
        super(type, recipientId, receivedAt);
        this.senderUsername = senderUsername;
        this.message = message;
    }
}
