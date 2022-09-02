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
public class MarvelCharacter {

    @Id
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Column(length = 3000)
    @Getter
    @Setter
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

}
