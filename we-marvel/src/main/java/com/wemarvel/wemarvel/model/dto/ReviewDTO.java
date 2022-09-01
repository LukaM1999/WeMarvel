package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.Recommendation;
import com.wemarvel.wemarvel.model.enums.ReviewType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long marvelEntityId;
    private String marvelEntityTitle;
    private String marvelEntityThumbnail;
    private Long ownerId;
    private String ownerUsername;
    private String ownerImageUrl;
    private ReviewType type;
    private Recommendation recommendation;
    private Integer rating;
    private String text;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate createdAt;

    public ReviewDTO(Long id, Long marvelEntityId, Long ownerId,
                     String ownerUsername, String ownerImageUrl,
                     ReviewType type, Recommendation recommendation,
                     Integer rating, String text, LocalDate createdAt) {
        this.id = id;
        this.marvelEntityId = marvelEntityId;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.ownerImageUrl = ownerImageUrl;
        this.type = type;
        this.recommendation = recommendation;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }
}
