package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"sender_id", "receiver_id"})})
@Getter
public class FriendRequest {
    @Id
    @SequenceGenerator(name = "friendRequestIdGen", sequenceName = "friendRequestIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friendRequestIdGen")
    private Long id;

    @Setter
    @Column(name = "sender_id")
    private Long senderId;

    @Setter
    @Column(name = "receiver_id")
    private Long receiverId;

    @Setter
    @Column(columnDefinition = "Boolean default false")
    private boolean accepted;

    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime sentAt;

    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate friendsSince;

    public FriendRequest(Long senderId, Long receiverId, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sentAt = sentAt;
    }
}
