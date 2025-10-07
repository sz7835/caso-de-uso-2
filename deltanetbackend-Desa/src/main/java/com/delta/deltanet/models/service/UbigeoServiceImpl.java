package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IUbigeoDao;
import com.delta.deltanet.models.entity.Ubigeo;

@Service
public class UbigeoServiceImpl implements IUbigeoService {
	
	@Autowired
	private IUbigeoDao ubigeoDao;

	@Override
	public List<Ubigeo> findAll() {
		return ubigeoDao.findAll();
	}

	@Override
	public Ubigeo findById(String id) {
		return ubigeoDao.findById(id).orElse(null);
	}

	@Override
	public Ubigeo save(Ubigeo Ubigeo) {
		return ubigeoDao.save(Ubigeo);
	}

	@Override
	public void delete(String id) {
		ubigeoDao.deleteById(id);
	}
	
}
