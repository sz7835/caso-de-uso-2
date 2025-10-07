package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Empresa;

public interface IEmpresaService {
	
	public List<Empresa> findAll();
	public Empresa findById(Long id);
	public Empresa save(Empresa empresa);
	public void delete(Long id);
	
}
