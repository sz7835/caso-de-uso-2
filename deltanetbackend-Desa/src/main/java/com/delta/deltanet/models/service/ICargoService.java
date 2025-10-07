package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Cargo;

public interface ICargoService {
	
	public List<Cargo> findAll();
	public Cargo findById(Long id);
	public Cargo save(Cargo cargo);
	public void delete(Long id);
	
}
