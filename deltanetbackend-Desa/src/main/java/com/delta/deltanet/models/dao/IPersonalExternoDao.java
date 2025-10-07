package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.PersonalExterno;

public interface IPersonalExternoDao extends JpaRepository<PersonalExterno, Long> {
	public PersonalExterno findByUsuario(String usuario);
}
