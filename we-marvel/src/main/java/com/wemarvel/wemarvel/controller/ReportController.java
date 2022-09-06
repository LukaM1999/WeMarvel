package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.dto.ReportDTO;
import com.wemarvel.wemarvel.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ReportDTO> getReports(){
        return reportService.getReports();
    }

    @PostMapping("/user")
    public void reportUser(@RequestBody ReportDTO reportDTO){
        reportService.reportUser(reportDTO.getReportedUsername(), reportDTO.getExplanation());
    }

    @PostMapping("/post")
    public void reportPost(@RequestBody ReportDTO reportDTO){
        reportService.reportPost(reportDTO.getPostId(), reportDTO.getExplanation());
    }

    @PatchMapping("/resolved")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void resolveReports(@RequestBody ReportDTO reportDTO){
        reportService.markReportsAsResolved(reportDTO.getReportIds());
    }
}
