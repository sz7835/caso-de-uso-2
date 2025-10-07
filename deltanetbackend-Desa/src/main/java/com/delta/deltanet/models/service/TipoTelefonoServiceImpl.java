package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoTelefonoDao;
import com.delta.deltanet.models.entity.TelefonoTipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTelefonoServiceImpl implements ITipoTelefonoService {
	@Autowired
	private ITipoTelefonoDao tipoTelefonoDao;

	@Override
	public List<TelefonoTipo> findAllByIdTipoPersona(Long idTipoPersona) {
		return tipoTelefonoDao.findAllByIdTipoPersona(idTipoPersona);
	}

	@Override
	public List<TelefonoTipo> findAll() {
		return tipoTelefonoDao.findAll();
	}

}
