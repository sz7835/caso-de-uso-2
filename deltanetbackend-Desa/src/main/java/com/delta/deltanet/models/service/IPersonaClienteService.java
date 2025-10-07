package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.PersonaCliente;

public interface IPersonaClienteService {

	public List<PersonaCliente> findAll();
	public List<PersonaCliente> findAllP(Long id);
	public List<PersonaCliente> findAllPS(Long id, Long estado);
	public PersonaCliente findById(Long id);
	public PersonaCliente save(PersonaCliente ubigeo);
	public void delete(Long id);
	public PersonaCliente findPersonaAndClient(Long idPersona, Long idCliente);
	
}
