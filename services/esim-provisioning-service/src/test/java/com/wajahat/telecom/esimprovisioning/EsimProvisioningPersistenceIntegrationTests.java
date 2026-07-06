package com.wajahat.telecom.esimprovisioning;

import static org.assertj.core.api.Assertions.assertThat;

import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;
import com.wajahat.telecom.esimprovisioning.dto.ProvisionEsimRequest;
import com.wajahat.telecom.esimprovisioning.service.EsimProvisioningService;
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
class EsimProvisioningPersistenceIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private EsimProvisioningService esimProvisioningService;

    @Test
    void persistsAndActivatesEsimProfile() {
        var provisioned = esimProvisioningService.provision(new ProvisionEsimRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()));

        var active = esimProvisioningService.activate(provisioned.iccid());

        assertThat(active.status()).isEqualTo(EsimStatus.ACTIVE);
        assertThat(active.smdpAddress()).isEqualTo("telecom-connect.example");
        assertThat(esimProvisioningService.getByIccid(provisioned.iccid()).activatedAt()).isNotNull();
    }
}
