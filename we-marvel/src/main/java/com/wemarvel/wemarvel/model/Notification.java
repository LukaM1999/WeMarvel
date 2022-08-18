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
    private String recipientUsername;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean read;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime receivedAt;

    public Notification(String type, String recipientUsername, LocalDateTime receivedAt) {
        this.type = type;
        this.recipientUsername = recipientUsername;
        this.receivedAt = receivedAt;
    }
}
