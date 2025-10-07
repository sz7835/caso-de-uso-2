package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CorreoTipo;

import java.util.List;

public interface ITipoCorreoService {
    List<CorreoTipo> findAllByIdTipoPersona(Long idTipoPersona);
    List<CorreoTipo> findAll();
}
