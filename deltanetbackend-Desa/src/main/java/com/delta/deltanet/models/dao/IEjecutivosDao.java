package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Ejecutivos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IEjecutivosDao extends JpaRepository<Ejecutivos,Long> {
	@Query("SELECT COUNT(e) FROM Ejecutivos e WHERE LOWER(TRIM(e.nombrePuesto)) = LOWER(TRIM(:nombrePuesto)) AND e.persona.id = :idPersona AND e.estado = 1 AND (:exceptId IS NULL OR e.id <> :exceptId)")
	long countByNombrePuestoAndPersonaActivo(@Param("nombrePuesto") String nombrePuesto, @Param("idPersona") Long idPersona, @Param("exceptId") Long exceptId);
}
