package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IGerenciaDao;
import com.delta.deltanet.models.entity.Gerencia;

@Service
public class GerenciaServiceImpl implements IGerenciaService {

    @Autowired
	private IGerenciaDao gerenciaDao;

	@Override
	public List<Gerencia> findAll() {
		return gerenciaDao.findAll();
    }

    @Override
	public List<Gerencia> findAllWithAreas() {
		return gerenciaDao.findAllWithAreas();
    }

	public Optional<Gerencia> findById(Long id){
		return gerenciaDao.findById(id);
	}
}