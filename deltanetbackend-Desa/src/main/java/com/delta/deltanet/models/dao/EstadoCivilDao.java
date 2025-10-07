package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.EstadoCivil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstadoCivilDao extends JpaRepository<EstadoCivil, Long> {

	@Query("SELECT COUNT(e) FROM EstadoCivil e WHERE LOWER(TRIM(e.nombre)) = LOWER(TRIM(:nombre)) AND e.estado = 1")
	Long countByNombreActivo(@Param("nombre") String nombre);

}
