package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ITipoEstadoCivilDao;
import com.delta.deltanet.models.entity.TipoEstadoCivil;

@Service
public class ITipoEstadoCivilServiceImpl implements ITipoEstadoCivilService {

    @Autowired
    private ITipoEstadoCivilDao tipoEstadoCivilDao;

    @Override
    public List<TipoEstadoCivil> findAll() {
        return tipoEstadoCivilDao.findAll();
    }

    @Override
	public TipoEstadoCivil findById(Long id) {
		return tipoEstadoCivilDao.findById(id).orElse(null);
	}
}