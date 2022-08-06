package com.wemarvel.wemarvel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.DataTruncation;
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
    private String resourceURI;

    @Getter
    @Setter
    private Double averageRating;

    @Getter
    @Setter
    private Long ratingCount;

    public CharacterDTO(Long id, String name, String description, String thumbnail, String resourceURI, Double averageRating, Long ratingCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.resourceURI = resourceURI;
        this.ratingCount = ratingCount;
        this.averageRating = Objects.requireNonNullElse(averageRating, 0.0);
    }
}
