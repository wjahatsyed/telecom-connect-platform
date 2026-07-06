package com.wajahat.telecom.subscription.domain;

import java.time.Instant;
import java.util.UUID;

public record Subscription(
        UUID subscriptionId,
        UUID customerId,
        UUID deviceId,
        String planCode,
        SubscriptionStatus status,
        Instant activatedAt,
        Instant suspendedAt,
        Instant cancelledAt,
        Instant createdAt,
        Instant updatedAt,
        Long version) {

    public Subscription transitionTo(
            SubscriptionStatus nextStatus,
            Instant activatedAt,
            Instant suspendedAt,
            Instant cancelledAt,
            Instant updatedAt) {
        return new Subscription(
                subscriptionId,
                customerId,
                deviceId,
                planCode,
                nextStatus,
                activatedAt,
                suspendedAt,
                cancelledAt,
                createdAt,
                updatedAt,
                version);
    }
}
