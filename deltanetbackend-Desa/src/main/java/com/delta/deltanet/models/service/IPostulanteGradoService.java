package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteGrado;

import java.util.List;

public interface IPostulanteGradoService {
	boolean existsNombreActivo(String nombre, Long exceptId);
    public List<PostulanteGrado> findAll();
	public PostulanteGrado findById(Long id);
	public PostulanteGrado save(PostulanteGrado reg);
	public PostulanteGrado update(PostulanteGrado reg);
	public PostulanteGrado changeEstado(Long id, Integer estado, String username);
}
