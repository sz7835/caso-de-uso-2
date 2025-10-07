package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.ClienteSolicitud;
import com.delta.deltanet.models.service.ClienteSolicitudService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/client-requests")
public class ClienteSolicitudController {

    @Autowired
    private ClienteSolicitudService clienteSolicitudService;

    @PostMapping("/search")
    public ResponseEntity<?> buscarClienteSolicitud(
            @RequestParam(required = false, defaultValue = "0") Integer id,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar que si una fecha está presente, la otra también
            if ((fechaInicio != null && fechaFin == null) || (fechaInicio == null && fechaFin != null)) {
                response.put("message", "Debe proporcionar tanto la fecha de inicio como la fecha de fin.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Validar que fechaInicio sea menor que fechaFin
            if (fechaInicio != null && fechaFin != null && fechaInicio.after(fechaFin)) {
                response.put("message", "La fecha de inicio debe ser menor o igual que la fecha de fin.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Realizar la búsqueda con los filtros proporcionados
            List<ClienteSolicitud> resultados = clienteSolicitudService.buscarClienteSolicitudConFechas(
                    id, descripcion, fechaInicio, fechaFin, estado);
            response.put("data", resultados);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al realizar la búsqueda");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearClienteSolicitud(
            @RequestParam String descripcion,
            @RequestParam String usuario,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaCompromiso) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Crear una nueva solicitud
            ClienteSolicitud nuevaSolicitud = new ClienteSolicitud();
            nuevaSolicitud.setDescripcion(descripcion);
            nuevaSolicitud.setEstado(1);
            nuevaSolicitud.setCreateUser(usuario);
            nuevaSolicitud.setCreateDate(new Date());
            nuevaSolicitud.setFechaCompromiso(fechaCompromiso);

            // Guardar la solicitud
            ClienteSolicitud savedSolicitud = clienteSolicitudService.guardar(nuevaSolicitud);
            response.put("message", "Solicitud de cliente creada exitosamente");
            response.put("data", savedSolicitud);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error al crear la solicitud de cliente");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizarClienteSolicitud(
            @RequestParam Integer id,
            @RequestParam String descripcion,
            @RequestParam String usuario,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaCompromiso) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar la solicitud existente
            ClienteSolicitud solicitudExistente = clienteSolicitudService.buscarPorId(id);
            if (solicitudExistente == null) {
                response.put("message", "La solicitud de cliente no fue encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Actualizar los campos
            solicitudExistente.setDescripcion(descripcion);
            solicitudExistente.setUpdateUser(usuario);
            solicitudExistente.setUpdateDate(new Date());
            solicitudExistente.setFechaCompromiso(fechaCompromiso);

            // Guardar los cambios
            ClienteSolicitud updatedSolicitud = clienteSolicitudService.guardar(solicitudExistente);
            response.put("message", "Solicitud de cliente actualizada satisfactoriamente");
            response.put("data", updatedSolicitud);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al actualizar la solicitud de cliente");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateEstado")
    public ResponseEntity<?> cambiarEstadoSolicitudCliente(
            @RequestParam Integer id,
            @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            ClienteSolicitud solicitudExistente = clienteSolicitudService.buscarPorId(id);
            if (solicitudExistente == null) {
                response.put("message", "No se encontró la solicitud de cliente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            String cambio = solicitudExistente.getEstado() == 1 ? "desactivó" : "activó";
            solicitudExistente.setEstado(solicitudExistente.getEstado() == 1 ? 2 : 1);
            solicitudExistente.setUpdateUser(usuario);
            solicitudExistente.setUpdateDate(new Date());
            ClienteSolicitud updatedSolicitud = clienteSolicitudService.guardar(solicitudExistente);
            response.put("message", "El registro se " + cambio + " satisfactoriamente.");
            response.put("data", updatedSolicitud);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al cambiar el estado de la solicitud de cliente");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
