package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.PuestoOcupacional;

import java.util.List;

public interface PuestoOcupacionalService {
    List<PuestoOcupacional> filtrarPuestoOcupacional(String nombrePuestoOcupacional, Integer estado);

    PuestoOcupacional updateStatus(Long id, String username);
}
