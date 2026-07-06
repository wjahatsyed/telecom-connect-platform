package com.wajahat.telecom.device.repository;

import com.wajahat.telecom.device.domain.Device;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaDeviceRepositoryAdapter implements DeviceRepository {

    private final JpaDeviceRepository jpaRepository;

    JpaDeviceRepositoryAdapter(JpaDeviceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Device save(Device device) {
        return jpaRepository.save(DeviceEntity.from(device)).toDomain();
    }

    @Override
    public Optional<Device> findById(UUID deviceId) {
        return jpaRepository.findById(deviceId).map(DeviceEntity::toDomain);
    }

    @Override
    public List<Device> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerIdOrderByCreatedAtAsc(customerId).stream()
                .map(DeviceEntity::toDomain)
                .toList();
    }
}
