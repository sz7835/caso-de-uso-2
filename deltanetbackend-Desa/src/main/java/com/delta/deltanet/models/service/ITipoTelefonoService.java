package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TelefonoTipo;

import java.util.List;

public interface ITipoTelefonoService {
    List<TelefonoTipo> findAllByIdTipoPersona(Long idTipoPersona);
    List<TelefonoTipo> findAll();
}
