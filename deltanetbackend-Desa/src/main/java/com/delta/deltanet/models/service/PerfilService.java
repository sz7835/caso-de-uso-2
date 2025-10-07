package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Perfil;

import java.util.List;

public interface PerfilService {
    List<Perfil> buscarPorNombreYEstado(String nombre, Integer estado);
    Perfil buscaId(Long idPer);
    public List<Perfil> findAll();
	public Perfil findById(Long id);
	public Perfil save(Perfil reg);
	public Perfil update(Perfil reg);
	public Perfil changeEstado(Long id, Integer estado, String username);
}
