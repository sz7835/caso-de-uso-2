package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ContactoTelf;

public interface IContactoTelfService {
	
	public List<ContactoTelf> findAll();
	public ContactoTelf findById(Long id);
	public ContactoTelf save(ContactoTelf contactoTelf);
	public void delete(Long id);
	
}
