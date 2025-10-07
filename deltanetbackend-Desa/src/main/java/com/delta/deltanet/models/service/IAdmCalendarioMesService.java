package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmCalendarioMes;
import java.util.List;
import java.util.Optional;

public interface IAdmCalendarioMesService {
    AdmCalendarioMes save(AdmCalendarioMes calendarioMes);
    AdmCalendarioMes update(AdmCalendarioMes calendarioMes);
    AdmCalendarioMes cambiarEstado(Long id, Integer estado);
    List<AdmCalendarioMes> buscarPorFiltros(String nombre, Integer estado);
    Optional<AdmCalendarioMes> findById(Long id);
    List<AdmCalendarioMes> findAll();
    boolean existsByNombre(String nombre);
    boolean existsByAcronimo(String acronimo);
}