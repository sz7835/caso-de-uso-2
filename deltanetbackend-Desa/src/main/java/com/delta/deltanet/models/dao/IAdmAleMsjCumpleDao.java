package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdmAleMsjCumpleDao extends JpaRepository<AdmAleMsjCumple, Integer> {

    // This matches the name your service is calling.
    // Native SQL so we can ORDER BY id DESC LIMIT 1 reliably.
    @Query(
        value = "SELECT * FROM adm_ale_msj_cumple " +
                "WHERE id_sexo = :sexoId AND estado_reg = :estadoReg " +
                "ORDER BY id DESC LIMIT 1",
        nativeQuery = true
    )
    AdmAleMsjCumple findTopBySexoIdAndEstadoRegOrderByIdDesc(@Param("sexoId") Integer sexoId,
                                                             @Param("estadoReg") Integer estadoReg);

    // (Optional) A clean Spring-Data derived query you can migrate your service to later:
    // AdmAleMsjCumple findFirstBySexo_IdAndEstadoRegOrderByIdDesc(Integer sexoId, Integer estadoReg);
}
