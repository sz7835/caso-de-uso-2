package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.LiquidacionTipoOperacion;

public interface LiquidacionTipoOperacionDao extends JpaRepository<LiquidacionTipoOperacion, Long> {

	@Query("SELECT COUNT(l) FROM LiquidacionTipoOperacion l WHERE LOWER(TRIM(l.nombre)) = LOWER(TRIM(:nombre)) AND l.estado = 1")
	Long countByNombreActivo(@Param("nombre") String nombre);

}