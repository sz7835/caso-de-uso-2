package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Calendario;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ICalendarioService {
	boolean existsCalendarioDuplicado(Long idAnio, Long idMes, Long idSemanaDia, Long idSemanaDiaTipo, Long contadorDia, Date fecha, Long idFeriado, Long exceptId);
	Calendario find(Long id);
	Calendario save(Calendario calendar);
	Calendario update(Calendario calendar);
	void delete(Calendario calendar);
	List<Calendario> buscaRangoFecha(Date fecIni, Pageable pageable);
	Calendario findDate(Date fecIni);
	List<Calendario> findYear(Long idAnio);
	List<Calendario> findBySearch(Date fecha, Long idMes, Long idSemanaDiaTipo, Long estado);
}
