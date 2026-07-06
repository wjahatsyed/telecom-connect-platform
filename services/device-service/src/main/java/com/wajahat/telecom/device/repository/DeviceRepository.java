package com.wajahat.telecom.device.repository;

import com.wajahat.telecom.device.domain.Device;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(UUID deviceId);

    List<Device> findByCustomerId(UUID customerId);
}
