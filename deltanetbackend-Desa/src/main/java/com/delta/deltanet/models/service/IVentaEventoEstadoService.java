package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.VentaEventoEstado;

public interface IVentaEventoEstadoService {
	boolean existsDescripcionActivo(String descripcion, Long exceptId);
	public List<VentaEventoEstado> findAll();
	public VentaEventoEstado findById(Long id);
	public VentaEventoEstado save(VentaEventoEstado reg);
	public VentaEventoEstado update(VentaEventoEstado reg);
	public VentaEventoEstado changeEstado(Long id, Integer estado, String username);
}
