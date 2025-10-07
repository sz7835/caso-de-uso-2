package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoCorreoDao;
import com.delta.deltanet.models.entity.CorreoTipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoCorreoServiceImpl implements ITipoCorreoService {
	@Autowired
	private ITipoCorreoDao tipoCorreoDao;

	@Override
	public List<CorreoTipo> findAllByIdTipoPersona(Long idTipoPersona) {
		return tipoCorreoDao.findAllByIdTipoPersona(idTipoPersona);
	}

	@Override
	public List<CorreoTipo> findAll() {
		return tipoCorreoDao.findAll();
	}

}
