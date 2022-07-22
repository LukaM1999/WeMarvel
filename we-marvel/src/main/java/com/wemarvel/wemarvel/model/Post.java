package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Long userId;

    @Getter
    private Long marvelEntityId;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private LocalDate createdAt;
}
