package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Ejecutivos;

public interface IEjecutivosService {
	public boolean existsNombrePuestoActivoPorPersona(String nombrePuesto, Long idPersona, Long exceptId);
	public List<Ejecutivos> findAll();
	public Ejecutivos findById(Long id);
	public Ejecutivos save(Ejecutivos ejecutivo);
	public Ejecutivos update(Ejecutivos ejecutivo);
	public Ejecutivos changeEstado(Long id, Integer estado, String username);
}
