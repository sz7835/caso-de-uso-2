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

import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.VentaEvento;
import com.delta.deltanet.models.service.VentaEventoService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/event-sales")
public class VentaEventoController {

    @Autowired
    private VentaEventoService ventaEventoService;

    @PostMapping("/buscar")
    public ResponseEntity<?> buscarVentaEvento(
            @RequestParam(required = false, defaultValue = "0") Integer id,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false, defaultValue = "0") Integer estado,
            @RequestParam(required = false, defaultValue = "0") Integer tipo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
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
            List<VentaEvento> resultados = ventaEventoService.buscarTipoVentaEventoConFechas(
                    id, descripcion, estado, tipo, fechaInicio, fechaFin);
            response.put("data", resultados);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al realizar la búsqueda");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/listar-tipos")
    public ResponseEntity<?> listarTiposVentaEvento() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoVentaEvento> tipos = ventaEventoService.listarTiposVentaEvento();
            response.put("data", tipos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al listar los tipos de eventos de venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearVentaEvento(
            @RequestParam String descripcion,
            @RequestParam Integer tipo,
            @RequestParam String usuario,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        Map<String, Object> response = new HashMap<>();
        try {
            TipoVentaEvento tipoEvento = ventaEventoService.buscarTipoEvento(tipo);
            if (tipoEvento == null) {
                response.put("message", "Tipo de evento no encontrado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Crear un nuevo evento
            VentaEvento nuevoEvento = new VentaEvento();
            nuevoEvento.setDescripcion(descripcion);
            nuevoEvento.setEstado(1);
            nuevoEvento.setCreateUser(usuario);
            nuevoEvento.setCreateDate(new Date());
            nuevoEvento.setFecha(fecha);
            nuevoEvento.setTipoVentaEvento(tipoEvento);

            // Guardar el evento
            VentaEvento savedEvento = ventaEventoService.guardar(nuevoEvento);
            response.put("message", "Evento de venta creado exitosamente");
            response.put("data", savedEvento);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error al crear el evento de venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizarVentaEvento(
            @RequestParam Integer id,
            @RequestParam String descripcion,
            @RequestParam Integer tipo,
            @RequestParam String usuario,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) { // Formato de fecha
        Map<String, Object> response = new HashMap<>();
        try {
            // Buscar el evento existente
            VentaEvento eventoExistente = ventaEventoService.buscarPorId(id);
            if (eventoExistente == null) {
                response.put("message", "El evento de venta no fue encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // Verificar si el tipo existe
            TipoVentaEvento tipoEvento = ventaEventoService.buscarTipoEvento(tipo);
            if (tipoEvento == null) {
                response.put("message", "Tipo de evento no encontrado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // Actualizar los campos
            eventoExistente.setDescripcion(descripcion);
            eventoExistente.setTipoVentaEvento(tipoEvento);
            eventoExistente.setUpdateUser(usuario);
            eventoExistente.setUpdateDate(new Date());
            eventoExistente.setFecha(fecha);
            // Guardar los cambios
            VentaEvento updatedEvento = ventaEventoService.guardar(eventoExistente);
            response.put("message", "Evento de venta actualizado satisfactoriamente");
            response.put("data", updatedEvento);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al actualizar el evento de venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateEstado")
    public ResponseEntity<?> cambiarEstadoVentaEvento(
            @RequestParam Integer id,
            @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            VentaEvento eventoExistente = ventaEventoService.buscarPorId(id);
            if (eventoExistente == null) {
                response.put("message", "No se encontró el evento de venta");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            String cambio = eventoExistente.getEstado() == 1 ? "desactivó" : "activó";
            eventoExistente.setEstado(eventoExistente.getEstado() == 1 ? 2 : 1);
            eventoExistente.setUpdateUser(usuario);
            eventoExistente.setUpdateDate(new Date());
            VentaEvento updatedEvento = ventaEventoService.guardar(eventoExistente);
            response.put("message", "El registro se " + cambio + " satisfactoriamente.");
            response.put("data", updatedEvento);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al cambiar el estado del evento de venta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
