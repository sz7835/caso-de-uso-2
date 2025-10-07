package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.PostulanteRequisito;

public interface PostulanteRequisitoService {
	boolean existsDescripcionActivo(String descripcion, Long exceptId);
    List<PostulanteRequisito> buscarPorNombreYEstado(String descripcion, Integer estado);
    PostulanteRequisito buscaId(Long idPer);
    public List<PostulanteRequisito> findAll();
	public PostulanteRequisito findById(Long id);
	public PostulanteRequisito save(PostulanteRequisito reg);
	public PostulanteRequisito update(PostulanteRequisito reg);
	public PostulanteRequisito changeEstado(Long id, Integer estado, String username);
}
