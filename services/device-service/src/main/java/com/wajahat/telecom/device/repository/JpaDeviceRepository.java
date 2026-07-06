package com.wajahat.telecom.device.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaDeviceRepository extends JpaRepository<DeviceEntity, UUID> {

    List<DeviceEntity> findByCustomerIdOrderByCreatedAtAsc(UUID customerId);
}
