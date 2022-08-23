package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Long firstTopicId;

    @Getter
    @Setter
    private Long secondTopicId;

    @Getter
    @Setter
    private String firstTopicTitle;

    @Getter
    @Setter
    private String secondTopicTitle;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime firstTopicDate;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime secondTopicDate;

    @Getter
    @Setter
    private String firstTopicUserId;

    @Getter
    @Setter
    private String secondTopicUserId;

    @Getter
    @Setter
    private String firstTopicUsername;

    @Getter
    @Setter
    private String secondTopicUsername;
}
