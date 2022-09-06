package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Report;
import com.wemarvel.wemarvel.model.dto.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.ReportDTO(r.id, r.reportingUserId, " +
            "u1.username, u1.imageUrl, r.reportedUserId, u2.username, u2.imageUrl, b.id, p.topicId, " +
            "t.title, p.id, p.content, r.explanation, r.sentAt, r.resolved) " +
            "FROM Report r " +
            "INNER JOIN RegisteredUser u1 ON r.reportingUserId = u1.id " +
            "LEFT JOIN RegisteredUser u2 ON r.reportedUserId = u2.id " +
            "LEFT JOIN Post p ON r.postId = p.id " +
            "LEFT JOIN Topic t ON p.topicId = t.id " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "ORDER BY r.resolved DESC, r.sentAt DESC")
    List<ReportDTO> getReports();
}
