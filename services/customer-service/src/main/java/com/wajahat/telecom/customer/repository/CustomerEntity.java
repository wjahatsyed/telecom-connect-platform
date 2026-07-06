package com.wajahat.telecom.customer.repository;

import com.wajahat.telecom.customer.domain.Customer;
import com.wajahat.telecom.customer.domain.CustomerStatus;
import com.wajahat.telecom.customer.domain.CustomerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers")
class CustomerEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType type;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected CustomerEntity() {
    }

    private CustomerEntity(
            UUID id,
            String name,
            CustomerType type,
            String email,
            String phoneNumber,
            CustomerStatus status,
            Instant createdAt,
            Instant updatedAt,
            Long version) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    static CustomerEntity from(Customer customer) {
        return new CustomerEntity(
                customer.customerId(),
                customer.displayName(),
                customer.type(),
                customer.email(),
                customer.phoneNumber(),
                customer.status(),
                customer.createdAt(),
                customer.updatedAt(),
                customer.version());
    }

    Customer toDomain() {
        return new Customer(id, type, name, email, phoneNumber, status, createdAt, updatedAt, version);
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
