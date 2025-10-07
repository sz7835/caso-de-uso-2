package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.TipoVentaEvento;

@Repository
public interface TipoVentaEventoDao extends JpaRepository<TipoVentaEvento, Integer> {

        @Query("SELECT COUNT(t) > 0 FROM TipoVentaEvento t WHERE t.estado = 1 AND LOWER(TRIM(t.descripcion)) = LOWER(TRIM(:descripcion))")
        boolean existsByDescripcionActiva(@Param("descripcion") String descripcion);

        @Query("SELECT t FROM TipoVentaEvento t WHERE " +
                        "((:id IS NULL) OR (:id = 0) OR (t.id = :id)) AND " +
                        "((:nombre IS NULL) OR (:nombre = '') OR (LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :nombre, '%')))) AND "
                        +
                        "((:estado IS NULL) OR (:estado = 0) OR (t.estado = :estado))")
        List<TipoVentaEvento> buscarTipoVentaEvento(
                        @Param("id") Integer id,
                        @Param("nombre") String nombre,
                        @Param("estado") Integer estado);

        @Query(value = "SELECT " +
                        "    t.id AS id, " +
                        "    t.descripcion AS descripcion, " +
                        "    COALESCE( " +
                        "        (SELECT v2.descripcion " +
                        "         FROM com_venta_evento v2 " +
                        "         WHERE v2.tipo = t.id " +
                        "         AND (:fechaInicio IS NULL OR v2.fecha >= :fechaInicio) " +
                        "         AND (:fechaFin IS NULL OR v2.fecha < DATE_ADD(:fechaFin, INTERVAL 1 DAY)) " +
                        "         AND v2.descripcion IS NOT NULL " +
                        "         ORDER BY v2.fecha DESC, v2.create_date DESC " +
                        "         LIMIT 1), " +
                        "        '-') AS ultimaDescripcion, " +
                        "    COALESCE(COUNT(DISTINCT v.id), 0) AS totalSolicitudes, " +
                        "    COALESCE(COUNT(DISTINCT CASE WHEN v.estado = 1 THEN v.id END), 0) AS solicitudesAtendidas, "
                        +
                        "    COALESCE(COUNT(DISTINCT CASE WHEN v.estado = 2 THEN v.id END), 0) AS solicitudesNoAtendidas, "
                        +
                        "    t.estado AS estado, " +
                        "    t.create_date AS fechaSolicitud " +
                        "FROM " +
                        "    com_venta_evento_tipo t " +
                        "LEFT JOIN " +
                        "    com_venta_evento v ON v.tipo = t.id " +
                        "    AND (:fechaInicio IS NULL OR v.fecha >= :fechaInicio) " +
                        "    AND (:fechaFin IS NULL OR v.fecha < DATE_ADD(:fechaFin, INTERVAL 1 DAY)) " +
                        "WHERE " +
                        "    (:estado = 0 OR t.estado = :estado) " +
                        "    AND (:tipo = 0 OR t.id = :tipo) " +
                        "GROUP BY " +
                        "    t.id, t.descripcion, t.estado, t.create_date " +
                        "ORDER BY " +
                        "    t.id", nativeQuery = true)
        List<Object[]> buscarTipoVentaEventoConEstadisticas(
                        @Param("estado") Integer estado,
                        @Param("tipo") Integer tipo,
                        @Param("fechaInicio") String fechaInicio,
                        @Param("fechaFin") String fechaFin);

}