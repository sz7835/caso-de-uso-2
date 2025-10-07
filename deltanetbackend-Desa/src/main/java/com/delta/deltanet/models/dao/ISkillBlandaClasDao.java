package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SkillBlandaClas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ISkillBlandaClasDao extends JpaRepository<SkillBlandaClas,Long> {

	@Query("SELECT COUNT(s) FROM SkillBlandaClas s WHERE LOWER(TRIM(s.descripcion)) = LOWER(TRIM(:descripcion)) AND s.estado = 1")
	Long countByDescripcionActivo(@Param("descripcion") String descripcion);
}
