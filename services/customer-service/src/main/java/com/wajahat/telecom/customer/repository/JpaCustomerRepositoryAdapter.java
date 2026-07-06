package com.wajahat.telecom.customer.repository;

import com.wajahat.telecom.customer.domain.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaCustomerRepositoryAdapter implements CustomerRepository {

    private final JpaCustomerRepository jpaRepository;

    JpaCustomerRepositoryAdapter(JpaCustomerRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return jpaRepository.save(CustomerEntity.from(customer)).toDomain();
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return jpaRepository.findById(customerId).map(CustomerEntity::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(CustomerEntity::toDomain)
                .toList();
    }
}
