package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ServicioProyecto;

public interface ISrvProyectoService {
	public List<ServicioProyecto> listaProyectos();
	public ServicioProyecto buscaById(Long id);
	public ServicioProyecto save(ServicioProyecto reg);

}
