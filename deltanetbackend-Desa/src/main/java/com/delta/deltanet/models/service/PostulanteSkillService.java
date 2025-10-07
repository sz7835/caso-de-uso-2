package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.PostulanteSkill;

public interface PostulanteSkillService {
	boolean existsDescripcionActivo(String descripcion, Long exceptId);
    List<PostulanteSkill> buscarPorNombreYEstado(String descripcion, Integer estado);

    PostulanteSkill buscaId(Long idPer);
    
    public List<PostulanteSkill> findAll();
	public PostulanteSkill findById(Long id);
	public PostulanteSkill save(PostulanteSkill reg);
	public PostulanteSkill update(PostulanteSkill reg);
	public PostulanteSkill changeEstado(Long id, Integer estado, String username);
}
