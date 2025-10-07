package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.SrvEstado;

public interface ISrvEstadoService {
	boolean existsNombreActivo(String nombre, Long idExcluir);
	public SrvEstado findByPk(Long id);
	public List<SrvEstado> findAll();
	public SrvEstado findById(Long id);
	public SrvEstado save(SrvEstado reg);
	public SrvEstado update(SrvEstado reg);
	public SrvEstado changeEstado(Long id, Integer estado, String username);
}
