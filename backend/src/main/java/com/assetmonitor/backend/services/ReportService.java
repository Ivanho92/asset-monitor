package com.assetmonitor.backend.services;

import com.assetmonitor.backend.domain.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class ReportService {

    // In-memory storage ; OK for learning project.
    // Nothing else in the app depends on this detail.
    private final List<Report> reports = new CopyOnWriteArrayList<>();

    private final SimpMessagingTemplate messagingTemplate;

    public Report addReport(Report incoming) {
        incoming.classifyPriority();
        reports.add(incoming);
        messagingTemplate.convertAndSend("/topic/reports", incoming);
        return incoming;
    }

    public List<Report> getAllReports() {
        List<Report> copy = new ArrayList<>(reports);
        Collections.reverse(copy); // newest first
        return copy;
    }
}