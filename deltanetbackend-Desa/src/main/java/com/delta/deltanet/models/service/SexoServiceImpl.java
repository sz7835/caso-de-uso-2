package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ISexoDao;
import com.delta.deltanet.models.entity.Sexo;

@Service
public class SexoServiceImpl implements ISexoService {
	
	@Autowired
	private ISexoDao sexoDao;

	@Override
	public List<Sexo> findAll() {
		return sexoDao.findAll();
	}

	@Override
	public Sexo findById(Long id) {
		return sexoDao.findById(id).orElse(null);
	}

	@Override
	public Sexo save(Sexo sexo) {
		return sexoDao.save(sexo);
	}

	@Override
	public void delete(Long id) {
		sexoDao.deleteById(id);
	}
	
}
