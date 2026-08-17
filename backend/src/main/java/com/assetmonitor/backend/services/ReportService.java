package com.assetmonitor.backend.services;

import com.assetmonitor.backend.domain.Report;
import com.assetmonitor.backend.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Report addReport(Report incoming) {
        incoming.classifyPriority();
        Report saved = reportRepository.save(incoming);
        messagingTemplate.convertAndSend("/topic/reports", saved);
        return saved;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAllByOrderByTimestampDesc();
    }
}