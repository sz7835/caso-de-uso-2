package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IContactoTelfDao;
import com.delta.deltanet.models.entity.ContactoTelf;

@Service
public class ContactoTelfServiceImpl implements IContactoTelfService {
	
	@Autowired
	private IContactoTelfDao contactoTelfDao;

	@Override
	public List<ContactoTelf> findAll() {
		return contactoTelfDao.findAll();
	}

	@Override
	public ContactoTelf findById(Long id) {
		return contactoTelfDao.findById(id).orElse(null);
	}

	@Override
	public ContactoTelf save(ContactoTelf contactoTelf) {
		return contactoTelfDao.save(contactoTelf);
	}

	@Override
	public void delete(Long id) {
		contactoTelfDao.deleteById(id);
	}
	
}
