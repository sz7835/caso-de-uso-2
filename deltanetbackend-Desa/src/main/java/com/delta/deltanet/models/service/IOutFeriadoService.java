package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.OutFeriado;

import java.util.Date;
import java.util.List;

public interface IOutFeriadoService {
    OutFeriado buscaFecha(Date fecha);
    OutFeriado buscaFechaFeriado(Date fecha);
    List<Object> buscaRangoFecha(Date fecIni, Date fecFin);
}
