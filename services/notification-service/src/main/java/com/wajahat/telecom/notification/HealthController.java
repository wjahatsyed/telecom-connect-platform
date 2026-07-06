package com.wajahat.telecom.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse("UP", "notification-service");
    }

    record HealthResponse(String status, String service) {
    }
}
