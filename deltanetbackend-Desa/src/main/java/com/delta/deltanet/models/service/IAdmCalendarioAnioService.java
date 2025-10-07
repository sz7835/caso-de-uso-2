package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;
import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;

public interface IAdmCalendarioAnioService {
    AdmCalendarioAnio save(AdmCalendarioAnio calendarioAnio);
    AdmCalendarioAnio update(AdmCalendarioAnio calendarioAnio);
    AdmCalendarioAnio cambiarEstado(Integer id, Integer estado);
    List<AdmCalendarioAnio> buscarPorFiltros(String nombre, Integer estado, Long idAnioEstado);
    Optional<AdmCalendarioAnio> findById(Integer id);
    List<AdmCalendarioAnioEstado> index();
    boolean existsByNombre(String nombre);
}