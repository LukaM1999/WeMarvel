package com.wemarvel.wemarvel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"character_id", "series_id"}))
public class CharacterInSeries {
    @Id
    @SequenceGenerator(name = "characterInSeriesIdGen", sequenceName = "characterInSeriesIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "characterInSeriesIdGen")
    @Getter
    private Long id;

    @Getter
    @Column(name = "character_id")
    private Long characterId;

    @Getter
    @Column(name = "series_id")
    private Long seriesId;
}
