package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Profesion;

import java.util.List;

public interface IProfesionService {

	/**
	 * Verifica si existe un registro activo con la misma descripción (ignorando mayúsculas/minúsculas y espacios).
	 * Si idExcluir no es null, lo excluye de la búsqueda (para update).
	 */
	boolean existsDescripcionActivo(String descripcion, Long idExcluir);
    
    public List<Profesion> findAll();
	public Profesion findById(Long id);
	public Profesion save(Profesion reg);
	public Profesion update(Profesion reg);
	public Profesion changeEstado(Long id, Integer estado, String username);
	
}
