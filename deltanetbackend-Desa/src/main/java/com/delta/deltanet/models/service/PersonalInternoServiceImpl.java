package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IPersonalInternoDao;
import com.delta.deltanet.models.entity.PersonalInterno;

@Service
public class PersonalInternoServiceImpl implements IPersonalInternoService {
	
	@Autowired
	private IPersonalInternoDao personalInternoDao;

	@Override
	public List<PersonalInterno> findAll() {
		return personalInternoDao.findAll();
	}

	@Override
	public PersonalInterno findById(Long id) {
		return personalInternoDao.findById(id).orElse(null);
	}

	@Override
	public PersonalInterno save(PersonalInterno personalInterno) {
		return personalInternoDao.save(personalInterno);
	}

	@Override
	public void delete(Long id) {
		personalInternoDao.deleteById(id);
	}

	@Override
	public PersonalInterno findByUsuario(String usuario) {
		return personalInternoDao.findByUsuario(usuario);
	}
	
}
