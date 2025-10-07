package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.RepArchivoEtiqueta;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoEtiquetaService {
    Optional<RepArchivoEtiqueta> findByNombre(String nombre);
    List<RepArchivoEtiqueta> findAll();
    Optional<RepArchivoEtiqueta> getById(Long id);
    RepArchivoEtiqueta save(RepArchivoEtiqueta etiqueta);
}
