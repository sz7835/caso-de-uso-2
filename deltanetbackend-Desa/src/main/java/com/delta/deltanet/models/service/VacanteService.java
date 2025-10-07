package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.Vacante;

public interface VacanteService {

    String cambiarEstadoVacante(Long id, String usuario);

    List<Vacante> searchVacantes(Integer puestoId, Integer expGeneral, String createDate, Integer estado,
            Integer forAcadId, Integer gradSitAcadId, Boolean colegiatura);
}
