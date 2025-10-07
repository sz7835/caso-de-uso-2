package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.FormaFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormaFactDao extends JpaRepository<FormaFact, Long> {
	@Query("SELECT COUNT(f) FROM FormaFact f WHERE LOWER(TRIM(f.nombre)) = LOWER(TRIM(:nombre)) AND f.estado = 1 AND (:exceptId IS NULL OR f.id <> :exceptId)")
	long countByNombreActivo(@Param("nombre") String nombre, @Param("exceptId") Long exceptId);
}
