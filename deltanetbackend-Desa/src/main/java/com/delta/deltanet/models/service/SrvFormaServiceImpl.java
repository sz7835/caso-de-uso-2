package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IServicioFormaDao;
import com.delta.deltanet.models.entity.ServicioForma;

@Service
public class SrvFormaServiceImpl implements ISrvFormaService {
	
	@Autowired
	private IServicioFormaDao formaServicio;

	@Override
	public List<ServicioForma> listaFormas() {
		return formaServicio.listaFormas();
	}

	@Override
	public ServicioForma buscaById(Long id) {
		return formaServicio.findById(id).orElse(null);
	}

}
