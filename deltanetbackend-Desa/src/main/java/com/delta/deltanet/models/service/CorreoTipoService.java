package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CorreoTipo;
import java.util.List;

public interface CorreoTipoService {
    List<CorreoTipo> findByEstado(Integer estado);
    List<CorreoTipo> findByIdTipoPersona(Long idTipoPersona);
    List<CorreoTipo> findAll();
    CorreoTipo findById(Long id);
    CorreoTipo save(CorreoTipo correoTipo);
    List<CorreoTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado);
}
