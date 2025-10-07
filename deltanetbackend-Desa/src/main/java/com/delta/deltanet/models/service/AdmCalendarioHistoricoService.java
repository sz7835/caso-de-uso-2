package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CalendarioHistorico;

import java.util.Date;
import java.util.List;

public interface AdmCalendarioHistoricoService {
    List<CalendarioHistorico> buscarPorFiltros(Long idAnio, Date fecha, Long idMes, Long idSemanaDiaTipo, Long estado);
}