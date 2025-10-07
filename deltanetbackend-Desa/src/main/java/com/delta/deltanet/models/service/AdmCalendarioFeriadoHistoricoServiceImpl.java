package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoHistoricoDao;
import com.delta.deltanet.models.entity.AdmCalendarioFeriadoHistorico;

@Service
public class AdmCalendarioFeriadoHistoricoServiceImpl implements IAdmCalendarioFeriadoHistoricoService {

    @Autowired
    private IAdmCalendarioFeriadoHistoricoDao calendarioFeriadoHistorico;

    @Override
    public AdmCalendarioFeriadoHistorico find(Long id) {
        return calendarioFeriadoHistorico.find(id);
    }

    @Override
    public AdmCalendarioFeriadoHistorico save(AdmCalendarioFeriadoHistorico calendarioAnio) {
        return calendarioFeriadoHistorico.save(calendarioAnio);
    }

    @Override
    public AdmCalendarioFeriadoHistorico update(AdmCalendarioFeriadoHistorico calendarioAnio) {
        return calendarioFeriadoHistorico.save(calendarioAnio);
    }

    @Override
    public List<AdmCalendarioFeriadoHistorico> findBySearch(String motivo, Long generado, Long anio, Long estado) {
        return calendarioFeriadoHistorico.findBySearch(motivo, generado, anio, estado);
    }

    @Override
    public void delete(AdmCalendarioFeriadoHistorico calendarioAnio) {
        calendarioFeriadoHistorico.delete(calendarioAnio);
    }

}