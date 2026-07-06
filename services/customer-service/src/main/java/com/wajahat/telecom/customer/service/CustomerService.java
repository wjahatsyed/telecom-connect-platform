package com.wajahat.telecom.customer.service;

import com.wajahat.telecom.customer.domain.Customer;
import com.wajahat.telecom.customer.dto.CreateCustomerRequest;
import com.wajahat.telecom.customer.exception.CustomerNotFoundException;
import com.wajahat.telecom.customer.repository.CustomerRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Clock clock;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this(customerRepository, Clock.systemUTC());
    }

    CustomerService(CustomerRepository customerRepository, Clock clock) {
        this.customerRepository = customerRepository;
        this.clock = clock;
    }

    public Customer create(CreateCustomerRequest request) {
        Customer customer = new Customer(
                UUID.randomUUID(),
                request.type(),
                request.displayName(),
                request.email(),
                request.phoneNumber(),
                Instant.now(clock));

        return customerRepository.save(customer);
    }

    public Customer getById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
}
