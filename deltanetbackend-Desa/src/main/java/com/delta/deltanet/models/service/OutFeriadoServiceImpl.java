package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IOutFeriadoDao;
import com.delta.deltanet.models.entity.OutFeriado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OutFeriadoServiceImpl implements IOutFeriadoService{

    @Autowired
    public IOutFeriadoDao outFeriadoDao;
    @Override
    public OutFeriado buscaFecha(Date fecha) {
        return outFeriadoDao.buscaFecha(fecha);
    }

    @Override
    public OutFeriado buscaFechaFeriado(Date fecha) {
        return outFeriadoDao.buscaFechaFeriado(fecha);
    }

    @Override
    public List<Object> buscaRangoFecha(Date fecIni, Date fecFin) {
        return outFeriadoDao.buscaRangoFecha(fecIni, fecFin);
    }
}
