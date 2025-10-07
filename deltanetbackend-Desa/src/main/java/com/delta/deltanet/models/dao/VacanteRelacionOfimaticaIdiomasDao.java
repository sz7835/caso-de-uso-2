package com.delta.deltanet.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.VacanteRelacionOfimaticaIdiomas;

public interface VacanteRelacionOfimaticaIdiomasDao extends JpaRepository<VacanteRelacionOfimaticaIdiomas, Long> {

    List<VacanteRelacionOfimaticaIdiomas> findByVacanteId(Integer vacanteId);

    @Query("SELECT r FROM VacanteRelacionOfimaticaIdiomas r JOIN FETCH r.ofimaticaIdiomas WHERE r.vacanteId = :vacanteId")
    List<VacanteRelacionOfimaticaIdiomas> findByVacanteIdWithOfimaticaIdiomas(
            @Param("vacanteId") Integer vacanteId);

    @Query("from VacanteRelacionOfimaticaIdiomas a where a.vacanteId = ?1 and a.id = ?2")
    Optional<VacanteRelacionOfimaticaIdiomas> busca(Integer vacanteId, Long idConocimiento);

}
