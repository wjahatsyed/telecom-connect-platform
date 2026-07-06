package com.wajahat.telecom.esimprovisioning.dto;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;
import java.time.Instant;
import java.util.UUID;

public record EsimProfileResponse(
        String iccid,
        UUID customerId,
        UUID deviceId,
        UUID subscriptionId,
        String activationCode,
        EsimStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static EsimProfileResponse from(EsimProfile profile) {
        return new EsimProfileResponse(
                profile.iccid(),
                profile.customerId(),
                profile.deviceId(),
                profile.subscriptionId(),
                profile.activationCode(),
                profile.status(),
                profile.createdAt(),
                profile.updatedAt());
    }
}
