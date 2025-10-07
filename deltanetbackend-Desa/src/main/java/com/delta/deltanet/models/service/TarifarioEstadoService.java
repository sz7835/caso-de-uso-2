package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TarifarioEstado;
import java.util.List;
import java.util.Optional;

public interface TarifarioEstadoService {
    List<TarifarioEstado> findAll();
    Optional<TarifarioEstado> findById(Long id);
    TarifarioEstado save(TarifarioEstado estado);
    boolean existsDescripcionActiva(String descripcion, Long exceptId);
}
