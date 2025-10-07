package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ServicioHabilitado;

public interface IServicioHabilitadoService {

	public List<ServicioHabilitado> buscarServicio(Integer id);

	public List<ServicioHabilitado> buscarporServicio(Long id);

	public void deleteService(Long id);

	public void insert(Long id, Long servicioID);
}
