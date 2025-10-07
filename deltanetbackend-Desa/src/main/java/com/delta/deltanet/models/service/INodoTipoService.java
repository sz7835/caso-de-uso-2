package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.NodoTipo;

public interface INodoTipoService {
	boolean existsAcronimoActivo(String acronimo, Long exceptId);
	boolean existsNombreActivo(String nombre, Long exceptId);
	NodoTipo searchNodo(String type);
	NodoTipo findById(Long id);
	List<NodoTipo> findAll();
	NodoTipo save(NodoTipo nodoTipo);
}
