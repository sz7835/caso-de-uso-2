package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.delta.deltanet.models.entity.SrvEstado;

public interface ISrvEstadoDao extends JpaRepository<SrvEstado, Long> {

	@Query("SELECT COUNT(e) FROM SrvEstado e WHERE LOWER(TRIM(e.nombre)) = LOWER(TRIM(:nombre)) AND e.estado = 1")
	Long countByNombreActivo(@Param("nombre") String nombre);

	@Query("from SrvEstado where estado = 1 and id = ?1")
	public SrvEstado findByPk(Long id);
}
