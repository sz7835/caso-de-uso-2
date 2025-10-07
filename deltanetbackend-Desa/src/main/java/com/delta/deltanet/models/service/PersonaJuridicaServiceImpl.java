package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IPersonaJuridicaDao;
import com.delta.deltanet.models.entity.PersonaJuridica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaJuridicaServiceImpl implements IPersonaJuridicaService {

    @Autowired
    IPersonaJuridicaDao personaJuridicaDao;
    @Override
    public PersonaJuridica save(PersonaJuridica personaJuridica) {
        return personaJuridicaDao.save(personaJuridica);
    }

    @Override
    public Optional<PersonaJuridica> findById(Long id) {
        return personaJuridicaDao.findById(id);
    }

	@Override
	public List<Object> listaClientes() {
		return personaJuridicaDao.listaClientes();
	}
}
