package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CronogramaModalidad;

import java.util.List;

public interface ICronogramaModalidadService {
    CronogramaModalidad findById(Long id);
    List<CronogramaModalidad> findAll();
    CronogramaModalidad save(CronogramaModalidad cronogramaModalidad);
    List<CronogramaModalidad> findActive();
}
