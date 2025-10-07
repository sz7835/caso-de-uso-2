package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.ComPropuestaEstado;
import java.util.List;
import java.util.Optional;

public interface ComPropuestaEstadoService {
    List<ComPropuestaEstado> findAll();
    Optional<ComPropuestaEstado> findById(Integer id);
    ComPropuestaEstado save(ComPropuestaEstado estado);
    boolean existsDescripcionActiva(String descripcion, Integer exceptId);
}
