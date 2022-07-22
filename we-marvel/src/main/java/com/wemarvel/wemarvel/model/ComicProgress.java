package com.wemarvel.wemarvel.model;

import com.wemarvel.wemarvel.model.enums.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ComicProgress {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private Long userId;

    @Getter
    private Long comicId;

    @Getter
    @Setter
    private ReadingStatus status;

    @Getter
    @Setter
    private int pagesRead;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;
}
