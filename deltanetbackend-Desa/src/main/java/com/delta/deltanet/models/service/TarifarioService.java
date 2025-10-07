package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Tarifario;
import com.delta.deltanet.models.entity.TarifarioEstado;
import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.entity.CronogramaModalidad;
import com.delta.deltanet.models.entity.Perfil;

import java.util.Date;
import java.util.List;

public interface TarifarioService {

  List<Tarifario> buscarTarifarios(Date fechaInicio, Date fechaFin, Integer lugar, Integer moneda, Integer estado);

  List<CronogramaModalidad> listarLugares();

  List<TarifarioMoneda> listarMonedas();

  List<TarifarioEstado> listarEstados();

  List<Perfil> listarPerfiles();

  Tarifario saveTarifario(Tarifario tarifario);

  Tarifario findTarifarioById(Long id);

  Tarifario saveT(Tarifario tarifario);
}
