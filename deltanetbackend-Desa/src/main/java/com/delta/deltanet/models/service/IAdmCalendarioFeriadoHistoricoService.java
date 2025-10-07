package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.AdmCalendarioFeriadoHistorico;

public interface IAdmCalendarioFeriadoHistoricoService {
	AdmCalendarioFeriadoHistorico find(Long id);
	AdmCalendarioFeriadoHistorico save(AdmCalendarioFeriadoHistorico calendarioAnio);
	AdmCalendarioFeriadoHistorico update(AdmCalendarioFeriadoHistorico calendarioAnio);
	void delete(AdmCalendarioFeriadoHistorico calendarioAnio);
    List<AdmCalendarioFeriadoHistorico> findBySearch(String motivo, Long generado, Long anio, Long estado);
}