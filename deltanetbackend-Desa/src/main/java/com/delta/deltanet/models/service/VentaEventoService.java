package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.VentaEvento;

public interface VentaEventoService {

    VentaEvento buscarPorId(Integer id);

    VentaEvento guardar(VentaEvento ventaEvento);

    TipoVentaEvento buscarTipoEvento(Integer id);

    List<VentaEvento> buscarTipoVentaEventoConFechas(
            Integer id, String descripcion, Integer estado, Integer tipo, Date fechaInicio, Date fechaFin);

    List<TipoVentaEvento> listarTiposVentaEvento();

}
