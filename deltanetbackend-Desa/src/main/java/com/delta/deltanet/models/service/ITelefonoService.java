package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Telefono;

import java.util.List;

@SuppressWarnings("ALL")
public interface ITelefonoService {
    List<Telefono> findAllFonos(Long id);
    List<Object> findAllPer(Long idPersona);
    List<Object> findByTelefonosPer(Long tipo, String numero, Integer estado, Long idPersona);
    List<Object> findByTelefonosPer(Long tipo, Long idPersona);
    List<Object> findByTelefonosPer(Long tipo, String numero, Long idPersona);
    List<Object> findByTelefonosPer(Long tipo, Integer estado, Long idPersona);
    List<Object> findByTelefonosPer(String numero, Long idPersona);
    List<Object> findByTelefonosPer(String numero, Integer estado, Long idPersona);
    List<Object> findByTelefonosPer(Integer estado, Long idPersona);
    Telefono findById(Long idTelefono);
    Telefono save(Telefono telefono);
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndPersona_IdNot(String numero, Long id);
    

}
