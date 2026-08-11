package com.assetmonitor.backend.controllers;

import com.assetmonitor.backend.domain.Report;
import com.assetmonitor.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // Called by the fake source generator (and later, the XMPP listener) to submit a new report.
    @PostMapping
    public ResponseEntity<Report> submitReport(@RequestBody Report report) {
        Report saved = reportService.addReport(report);
        return ResponseEntity.ok(saved);
    }

    // Called by frontend client to load the current aggregated feed.
    @GetMapping
    public List<Report> getReports() {
        return reportService.getAllReports();
    }
}
