package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.LiquidacionTipoOperacion;

public interface LiquidacionTipoOperacionService {
	boolean existsNombreActivo(String nombre, Long idExcluir);
	List<LiquidacionTipoOperacion> findAll();
	LiquidacionTipoOperacion getById(Long id);
	public LiquidacionTipoOperacion findById(Long id);
	public LiquidacionTipoOperacion save(LiquidacionTipoOperacion reg);
	public LiquidacionTipoOperacion update(LiquidacionTipoOperacion reg);
	public LiquidacionTipoOperacion changeEstado(Long id, Integer estado, String username);
}