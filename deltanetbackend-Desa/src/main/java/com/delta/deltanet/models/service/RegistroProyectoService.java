package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmCronograma;
import com.delta.deltanet.models.entity.RegistroProyecto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface RegistroProyectoService {

    List<RegistroProyecto> findAll();

    Optional<RegistroProyecto> findById(Long id);

    Optional<RegistroProyecto> findByCodigo(String codigo);

    // RegistroProyecto save(RegistroProyecto registroProyecto);
    RegistroProyecto save(Long idPersona, String codigo, String proyectoDescripcion, String createUser);

    Optional<RegistroProyecto> updateRegistroProyecto(Long idProyecto, String proyectoDescripcion, String updateUser);

    // Map<String, Object> changeStatus(int idCronograma, int idProyecto, String
    // updateUser);

    Map<String, Object> delete(Long idProyecto);

    ResponseEntity<?> activate(List<Long> requestData, String updateUser);

    List<AdmCronograma> listCronograma(int idConsultor);

    List<AdmCronograma> findCronograma(Integer idConsultor);

    // List<RegistroProyecto> index(int idConsultor);

    List<Map<String, Object>> index(Long idConsultor, Optional<String> proyectoDescripcion, Optional<Integer> estado);

    Map<String, Object> changeStatus(Long idProyecto, String updateUser);

}