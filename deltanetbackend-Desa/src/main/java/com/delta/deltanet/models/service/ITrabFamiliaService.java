package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TrabajadorFamilia;

import java.util.List;

public interface ITrabFamiliaService {
    public void save(TrabajadorFamilia trabajadorFamilia);
    public TrabajadorFamilia findById(Long id);
    public List<Object> listaFamilia(Long idPerNat);
}
