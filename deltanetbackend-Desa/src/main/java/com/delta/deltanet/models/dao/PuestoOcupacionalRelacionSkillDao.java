package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.PuestoOcupacionalRelacionSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PuestoOcupacionalRelacionSkillDao extends JpaRepository<PuestoOcupacionalRelacionSkill, Long> {

    List<PuestoOcupacionalRelacionSkill> findByPuestoId(Long puestoId);

    @Query("from PuestoOcupacionalRelacionSkill a where a.puestoId = ?1 and a.id = ?2")
    Optional<PuestoOcupacionalRelacionSkill> busca(Long puestoId, Long skillId);
}