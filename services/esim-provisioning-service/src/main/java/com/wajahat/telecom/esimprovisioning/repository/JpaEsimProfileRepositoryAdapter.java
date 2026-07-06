package com.wajahat.telecom.esimprovisioning.repository;

import com.wajahat.telecom.esimprovisioning.domain.EsimProfile;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaEsimProfileRepositoryAdapter implements EsimProfileRepository {

    private final JpaEsimProfileRepository jpaRepository;

    JpaEsimProfileRepositoryAdapter(JpaEsimProfileRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EsimProfile save(EsimProfile profile) {
        return jpaRepository.save(EsimProfileEntity.from(profile)).toDomain();
    }

    @Override
    public Optional<EsimProfile> findByIccid(String iccid) {
        return jpaRepository.findById(iccid).map(EsimProfileEntity::toDomain);
    }
}
