package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"owner_username", "marvel_entity_id"})})
public class Review {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "owner_username", nullable = false)
    private String ownerUsername;

    @Getter
    @Setter
    @Column(name = "marvel_entity_id")
    private Long marvelEntityId;

    @Getter
    @Setter
    private LocalDate createdAt;

    @Getter
    @Setter
    private Integer rating;

    @Getter
    @Setter
    private String text;


}
