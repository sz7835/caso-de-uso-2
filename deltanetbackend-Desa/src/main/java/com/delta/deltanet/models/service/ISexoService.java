package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Sexo;

public interface ISexoService {
	
	public List<Sexo> findAll();
	public Sexo findById(Long id);
	public Sexo save(Sexo sexo);
	public void delete(Long id);
	
}
