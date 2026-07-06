package com.wajahat.telecom.device.dto;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.domain.DeviceType;
import java.time.Instant;
import java.util.UUID;

public record DeviceResponse(
        UUID deviceId,
        UUID customerId,
        DeviceType type,
        String name,
        String imei,
        Instant createdAt) {

    public static DeviceResponse from(Device device) {
        return new DeviceResponse(
                device.deviceId(),
                device.customerId(),
                device.type(),
                device.name(),
                device.imei(),
                device.createdAt());
    }
}
