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
    private Long ownerId;
    @Getter
    @Setter
    private String ownerUsername;
    @Getter
    @Setter
    private String ownerImageUrl;
    @Getter
    @Setter
    private boolean ownerEnabled;
    @Getter
    @Setter
    private Long topicId;
    @Getter
    @Setter
    private String topicTitle;
    @Getter
    @Setter
    private Long quotedPostId;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime createdAt;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime modifiedAt;
    @Getter
    @Setter
    private Long modifiedById;
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

    public PostDTO(Long id, Long ownerId, String ownerUsername, String ownerImageUrl,
                   boolean ownerEnabled, Long topicId, String topicTitle, Long quotedPostId,
                   LocalDateTime createdAt, String content, LocalDateTime modifiedAt, Long modifiedById,
                   String modifiedByUsername, int modifications, boolean deleted) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.ownerImageUrl = ownerImageUrl;
        this.ownerEnabled = ownerEnabled;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.quotedPostId = quotedPostId;
        this.createdAt = createdAt;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.modifiedById = modifiedById;
        this.modifiedByUsername = modifiedByUsername;
        this.modifications = modifications;
        this.deleted = deleted;
    }
}
