package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.FormaFact;

import java.util.List;

public interface IFormaFactService {
	boolean existsNombreActivo(String nombre, Long exceptId);

	public List<FormaFact> findAll();
	public FormaFact findById(Long id);
	public FormaFact save(FormaFact reg);
	public FormaFact update(FormaFact reg);
	public FormaFact changeEstado(Long id, Integer estado, String username);
}
