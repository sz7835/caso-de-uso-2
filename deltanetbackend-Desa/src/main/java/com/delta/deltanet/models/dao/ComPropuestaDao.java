package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.ComPropuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComPropuestaDao extends JpaRepository<ComPropuesta, Integer> {
        @Query("SELECT p FROM ComPropuesta p WHERE " +
                        "(:fechaInicio IS NULL OR :fechaFin IS NULL OR p.fecha BETWEEN :fechaInicio AND :fechaFin) AND "
                        +
                        "(:monedaId = 0 OR p.moneda = :monedaId) AND " +
                        "(:estadoId = 0 OR p.estado = :estadoId)")
        List<ComPropuesta> findByFiltros(
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFin") LocalDate fechaFin,
                        @Param("monedaId") Integer monedaId,
                        @Param("estadoId") Integer estadoId);

        @Query("SELECT p FROM ComPropuesta p WHERE p.id = :idPropuesta")
        Optional<ComPropuesta> buscarPorId(@Param("idPropuesta") Integer idPropuesta);
}