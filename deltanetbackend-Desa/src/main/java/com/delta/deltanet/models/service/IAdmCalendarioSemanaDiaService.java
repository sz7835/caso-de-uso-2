package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;

public interface IAdmCalendarioSemanaDiaService {
    AdmCalendarioSemanaDia save(AdmCalendarioSemanaDia calendarioSemanaDia);
    AdmCalendarioSemanaDia update(AdmCalendarioSemanaDia calendarioSemanaDia);
    AdmCalendarioSemanaDia cambiarEstado(Long id, Integer estado);
    List<AdmCalendarioSemanaDia> buscarPorFiltros(String nombre, Integer estado);
    Optional<AdmCalendarioSemanaDia> findById(Long id);
    List<AdmCalendarioSemanaDia> findAll();
    boolean existsByNombre(String nombre);
    boolean existsByAcronimo(String acronimo);
}