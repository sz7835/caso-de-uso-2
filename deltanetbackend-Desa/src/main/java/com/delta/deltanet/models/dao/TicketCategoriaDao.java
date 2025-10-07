package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TicketCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketCategoriaDao extends JpaRepository<TicketCategoria, Long> {

	@Query("SELECT COUNT(c) FROM TicketCategoria c WHERE LOWER(TRIM(c.nombre)) = LOWER(TRIM(:nombre)) AND c.estado = 'A'")
	Long countByNombreActivo(@Param("nombre") String nombre);
}
