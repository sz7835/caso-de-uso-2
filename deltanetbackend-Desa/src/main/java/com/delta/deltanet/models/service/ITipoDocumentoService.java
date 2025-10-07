package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.TipoDocumento;

import java.util.List;

public interface ITipoDocumentoService {
    List<TipoDocumento> findAll();
    TipoDocumento buscaTipDoc(Long idTipoDoc);
    Integer buscarLongitud(Long idTipoDoc);
}
