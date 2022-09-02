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
public class MessageNotification extends Notification {

    @Getter
    @Setter
    private String message;

    public MessageNotification(String type, Long recipientId, Long senderId,
                               LocalDateTime receivedAt, String message) {
        super(type, recipientId, senderId, receivedAt);
        this.message = message;
    }
}
