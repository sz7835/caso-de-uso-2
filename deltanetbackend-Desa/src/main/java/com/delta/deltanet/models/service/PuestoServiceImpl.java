package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IPuestoDao;
import com.delta.deltanet.models.entity.Puestos;

@Service
public class PuestoServiceImpl implements IPuestoService {
	
	@Autowired
	private IPuestoDao puestoDao;

	@Override
	public List<Puestos> findAll() {
		return puestoDao.findAll();
	}

	@Override
	public Puestos findById(Long id) {
		return puestoDao.findById(id).orElse(null);
	}

	@Override
	public Puestos save(Puestos puestos) {
		return puestoDao.save(puestos);
	}

	@Override
	public void delete(Long id) {
		puestoDao.deleteById(id);
	}
	
}
