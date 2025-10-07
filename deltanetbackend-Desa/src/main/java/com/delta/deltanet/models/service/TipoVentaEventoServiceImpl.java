package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.TipoVentaEventoDao;
import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.TipoVentaEventoEstadisticasDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoVentaEventoServiceImpl implements TipoVentaEventoService {
  @Override
  public boolean existsByDescripcionActiva(String descripcion) {
    return tipoVentaEventoDao.existsByDescripcionActiva(descripcion);
  }

  @Autowired
  private TipoVentaEventoDao tipoVentaEventoDao;

  @Override
  public List<TipoVentaEvento> buscarTipoVentaEvento(Integer id, String nombre, Integer estado) {
    return tipoVentaEventoDao.buscarTipoVentaEvento(id, nombre, estado);
  }

  @Override
  public TipoVentaEvento buscarPorId(Integer id) {
    return tipoVentaEventoDao.findById(id).orElse(null);
  }

  @Override
  public TipoVentaEvento guardar(TipoVentaEvento tipoVentaEvento) {
    return tipoVentaEventoDao.save(tipoVentaEvento);
  }

  @Override
  public List<TipoVentaEventoEstadisticasDTO> buscarTipoVentaEventoConEstadisticas(
      Integer estado, Integer tipo, String fechaInicio, String fechaFin) {
    List<Object[]> resultados = tipoVentaEventoDao.buscarTipoVentaEventoConEstadisticas(estado, tipo, fechaInicio,
        fechaFin);
    return resultados.stream()
        .map(this::convertirAFilaDTO)
        .collect(Collectors.toList());
  }

  private TipoVentaEventoEstadisticasDTO convertirAFilaDTO(Object[] fila) {
    return new TipoVentaEventoEstadisticasDTO(
        (Integer) fila[0], // id
        (String) fila[1], // nombre
        (String) fila[2], // descripcion
        ((Number) fila[3]).longValue(), // totalSolicitudes
        ((Number) fila[4]).longValue(), // solicitudesAtendidas
        ((Number) fila[5]).longValue(), // solicitudesNoAtendidas
        (Integer) fila[6], // estado
        (Date) fila[7] // fechaSolicitud
    );
  }
}