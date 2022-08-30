package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeriesDTO {
    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private String url;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime modified;
    private String type;
    private Long startYear;
    private Long endYear;
    private double averageRating;
    private Long postCount;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss", timezone = "Europe/Belgrade")
    private LocalDateTime lastPostDate;

    public SeriesDTO(Long id, String title, String thumbnail) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public SeriesDTO(Long id, String title, String thumbnail,
                     Long postCount, LocalDateTime lastPostDate) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.postCount = postCount;
        this.lastPostDate = lastPostDate;
    }
}
