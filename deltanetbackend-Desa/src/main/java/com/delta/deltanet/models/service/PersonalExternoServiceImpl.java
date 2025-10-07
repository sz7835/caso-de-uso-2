package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IPersonalExternoDao;
import com.delta.deltanet.models.entity.PersonalExterno;

@Service
public class PersonalExternoServiceImpl implements IPersonalExternoService {
	
	@Autowired
	private IPersonalExternoDao personalExternoDao;

	@Override
	public List<PersonalExterno> findAll() {
		return personalExternoDao.findAll();
	}

	@Override
	public PersonalExterno findById(Long id) {
		return personalExternoDao.findById(id).orElse(null);
	}

	@Override
	public PersonalExterno save(PersonalExterno personalExterno) {
		return personalExternoDao.save(personalExterno);
	}

	@Override
	public void delete(Long id) {
		personalExternoDao.deleteById(id);
	}
	
	@Override
	public PersonalExterno findByUsuario(String usuario) {
		return personalExternoDao.findByUsuario(usuario);
	}
	
}
