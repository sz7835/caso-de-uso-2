package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Cronograma;
import com.delta.deltanet.models.entity.lstCrono4;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ICronogramaService {
    List<Object> listaRelacionOutsourcing();

    List<lstCrono4> listadoFiltrado(Long idContrato,
            String descrip,
            Long idConsultor,
            Date fecIni,
            Date fecFin,
            String nroOC,
            Date fechaOC,
            Long estado,
            Integer nDias);

    Page<lstCrono4> listadoFiltrado(Long idContrato,
            String descrip,
            Long idConsultor,
            Date fecIni,
            Date fecFin,
            String nroOC,
            Date fechaOC,
            Long estado,
            Integer nDias,
            Integer column,
            String dir,
            Pageable pageable);

    List<Object> listaCronoPorIdPer(Long id);

    List<Object> ListaPorVencer();

    String ObtEmpresa(Long IdContrato);
    String ObtUsuario(Long IdContrato);
    String ObtTipoDocumento(Long IdContrato);
    String ObtNroDocumento(Long IdContrato);

    Object BuscaId(Long id);

    Cronograma findById(Long id);

    Cronograma save(Cronograma cronograma);

    // Busqueda full (sin filtros)
    List<Object> BusquedaFiltrada();

    // Busqueda por rango de fechas
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin);

    // Busqueda por rango de fechas y tipo de documento
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc);

    // Busqueda por rango de fechas, tipo de documento y nro Documento
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc);

    // Busqueda por rango de fechas, tipo de documento, nro Documento y Nombres
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres);

    // Busqueda por rango de fechas, tipo de documento, nro Documento, Nombres y
    // estado
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, String nombres, Long estado);

    // Busqueda por tipo de documento
    List<Object> BusquedaFiltrada(Long tipoDoc);

    // Busqueda por tipo de documento y nro Documento
    List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc);

    // Busqueda por tipo de documento, nro Documento y Nombres
    List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres);

    // Busqueda por tipo de documento, nro Documento, Nombres y Estado
    List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, String nombres, Long estado);

    // Busqueda por nro Documento
    List<Object> BusquedaFiltrada(String nroDoc);

    // Busqueda por nro Documento y Nombres
    List<Object> BusquedaFiltrada(String nroDoc, String nombres);

    // Busqueda por nro Documento, Nombres y Estado
    List<Object> BusquedaFiltrada(String nroDoc, String nombres, Long estado);

    // Busqueda por Nombres
    List<Object> BusquedaFiltrada1(String nombres);

    // Busqueda por Nombres y Estado
    List<Object> BusquedaFiltrada(String nombres, Long estado);

    // Busqueda por Estado
    List<Object> BusquedaFiltrada1(Long estado);

    // Busqueda por Rango de Fecgas y nro documento
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc);

    // Busqueda por Rango de Fecgas y nro documento
    List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres);

    // Busqueda por Rango de Fecgas y estado
    List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long estado);

    // Busqueda por tipo documento y nombres
    List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres);

    // Busqueda por tipo documento y estado
    List<Object> BusquedaFiltrada(Long tipoDoc, Long estado);

    // Busqueda por tipo documento y estado
    List<Object> BusquedaFiltrada1(String nroDoc, Long estado);

    // Busqueda por Rango, tipo documento y nombres
    List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nombres);

    // Busqueda por Rango, tipo documento y estado
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, Long estado);

    // Busqueda por Rango, nro doc y nombres
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres);

    // Busqueda por Rango, nro doc y estado
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, Long estado);

    // Busqueda por Rango, nombre y estado
    List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, String nombres, Long estado);

    // Busqueda por Tipo Doc, Nro doc, estado
    List<Object> BusquedaFiltrada(Long tipoDoc, String nroDoc, Long estado);

    // Busqueda por Tipo Doc, Nombres y estado
    List<Object> BusquedaFiltrada1(Long tipoDoc, String nombres, Long estado);

    // Busqueda por Rango, nro Doc, nombre y estado
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, String nroDoc, String nombres, Long estado);

    // Busqueda por Rango, tipo Doc, nombre y estado
    List<Object> BusquedaFiltrada(Date fecIni, Date fecFin, Long tipoDoc, String nombres, Long estado);

    // Busqueda por Rango, tipo Doc, nro Doc y estado
    List<Object> BusquedaFiltrada1(Date fecIni, Date fecFin, Long tipoDoc, String nroDoc, Long estado);

    List<Object> listaRecursos();

    List<Cronograma> BusquedaCronogramas(Long contactID);

    List<Cronograma> findByPersonaAndEstado(Long idPersona, Long estado);

    List<Cronograma> findByEstado(Long estado);

}
