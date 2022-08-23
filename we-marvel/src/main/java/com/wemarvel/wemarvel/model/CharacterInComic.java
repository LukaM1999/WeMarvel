package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"character_id", "comic_id"}))
public class CharacterInComic {
    @Id
    @SequenceGenerator(name = "characterInComicIdGen", sequenceName = "characterInComicIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "characterInComicIdGen")
    @Getter
    private Long id;

    @Getter
    @Column(name = "character_id")
    private Long characterId;

    @Getter
    @Column(name = "comic_id")
    private Long comicId;
}
