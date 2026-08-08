package com.assetmonitor.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    // Simple endpoint to confirm the backend is up and reachable.
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "ok test",
                "service", "asset-monitor-backend"
        );
    }
}
