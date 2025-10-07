package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.ServicioInformeUsuarios;

public interface ISrvInformeUsuariosDao extends JpaRepository<ServicioInformeUsuarios, Long> {
	
}
