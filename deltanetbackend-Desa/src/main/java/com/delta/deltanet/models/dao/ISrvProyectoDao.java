package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.ServicioProyecto;

public interface ISrvProyectoDao extends JpaRepository<ServicioProyecto, Long> {
	
	@Query("from ServicioProyecto sp where sp.estado = 1")
	List<ServicioProyecto> listaProyectos();

}
