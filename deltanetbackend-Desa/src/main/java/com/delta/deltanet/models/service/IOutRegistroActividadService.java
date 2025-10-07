package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.OutRegistroActividad;

import java.util.Date;
import java.util.List;

public interface IOutRegistroActividadService {
    List<Object> BuscarPorRangoFechas(Long idPer, Date fecIni, Date fecFin, Long idTipoAct);
    OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha, Long idTipoAct);
    OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer, Date fecha);
    OutRegistroActividad BuscarPorPersonaTipoAct(Long idPer);
}
