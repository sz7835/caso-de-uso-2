package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import com.delta.deltanet.models.entity.AdmTipoDiaSemana;

public interface IAdmTipoDiaSemanaService {
    AdmTipoDiaSemana save(AdmTipoDiaSemana calendarioAnioEstado);
    AdmTipoDiaSemana update(AdmTipoDiaSemana calendarioAnioEstado);
    AdmTipoDiaSemana cambiarEstado(Long id, Integer nuevoEstado);
    List<AdmTipoDiaSemana> buscarPorFiltros(String nombre, Integer estado);
    Optional<AdmTipoDiaSemana> findById(Long id);
    List<AdmTipoDiaSemana> findAll();
    boolean existsByNombre(String nombre);
    boolean existsByAcronimo(String acronimo);
}