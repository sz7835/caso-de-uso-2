package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ITipoDocDao;
import com.delta.deltanet.models.entity.TipoDoc;

@Service
public class TipoDocServiceImpl implements ITipoDocService {
	
	@Autowired
	private ITipoDocDao tipoDocDao;

	@Override
	public List<TipoDoc> findAll() {
		return tipoDocDao.findAll();
	}

	@Override
	public TipoDoc findById(Long id) {
		return tipoDocDao.findById(id).orElse(null);
	}

	@Override
	public TipoDoc save(TipoDoc TipoDoc) {
		return tipoDocDao.save(TipoDoc);
	}

	@Override
	public void delete(Long id) {
		tipoDocDao.deleteById(id);
	}
	
}
