package com.wajahat.telecom.subscription.repository;

import com.wajahat.telecom.subscription.domain.Subscription;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(UUID subscriptionId);

    List<Subscription> findByDeviceId(UUID deviceId);
}
