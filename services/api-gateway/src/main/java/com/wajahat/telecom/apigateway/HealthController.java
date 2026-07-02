package com.wajahat.telecom.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    HealthResponse health() {
        return new HealthResponse("UP", "api-gateway");
    }

    record HealthResponse(String status, String service) {
    }
}
