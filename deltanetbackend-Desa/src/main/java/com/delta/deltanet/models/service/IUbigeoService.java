package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Ubigeo;

public interface IUbigeoService {
	
	public List<Ubigeo> findAll();
	public Ubigeo findById(String id);
	public Ubigeo save(Ubigeo ubigeo);
	public void delete(String id);
	
}
