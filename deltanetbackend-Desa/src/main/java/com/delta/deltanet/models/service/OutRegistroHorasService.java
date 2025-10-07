package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.delta.deltanet.models.entity.DetalleRegistro;
import com.delta.deltanet.models.entity.OutRegistroHoras;
import com.delta.deltanet.models.entity.RegistroProyecto;
import com.delta.deltanet.models.entity.ShowDataResponse;

public interface OutRegistroHorasService {
    List<Object> findByPersonaCronogramaFechaEstado(int idPersona, Date fechaInicio, Date fechaFin, int estado);

    List<Object> findByPersonaCronogramaFechaEstado2(int idPersona, Date fechaInicio, Date fechaFin, int estado);

    List<OutRegistroHoras> createRegistroHoras(int idProyecto, int idPersona, List<DetalleRegistro> detalles, String dia, String createUser);

    Optional<OutRegistroHoras> updateRegistroHoras(int id, String actividad, Integer horas, String update_user);

    List<Map<String, Object>> activarRegistroHoras(List<Integer> ids, String update_user);

    List<Map<String, Object>> mostrarCronograma(int idCronograma);

    List<RegistroProyecto> mostrarProyecto(Long idPersona);

    ShowDataResponse show(int id);

    Map<String, Object> deleteRegistroHoras(int id);

    List<OutRegistroHoras> findByIdProyecto(int id);

    // List<RegistroHorasResponse> index(int idPersona, int idCronograma, int meses, int estado);
    List<OutRegistroHoras> index(Integer idPersona, Integer meses, Integer estado, String fechaInicio, String fechaFin);
}
