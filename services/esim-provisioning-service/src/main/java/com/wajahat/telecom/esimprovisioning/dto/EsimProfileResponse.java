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
        String smdpAddress,
        EsimStatus status,
        Instant provisionedAt,
        Instant activatedAt,
        Instant suspendedAt,
        Instant terminatedAt,
        Instant createdAt,
        Instant updatedAt) {

    public static EsimProfileResponse from(EsimProfile profile) {
        return new EsimProfileResponse(
                profile.iccid(),
                profile.customerId(),
                profile.deviceId(),
                profile.subscriptionId(),
                profile.activationCode(),
                profile.smdpAddress(),
                profile.status(),
                profile.provisionedAt(),
                profile.activatedAt(),
                profile.suspendedAt(),
                profile.terminatedAt(),
                profile.createdAt(),
                profile.updatedAt());
    }
}
