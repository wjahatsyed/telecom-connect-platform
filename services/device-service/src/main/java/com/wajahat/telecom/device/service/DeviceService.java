package com.wajahat.telecom.device.service;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.dto.CreateDeviceRequest;
import com.wajahat.telecom.device.exception.DeviceNotFoundException;
import com.wajahat.telecom.device.repository.DeviceRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final Clock clock;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this(deviceRepository, Clock.systemUTC());
    }

    DeviceService(DeviceRepository deviceRepository, Clock clock) {
        this.deviceRepository = deviceRepository;
        this.clock = clock;
    }

    public Device create(CreateDeviceRequest request) {
        Device device = new Device(
                UUID.randomUUID(),
                request.customerId(),
                request.type(),
                request.name(),
                request.imei(),
                Instant.now(clock));

        return deviceRepository.save(device);
    }

    public Device getById(UUID deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    public List<Device> getByCustomerId(UUID customerId) {
        return deviceRepository.findByCustomerId(customerId);
    }
}
