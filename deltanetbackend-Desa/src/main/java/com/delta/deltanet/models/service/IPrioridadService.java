
	package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Prioridad;

public interface IPrioridadService {
	public List<Prioridad> findAll();
	public List<Prioridad> findAllActivos();
	public Prioridad findById(Long id);
	public Prioridad save(Prioridad prioridad);
	public void delete(Long id);
	public boolean existsNombreActivo(String nombre, Long idIgnore);
}
