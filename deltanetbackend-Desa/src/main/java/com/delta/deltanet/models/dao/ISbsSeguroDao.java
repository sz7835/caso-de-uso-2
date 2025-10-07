package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SbsSeguro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISbsSeguroDao extends JpaRepository<SbsSeguro, Integer> {
	int countByNombreAndEstado(String nombre, int estado);
	int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id);
	int countByCodsbsAndEstado(String codsbs, int estado);
	int countByCodsbsAndEstadoAndIdNot(String codsbs, int estado, Integer id);
}
