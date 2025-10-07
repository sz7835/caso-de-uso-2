package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Direccion;
import com.delta.deltanet.models.entity.lstDireccion1;

import java.util.List;

public interface IDireccionService {
    Direccion save(Direccion direccion);
    Direccion buscaDir(Long idDireccion);
    List<Object> findAllPer(Long idPer);
    List<Object> findByDirPer(Long tipo, String dire, int estado, Long idPer);
    List<Object> findByDirPer(Long tipo, Long idPer);
    List<Object> findByDirPer(Long tipo, String dire, Long idPer);
    List<Object> findByDirPer(Long tipo, int estado, Long idPer);
    List<Object> findByDirPer(String dire, Long idPer);
    List<Object> findByDirPer(String dire, int estado, Long idPer);
    List<Object> findByDirPer2(int estado, Long idPer);
    List<Direccion> findByPersonaAndDireccion(Long idPersona, String direccion);
    List<lstDireccion1> buscarDirecciones(Long idPersona, Long tipo, String direccion, Integer estado);
}
