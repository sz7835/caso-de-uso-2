package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.DireccionTipo;

import java.util.List;

public interface ITipoDireccionService {
    List<DireccionTipo> findAllByIdTipoPersona(Long idTipoPersona);
    List<DireccionTipo> findAll();
}
