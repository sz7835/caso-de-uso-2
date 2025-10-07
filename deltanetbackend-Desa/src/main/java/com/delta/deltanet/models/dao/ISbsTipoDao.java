package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SbsTipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISbsTipoDao extends JpaRepository<SbsTipo, Integer> {
	int countByNombreAndEstado(String nombre, int estado);
	int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id);
}
