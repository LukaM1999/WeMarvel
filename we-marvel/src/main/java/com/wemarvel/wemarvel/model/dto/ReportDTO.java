package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportDTO {
    private Long id;
    private Long reportingUserId;
    private String reportingUsername;
    private String reportingUserImageUrl;
    private Long reportedUserId;
    private String reportedUsername;
    private String reportedUserImageUrl;
    private Long boardId;
    private Long topicId;
    private String topicTitle;
    private Long postId;
    private String postContent;
    private String explanation;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm", timezone = "Europe/Belgrade")
    private LocalDateTime sentAt;
    private boolean resolved;
    private List<Long> reportIds;

    public ReportDTO(Long id, Long reportingUserId, String reportingUsername,
                     String reportingUserImageUrl, Long reportedUserId,
                     String reportedUsername, String reportedUserImageUrl,
                     Long boardId, Long topicId, String topicTitle, Long postId,
                     String postContent, String explanation,
                     LocalDateTime sentAt, boolean resolved) {
        this.id = id;
        this.reportingUserId = reportingUserId;
        this.reportingUsername = reportingUsername;
        this.reportingUserImageUrl = reportingUserImageUrl;
        this.reportedUserId = reportedUserId;
        this.reportedUsername = reportedUsername;
        this.reportedUserImageUrl = reportedUserImageUrl;
        this.boardId = boardId;
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.postId = postId;
        this.postContent = postContent;
        this.explanation = explanation;
        this.sentAt = sentAt;
        this.resolved = resolved;
    }
}
