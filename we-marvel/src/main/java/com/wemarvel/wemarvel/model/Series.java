package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Series {
    @Id
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Column(length = 3000)
    private String description;

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
    private String type;

    @Getter
    @Setter
    private Long startYear;

    @Getter
    @Setter
    private Long endYear;

}
