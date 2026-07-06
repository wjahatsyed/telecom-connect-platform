package com.wajahat.telecom.subscription.domain;

import java.time.Instant;
import java.util.UUID;

public record Subscription(
        UUID subscriptionId,
        UUID customerId,
        UUID deviceId,
        String planCode,
        SubscriptionStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public Subscription withStatus(SubscriptionStatus nextStatus, Instant updatedAt) {
        return new Subscription(subscriptionId, customerId, deviceId, planCode, nextStatus, createdAt, updatedAt);
    }
}
