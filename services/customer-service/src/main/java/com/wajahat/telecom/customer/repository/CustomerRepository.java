package com.wajahat.telecom.customer.repository;

import com.wajahat.telecom.customer.domain.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID customerId);

    List<Customer> findAll();
}
