package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.JefaturaPuesto;

public interface IJefaturaPuestoService {

	boolean existsNombreActivoPorJefatura(String nombre, Long idJefatura, Long exceptId);
	public List<JefaturaPuesto> findAll();
	public JefaturaPuesto findById(Long id);
	public JefaturaPuesto save(JefaturaPuesto reg);
	public JefaturaPuesto update(JefaturaPuesto reg);
	public JefaturaPuesto changeEstado(Long id, Integer estado, String username);

}
