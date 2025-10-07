package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.ServicioForma;

public interface ISrvFormaService {
	public List<ServicioForma> listaFormas();
	ServicioForma buscaById(Long id);

}
