package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.INodoTipoDao;
import com.delta.deltanet.models.entity.NodoTipo;

@Service
public class NodoTipoServiceImpl implements INodoTipoService {
	@Override
	public boolean existsAcronimoActivo(String acronimo, Long exceptId) {
		if (acronimo == null) return false;
		String acronimoTrim = acronimo.trim();
		long count = nodoTipo.countByAcronimoActivo(acronimoTrim, exceptId);
		return count > 0;
	}

	@Override
	public boolean existsNombreActivo(String nombre, Long exceptId) {
		if (nombre == null) return false;
		String nombreTrim = nombre.trim();
		long count = nodoTipo.countByNombreActivo(nombreTrim, exceptId);
		return count > 0;
	}

	@Autowired
	private INodoTipoDao nodoTipo;

	@Override
	public NodoTipo searchNodo(String type) {
		return nodoTipo.searchNodo(type);
	}

	@Override
	public NodoTipo findById(Long id) {
		return nodoTipo.findById(id).orElse(null);
	}

	@Override
	public List<NodoTipo> findAll() {
		return nodoTipo.findAll();
	}

	@Override
	public NodoTipo save(NodoTipo nodoTipoEntity) {
		return nodoTipo.save(nodoTipoEntity);
	}
}
