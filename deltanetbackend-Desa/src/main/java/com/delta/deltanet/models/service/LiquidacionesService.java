package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.LiquidacionCronograma;
import com.delta.deltanet.models.entity.LiquidacionResponseDTO;
import com.delta.deltanet.models.entity.Liquidaciones;
import com.delta.deltanet.models.entity.Persona;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LiquidacionesService {

  List<LiquidacionResponseDTO> buscarLiquidacionesConFiltros(Integer idCliente, Date fechaInicio, Date fechaFin, Integer estado);
  Page<LiquidacionResponseDTO> buscarLiquidacionesConFiltrosPaginado(Integer idCliente, Date fechaInicio, Date fechaFin, Integer estado, Pageable pageable);

  Liquidaciones findLiquidacionById(Long id);

  Liquidaciones save(Liquidaciones liquidacion);

  List<Persona> findPersonasJuridicasByTipoPersona(Long tipoPersonaId, String razonSocial, String documento);

  List<Contrato> getContratosByFilters(Long idTipoContrato, Long idTipoServicio, String descripcion, Long personaId);

  Map<String, Object> obtenerTipos();

  Map<String, Object> obtenerDatosCronograma(Long idContrato);

  void deleteById(Long id);
  List<Liquidaciones> findAll();

  Optional<LiquidacionCronograma> buscaLiqCro(Long idCronograma, Long idLiquidacion);

  LiquidacionCronograma saveLiqCrono(LiquidacionCronograma liquidacionCronograma);
  List<LiquidacionCronograma> ListCronos(Long id);

  void deleteCronoLiq(Long id);

  void deleteCronoLiqs(Long id);
}