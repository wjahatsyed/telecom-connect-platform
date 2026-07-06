package com.wajahat.telecom.esimprovisioning.domain;

import java.time.Instant;
import java.util.UUID;

public record EsimProfile(
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
        Instant updatedAt,
        Long version) {

    public EsimProfile transitionTo(
            EsimStatus nextStatus,
            Instant activatedAt,
            Instant suspendedAt,
            Instant terminatedAt,
            Instant updatedAt) {
        return new EsimProfile(
                iccid,
                customerId,
                deviceId,
                subscriptionId,
                activationCode,
                smdpAddress,
                nextStatus,
                provisionedAt,
                activatedAt,
                suspendedAt,
                terminatedAt,
                createdAt,
                updatedAt,
                version);
    }
}
