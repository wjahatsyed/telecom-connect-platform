package com.wajahat.telecom.customer.dto;

import com.wajahat.telecom.customer.domain.Customer;
import com.wajahat.telecom.customer.domain.CustomerType;
import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID customerId,
        CustomerType type,
        String displayName,
        String email,
        String phoneNumber,
        Instant createdAt) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.customerId(),
                customer.type(),
                customer.displayName(),
                customer.email(),
                customer.phoneNumber(),
                customer.createdAt());
    }
}
