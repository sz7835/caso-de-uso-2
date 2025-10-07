package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoRelacion;

import java.util.List;

public interface ITipoRelacionService {
    List<TipoRelacion> findAll();
    List<Object> listaRelaciones();
    List<TipoRelacion> Lista();
    TipoRelacion buscaId(Long idTipo);
    List<TipoRelacion> findByTipo(List<Integer> tipos, Long destino);
    List<Long> findIdsByNodoTipoOrigenAndTipoIn(Long nodoTipoOrigenId, List<Integer> tipos);
    TipoRelacion save(TipoRelacion tipoRelacion);
    List<TipoRelacion> findByDescripAndOrigenAndDestinoAndTipo(String nombre, Long origen, Long destino, Integer tipo);
}
