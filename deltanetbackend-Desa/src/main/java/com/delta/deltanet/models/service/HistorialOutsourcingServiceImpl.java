package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IHistorialOutsourcingDao;
import com.delta.deltanet.models.entity.HistorialOutsourcing;

@Service
public class HistorialOutsourcingServiceImpl implements IHistorialOutsourcingService {
	
	@Autowired
	private IHistorialOutsourcingDao historialOutsourcingDao;

	@Override
	public List<HistorialOutsourcing> findAll() {
		return historialOutsourcingDao.findAll();
	}

	@Override
	public HistorialOutsourcing findById(Long id) {
		return historialOutsourcingDao.findById(id).orElse(null);
	}

	@Override
	public HistorialOutsourcing save(HistorialOutsourcing historialOutsourcing) {
		return historialOutsourcingDao.save(historialOutsourcing);
	}

	@Override
	public void delete(Long id) {
		historialOutsourcingDao.deleteById(id);
	}
	
}
