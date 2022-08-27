package com.wemarvel.wemarvel.model.dto;

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
    private LocalDateTime modified;
    private String type;
    private Long startYear;
    private Long endYear;
    private double averageRating;

    public SeriesDTO(Long id, String title, String thumbnail) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
    }
}
