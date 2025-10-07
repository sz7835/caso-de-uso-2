package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Tarifario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TarifarioDao extends JpaRepository<Tarifario, Long> {

    @Query("SELECT t FROM Tarifario t WHERE " +
            "(:fechaInicio IS NULL OR :fechaFin IS NULL OR " +
            "(t.fechaIni <= :fechaFin AND t.fechaFin >= :fechaInicio)) AND " +
            "(:lugar = 0 OR t.lugar = :lugar) AND " +
            "(:moneda = 0 OR t.moneda = :moneda) AND " +
            "(:estado = 0 OR t.estado = :estado)")
    List<Tarifario> buscarTarifarios(@Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
            @Param("lugar") Integer lugar,
            @Param("moneda") Integer moneda,
            @Param("estado") Integer estado);

}
