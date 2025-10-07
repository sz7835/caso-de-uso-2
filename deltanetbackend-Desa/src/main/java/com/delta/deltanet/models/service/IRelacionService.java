package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Relacion;
import com.delta.deltanet.models.entity.RelacionDTO;
import com.delta.deltanet.models.entity.RelacionTrabajadorDatosDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface IRelacionService {
    List<Object> listaRelaciones(Long idPer);

    Long nroContratos(Long idRel);

    Relacion save(Relacion relacion);
    
    Relacion saveAndFlush(Relacion relacion);

    Relacion buscaId(Long idRel);

    List<Object> busFiltrada(Long idPer);

    List<Object> busFiltrada(Long idPer, Long idTipoRel);

    List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin);

    List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin, Long estado);

    List<Object> busFiltrada(Long idPer, Long idTipoRel, Long estado);

    List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin);

    List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin, Long estado);

    List<Object> busFiltrada4(Long idPer, Long estado);

    List<Object> busFiltrada2(Long idPer, Long estado);

    List<Object> busFiltrada3(Long idPer, Long idArea, Long estado);

    List<Object> listaPersonal();

    List<Object> listaOutsourcing();

    Long idArea(Long iRel);

    Optional<Long> findGerenciaIdByAreaId(Long idArea);

    String findAreaNameById(Long idArea);

    String findGerenciaNameById(Long idGerencia);

    List<Relacion> buscaPorArea(Long idArea);

    Relacion buscaReverse(Long NodoOrigen, Long NodoDestino, Long idTipo);

    List<RelacionDTO> findRelacionesForContrato();

    List<RelacionDTO> findRelacionesForContratoOutsourcing();

    List<RelacionTrabajadorDatosDTO> searchTrabajadorOutsourcing(String nombre, String documento);

    List<RelacionDTO> findRelacionesClientesForContratoOutsourcing();

    List<Relacion> findByNodoOrigen(Long idNodoOrigen);

	List<Relacion> buscarRelacionContactos(Long idPer, Long idTipoNodoDestino, String nombre, Long idMotivo, List<Integer> tipoRelTipos, Long tipoDoc, String nroDoc, Long estado);

    List<Relacion> searchRelCto(Long idPer);

    Relacion findByOrigenDestinoTipoEstado(Long idOrigen, Long idDestino, Long idTipoRel, Long estado);
}
