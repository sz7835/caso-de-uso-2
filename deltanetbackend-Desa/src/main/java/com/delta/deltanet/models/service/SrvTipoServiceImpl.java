package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IServicioTipoDao;
import com.delta.deltanet.models.entity.ServicioTipo;

@Service
public class SrvTipoServiceImpl implements ISrvTipoService {
	
	@Autowired
	private IServicioTipoDao tipoServicio;

	@Override
	public List<ServicioTipo> listaTipos() {
		return tipoServicio.listaTipos();
	}

	@Override
	public ServicioTipo buscaById(Long id) {
		return tipoServicio.findById(id).orElse(null);
	}

	@Override
	public ServicioTipo save(ServicioTipo servicioTipo) {
		return tipoServicio.save(servicioTipo);
	}

	@Override
	public List<ServicioTipo> findByEstado(int estado) {
		return tipoServicio.findByEstado(estado);
	}

	@Override
	public List<ServicioTipo> findAll() {
		return tipoServicio.findAll();
	}

	@Override
	public List<ServicioTipo> findByNombreAndEstado(String nombre, int estado) {
		return tipoServicio.findByNombreAndEstado(nombre, estado);
	}
}
