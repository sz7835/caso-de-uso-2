package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.PersonalInterno;

public interface IPersonalInternoDao extends JpaRepository<PersonalInterno, Long> {
	public PersonalInterno findByUsuario(String usuario);
}

