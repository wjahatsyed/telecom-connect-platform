package com.wajahat.telecom.subscription.repository;

import com.wajahat.telecom.subscription.domain.Subscription;
import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
class SubscriptionEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "plan_code", nullable = false)
    private String planCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected SubscriptionEntity() {
    }

    private SubscriptionEntity(Subscription subscription) {
        this.id = subscription.subscriptionId();
        this.customerId = subscription.customerId();
        this.deviceId = subscription.deviceId();
        this.planCode = subscription.planCode();
        this.status = subscription.status();
        this.activatedAt = subscription.activatedAt();
        this.suspendedAt = subscription.suspendedAt();
        this.cancelledAt = subscription.cancelledAt();
        this.createdAt = subscription.createdAt();
        this.updatedAt = subscription.updatedAt();
        this.version = subscription.version();
    }

    static SubscriptionEntity from(Subscription subscription) {
        return new SubscriptionEntity(subscription);
    }

    Subscription toDomain() {
        return new Subscription(
                id,
                customerId,
                deviceId,
                planCode,
                status,
                activatedAt,
                suspendedAt,
                cancelledAt,
                createdAt,
                updatedAt,
                version);
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
