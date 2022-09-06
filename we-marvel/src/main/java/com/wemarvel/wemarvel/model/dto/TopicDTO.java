package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {
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
    private boolean ownerEnabled;
    @Getter
    @Setter
    private Long boardId;
    @Getter
    @Setter
    private String boardTitle;
    @Getter
    @Setter
    private Long marvelEntityId;
    @Getter
    @Setter
    private String marvelEntityName;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime createdAt;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private boolean sticky;
    @Getter
    @Setter
    private boolean locked;
    @Getter
    @Setter
    private List<PostDTO> posts = new ArrayList<>();
    @Getter
    @Setter
    private Long postCount;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime lastPostDate;
    @Getter
    @Setter
    private boolean watched;
    @Getter
    @Setter
    private String firstPostContent;

    public TopicDTO(Long id, Long ownerId, String ownerUsername,
                    boolean ownerEnabled, Long boardId, String boardTitle,
                    LocalDateTime createdAt,
                    String title, boolean sticky, boolean locked) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.ownerEnabled = ownerEnabled;
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.createdAt = createdAt;
        this.title = title;
        this.sticky = sticky;
        this.locked = locked;
    }

    public TopicDTO(Long id, String title, Long posts, LocalDateTime lastPostDate,
                    LocalDateTime createdAt, Long ownerId, String ownerUsername, boolean ownerEnabled,
                    Long marvelEntityId, String marvelEntityName, boolean sticky) {
        this.id = id;
        this.title = title;
        this.postCount = posts;
        this.lastPostDate = lastPostDate;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.ownerEnabled = ownerEnabled;
        this.marvelEntityId = marvelEntityId;
        this.marvelEntityName = marvelEntityName;
        this.sticky = sticky;
    }

    public TopicDTO(Long id, String title, Long posts, LocalDateTime lastPostDate,
                    LocalDateTime createdAt, Long ownerId,
                    String ownerUsername, boolean ownerEnabled, boolean sticky) {
        this.id = id;
        this.title = title;
        this.postCount = posts;
        this.lastPostDate = lastPostDate;
        this.createdAt = createdAt;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.ownerEnabled = ownerEnabled;
        this.sticky = sticky;
    }
}
