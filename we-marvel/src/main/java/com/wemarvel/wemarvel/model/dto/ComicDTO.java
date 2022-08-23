package com.wemarvel.wemarvel.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
public class ComicDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long seriesId;

    @Getter
    @Setter
    private String title;

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
    private int pageCount;

    @Getter
    @Setter
    private Double averageRating;

    @Getter
    @Setter
    private Long ratingCount;

    @Getter
    @Setter
    private Long readingCount;

    public ComicDTO(Long id, Long seriesId, String title, String description, String thumbnail, String url, int pageCount,
                    Double averageRating, Long ratingCount) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.url = url;
        this.pageCount = pageCount;
        this.ratingCount = ratingCount;
        this.averageRating = Objects.requireNonNullElse(averageRating, 0.0);
    }

    public ComicDTO(Long id, Long seriesId, String title, String description, String thumbnail, String url, int pageCount,
                    Long readingCount) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.url = url;
        this.pageCount = pageCount;
        this.readingCount = readingCount;
    }
}
