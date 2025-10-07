package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.TrabajadorParentesco;

public interface ITrabajadorParentescoService {
	
	public List<TrabajadorParentesco> findAll();
	public TrabajadorParentesco findById(Long id);
	public TrabajadorParentesco save(TrabajadorParentesco reg);
	public TrabajadorParentesco update(TrabajadorParentesco reg);
	public TrabajadorParentesco changeEstado(Long id, Integer estado, String username);
	
}
