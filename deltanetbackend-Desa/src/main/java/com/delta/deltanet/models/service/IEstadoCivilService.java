package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.EstadoCivil;

import java.util.List;

public interface IEstadoCivilService {
	boolean existsNombreActivo(String nombre, Long idExcluir);
    public List<EstadoCivil> findAll();
	public EstadoCivil findById(Long id);
	public EstadoCivil save(EstadoCivil reg);
	public EstadoCivil update(EstadoCivil reg);
	public EstadoCivil changeEstado(Long id, Integer estado, String username);
}
