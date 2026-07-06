package com.wajahat.telecom.customer;

import static org.assertj.core.api.Assertions.assertThat;

import com.wajahat.telecom.customer.domain.CustomerType;
import com.wajahat.telecom.customer.dto.CreateCustomerRequest;
import com.wajahat.telecom.customer.service.CustomerService;
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
class CustomerPersistenceIntegrationTests {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerService customerService;

    @Test
    void persistsAndReadsCustomer() {
        var created = customerService.create(new CreateCustomerRequest(
                CustomerType.ENTERPRISE,
                "Acme Connectivity",
                "ops@acme.test",
                "+15551234567"));

        var found = customerService.getById(created.customerId());

        assertThat(found.email()).isEqualTo("ops@acme.test");
        assertThat(found.createdAt()).isNotNull();
        assertThat(found.updatedAt()).isNotNull();
        assertThat(found.version()).isNotNull();
    }
}
