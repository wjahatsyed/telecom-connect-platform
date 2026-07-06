package com.wajahat.telecom.subscription.repository;

import com.wajahat.telecom.subscription.domain.Subscription;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaSubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final JpaSubscriptionRepository jpaRepository;

    JpaSubscriptionRepositoryAdapter(JpaSubscriptionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return jpaRepository.save(SubscriptionEntity.from(subscription)).toDomain();
    }

    @Override
    public Optional<Subscription> findById(UUID subscriptionId) {
        return jpaRepository.findById(subscriptionId).map(SubscriptionEntity::toDomain);
    }

    @Override
    public List<Subscription> findByDeviceId(UUID deviceId) {
        return jpaRepository.findByDeviceIdOrderByCreatedAtAsc(deviceId).stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
    }
}
