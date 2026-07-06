package com.wajahat.telecom.device.dto;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.domain.DeviceStatus;
import com.wajahat.telecom.device.domain.DeviceType;
import java.time.Instant;
import java.util.UUID;

public record DeviceResponse(
        UUID deviceId,
        UUID customerId,
        DeviceType type,
        String name,
        String imei,
        String eid,
        DeviceStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static DeviceResponse from(Device device) {
        return new DeviceResponse(
                device.deviceId(),
                device.customerId(),
                device.type(),
                device.name(),
                device.imei(),
                device.eid(),
                device.status(),
                device.createdAt(),
                device.updatedAt());
    }
}
