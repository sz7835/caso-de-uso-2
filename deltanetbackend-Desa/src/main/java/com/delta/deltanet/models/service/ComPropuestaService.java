package com.delta.deltanet.models.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.entity.ComPropuestaEstado;
import com.delta.deltanet.models.entity.ComPropuestaMotivoRechazo;
import com.delta.deltanet.models.entity.BusquedaPropuestaDTO;
import com.delta.deltanet.models.entity.ComProductoServicio;
import com.delta.deltanet.models.entity.ComPropuesta;

public interface ComPropuestaService {

    List<TarifarioMoneda> obtenerTiposMoneda();

    List<ComPropuestaEstado> obtenerEstadosPropuesta();

    List<ComProductoServicio> obtenerTiposProductos();

    List<ComPropuesta> buscarPropuestas(BusquedaPropuestaDTO filtro);

    void save(ComPropuesta propuesta);

    Optional<ComPropuesta> findById(Integer id);

    ResponseEntity<?> cambiarEstadoPropuesta(Integer idPropuesta, Integer nuevoEstado, String usuario,
            String motivoRechazo, LocalDate fechaRechazo);

    Optional<ComPropuestaMotivoRechazo> buscarPorIdRechazo(Long idPropuesta);
}