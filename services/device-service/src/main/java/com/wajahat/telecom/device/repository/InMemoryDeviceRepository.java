package com.wajahat.telecom.device.repository;

import com.wajahat.telecom.device.domain.Device;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryDeviceRepository implements DeviceRepository {

    private final ConcurrentHashMap<UUID, Device> devices = new ConcurrentHashMap<>();

    @Override
    public Device save(Device device) {
        devices.put(device.deviceId(), device);
        return device;
    }

    @Override
    public Optional<Device> findById(UUID deviceId) {
        return Optional.ofNullable(devices.get(deviceId));
    }

    @Override
    public List<Device> findByCustomerId(UUID customerId) {
        return devices.values().stream()
                .filter(device -> device.customerId().equals(customerId))
                .sorted(Comparator.comparing(Device::createdAt))
                .toList();
    }
}
