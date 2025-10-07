package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAdmCalendarioAnioDao;
import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoDao;
import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoHistoricoDao;
import com.delta.deltanet.models.dao.ICalendarioDao;
import com.delta.deltanet.models.dao.ICalendarioHistoricoDao;
import com.delta.deltanet.models.entity.Calendario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CalendarioServiceImpl implements ICalendarioService{

    @Override
    public boolean existsCalendarioDuplicado(Long idAnio, Long idMes, Long idSemanaDia, Long idSemanaDiaTipo, Long contadorDia, Date fecha, Long idFeriado, Long exceptId) {
        if (idAnio == null && idMes == null && idSemanaDia == null && idSemanaDiaTipo == null && contadorDia == null && idFeriado == null) {
            // Solo validar por fecha activa
            long count = calendarioDao.countByFechaAndEstadoActivo(fecha, exceptId);
            return count > 0;
        } else {
            long count = calendarioDao.countByCamposUnicos(idAnio, idMes, idSemanaDia, idSemanaDiaTipo, contadorDia, fecha, idFeriado, exceptId);
            return count > 0;
        }
    }

    @Autowired
    public IAdmCalendarioAnioDao admCalendarioAnioDao;

    @Autowired
    public IAdmCalendarioFeriadoDao admCalendarioFeriadoDao;

    @Autowired
    public IAdmCalendarioFeriadoHistoricoDao admCalendarioFeriadoHistoricoDao;
    
    @Autowired
    public ICalendarioDao calendarioDao;

    @Autowired
    public ICalendarioHistoricoDao calendarioHistoricoDao;

    @Override
    public Calendario find(Long id) {
        return calendarioDao.find(id);
    }

    @Override
    public List<Calendario> findYear(Long id) {
        return calendarioDao.findYear(id);
    }

    @Override
    public Calendario save(Calendario calendar) {
        return calendarioDao.save(calendar);
    }

    @Override
    public Calendario update(Calendario calendar) {
        return calendarioDao.save(calendar);
    }

    @Override
    public Calendario findDate(Date fecIni) {
        return calendarioDao.findDate(fecIni);
    }
    
    @Override
    public List<Calendario> buscaRangoFecha(Date fecIni, Pageable pageable) {
        return calendarioDao.buscaRangoFecha(fecIni, pageable);
    }
    
    @Override
    public List<Calendario> findBySearch(Date fecha, Long idMes, Long idSemanaDiaTipo, Long estado) {
        return calendarioDao.findBySearch(fecha, idMes, idSemanaDiaTipo, estado);
    }

    @Override
    public void delete(Calendario calendar) {
    	calendarioDao.delete(calendar);
    }

}
