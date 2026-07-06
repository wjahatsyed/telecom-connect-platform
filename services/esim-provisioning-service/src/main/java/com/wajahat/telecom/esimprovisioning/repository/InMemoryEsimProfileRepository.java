package com.wajahat.telecom.esimprovisioning.repository;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryEsimProfileRepository implements EsimProfileRepository {

    private final ConcurrentHashMap<String, EsimProfile> profiles = new ConcurrentHashMap<>();

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
