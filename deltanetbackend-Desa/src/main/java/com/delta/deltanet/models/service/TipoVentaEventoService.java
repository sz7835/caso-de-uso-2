package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.TipoVentaEventoEstadisticasDTO;

import java.util.List;

public interface TipoVentaEventoService {

  boolean existsByDescripcionActiva(String descripcion);

  List<TipoVentaEvento> buscarTipoVentaEvento(Integer id, String nombre, Integer estado);

  TipoVentaEvento buscarPorId(Integer id);

  TipoVentaEvento guardar(TipoVentaEvento ventaEvento);

  List<TipoVentaEventoEstadisticasDTO> buscarTipoVentaEventoConEstadisticas(
      Integer estado,
      Integer tipo,
      String fechaInicio,
      String fechaFin);

}