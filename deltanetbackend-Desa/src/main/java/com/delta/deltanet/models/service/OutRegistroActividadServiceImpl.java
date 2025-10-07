package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IOutRegistroActividadDAO;
import com.delta.deltanet.models.entity.OutRegistroActividad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OutRegistroActividadServiceImpl implements IOutRegistroActividadService {

    @Autowired
    public IOutRegistroActividadDAO outRegistroActividadDAO;

    @Override
    public List<Object> BuscarPorRangoFechas(Long idPer, Date fecIni, Date fecFin, Long idTipoAct) {
        return outRegistroActividadDAO.BuscarPorRangoFechas(idPer, fecIni, fecFin, idTipoAct);
    }

    @Override
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha, Long idTipoAct) {
        return outRegistroActividadDAO.BuscarPorPersonaTipoAct(idPer, fecha, idTipoAct);
    }

    @Override
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha) {
        return outRegistroActividadDAO.BuscarPorPersonaTipoAct(idPer, fecha);
    }

    @Override
    public OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer) {
        return outRegistroActividadDAO.BuscarPorPersonaTipoAct(idPer);
    }
}
