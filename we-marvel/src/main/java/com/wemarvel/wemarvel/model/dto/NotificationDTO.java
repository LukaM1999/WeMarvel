package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    @Getter
    private Long id;
    @Getter
    private Long recipientId;
    @Getter
    @Setter
    private String recipientUsername;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private boolean read;
    @Getter
    @Setter
    private Long senderId;
    @Getter
    @Setter
    private String senderUsername;
    @Getter
    @Setter
    private String senderImageUrl;
    @Getter
    @Setter
    private Long boardId;
    @Getter
    @Setter
    private Long topicId;
    @Getter
    @Setter
    private String topicTitle;
    @Getter
    @Setter
    private String socketId;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime receivedAt;
    @Getter
    @Setter
    private List<Long> toMarkAsRead;

    public NotificationDTO(Long id, String type, Long recipientId, Long senderId, LocalDateTime receivedAt) {
        this.id = id;
        this.type = type;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.receivedAt = receivedAt;
    }
}
