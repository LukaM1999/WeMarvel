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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "marvel_entity_id"})})
public class Review {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_id")
    private Long userId;

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
