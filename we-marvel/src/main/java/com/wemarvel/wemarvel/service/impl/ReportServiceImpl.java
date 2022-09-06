package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.Report;
import com.wemarvel.wemarvel.model.dto.ReportDTO;
import com.wemarvel.wemarvel.repository.ReportRepository;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Override
    public void reportUser(String username, String reason) {
        RegisteredUser reportingUser = getSignedInUser();
        if(reportingUser == null) {
            throw new UsernameNotFoundException("You must be logged in to report a user");
        }
        RegisteredUser reportedUser = registeredUserService.getUserByUsername(username);
        if(reportedUser == null) {
            throw new UsernameNotFoundException("Reported user not found");
        }
        Report userReport = new Report();
        userReport.setReportingUserId(reportingUser.getId());
        userReport.setReportedUserId(reportedUser.getId());
        userReport.setExplanation(reason);
        userReport.setSentAt(LocalDateTime.now());
        reportRepository.save(userReport);
    }

    @Override
    public void reportPost(Long postId, String reason) {
        RegisteredUser reportingUser = getSignedInUser();
        if(reportingUser == null) {
            throw new UsernameNotFoundException("You must be logged in to report a post");
        }
        Report postReport = new Report();
        postReport.setReportingUserId(reportingUser.getId());
        postReport.setPostId(postId);
        postReport.setExplanation(reason);
        postReport.setSentAt(LocalDateTime.now());
        reportRepository.save(postReport);
    }

    @Override
    public List<ReportDTO> getReports() {
        return reportRepository.getReports();
    }

    @Override
    public void markReportsAsResolved(List<Long> reportIds) {
        for(Long reportId : reportIds) {
            Report report = reportRepository.findById(reportId).orElse(null);
            if(report == null || report.isResolved()) continue;
            report.setResolved(true);
            reportRepository.save(report);
        }
    }
}
