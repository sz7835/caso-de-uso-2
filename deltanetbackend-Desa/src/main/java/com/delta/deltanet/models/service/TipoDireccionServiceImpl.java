package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoDireccionDao;
import com.delta.deltanet.models.entity.DireccionTipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDireccionServiceImpl implements ITipoDireccionService {
	@Autowired
	private ITipoDireccionDao tipoDireccionDao;

	@Override
	public List<DireccionTipo> findAllByIdTipoPersona(Long idTipoPersona) {
		return tipoDireccionDao.findAllByIdTipoPersona(idTipoPersona);
	}

	@Override
	public List<DireccionTipo> findAll() {
		return tipoDireccionDao.findAll();
	}

}
