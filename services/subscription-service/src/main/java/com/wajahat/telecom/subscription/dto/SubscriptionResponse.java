package com.wajahat.telecom.subscription.dto;

import com.wajahat.telecom.subscription.domain.Subscription;
import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(
        UUID subscriptionId,
        UUID customerId,
        UUID deviceId,
        String planCode,
        SubscriptionStatus status,
        Instant activatedAt,
        Instant suspendedAt,
        Instant cancelledAt,
        Instant createdAt,
        Instant updatedAt) {

    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.subscriptionId(),
                subscription.customerId(),
                subscription.deviceId(),
                subscription.planCode(),
                subscription.status(),
                subscription.activatedAt(),
                subscription.suspendedAt(),
                subscription.cancelledAt(),
                subscription.createdAt(),
                subscription.updatedAt());
    }
}
