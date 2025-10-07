package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Distrito;

import java.util.List;

public interface IDistritoService {
    public List<Distrito> findAll();
    public Distrito finddByIdDist(Long id);
    public List<Object> lstPorProv(Long idProv);
}
