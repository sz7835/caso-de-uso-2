package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoDao;
import com.delta.deltanet.models.entity.AdmCalendarioFeriado;

@Service
public class AdmCalendarioFeriadoServiceImpl implements IAdmCalendarioFeriadoService {

    @Override
    public List<AdmCalendarioFeriado> findByMotivoAndCamposClave(String motivo, Long idAnio, Long idGeneradoPor, String aplicableSectorPublico, String aplicableSectorPrivado, java.util.Date fecha, Long idDiaSemana) {
        return calendarioFeriado.findByMotivoAndCamposClave(motivo, idAnio, idGeneradoPor, aplicableSectorPublico, aplicableSectorPrivado, fecha, idDiaSemana);
    }

    @Autowired
    private IAdmCalendarioFeriadoDao calendarioFeriado;

    @Override
    public AdmCalendarioFeriado find(Long id) {
        return calendarioFeriado.find(id);
    }

    @Override
    public AdmCalendarioFeriado save(AdmCalendarioFeriado calendarioAnio) {
        return calendarioFeriado.save(calendarioAnio);
    }

    @Override
    public AdmCalendarioFeriado update(AdmCalendarioFeriado calendarioAnio) {
        return calendarioFeriado.save(calendarioAnio);
    }

    @Override
    public List<AdmCalendarioFeriado> findBySearch(String motivo, Long generado, Long anio, Long estado) {
        return calendarioFeriado.findBySearch(motivo, generado, anio, estado);
    }

    @Override
    public Page<AdmCalendarioFeriado> findBySearchPaginate(String motivo, Long generado, Long anio, Long estado, Pageable pageable) {
        return calendarioFeriado.findBySearchPaginated(motivo, generado, anio, estado, pageable);
    }

    @Override
    public void delete(AdmCalendarioFeriado calendarioAnio) {
        calendarioFeriado.delete(calendarioAnio);
    }

}