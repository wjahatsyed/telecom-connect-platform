package com.wajahat.telecom.esimprovisioning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import com.wajahat.telecom.esimprovisioning.domain.EsimStatus;
import com.wajahat.telecom.esimprovisioning.dto.ProvisionEsimRequest;
import com.wajahat.telecom.esimprovisioning.exception.InvalidEsimStateException;
import com.wajahat.telecom.esimprovisioning.repository.EsimProfileRepository;
import com.wajahat.telecom.esimprovisioning.service.EsimProvisioningService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EsimProvisioningServiceTests {

    private final EsimProfileRepository repository = new TestEsimProfileRepository();
    private final EsimProvisioningService service = new EsimProvisioningService(repository);

    @Test
    void provisionsEsimProfile() {
        EsimProfile profile = service.provision(new ProvisionEsimRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()));

        assertThat(profile.iccid()).startsWith("89");
        assertThat(profile.activationCode()).startsWith("LPA:1$");
        assertThat(profile.status()).isEqualTo(EsimStatus.PROVISIONED);
    }

    @Test
    void supportsActivationSuspensionAndTermination() {
        EsimProfile profile = service.provision(new ProvisionEsimRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()));

        assertThat(service.activate(profile.iccid()).status()).isEqualTo(EsimStatus.ACTIVE);
        assertThat(service.suspend(profile.iccid()).status()).isEqualTo(EsimStatus.SUSPENDED);
        assertThat(service.terminate(profile.iccid()).status()).isEqualTo(EsimStatus.TERMINATED);
    }

    @Test
    void rejectsInvalidTransition() {
        EsimProfile profile = service.provision(new ProvisionEsimRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()));

        assertThatThrownBy(() -> service.suspend(profile.iccid()))
                .isInstanceOf(InvalidEsimStateException.class);
    }

    private static class TestEsimProfileRepository implements EsimProfileRepository {

        private final java.util.Map<String, EsimProfile> profiles = new java.util.LinkedHashMap<>();

        @Override
        public EsimProfile save(EsimProfile profile) {
            profiles.put(profile.iccid(), profile);
            return profile;
        }

        @Override
        public Optional<EsimProfile> findByIccid(String iccid) {
            return Optional.ofNullable(profiles.get(iccid));
        }
    }
}
