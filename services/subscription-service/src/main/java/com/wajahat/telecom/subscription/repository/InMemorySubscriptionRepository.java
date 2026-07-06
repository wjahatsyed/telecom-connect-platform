package com.wajahat.telecom.subscription.repository;

import com.wajahat.telecom.subscription.domain.Subscription;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemorySubscriptionRepository implements SubscriptionRepository {

    private final ConcurrentHashMap<UUID, Subscription> subscriptions = new ConcurrentHashMap<>();

    @Override
    public Subscription save(Subscription subscription) {
        subscriptions.put(subscription.subscriptionId(), subscription);
        return subscription;
    }

    @Override
    public Optional<Subscription> findById(UUID subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId));
    }

    @Override
    public List<Subscription> findByDeviceId(UUID deviceId) {
        return subscriptions.values().stream()
                .filter(subscription -> subscription.deviceId().equals(deviceId))
                .sorted(Comparator.comparing(Subscription::createdAt))
                .toList();
    }
}
