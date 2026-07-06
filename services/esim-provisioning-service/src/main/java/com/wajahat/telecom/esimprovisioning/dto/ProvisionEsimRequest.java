package com.wajahat.telecom.esimprovisioning.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProvisionEsimRequest(
        @NotNull UUID customerId,
        @NotNull UUID deviceId,
        @NotNull UUID subscriptionId) {
}
