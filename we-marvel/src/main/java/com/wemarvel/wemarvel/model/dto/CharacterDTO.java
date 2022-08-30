package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.DataTruncation;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
public class CharacterDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String thumbnail;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private Double averageRating;

    @Getter
    @Setter
    private Long ratingCount;

    @Getter
    @Setter
    private Long topicCount;

    @Getter
    @Setter
    private Long postCount;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime lastPostDate;

    public CharacterDTO(Long id, String name, String description, String thumbnail, String url, Double averageRating, Long ratingCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.url = url;
        this.ratingCount = ratingCount;
        this.averageRating = Objects.requireNonNullElse(averageRating, 0.0);
    }

    public CharacterDTO(Long id, String name, String thumbnail, Long topicCount,
                        Long postCount, LocalDateTime lastPostDate) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.topicCount = topicCount;
        this.postCount = postCount;
        this.lastPostDate = lastPostDate;
    }
}
