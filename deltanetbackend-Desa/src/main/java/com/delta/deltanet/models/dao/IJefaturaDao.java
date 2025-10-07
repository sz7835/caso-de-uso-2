package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Jefatura;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IJefaturaDao extends JpaRepository<Jefatura,Long> {
	@Query("SELECT COUNT(j) FROM Jefatura j WHERE LOWER(TRIM(j.nombre)) = LOWER(TRIM(:nombre)) AND LOWER(TRIM(j.nombrePuesto)) = LOWER(TRIM(:nombrePuesto)) AND j.gerencia.id = :idGerencia AND j.persona.id = :idPersona AND j.estado = 1 AND (:exceptId IS NULL OR j.id <> :exceptId)")
	long countByUniqueFieldsActivo(@Param("nombre") String nombre, @Param("nombrePuesto") String nombrePuesto, @Param("idGerencia") Long idGerencia, @Param("idPersona") Long idPersona, @Param("exceptId") Long exceptId);
}
