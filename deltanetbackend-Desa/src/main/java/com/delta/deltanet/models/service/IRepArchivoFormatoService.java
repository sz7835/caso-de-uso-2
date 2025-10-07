package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.RepArchivoFormato;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoFormatoService {
    int countByNombreAndEstado(String nombre, int estado);
    int countByNombreAndEstadoExcluyendoId(String nombre, int estado, Long id);
    Optional<RepArchivoFormato> buscaExt(String extension);
    RepArchivoFormato save(RepArchivoFormato repArchivoFormato);
    RepArchivoFormato findById(Long idArchivo);
    List<RepArchivoFormato> findAll();
}
