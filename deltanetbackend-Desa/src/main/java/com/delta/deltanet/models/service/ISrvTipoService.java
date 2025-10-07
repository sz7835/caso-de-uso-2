package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ServicioTipo;

public interface ISrvTipoService {
	List<ServicioTipo> listaTipos();
	ServicioTipo buscaById(Long id);
	ServicioTipo save(ServicioTipo servicioTipo);
	List<ServicioTipo> findByEstado(int estado);
	List<ServicioTipo> findAll();
	List<ServicioTipo> findByNombreAndEstado(String nombre, int estado);
}
