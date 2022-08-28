package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDTO {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String senderImageUrl;
    private Long receiverId;
    private String receiverUsername;
    private String receiverImageUrl;
    private boolean accepted;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime sentAt;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate friendsSince;
    private String username;
    private String imageUrl;
    private Gender gender;
    private String location;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate birthday;

    public FriendRequestDTO(Long id, Long senderId, String senderUsername, String senderImageUrl,
                            Long receiverId, String receiverUsername, String receiverImageUrl,
                            boolean accepted, LocalDateTime sentAt, LocalDate friendsSince) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderImageUrl = senderImageUrl;
        this.receiverId = receiverId;
        this.receiverUsername = receiverUsername;
        this.receiverImageUrl = receiverImageUrl;
        this.accepted = accepted;
        this.sentAt = sentAt;
        this.friendsSince = friendsSince;
    }
}
