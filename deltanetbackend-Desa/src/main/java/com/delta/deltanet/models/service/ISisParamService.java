package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.SisParam;

import java.util.List;

public interface ISisParamService {
    public List<SisParam> findAll();
    public SisParam findById(Long id);
    public SisParam save(SisParam sisparam);
    public void delete(Long id);
    SisParam buscaEtiqueta(String etiqueta);
}
