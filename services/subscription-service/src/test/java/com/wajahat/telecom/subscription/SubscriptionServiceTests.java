package com.wajahat.telecom.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wajahat.telecom.subscription.domain.Subscription;
import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import com.wajahat.telecom.subscription.dto.CreateSubscriptionRequest;
import com.wajahat.telecom.subscription.exception.InvalidSubscriptionStateException;
import com.wajahat.telecom.subscription.repository.SubscriptionRepository;
import com.wajahat.telecom.subscription.service.SubscriptionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SubscriptionServiceTests {

    private final SubscriptionRepository repository = new TestSubscriptionRepository();
    private final SubscriptionService subscriptionService = new SubscriptionService(repository);

    @Test
    void createsPendingSubscription() {
        Subscription subscription = subscriptionService.create(new CreateSubscriptionRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "GLOBAL_5GB"));

        assertThat(subscription.subscriptionId()).isNotNull();
        assertThat(subscription.status()).isEqualTo(SubscriptionStatus.PENDING);
    }

    @Test
    void supportsActivationSuspensionResumeAndCancellation() {
        Subscription subscription = subscriptionService.create(new CreateSubscriptionRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "GLOBAL_5GB"));

        assertThat(subscriptionService.activate(subscription.subscriptionId()).status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(subscriptionService.suspend(subscription.subscriptionId()).status()).isEqualTo(SubscriptionStatus.SUSPENDED);
        assertThat(subscriptionService.resume(subscription.subscriptionId()).status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(subscriptionService.cancel(subscription.subscriptionId()).status()).isEqualTo(SubscriptionStatus.CANCELLED);
    }

    @Test
    void rejectsInvalidTransition() {
        Subscription subscription = subscriptionService.create(new CreateSubscriptionRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "GLOBAL_5GB"));

        assertThatThrownBy(() -> subscriptionService.suspend(subscription.subscriptionId()))
                .isInstanceOf(InvalidSubscriptionStateException.class);
    }

    private static class TestSubscriptionRepository implements SubscriptionRepository {

        private final java.util.Map<UUID, Subscription> subscriptions = new java.util.LinkedHashMap<>();

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
                    .toList();
        }
    }
}
