package com.wajahat.telecom.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wajahat.telecom.customer.domain.CustomerType;
import com.wajahat.telecom.customer.dto.CreateCustomerRequest;
import com.wajahat.telecom.customer.exception.CustomerNotFoundException;
import com.wajahat.telecom.customer.repository.CustomerRepository;
import com.wajahat.telecom.customer.service.CustomerService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CustomerServiceTests {

    private final CustomerRepository repository = new TestCustomerRepository();
    private final CustomerService customerService = new CustomerService(repository);

    @Test
    void createsCustomerWithGeneratedIdentifier() {
        var customer = customerService.create(new CreateCustomerRequest(
                CustomerType.ENTERPRISE,
                "Acme Connectivity",
                "ops@acme.test",
                "+15551234567"));

        assertThat(customer.customerId()).isNotNull();
        assertThat(customer.type()).isEqualTo(CustomerType.ENTERPRISE);
        assertThat(customer.displayName()).isEqualTo("Acme Connectivity");
    }

    @Test
    void throwsWhenCustomerDoesNotExist() {
        UUID missingCustomerId = UUID.randomUUID();

        assertThatThrownBy(() -> customerService.getById(missingCustomerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining(missingCustomerId.toString());
    }

    private static class TestCustomerRepository implements CustomerRepository {

        private final java.util.Map<UUID, com.wajahat.telecom.customer.domain.Customer> customers =
                new java.util.LinkedHashMap<>();

        @Override
        public com.wajahat.telecom.customer.domain.Customer save(
                com.wajahat.telecom.customer.domain.Customer customer) {
            customers.put(customer.customerId(), customer);
            return customer;
        }

        @Override
        public Optional<com.wajahat.telecom.customer.domain.Customer> findById(UUID customerId) {
            return Optional.ofNullable(customers.get(customerId));
        }

        @Override
        public java.util.List<com.wajahat.telecom.customer.domain.Customer> findAll() {
            return java.util.List.copyOf(customers.values());
        }
    }
}
