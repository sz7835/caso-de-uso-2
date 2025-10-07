package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.HistorialOutsourcing;

public interface IHistorialOutsourcingService {
	
	public List<HistorialOutsourcing> findAll();
	public HistorialOutsourcing findById(Long id);
	public HistorialOutsourcing save(HistorialOutsourcing historialOutsourcing);
	public void delete(Long id);
	
}
