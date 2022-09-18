package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Long id;
    private Long ownerId;
    private String ownerUsername;
    private String ownerImageUrl;
    private boolean ownerEnabled;
    private Long topicId;
    private String topicTitle;
    private Long quotedPostId;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime createdAt;
    private String content;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime modifiedAt;
    private Long modifiedById;
    private String modifiedByUsername;
    private int modifications;
    private boolean deleted;
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
