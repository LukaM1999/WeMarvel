package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String ownerUsername;
    @Getter
    @Setter
    private Long topicId;
    @Getter
    @Setter
    private String topicTitle;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime createdAt;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime modifiedAt;
    @Getter
    @Setter
    private String modifiedByUsername;
    @Getter
    @Setter
    private int modifications;
    @Getter
    @Setter
    private boolean deleted;
    @Getter
    @Setter
    private ProfileDTO owner;

    public PostDTO(Long id, String ownerUsername, Long topicId, String topicTitle,
                   LocalDateTime createdAt, String content, LocalDateTime modifiedAt,
                   String modifiedByUsername, int modifications, boolean deleted) {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.createdAt = createdAt;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.modifiedByUsername = modifiedByUsername;
        this.modifications = modifications;
        this.deleted = deleted;
    }
}
