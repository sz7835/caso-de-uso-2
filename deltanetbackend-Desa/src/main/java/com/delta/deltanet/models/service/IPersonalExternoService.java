package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.PersonalExterno;

public interface IPersonalExternoService {
	
	public List<PersonalExterno> findAll();
	public PersonalExterno findById(Long id);
	public PersonalExterno findByUsuario(String usuario);
	public PersonalExterno save(PersonalExterno personalExterno);
	public void delete(Long id);
	
}
