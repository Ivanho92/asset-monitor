package com.assetmonitor.backend.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReportTest {

    @Test
    void offlineStatusIsHighPriority() {
        Report report = new Report();
        report.setStatus(Status.OFFLINE);

        report.classifyPriority();

        assertThat(report.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void malfunctionStatusIsHighPriority() {
        Report report = new Report();
        report.setStatus(Status.MALFUNCTION);

        report.classifyPriority();

        assertThat(report.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void operationalStatusIsNormalPriority() {
        Report report = new Report();
        report.setStatus(Status.OPERATIONAL);

        report.classifyPriority();

        assertThat(report.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    void nullStatusIsNormalPriority() {
        Report report = new Report();
        report.setStatus(null);

        report.classifyPriority();

        assertThat(report.getPriority()).isEqualTo(Priority.NORMAL);
    }
}