package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IPersonaClienteDao;
import com.delta.deltanet.models.entity.PersonaCliente;

@Service
public class PersonaClienteServiceImpl implements IPersonaClienteService {
	
	@Autowired
	private IPersonaClienteDao personaClienteDao;

	@Override
	public List<PersonaCliente> findAll() {
		return personaClienteDao.findAll();
	}


	@Override
	public List<PersonaCliente> findAllP(Long idPersona) {
		return personaClienteDao.findAllP(idPersona);
	}

	@Override
	public List<PersonaCliente> findAllPS(Long idPersona, Long estado) {
		return personaClienteDao.findAllPS(idPersona, estado);
	}

	@Override
	public PersonaCliente findById(Long id) {
		return personaClienteDao.findById(id).orElse(null);
	}

	@Override
	public PersonaCliente save(PersonaCliente person) {
		return personaClienteDao.save(person);
	}

	@Override
	public void delete(Long id) {
		personaClienteDao.deleteById(id);
	}

	@Override
	public PersonaCliente findPersonaAndClient(Long idPersona, Long idCliente) {
		return personaClienteDao.findPersonaAndClient(idPersona, idCliente);
	}
	
}
