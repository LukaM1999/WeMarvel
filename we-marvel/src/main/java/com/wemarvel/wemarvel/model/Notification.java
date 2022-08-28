package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Notification {

    @Id
    @SequenceGenerator(name = "notificationIdGen", sequenceName = "notificationIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificationIdGen")
    @Getter
    private Long id;

    @Getter
    @Setter
    private String type;

    @Getter
    private Long recipientId;

    @Getter
    private Long senderId;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean read;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime receivedAt;

    public Notification(String type, Long recipientId, Long senderId, LocalDateTime receivedAt) {
        this.type = type;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.receivedAt = receivedAt;
    }
}
