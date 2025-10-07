package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Puestos;

public interface IPuestoService {
	
	public List<Puestos> findAll();
	public Puestos findById(Long id);
	public Puestos save(Puestos sexo);
	public void delete(Long id);
	
}
