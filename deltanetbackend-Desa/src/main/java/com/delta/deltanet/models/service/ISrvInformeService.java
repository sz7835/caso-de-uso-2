package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ServicioInforme;

public interface ISrvInformeService {
	
	public ServicioInforme findById(Long id);
	public ServicioInforme save(ServicioInforme reg);
	public List<ServicioInforme> findAll();
	public Integer findMaxNumeroByCliente(Long idCli);
	boolean existsByClienteAndMesFac(Long idCliente, String mesFac);

}
