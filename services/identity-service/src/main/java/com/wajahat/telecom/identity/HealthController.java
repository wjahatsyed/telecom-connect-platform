package com.wajahat.telecom.identity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse("UP", "identity-service");
    }

    record HealthResponse(String status, String service) {
    }
}
