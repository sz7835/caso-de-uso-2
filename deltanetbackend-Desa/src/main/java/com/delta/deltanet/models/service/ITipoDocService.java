package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.TipoDoc;

public interface ITipoDocService {
	
	public List<TipoDoc> findAll();
	public TipoDoc findById(Long id);
	public TipoDoc save(TipoDoc tipoDoc);
	public void delete(Long id);
	
}
