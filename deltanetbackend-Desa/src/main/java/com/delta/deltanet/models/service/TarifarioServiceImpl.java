package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.TarifarioDao;
import com.delta.deltanet.models.dao.ICronogramaModalidadDao;
import com.delta.deltanet.models.dao.IPerfilDao;
import com.delta.deltanet.models.dao.ITarifarioEstadoDao;
import com.delta.deltanet.models.dao.ITarifarioMonedaDao;
import com.delta.deltanet.models.entity.Tarifario;
import com.delta.deltanet.models.entity.TarifarioEstado;
import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.entity.CronogramaModalidad;
import com.delta.deltanet.models.entity.Perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TarifarioServiceImpl implements TarifarioService {

  @Autowired
  private TarifarioDao tarifarioDao;

  @Autowired
  private ICronogramaModalidadDao cronogramaModalidadDao;

  @Autowired
  private ITarifarioMonedaDao tarifarioMonedaDao;

  @Autowired
  private ITarifarioEstadoDao tarifarioEstadoDao;

  @Autowired
  private IPerfilDao perfilDao;

  @Override
  @Transactional(readOnly = true)
  public List<Tarifario> buscarTarifarios(Date fechaInicio, Date fechaFin, Integer lugar, Integer moneda,
      Integer estado) {
    return tarifarioDao.buscarTarifarios(fechaInicio, fechaFin, lugar, moneda, estado);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CronogramaModalidad> listarLugares() {
    return cronogramaModalidadDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TarifarioMoneda> listarMonedas() {
    return tarifarioMonedaDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<TarifarioEstado> listarEstados() {
    return tarifarioEstadoDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Perfil> listarPerfiles() {
    return perfilDao.findAll();
  }

  @Override
  @Transactional
  public Tarifario saveTarifario(Tarifario tarifario) throws IllegalArgumentException {
    if (tarifario.getFechaIni().compareTo(tarifario.getFechaFin()) > 0) {
      throw new IllegalArgumentException("La fecha de inicio debe ser menor a la fecha de fin");
    }
    if (!perfilDao.existsById(Long.valueOf(tarifario.getPerfil()))) {
      throw new IllegalArgumentException("El perfil especificado no existe");
    }
    if (!cronogramaModalidadDao.existsById(Long.valueOf(tarifario.getLugar()))) {
      throw new IllegalArgumentException("El lugar especificado no existe");
    }
    if (!tarifarioMonedaDao.existsById(Long.valueOf(tarifario.getMoneda()))) {
      throw new IllegalArgumentException("La moneda especificada no existe");
    }
    tarifario.setEstado(1);
    tarifario.setCreateDate(new Date());

    return tarifarioDao.save(tarifario);
  }

  @Override
  public Tarifario findTarifarioById(Long id) {
    return tarifarioDao.findById(id).orElse(null);
  }

  @Override
  public Tarifario saveT(Tarifario tarifario) {
    return tarifarioDao.save(tarifario);
  }

}
