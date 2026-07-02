package com.wajahat.telecom.device;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse("UP", "device-service");
    }

    record HealthResponse(String status, String service) {
    }
}
