package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoServicio;

import java.util.List;

public interface ITipoServicioService {
    TipoServicio findById(Long id);
    List<TipoServicio> findAll();
    TipoServicio save (TipoServicio tipoServicio);
    List<TipoServicio> findByDescripAndEstado(String descrip, Integer estado);
}
