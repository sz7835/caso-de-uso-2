package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ICargoDao;
import com.delta.deltanet.models.entity.Cargo;

@Service
public class CargoServiceImpl implements ICargoService {
	
	@Autowired
	private ICargoDao cargoDao;

	@Override
	public List<Cargo> findAll() {
		return cargoDao.findAll();
	}

	@Override
	public Cargo findById(Long id) {
		return cargoDao.findById(id).orElse(null);
	}

	@Override
	public Cargo save(Cargo Cargo) {
		return cargoDao.save(Cargo);
	}

	@Override
	public void delete(Long id) {
		cargoDao.deleteById(id);
	}
	
}
