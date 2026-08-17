package com.assetmonitor.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Data
@Entity
public class Report {

    // Statuses that automatically mark a report as high priority.
    private static final Set<Status> HIGH_PRIORITY_STATUSES = Set.of(Status.OFFLINE, Status.MALFUNCTION);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String sourceId;
    private String entityType;
    private double lat;
    private double lon;
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private Priority priority;

    public void classifyPriority() {
        this.priority = (status != null && HIGH_PRIORITY_STATUSES.contains(status))
                ? Priority.HIGH
                : Priority.NORMAL;
    }
}
