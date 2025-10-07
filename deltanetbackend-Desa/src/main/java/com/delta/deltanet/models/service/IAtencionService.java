package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Atencion;
import java.util.List;
import java.util.Optional;

public interface IAtencionService {
    List<Atencion> findAll();
    Optional<Atencion> findById(Integer id);
    List<Atencion> findByEstado(int estado);
    List<Atencion> findByIdTipoPer(int idTipoPer);
    Atencion save(Atencion atencion);

    List<Atencion> findByDescripAndIdTipoPerAndEstado(String descrip, int idTipoPer, int estado);
}
