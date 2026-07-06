package com.wajahat.telecom.esimprovisioning.repository;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;
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
@Table(name = "esim_profiles")
class EsimProfileEntity {

    @Id
    private String iccid;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "subscription_id")
    private UUID subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EsimStatus status;

    @Column(name = "activation_code", nullable = false)
    private String activationCode;

    @Column(name = "smdp_address", nullable = false)
    private String smdpAddress;

    @Column(name = "provisioned_at", nullable = false)
    private Instant provisionedAt;

    @Column(name = "activated_at")
    private Instant activatedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "terminated_at")
    private Instant terminatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected EsimProfileEntity() {
    }

    private EsimProfileEntity(EsimProfile profile) {
        this.iccid = profile.iccid();
        this.customerId = profile.customerId();
        this.deviceId = profile.deviceId();
        this.subscriptionId = profile.subscriptionId();
        this.status = profile.status();
        this.activationCode = profile.activationCode();
        this.smdpAddress = profile.smdpAddress();
        this.provisionedAt = profile.provisionedAt();
        this.activatedAt = profile.activatedAt();
        this.suspendedAt = profile.suspendedAt();
        this.terminatedAt = profile.terminatedAt();
        this.createdAt = profile.createdAt();
        this.updatedAt = profile.updatedAt();
        this.version = profile.version();
    }

    static EsimProfileEntity from(EsimProfile profile) {
        return new EsimProfileEntity(profile);
    }

    EsimProfile toDomain() {
        return new EsimProfile(
                iccid,
                customerId,
                deviceId,
                subscriptionId,
                activationCode,
                smdpAddress,
                status,
                provisionedAt,
                activatedAt,
                suspendedAt,
                terminatedAt,
                createdAt,
                updatedAt,
                version);
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
