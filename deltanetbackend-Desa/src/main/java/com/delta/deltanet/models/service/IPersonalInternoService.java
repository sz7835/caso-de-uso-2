package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.PersonalInterno;

public interface IPersonalInternoService {
	
	public List<PersonalInterno> findAll();
	public PersonalInterno findById(Long id);
	public PersonalInterno findByUsuario(String usuario);
	public PersonalInterno save(PersonalInterno personalInterno);
	public void delete(Long id);
	
}
