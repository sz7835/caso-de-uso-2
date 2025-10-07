package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.delta.deltanet.models.entity.AutenticacionUsuario;

public interface IAutenticacionUsuarioService {
	
	public List<AutenticacionUsuario> findAll();
	public AutenticacionUsuario buscaUserDelta(Long id);
	public AutenticacionUsuario buscaUserDelta2(Long id);
	public Optional<AutenticacionUsuario> findById(Long id);
	public AutenticacionUsuario save(AutenticacionUsuario autenticacionUsuario);
	public void delete(Long id);
	public AutenticacionUsuario findByUsuario(String usuario);
	public Map<String, Object> findAllFiltroPaginado(Long idArea,
													String usuario,
													String nombre,
													String apellidos,
													int length,
													int start);
	public List<Object> buscaUsuarioAutenticado(String usuario);
	public List<Object> listaFullUsuarios();
	public AutenticacionUsuario buscaById(Long id);
	public List<Object> listaRecursos();
}
