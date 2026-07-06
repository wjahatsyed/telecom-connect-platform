package com.wajahat.telecom.esimprovisioning.domain;

import java.time.Instant;
import java.util.UUID;

public record EsimProfile(
        String iccid,
        UUID customerId,
        UUID deviceId,
        UUID subscriptionId,
        String activationCode,
        EsimStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public EsimProfile withStatus(EsimStatus nextStatus, Instant updatedAt) {
        return new EsimProfile(
                iccid,
                customerId,
                deviceId,
                subscriptionId,
                activationCode,
                nextStatus,
                createdAt,
                updatedAt);
    }
}
