package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.UsuarioServicio;

public interface IUsuarioServicioDao extends JpaRepository<UsuarioServicio, Long> {

	@Query("from UsuarioServicio where estadoRegistro = 'A'")
	public List<UsuarioServicio> findAll();
	public List<UsuarioServicio> findByEstadoRegistro(String estado);
	
	public UsuarioServicio findByIdAndEstadoRegistro(Long id, String estado);
	public UsuarioServicio findByUsuario(String usuario);
}
