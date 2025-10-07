package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TelefonoTipo;
import java.util.List;

public interface TelefonoTipoService {
    List<TelefonoTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado);
    List<TelefonoTipo> findByEstado(Integer estado);
    List<TelefonoTipo> findByIdTipoPersona(Long idTipoPersona);
    List<TelefonoTipo> findAll();
    TelefonoTipo findById(Long id);
    TelefonoTipo save(TelefonoTipo telefonoTipo);
}
