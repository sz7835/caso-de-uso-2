package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteExperiencia;

import java.util.List;

public interface IPostulanteExperienciaService {
	boolean existsNombreActivo(String nombre, Long exceptId);
    public List<PostulanteExperiencia> findAll();
	public PostulanteExperiencia findById(Long id);
	public PostulanteExperiencia save(PostulanteExperiencia reg);
	public PostulanteExperiencia update(PostulanteExperiencia reg);
	public PostulanteExperiencia changeEstado(Long id, Integer estado, String username);
	
}
