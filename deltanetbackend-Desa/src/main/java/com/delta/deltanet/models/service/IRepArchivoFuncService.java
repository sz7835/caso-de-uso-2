package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoFuncService {
    List<RepArchivoFuncionalidad> getRepFuncionalidades(Long idSubModulo);
    Optional<RepArchivoFuncionalidad> busca(Long idFunc);


    List<RepArchivoFuncionalidad> findAll();
    RepArchivoFuncionalidad save(RepArchivoFuncionalidad funcionalidad);
}
