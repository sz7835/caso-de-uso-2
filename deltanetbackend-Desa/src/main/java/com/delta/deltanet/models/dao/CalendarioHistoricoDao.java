package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.CalendarioHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CalendarioHistoricoDao extends JpaRepository<CalendarioHistorico, Long> {

    @Query("SELECT c FROM CalendarioHistorico c " +
           "WHERE (:idAnio IS NULL OR c.idAnio = :idAnio) " +
           "AND (:fecha IS NULL OR FUNCTION('DATE', c.fecha) = :fecha) " +
           "AND (:idMes IS NULL OR c.idMes = :idMes) " +
           "AND (:idSemanaDiaTipo IS NULL OR c.idSemanaDiaTipo = :idSemanaDiaTipo) " +
           "AND (:estado IS NULL OR c.estado = :estado)")
    List<CalendarioHistorico> findByFiltros(
            @Param("idAnio") Long idAnio,
            @Param("fecha") Date fecha,
            @Param("idMes") Long idMes,
            @Param("idSemanaDiaTipo") Long idSemanaDiaTipo,
            @Param("estado") Long estado
    );
}