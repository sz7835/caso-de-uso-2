package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.Gerencia;

public interface IGerenciaService {

    public List<Gerencia> findAll();

    public List<Gerencia> findAllWithAreas();
    
    public Optional<Gerencia> findById(Long id);
}
