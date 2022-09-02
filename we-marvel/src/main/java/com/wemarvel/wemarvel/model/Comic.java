package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comic {
    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long seriesId;

    @Getter
    @Setter
    private String title;

    @Column(length = 3000)
    @Getter
    @Setter
    private String description;

    @Column(length = 1000)
    @Getter
    @Setter
    private String variantDescription;

    @Getter
    @Setter
    private String thumbnail;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private LocalDateTime modified;

    @Getter
    @Setter
    private String format;

    @Getter
    @Setter
    private int pageCount;

    @Getter
    @Setter
    private Double issueNumber;

    public Comic(Long id, Long seriesId, String title, String thumbnail, String url) {
        this.id = id;
        this.seriesId = seriesId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
    }
}
