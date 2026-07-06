package com.wajahat.telecom.customer.repository;

import com.wajahat.telecom.customer.domain.Customer;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCustomerRepository implements CustomerRepository {

    private final ConcurrentHashMap<UUID, Customer> customers = new ConcurrentHashMap<>();

    @Override
    public Customer save(Customer customer) {
        customers.put(customer.customerId(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    @Override
    public List<Customer> findAll() {
        return customers.values().stream()
                .sorted(Comparator.comparing(Customer::createdAt))
                .toList();
    }
}
