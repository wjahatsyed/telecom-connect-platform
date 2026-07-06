package com.wajahat.telecom.esimprovisioning.service;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;
import com.wajahat.telecom.esimprovisioning.dto.ProvisionEsimRequest;
import com.wajahat.telecom.esimprovisioning.exception.EsimProfileNotFoundException;
import com.wajahat.telecom.esimprovisioning.exception.InvalidEsimStateException;
import com.wajahat.telecom.esimprovisioning.repository.EsimProfileRepository;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsimProvisioningService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final EsimProfileRepository esimProfileRepository;
    private final Clock clock;

    @Autowired
    public EsimProvisioningService(EsimProfileRepository esimProfileRepository) {
        this(esimProfileRepository, Clock.systemUTC());
    }

    EsimProvisioningService(EsimProfileRepository esimProfileRepository, Clock clock) {
        this.esimProfileRepository = esimProfileRepository;
        this.clock = clock;
    }

    public EsimProfile provision(ProvisionEsimRequest request) {
        Instant now = Instant.now(clock);
        EsimProfile profile = new EsimProfile(
                generateIccid(),
                request.customerId(),
                request.deviceId(),
                request.subscriptionId(),
                generateActivationCode(),
                EsimStatus.PROVISIONED,
                now,
                now);

        return esimProfileRepository.save(profile);
    }

    public EsimProfile getByIccid(String iccid) {
        return esimProfileRepository.findByIccid(iccid)
                .orElseThrow(() -> new EsimProfileNotFoundException(iccid));
    }

    public EsimProfile activate(String iccid) {
        EsimProfile profile = getByIccid(iccid);
        if (profile.status() != EsimStatus.PROVISIONED && profile.status() != EsimStatus.SUSPENDED) {
            throw new InvalidEsimStateException(iccid, profile.status(), "activate");
        }
        return saveWithStatus(profile, EsimStatus.ACTIVE);
    }

    public EsimProfile suspend(String iccid) {
        EsimProfile profile = getByIccid(iccid);
        if (profile.status() != EsimStatus.ACTIVE) {
            throw new InvalidEsimStateException(iccid, profile.status(), "suspend");
        }
        return saveWithStatus(profile, EsimStatus.SUSPENDED);
    }

    public EsimProfile terminate(String iccid) {
        EsimProfile profile = getByIccid(iccid);
        if (profile.status() == EsimStatus.TERMINATED) {
            throw new InvalidEsimStateException(iccid, profile.status(), "terminate");
        }
        return saveWithStatus(profile, EsimStatus.TERMINATED);
    }

    private EsimProfile saveWithStatus(EsimProfile profile, EsimStatus status) {
        return esimProfileRepository.save(profile.withStatus(status, Instant.now(clock)));
    }

    private static String generateIccid() {
        return "89" + Math.abs(RANDOM.nextLong() % 1_000_000_000_000_0000L);
    }

    private static String generateActivationCode() {
        return "LPA:1$telecom-connect.example$" + UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);
    }
}
