package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PuestoOcupacional;
import java.util.List;

public interface PuestoOcupacionalEspecialService {
    List<PuestoOcupacional> findAll();
    PuestoOcupacional findById(Long id);
    PuestoOcupacional save(PuestoOcupacional puesto);
    boolean existsNombreUnidadOrganicaActivo(String nombre, String unidadOrganica, Long exceptId);
}
