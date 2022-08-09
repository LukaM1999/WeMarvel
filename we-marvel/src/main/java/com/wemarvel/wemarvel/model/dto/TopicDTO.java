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
    private String ownerUsername;
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
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Belgrade")
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

    public TopicDTO(Long id, String ownerUsername, Long boardId, String boardTitle,
                    Long marvelEntityId, LocalDateTime createdAt,
                    String title, boolean sticky, boolean locked) {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.marvelEntityId = marvelEntityId;
        this.createdAt = createdAt;
        this.title = title;
        this.sticky = sticky;
        this.locked = locked;
    }
}
