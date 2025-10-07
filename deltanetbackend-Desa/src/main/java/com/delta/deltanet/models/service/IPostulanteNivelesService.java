package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteNiveles;

import java.util.List;

public interface IPostulanteNivelesService {
	boolean existsDescripcionActivo(String descripcion, Long idExcluir);
    public List<PostulanteNiveles> findAll();
	public PostulanteNiveles findById(Long id);
	public PostulanteNiveles save(PostulanteNiveles reg);
	public PostulanteNiveles update(PostulanteNiveles reg);
	public PostulanteNiveles changeEstado(Long id, Integer estado, String username);
}
