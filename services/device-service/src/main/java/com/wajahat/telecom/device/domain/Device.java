package com.wajahat.telecom.device.domain;

import java.time.Instant;
import java.util.UUID;

public record Device(
        UUID deviceId,
        UUID customerId,
        DeviceType type,
        String name,
        String imei,
        String eid,
        DeviceStatus status,
        Instant createdAt,
        Instant updatedAt,
        Long version) {
}
