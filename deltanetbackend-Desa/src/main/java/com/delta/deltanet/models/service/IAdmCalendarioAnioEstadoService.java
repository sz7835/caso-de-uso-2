package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;

public interface IAdmCalendarioAnioEstadoService {
    AdmCalendarioAnioEstado save(AdmCalendarioAnioEstado calendarioAnioEstado);
    AdmCalendarioAnioEstado update(AdmCalendarioAnioEstado calendarioAnioEstado);
    AdmCalendarioAnioEstado cambiarEstado(Long id, Integer nuevoEstado);
    List<AdmCalendarioAnioEstado> buscarPorFiltros(String nombre, Integer estado);
    Optional<AdmCalendarioAnioEstado> findById(Long id);
    List<AdmCalendarioAnioEstado> findAll();
    boolean existsByNombre(String nombre);
    boolean existsByAcronimo(String acronimo);
    boolean existeEstadoPrincipalActivo();
    Map<String, Boolean> obtenerEstadosPrincipalesActivos();
}