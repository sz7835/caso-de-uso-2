package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IModalidadDao;
import com.delta.deltanet.models.entity.Modalidad;

@Service
public class ModalidadServiceImpl implements ModalidadService{

	@Autowired
	private IModalidadDao modalidadDao;

	@Override

	public List<Modalidad> findByEstado(int estado) {
		return modalidadDao.findByEstado(estado);
	}

	@Override
	public List<Modalidad> findByNombreAndEstadoLike(String nombre, int estado) {
		return modalidadDao.findByNombreIgnoreCaseAndEstado(nombre, estado);
	}

	@Override
	public Modalidad findById(Long id) {
		return modalidadDao.findById(id).orElse(null);
	}

	@Override
	public Modalidad save(Modalidad modalidad) {
		return modalidadDao.save(modalidad);
	}

	@Override
	public List<Modalidad> findAll() {
		return modalidadDao.findAll();
	}
}
