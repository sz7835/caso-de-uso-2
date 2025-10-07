package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ISrvInformeDao;
import com.delta.deltanet.models.entity.ServicioInforme;

@Service
public class SrvInformeServiceImpl implements ISrvInformeService {

	@Autowired
	private ISrvInformeDao informeServicio;

	@Override
	public ServicioInforme findById(Long id) {
		return informeServicio.findById(id).orElse(null);
	}
	
	@Override
	public ServicioInforme save(ServicioInforme reg) {
		return informeServicio.save(reg);
	}

	@Override
	public List<ServicioInforme> findAll() {
		return informeServicio.findAll();
	}
	

	@Override
	public Integer findMaxNumeroByCliente(Long idCli) {
		return informeServicio.findMaxNumeroByCliente(idCli).orElse(0);
	}

	@Override
	public boolean existsByClienteAndMesFac(Long idCliente, String mesFac) {
		return informeServicio.existsByCliente_IdPerJurAndMesFac(idCliente, mesFac);
	}

}
