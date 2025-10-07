package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.CalendarioHistoricoDao;
import com.delta.deltanet.models.entity.CalendarioHistorico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdmCalendarioHistoricoServiceImpl implements AdmCalendarioHistoricoService {

    @Autowired
    private CalendarioHistoricoDao calendarioHistoricoRepository;

    @Override
    public List<CalendarioHistorico> buscarPorFiltros(Long idAnio, Date fecha, Long idMes, Long idSemanaDiaTipo, Long estado) {
        return calendarioHistoricoRepository.findByFiltros(idAnio, fecha, idMes, idSemanaDiaTipo, estado);
    }
}