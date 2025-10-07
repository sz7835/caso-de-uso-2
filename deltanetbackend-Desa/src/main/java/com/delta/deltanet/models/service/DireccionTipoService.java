package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.DireccionTipo;
import java.util.List;

public interface DireccionTipoService {
    List<DireccionTipo> findByEstado(Integer estado);
    List<DireccionTipo> findByIdTipoPersona(Long idTipoPersona);
    List<DireccionTipo> findAll();
    DireccionTipo findById(Long id);
    DireccionTipo save(DireccionTipo direccionTipo);
    List<DireccionTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado);

}
