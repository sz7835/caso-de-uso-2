package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Provincia;

import java.util.List;

public interface IProvinciaService {
    public List<Provincia> listadoFull();
    public List<Object> lstPorDpto(Long idDpto);
}
