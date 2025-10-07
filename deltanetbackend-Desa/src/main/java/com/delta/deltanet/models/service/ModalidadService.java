package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Modalidad;

public interface ModalidadService {

	List<Modalidad> findByEstado(int estado);

	List<Modalidad> findByNombreAndEstadoLike(String nombre, int estado);
	
	Modalidad findById(Long id);

	Modalidad save(Modalidad modalidad);

	List<Modalidad> findAll();
}
