package com.wajahat.telecom.customer.domain;

import java.time.Instant;
import java.util.UUID;

public record Customer(
        UUID customerId,
        CustomerType type,
        String displayName,
        String email,
        String phoneNumber,
        CustomerStatus status,
        Instant createdAt,
        Instant updatedAt,
        Long version) {
}
