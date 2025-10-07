package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.JefaturaPuesto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IJefaturaPuestoDao extends JpaRepository<JefaturaPuesto,Long> {

	@Query("SELECT COUNT(p) FROM JefaturaPuesto p WHERE LOWER(TRIM(p.nombre)) = LOWER(TRIM(:nombre)) AND p.jefatura.id = :idJefatura AND p.estado = 1 AND (:exceptId IS NULL OR p.id <> :exceptId)")
	long countByNombreAndJefaturaAndEstadoActivo(@Param("nombre") String nombre, @Param("idJefatura") Long idJefatura, @Param("exceptId") Long exceptId);
}
