package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoPersona;

import java.util.List;

public interface ITipoPersonaService {
    List<TipoPersona> findAll();
    TipoPersona buscaTipoPer(Long idTipoPer);
}
