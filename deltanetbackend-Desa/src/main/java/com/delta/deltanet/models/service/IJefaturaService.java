package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Jefatura;

public interface IJefaturaService {
	public boolean existsCombinacionActiva(String nombre, String nombrePuesto, Long idGerencia, Long idPersona, Long exceptId);
	public List<Jefatura> findAll();
	public Jefatura findById(Long id);
	public Jefatura save(Jefatura ejecutivo);
	public Jefatura update(Jefatura ejecutivo);
	public Jefatura changeEstado(Long id, Integer estado, String username);
}
