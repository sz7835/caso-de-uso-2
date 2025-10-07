package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PostulanteOfimaticaIdiomas;

import java.util.List;

public interface IPostulanteOfimaticaIdiomasService {
	boolean existsDescripcionActivo(String descripcion, Long idExcluir);
    public List<PostulanteOfimaticaIdiomas> findAll();
	public PostulanteOfimaticaIdiomas findById(Long id);
	public PostulanteOfimaticaIdiomas save(PostulanteOfimaticaIdiomas reg);
	public PostulanteOfimaticaIdiomas update(PostulanteOfimaticaIdiomas reg);
	public PostulanteOfimaticaIdiomas changeEstado(Long id, Integer estado, String username);
}
