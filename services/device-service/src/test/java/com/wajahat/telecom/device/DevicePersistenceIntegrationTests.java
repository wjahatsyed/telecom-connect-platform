package com.wajahat.telecom.device;

import static org.assertj.core.api.Assertions.assertThat;

import com.wajahat.telecom.device.domain.DeviceType;
import com.wajahat.telecom.device.dto.CreateDeviceRequest;
import com.wajahat.telecom.device.service.DeviceService;
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
class DevicePersistenceIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DeviceService deviceService;

    @Test
    void persistsAndListsDeviceByCustomer() {
        UUID customerId = UUID.randomUUID();

        var created = deviceService.create(new CreateDeviceRequest(
                customerId,
                DeviceType.IOT_SENSOR,
                "Cold Chain Sensor",
                "123456789012345",
                "89049032000000000000000000000001"));

        assertThat(deviceService.getById(created.deviceId()).imei()).isEqualTo("123456789012345");
        assertThat(deviceService.getByCustomerId(customerId)).hasSize(1);
    }
}
