package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteFormacion;

import java.util.List;

public interface IPostulanteFormacionService {
	boolean existsNombreActivo(String nombre, Long exceptId);
    public List<PostulanteFormacion> findAll();
	public PostulanteFormacion findById(Long id);
	public PostulanteFormacion save(PostulanteFormacion reg);
	public PostulanteFormacion update(PostulanteFormacion reg);
	public PostulanteFormacion changeEstado(Long id, Integer estado, String username);
}
