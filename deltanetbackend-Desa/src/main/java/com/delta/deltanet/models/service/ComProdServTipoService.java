package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.ComProdServTipo;
import java.util.List;
import java.util.Optional;

public interface ComProdServTipoService {
    List<ComProdServTipo> findAll();
    Optional<ComProdServTipo> findById(Integer id);
    ComProdServTipo save(ComProdServTipo tipo);
    boolean existsDescripcionActiva(String descripcion, Integer exceptId);
}
