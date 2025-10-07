package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.Profesion;

public interface ProfesionDao extends JpaRepository<Profesion, Long> {
	@Query("SELECT COUNT(p) FROM Profesion p WHERE LOWER(TRIM(p.descripcion)) = LOWER(TRIM(:descripcion)) AND p.estado = 1")
	Long countByDescripcionActivo(@Param("descripcion") String descripcion);
}