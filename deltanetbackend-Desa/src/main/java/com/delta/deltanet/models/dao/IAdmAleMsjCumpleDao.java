package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdmAleMsjCumpleDao extends JpaRepository<AdmAleMsjCumple, Integer> {

    // Latest active template for a specific gender (by id_sexo)
    @Query(
        value = "SELECT * FROM adm_ale_msj_cumple " +
                "WHERE id_sexo = :sexo AND estado_reg = 1 " +
                "ORDER BY id DESC LIMIT 1",
        nativeQuery = true
    )
    AdmAleMsjCumple findLatestBySexo(@Param("sexo") Integer sexo);
}
