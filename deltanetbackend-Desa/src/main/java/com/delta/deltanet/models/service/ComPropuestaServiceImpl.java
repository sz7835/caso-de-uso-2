package com.delta.deltanet.models.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.entity.ComPropuestaEstado;
import com.delta.deltanet.models.entity.ComPropuestaMotivoRechazo;
import com.delta.deltanet.models.dao.ComProductoServicioDao;
import com.delta.deltanet.models.dao.ComPropuestaDao;
import com.delta.deltanet.models.dao.ComPropuestaEstadoDao;
import com.delta.deltanet.models.dao.ComPropuestaRechazoDao;
import com.delta.deltanet.models.dao.ITarifarioMonedaDao;
import com.delta.deltanet.models.entity.BusquedaPropuestaDTO;
import com.delta.deltanet.models.entity.ComProductoServicio;
import com.delta.deltanet.models.entity.ComPropuesta;

@Service
public class ComPropuestaServiceImpl implements ComPropuestaService {

    @Autowired
    private ITarifarioMonedaDao monedaRepository;

    @Autowired
    private ComPropuestaEstadoDao estadoRepository;

    @Autowired
    private ComProductoServicioDao productoRepository;

    @Autowired
    private ComPropuestaDao propuestaRepository;

    @Autowired
    private ComPropuestaRechazoDao propuestaRechazoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TarifarioMoneda> obtenerTiposMoneda() {
        return monedaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComPropuestaEstado> obtenerEstadosPropuesta() {
        return estadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComProductoServicio> obtenerTiposProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComPropuesta> buscarPropuestas(BusquedaPropuestaDTO filtro) {
        validarFiltros(filtro);

        return propuestaRepository.findByFiltros(
                filtro.getFechaInicio(),
                filtro.getFechaFin(),
                filtro.getMonedaId(),
                filtro.getEstadoId());
    }

    private void validarFiltros(BusquedaPropuestaDTO filtro) {
        if (filtro.getFechaInicio() != null && filtro.getFechaFin() == null) {
            throw new IllegalArgumentException(
                    "Si se proporciona fecha de inicio, también debe proporcionarse fecha de fin.");
        }

        if (filtro.getFechaFin() != null && filtro.getFechaInicio() == null) {
            throw new IllegalArgumentException(
                    "Si se proporciona fecha de fin, también debe proporcionarse fecha de inicio.");
        }

        if (filtro.getFechaInicio() != null && filtro.getFechaFin() != null &&
                filtro.getFechaInicio().isAfter(filtro.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }

    @Override
    @Transactional
    public void save(ComPropuesta propuesta) {
        propuestaRepository.save(propuesta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComPropuesta> findById(Integer idPropuesta) {
        return propuestaRepository.buscarPorId(idPropuesta);
    }

    @Override
    @Transactional
    public ResponseEntity<?> cambiarEstadoPropuesta(Integer idPropuesta, Integer nuevoEstado, String usuario,
            String motivoRechazo, LocalDate fechaRechazo) {
        // Validar existencia de la propuesta
        Optional<ComPropuesta> propuestaOpt = propuestaRepository.findById(idPropuesta);
        if (propuestaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Propuesta no encontrada"));
        }

        ComPropuesta propuesta = propuestaOpt.get();
        Integer estadoActual = propuesta.getEstado();

        // Lógica de cambio de estado
        switch (estadoActual) {
            case 1: // Activo
                if (nuevoEstado == 4) { // Rechazar
                    return rechazarPropuesta(propuesta, usuario, motivoRechazo, fechaRechazo);
                } else if (nuevoEstado == 2) { // Desactivar
                    return desactivarPropuesta(propuesta, usuario);
                } else if (nuevoEstado == 3) { // Aceptar
                    return aceptarPropuesta(propuesta, usuario);
                }
                break;
            case 3: // Aceptado
                if (nuevoEstado == 2) { // Desactivar
                    return desactivarPropuesta(propuesta, usuario);
                } else if (nuevoEstado == 1) { // Activar
                    return activarPropuesta(propuesta, usuario);
                }
                break;
            case 2: // Desactivado
                if (nuevoEstado == 1) { // Activar
                    return activarPropuesta(propuesta, usuario);
                }
                break;
            case 4: // Rechazado
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "No se pueden realizar cambios en una propuesta rechazada"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Cambio de estado no permitido"));
    }

    private ResponseEntity<?> rechazarPropuesta(ComPropuesta propuesta, String usuario, String motivoRechazo,
            LocalDate fechaRechazo) {
        if (motivoRechazo == null || motivoRechazo.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Se requiere un motivo de rechazo"));
        }
        if (fechaRechazo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Se requiere una fecha de rechazo"));
        }

        // Crear entidad de motivo de rechazo
        ComPropuestaMotivoRechazo motivoRechazoEntity = new ComPropuestaMotivoRechazo();
        motivoRechazoEntity.setComPropuesta(propuesta.getId().longValue());
        motivoRechazoEntity.setDescripcion(motivoRechazo);
        motivoRechazoEntity.setFecha(fechaRechazo);
        motivoRechazoEntity.setCreateDate(LocalDateTime.now());
        motivoRechazoEntity.setCreateUser(usuario);

        propuesta.setEstado(4); // Rechazado
        propuesta.setUpdateUser(usuario);
        propuesta.setUpdateDate(LocalDateTime.now());

        try {
            propuestaRepository.save(propuesta);
            propuestaRechazoRepository.save(motivoRechazoEntity);
            return ResponseEntity.ok(Map.of("message", "Propuesta rechazada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al rechazar la propuesta"));
        }
    }

    private ResponseEntity<?> desactivarPropuesta(ComPropuesta propuesta, String usuario) {
        propuesta.setEstado(2); // Desactivado
        propuesta.setUpdateUser(usuario);
        propuesta.setUpdateDate(LocalDateTime.now());

        try {
            propuestaRepository.save(propuesta);
            return ResponseEntity.ok(Map.of("message", "Propuesta desactivada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al desactivar la propuesta"));
        }
    }

    private ResponseEntity<?> activarPropuesta(ComPropuesta propuesta, String usuario) {
        propuesta.setEstado(1); // Activo
        propuesta.setUpdateUser(usuario);
        propuesta.setUpdateDate(LocalDateTime.now());

        try {
            propuestaRepository.save(propuesta);
            return ResponseEntity.ok(Map.of("message", "Propuesta activada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al activar la propuesta"));
        }
    }

    private ResponseEntity<?> aceptarPropuesta(ComPropuesta propuesta, String usuario) {
        propuesta.setEstado(3); // Aceptado
        propuesta.setUpdateUser(usuario);
        propuesta.setUpdateDate(LocalDateTime.now());

        try {
            propuestaRepository.save(propuesta);
            return ResponseEntity.ok(Map.of("message", "Propuesta aceptada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al aceptar la propuesta"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComPropuestaMotivoRechazo> buscarPorIdRechazo(Long idPropuesta) {
        return propuestaRechazoRepository.findByComPropuesta(idPropuesta);
    }
}