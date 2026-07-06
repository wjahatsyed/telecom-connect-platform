package com.wajahat.telecom.subscription.service;

import com.wajahat.telecom.subscription.domain.Subscription;
import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import com.wajahat.telecom.subscription.dto.CreateSubscriptionRequest;
import com.wajahat.telecom.subscription.exception.InvalidSubscriptionStateException;
import com.wajahat.telecom.subscription.exception.SubscriptionNotFoundException;
import com.wajahat.telecom.subscription.repository.SubscriptionRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final Clock clock;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this(subscriptionRepository, Clock.systemUTC());
    }

    SubscriptionService(SubscriptionRepository subscriptionRepository, Clock clock) {
        this.subscriptionRepository = subscriptionRepository;
        this.clock = clock;
    }

    public Subscription create(CreateSubscriptionRequest request) {
        Instant now = Instant.now(clock);
        Subscription subscription = new Subscription(
                UUID.randomUUID(),
                request.customerId(),
                request.deviceId(),
                request.planCode(),
                SubscriptionStatus.PENDING,
                null,
                null,
                null,
                now,
                now,
                null);

        return subscriptionRepository.save(subscription);
    }

    public Subscription getById(UUID subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));
    }

    public List<Subscription> getByDeviceId(UUID deviceId) {
        return subscriptionRepository.findByDeviceId(deviceId);
    }

    public Subscription activate(UUID subscriptionId) {
        Subscription subscription = getById(subscriptionId);
        if (subscription.status() != SubscriptionStatus.PENDING && subscription.status() != SubscriptionStatus.SUSPENDED) {
            throw new InvalidSubscriptionStateException(subscriptionId, subscription.status(), "activate");
        }
        return saveWithStatus(subscription, SubscriptionStatus.ACTIVE, Instant.now(clock), subscription.suspendedAt(), subscription.cancelledAt());
    }

    public Subscription suspend(UUID subscriptionId) {
        Subscription subscription = getById(subscriptionId);
        if (subscription.status() != SubscriptionStatus.ACTIVE) {
            throw new InvalidSubscriptionStateException(subscriptionId, subscription.status(), "suspend");
        }
        return saveWithStatus(subscription, SubscriptionStatus.SUSPENDED, subscription.activatedAt(), Instant.now(clock), subscription.cancelledAt());
    }

    public Subscription resume(UUID subscriptionId) {
        Subscription subscription = getById(subscriptionId);
        if (subscription.status() != SubscriptionStatus.SUSPENDED) {
            throw new InvalidSubscriptionStateException(subscriptionId, subscription.status(), "resume");
        }
        return saveWithStatus(subscription, SubscriptionStatus.ACTIVE, subscription.activatedAt(), subscription.suspendedAt(), subscription.cancelledAt());
    }

    public Subscription cancel(UUID subscriptionId) {
        Subscription subscription = getById(subscriptionId);
        if (subscription.status() == SubscriptionStatus.CANCELLED) {
            throw new InvalidSubscriptionStateException(subscriptionId, subscription.status(), "cancel");
        }
        return saveWithStatus(subscription, SubscriptionStatus.CANCELLED, subscription.activatedAt(), subscription.suspendedAt(), Instant.now(clock));
    }

    private Subscription saveWithStatus(
            Subscription subscription,
            SubscriptionStatus status,
            Instant activatedAt,
            Instant suspendedAt,
            Instant cancelledAt) {
        return subscriptionRepository.save(subscription.transitionTo(
                status,
                activatedAt,
                suspendedAt,
                cancelledAt,
                Instant.now(clock)));
    }
}
