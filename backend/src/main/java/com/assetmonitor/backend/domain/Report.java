package com.assetmonitor.backend.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Data
public class Report {

    // Statuses that automatically mark a report as high priority.
    private static final Set<Status> HIGH_PRIORITY_STATUSES = Set.of(Status.OFFLINE, Status.MALFUNCTION);

    private String sourceId;
    private String entityType;
    private double lat;
    private double lon;
    private Instant timestamp;
    private Status status;

    @Setter(AccessLevel.NONE)
    private Priority priority;

    public void classifyPriority() {
        this.priority = (status != null && HIGH_PRIORITY_STATUSES.contains(status))
                ? Priority.HIGH
                : Priority.NORMAL;
    }
}
