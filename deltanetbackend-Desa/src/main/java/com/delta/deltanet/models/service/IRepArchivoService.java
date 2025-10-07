package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.RepArchivo;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoService {
    List<RepArchivo> getRepArchivos(Long idContrato);
    List<RepArchivo> getRepArchivos(Long idContrato, String table);
    RepArchivo save (RepArchivo repArchivo);
    void delete(Long idArchivo);
    Optional<RepArchivo> getArchivo(Long idArchivo);
}
