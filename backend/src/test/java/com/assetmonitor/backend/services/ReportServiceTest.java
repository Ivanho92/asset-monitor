package com.assetmonitor.backend.services;

import com.assetmonitor.backend.domain.Priority;
import com.assetmonitor.backend.domain.Report;
import com.assetmonitor.backend.domain.Status;
import com.assetmonitor.backend.repositories.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ReportService reportService;

    @Test
    void addReportClassifiesPersistsAndBroadcasts() {
        Report incoming = new Report();
        incoming.setSourceId("unit-07");
        incoming.setStatus(Status.OFFLINE);

        when(reportRepository.save(incoming)).thenReturn(incoming);

        Report result = reportService.addReport(incoming);

        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        verify(reportRepository).save(incoming);
        verify(messagingTemplate).convertAndSend(eq("/topic/reports"), eq(incoming));
    }
}