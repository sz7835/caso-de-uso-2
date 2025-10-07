package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Parametro;

import java.util.List;

public interface IParametroService {
    public List<Parametro> findByParams(Long idRubro);
    public Parametro findByParam(Long idRubro,Long idItem);
}
