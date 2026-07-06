package com.wajahat.telecom.device.repository;

import com.wajahat.telecom.device.domain.Device;
import com.wajahat.telecom.device.domain.DeviceStatus;
import com.wajahat.telecom.device.domain.DeviceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "devices")
class DeviceEntity {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;

    @Column(unique = true)
    private String imei;

    @Column(unique = true)
    private String eid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected DeviceEntity() {
    }

    private DeviceEntity(Device device) {
        this.id = device.deviceId();
        this.customerId = device.customerId();
        this.name = device.name();
        this.type = device.type();
        this.imei = device.imei();
        this.eid = device.eid();
        this.status = device.status();
        this.createdAt = device.createdAt();
        this.updatedAt = device.updatedAt();
        this.version = device.version();
    }

    static DeviceEntity from(Device device) {
        return new DeviceEntity(device);
    }

    Device toDomain() {
        return new Device(id, customerId, type, name, imei, eid, status, createdAt, updatedAt, version);
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
