package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.Recommendation;
import com.wemarvel.wemarvel.model.enums.ReviewType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"owner_id", "marvel_entity_id"})})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewIdGen")
    @SequenceGenerator(name = "reviewIdGen", sequenceName = "reviewIdSeq", initialValue = 1, allocationSize = 1)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "owner_id")
    private Long ownerId;

    @Getter
    @Setter
    @Column(name = "marvel_entity_id")
    private Long marvelEntityId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ReviewType type;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate createdAt;

    @Getter
    @Setter
    private Integer rating;

    @Getter
    @Setter
    @Column(length = 3000)
    private String text;
}
