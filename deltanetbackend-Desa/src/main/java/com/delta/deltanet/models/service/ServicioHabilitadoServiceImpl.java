package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IServicioHabilitadoDao;
import com.delta.deltanet.models.entity.ServicioHabilitado;

@Service
public class ServicioHabilitadoServiceImpl implements IServicioHabilitadoService {

	@Autowired
	private IServicioHabilitadoDao servicioHabilitadoDao;

	@Override
	public List<ServicioHabilitado> buscarporServicio(Long id) {
		return servicioHabilitadoDao.buscarporServicio(id);
	}

	@Override
	public List<ServicioHabilitado> buscarServicio(Integer id) {
		return servicioHabilitadoDao.buscarServicio(id);
	}

	@Override
	public void deleteService(Long id) {
		servicioHabilitadoDao.deleteService(id);
	}

	@Override
	public void insert(Long id, Long servicioID) {
		servicioHabilitadoDao.insert(id, servicioID);
	}
}
