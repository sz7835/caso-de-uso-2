package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.VentaEventoEstado;

public interface VentaEventoEstadoDao extends JpaRepository<VentaEventoEstado, Long> {
	@Query("SELECT COUNT(v) FROM VentaEventoEstado v WHERE LOWER(TRIM(v.descripcion)) = LOWER(TRIM(:descripcion)) AND v.estado = 1 AND (:exceptId IS NULL OR v.id <> :exceptId)")
	long countByDescripcionActivo(@Param("descripcion") String descripcion, @Param("exceptId") Long exceptId);
}
