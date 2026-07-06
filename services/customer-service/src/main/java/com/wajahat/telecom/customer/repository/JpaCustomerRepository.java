package com.wajahat.telecom.customer.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaCustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    List<CustomerEntity> findAllByOrderByCreatedAtAsc();
}
