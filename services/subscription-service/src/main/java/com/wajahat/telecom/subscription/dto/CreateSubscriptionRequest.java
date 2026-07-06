package com.wajahat.telecom.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateSubscriptionRequest(
        @NotNull UUID customerId,
        @NotNull UUID deviceId,
        @NotBlank @Size(max = 80) @Pattern(regexp = "[A-Z0-9_-]+") String planCode) {
}
