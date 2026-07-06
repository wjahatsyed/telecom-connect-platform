package com.wajahat.telecom.subscription;

import static org.assertj.core.api.Assertions.assertThat;

import com.wajahat.telecom.subscription.domain.SubscriptionStatus;
import com.wajahat.telecom.subscription.dto.CreateSubscriptionRequest;
import com.wajahat.telecom.subscription.service.SubscriptionService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class SubscriptionPersistenceIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    void persistsAndTransitionsSubscription() {
        UUID deviceId = UUID.randomUUID();
        var created = subscriptionService.create(new CreateSubscriptionRequest(
                UUID.randomUUID(),
                deviceId,
                "GLOBAL_5GB"));

        var active = subscriptionService.activate(created.subscriptionId());

        assertThat(active.status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(active.activatedAt()).isNotNull();
        assertThat(subscriptionService.getByDeviceId(deviceId)).hasSize(1);
    }
}
