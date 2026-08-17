package com.assetmonitor.backend.xmpp;

import com.assetmonitor.backend.domain.Report;
import com.assetmonitor.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class XmppReportListener {

    private final ReportService reportService;
    private final JsonMapper jsonMapper;
    private final XmppProperties xmppProperties;

    private static final long RETRY_DELAY_MS = 5000;

    @EventListener(ApplicationReadyEvent.class)
    public void connect() {
        Thread thread = new Thread(this::connectWithRetry, "xmpp-listener");
        thread.setDaemon(true);
        thread.start();
    }

    private void connectWithRetry() {
        var username = xmppProperties.backend().username();
        var domain = xmppProperties.server().domain();

        int attempt = 0;
        while (true) {
            attempt++;
            try {
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setHost(xmppProperties.server().host())
                        .setPort(xmppProperties.server().port())
                        .setXmppDomain(xmppProperties.server().domain())
                        .setUsernameAndPassword(username, xmppProperties.backend().password())
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .build();

                XMPPTCPConnection connection = new XMPPTCPConnection(config);
                connection.connect();
                connection.login();

                ReconnectionManager.getInstanceFor(connection).enableAutomaticReconnection();

                ChatManager.getInstanceFor(connection).addIncomingListener(
                        (from, message, chat) -> handleIncomingMessage(message.getBody()));

                log.info("Connected to XMPP server as {}@{}", username, domain);
                return;
            } catch (Exception e) {
                log.warn("XMPP connection attempt {} failed: {}. Retrying in 5s...", attempt, e.getMessage());
                sleep();
            }
        }
    }

    private void handleIncomingMessage(String body) {
        if (body == null) {
            return;
        }
        try {
            var report = jsonMapper.readValue(body, Report.class);
            reportService.addReport(report);
        } catch (JacksonException e) {
            log.warn("Failed to parse incoming XMPP message as a Report: {}", body, e);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}