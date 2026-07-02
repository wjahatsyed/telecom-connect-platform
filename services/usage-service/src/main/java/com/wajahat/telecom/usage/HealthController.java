package com.wajahat.telecom.usage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse("UP", "usage-service");
    }

    record HealthResponse(String status, String service) {
    }
}
