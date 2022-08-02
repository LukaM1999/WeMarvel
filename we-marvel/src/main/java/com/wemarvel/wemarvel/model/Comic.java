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
    private String resourceURI;

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

    @Getter
    @Setter
    @Column(columnDefinition = "Decimal(4, 2) default '0.0'")
    private double averageRating;
}
