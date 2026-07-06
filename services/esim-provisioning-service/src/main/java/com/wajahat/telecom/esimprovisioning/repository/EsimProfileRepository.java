package com.wajahat.telecom.esimprovisioning.repository;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import java.util.Optional;

public interface EsimProfileRepository {

    EsimProfile save(EsimProfile profile);

    Optional<EsimProfile> findByIccid(String iccid);
}
