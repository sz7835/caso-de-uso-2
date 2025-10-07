package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.TipoEstadoCivil;

public interface  ITipoEstadoCivilService {
    List<TipoEstadoCivil> findAll();
    public TipoEstadoCivil findById(Long id);
}
