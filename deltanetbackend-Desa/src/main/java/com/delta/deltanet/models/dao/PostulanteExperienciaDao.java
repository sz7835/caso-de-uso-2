package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.PostulanteExperiencia;

public interface PostulanteExperienciaDao extends JpaRepository<PostulanteExperiencia, Long> {

	@Query("SELECT COUNT(p) FROM PostulanteExperiencia p WHERE LOWER(TRIM(p.nombre)) = LOWER(TRIM(:nombre)) AND p.estado = 1 AND (:exceptId IS NULL OR p.id <> :exceptId)")
	long countByNombreActivo(@Param("nombre") String nombre, @Param("exceptId") Long exceptId);

}
