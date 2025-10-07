package com.delta.deltanet.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.VentaEvento;

public interface VentaEventoDAO extends JpaRepository<VentaEvento, Integer> {

        @Query("SELECT ve FROM VentaEvento ve WHERE "
                        + "(:id = 0 OR ve.id = :id) AND "
                        + "(:descripcion IS NULL OR ve.descripcion LIKE %:descripcion%) AND "
                        + "(:estado = 0 OR ve.estado = :estado) AND "
                        + "(:tipo = 0 OR ve.tipoVentaEvento.id = :tipo) AND "
                        + "(:fechaInicio IS NULL OR ve.fecha >= :fechaInicio) AND "
                        + "(:fechaFin IS NULL OR ve.fecha <= :fechaFin)")
        List<VentaEvento> findWithFilters(
                        @Param("id") Integer id,
                        @Param("descripcion") String descripcion,
                        @Param("estado") Integer estado,
                        @Param("tipo") Integer tipo,
                        @Param("fechaInicio") Date fechaInicio,
                        @Param("fechaFin") Date fechaFin);
}
