package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PuestoOcupacionalRelacionRequisito;

import java.util.List;
import java.util.Optional;

public interface PuestoOcupacionalRelacionRequisitoDao extends JpaRepository<PuestoOcupacionalRelacionRequisito, Long> {

    List<PuestoOcupacionalRelacionRequisito> findByPuestoId(Long puestoId);

    @Query("SELECT r FROM PuestoOcupacionalRelacionRequisito r JOIN FETCH r.requisito WHERE r.puestoId = :puestoId")
    List<PuestoOcupacionalRelacionRequisito> findByPuestoRequisito(
            @Param("puestoId") Long puestoId);

    @Query("from PuestoOcupacionalRelacionRequisito a where a.puestoId = ?1 and a.id = ?2")
    Optional<PuestoOcupacionalRelacionRequisito> busca(Long idPuesto, Long idRequisito);

}
