package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    @Getter
    private Long id;
    @Getter
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
    private String senderUsername;
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
    private String posterUsername;
    @Getter
    @Setter
    private String socketId;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime receivedAt;
}
