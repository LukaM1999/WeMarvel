package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComicProgressDTO {
    private Long comicId;
    private String comicTitle;
    private String comicUrl;
    private String comicThumbnail;
    private int comicPages;
    private Long seriesId;
    private String seriesTitle;
    private String seriesThumbnail;
    private Long firstUserId;
    private String firstUsername;
    private ReadingStatus firstStatus;
    private int firstPagesRead;
    private int firstRating;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate firstStartDate;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate firstEndDate;
    private Long secondUserId;
    private String secondUsername;
    private ReadingStatus secondStatus;
    private int secondPagesRead;
    private int secondRating;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate secondStartDate;
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate secondEndDate;

    public ComicProgressDTO(Long comicId, String comicTitle, String comicUrl, String comicThumbnail, Long firstUserId,
                            ReadingStatus firstStatus, int firstPagesRead, int firstRating,
                            LocalDate firstStartDate, LocalDate firstEndDate, Long secondUserId,
                            ReadingStatus secondStatus, int secondPagesRead, int secondRating,
                            LocalDate secondStartDate, LocalDate secondEndDate) {
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicUrl = comicUrl;
        this.comicThumbnail = comicThumbnail;
        this.firstUserId = firstUserId;
        this.firstStatus = firstStatus;
        this.firstPagesRead = firstPagesRead;
        this.firstRating = firstRating;
        this.firstStartDate = firstStartDate;
        this.firstEndDate = firstEndDate;
        this.secondUserId = secondUserId;
        this.secondStatus = secondStatus;
        this.secondPagesRead = secondPagesRead;
        this.secondRating = secondRating;
        this.secondStartDate = secondStartDate;
        this.secondEndDate = secondEndDate;
    }

    public ComicProgressDTO(Long comicId, String comicTitle, String comicUrl, String comicThumbnail,
                            Long seriesId, String seriesTitle, String seriesThumbnail,
                            Long firstUserId, ReadingStatus firstStatus,
                            int firstPagesRead, int firstRating,
                            LocalDate firstStartDate, LocalDate firstEndDate) {
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicUrl = comicUrl;
        this.comicThumbnail = comicThumbnail;
        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.seriesThumbnail = seriesThumbnail;
        this.firstUserId = firstUserId;
        this.firstStatus = firstStatus;
        this.firstPagesRead = firstPagesRead;
        this.firstRating = firstRating;
        this.firstStartDate = firstStartDate;
        this.firstEndDate = firstEndDate;
    }
}
