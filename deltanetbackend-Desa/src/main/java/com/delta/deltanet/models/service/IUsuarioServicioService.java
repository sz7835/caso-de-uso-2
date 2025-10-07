package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Map;

import com.delta.deltanet.models.entity.UsuarioServicio;

public interface IUsuarioServicioService {
	
	public List<UsuarioServicio> findAll();
	public List<UsuarioServicio> listado();
	public UsuarioServicio findById(Long id);
	public UsuarioServicio findByIdAndEstado(Long id, String estado);
	public UsuarioServicio save(UsuarioServicio usuarioServicio);
	public void delete(Long id);
	public UsuarioServicio findByUsuario(String usuario);
	public Map<String, Object> findAllFiltroPaginado(Long idArea,
													String usuario,
													String nombre,
													String apellidos,
													int length,
													int start);
}
