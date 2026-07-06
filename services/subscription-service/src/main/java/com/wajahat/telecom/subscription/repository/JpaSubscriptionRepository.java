package com.wajahat.telecom.subscription.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

    List<SubscriptionEntity> findByDeviceIdOrderByCreatedAtAsc(UUID deviceId);
}
