package com.assetmonitor.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // Simple endpoint to confirm the backend is up and reachable.
    // XMPP listener, report persistence, and priority logic get added in later steps.
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "ok", "service", "asset-monitor-backend");
    }
}
