package com.delta.deltanet.models.repository;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdmAleMsjCumpleRepository extends JpaRepository<AdmAleMsjCumple, Integer> {
    Optional<AdmAleMsjCumple> findTopByIdSexoOrderByIdDesc(Integer idSexo);
}
