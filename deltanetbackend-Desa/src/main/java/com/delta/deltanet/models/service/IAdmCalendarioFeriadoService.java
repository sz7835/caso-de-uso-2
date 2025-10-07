package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.delta.deltanet.models.entity.AdmCalendarioFeriado;

public interface IAdmCalendarioFeriadoService {
	List<AdmCalendarioFeriado> findByMotivoAndCamposClave(String motivo, Long idAnio, Long idGeneradoPor, String aplicableSectorPublico, String aplicableSectorPrivado, java.util.Date fecha, Long idDiaSemana);
	AdmCalendarioFeriado find(Long id);
	AdmCalendarioFeriado save(AdmCalendarioFeriado calendarioAnio);
	AdmCalendarioFeriado update(AdmCalendarioFeriado calendarioAnio);
	void delete(AdmCalendarioFeriado calendarioAnio);
    List<AdmCalendarioFeriado> findBySearch(String motivo, Long generado, Long anio, Long estado);
    Page<AdmCalendarioFeriado> findBySearchPaginate(String motivo, Long generado, Long anio, Long estado, Pageable pageable);
}