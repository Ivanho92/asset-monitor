package com.assetmonitor.backend.services;

import com.assetmonitor.backend.domain.Report;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ReportService {

    // In-memory storage ; OK for a learning project.
    // Nothing else in the app depends on this detail.
    private final List<Report> reports = new CopyOnWriteArrayList<>();

    public Report addReport(Report incoming) {
        incoming.classifyPriority();
        reports.add(incoming);
        return incoming;
    }

    public List<Report> getAllReports() {
        List<Report> copy = new ArrayList<>(reports);
        Collections.reverse(copy); // newest first
        return copy;
    }
}
