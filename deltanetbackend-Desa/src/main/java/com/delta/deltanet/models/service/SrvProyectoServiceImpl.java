package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ISrvProyectoDao;
import com.delta.deltanet.models.entity.ServicioProyecto;

@Service
public class SrvProyectoServiceImpl implements ISrvProyectoService {
	
	@Autowired
	private ISrvProyectoDao proyServicio;

	@Override
	public List<ServicioProyecto> listaProyectos() {
		return proyServicio.listaProyectos();
	}

	@Override
	public ServicioProyecto buscaById(Long id) {
		return proyServicio.findById(id).orElse(null);
	}

	@Override
	public ServicioProyecto save(ServicioProyecto reg) {
		return proyServicio.save(reg);
	}

}
