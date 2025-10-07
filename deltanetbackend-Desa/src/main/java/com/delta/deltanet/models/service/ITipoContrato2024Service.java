package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoContrato;

import java.util.List;

public interface ITipoContrato2024Service {
    TipoContrato findById(Long id);
    List<TipoContrato> findAll();
}
