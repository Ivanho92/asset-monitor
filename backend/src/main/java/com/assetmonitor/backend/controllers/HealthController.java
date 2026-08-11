package com.assetmonitor.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // Simple endpoint to confirm the backend is up and reachable.
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "ok ok",
                "service", "asset-monitor-backend"
        );
    }
}
