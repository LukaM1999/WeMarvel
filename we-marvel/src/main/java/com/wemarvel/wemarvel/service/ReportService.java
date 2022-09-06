package com.wemarvel.wemarvel.service;


import com.wemarvel.wemarvel.model.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    void reportUser(String username, String reason);
    void reportPost(Long postId, String reason);
    List<ReportDTO> getReports();
    void markReportsAsResolved(List<Long> reportIds);
}
