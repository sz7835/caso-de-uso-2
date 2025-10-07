package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.AdmCalendarioFeriadoGeneradoPor;

public interface IAdmCalendarioFeriadoGeneradoPorService {
    AdmCalendarioFeriadoGeneradoPor save(AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor);
    AdmCalendarioFeriadoGeneradoPor update(AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor);
    AdmCalendarioFeriadoGeneradoPor cambiarEstado(Long id, Integer estado);
    List<AdmCalendarioFeriadoGeneradoPor> buscarPorFiltros(String descripcion, Integer estado);
    List<AdmCalendarioFeriadoGeneradoPor> findAll();
    Optional<AdmCalendarioFeriadoGeneradoPor> findById(Long id);
    boolean existsByAcronimo(String acronimo);
    boolean existsByDescripcion(String descripcion);
}