package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private String seriesTitle;

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
    private String variantDescription;

    @Getter
    @Setter
    private String format;

    @Getter
    @Setter
    private Double issueNumber;

    @Getter
    @Setter
    private Double averageRating;

    @Getter
    @Setter
    private Long ratingCount;

    @Getter
    @Setter
    private Long readingCount;

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

    public ComicDTO(Long id, Long seriesId, String title, String thumbnail) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public ComicDTO(Long id, Long seriesId, String seriesTitle, String title,
                    String thumbnail, Long topicCount, Long postCount,
                    LocalDateTime lastPostDate) {
        this.id = id;
        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.title = title;
        this.thumbnail = thumbnail;
        this.topicCount = topicCount;
        this.postCount = postCount;
        this.lastPostDate = lastPostDate;
    }

    public ComicDTO(Long id, Long seriesId, String seriesTitle,
                    String title, String description, String thumbnail,
                    String url, int pageCount, String variantDescription,
                    String format, Double issueNumber) {
        this.id = id;
        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.url = url;
        this.pageCount = pageCount;
        this.variantDescription = variantDescription;
        this.format = format;
        this.issueNumber = issueNumber;
    }
}
