package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ISrvInformeUsuariosDao;
import com.delta.deltanet.models.entity.ServicioInformeUsuarios;

@Service
public class SrvInformeUsuariosServiceImpl implements ISrvInformeUsuariosService {

	@Autowired
	private ISrvInformeUsuariosDao informeUsuariosServicio;

	@Override
	public List<ServicioInformeUsuarios> findAll() {
		return informeUsuariosServicio.findAll();
	}

}
