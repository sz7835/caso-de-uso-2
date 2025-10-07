package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoActividad;

import java.util.List;

public interface ITipoActividad2Service {
	boolean existsNombreActivo(String nombre, Long exceptId);
    public List<TipoActividad> findAll();
	public TipoActividad findById(Long id);
	public TipoActividad save(TipoActividad reg);
	public TipoActividad update(TipoActividad reg);
	public TipoActividad changeEstado(Long id, Integer estado, String username);
}
