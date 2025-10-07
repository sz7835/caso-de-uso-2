package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IEmpresaDao;
import com.delta.deltanet.models.entity.Empresa;

@Service
public class EmpresaServiceImpl implements IEmpresaService {
	
	@Autowired
	private IEmpresaDao empresaDao;

	@Override
	public List<Empresa> findAll() {
		return empresaDao.findAll();
	}

	@Override
	public Empresa findById(Long id) {
		return empresaDao.findById(id).orElse(null);
	}

	@Override
	public Empresa save(Empresa Empresa) {
		return empresaDao.save(Empresa);
	}

	@Override
	public void delete(Long id) {
		empresaDao.deleteById(id);
	}
	
}
