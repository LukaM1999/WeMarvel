package com.wemarvel.wemarvel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "comic_id"})})
public class ComicProgress {

    @Id
    @SequenceGenerator(name = "comicProgressIdGen", sequenceName = "comicProgressIdSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comicProgressIdGen")
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_id")
    private Long userId;

    @Getter
    @Setter
    @Column(name = "comic_id")
    private Long comicId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    @Getter
    @Setter
    private int pagesRead;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate startDate;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate endDate;
}
