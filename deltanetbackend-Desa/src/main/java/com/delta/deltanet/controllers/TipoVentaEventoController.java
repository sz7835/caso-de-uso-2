package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.TipoVentaEventoEstadisticasDTO;
import com.delta.deltanet.models.service.TipoVentaEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/types-event-sales")
public class TipoVentaEventoController {

  @Autowired
  private TipoVentaEventoService tipoVentaEventoService;

  @PostMapping("/buscar-estadisticas")
  public ResponseEntity<?> buscarTipoVentaEventoConEstadisticas(
      @RequestParam(required = false, defaultValue = "0") Integer estado,
      @RequestParam(required = false, defaultValue = "0") Integer tipo,
      @RequestParam(required = false) String fechaInicio,
      @RequestParam(required = false) String fechaFin) {

    Map<String, Object> response = new HashMap<>();
    try {
      // Validar fechas
      if ((fechaInicio != null && fechaFin == null) ||
          (fechaInicio == null && fechaFin != null)) {
        response.put("message", "Debe proporcionar tanto la fecha de inicio como la fecha de fin.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }

      List<TipoVentaEventoEstadisticasDTO> resultados = tipoVentaEventoService.buscarTipoVentaEventoConEstadisticas(
          estado, tipo, fechaInicio, fechaFin);

      response.put("data", resultados);
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
      response.put("message", "Error al realizar la búsqueda con estadísticas");
      response.put("error", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/buscar")
  public ResponseEntity<?> buscarTipoVentaEvento(
      @RequestParam(required = false, defaultValue = "0") Integer id,
      @RequestParam(required = false) String nombre,
      @RequestParam(required = false, defaultValue = "0") Integer estado) {
    Map<String, Object> response = new HashMap<>();
    try {
      List<TipoVentaEvento> resultados = tipoVentaEventoService.buscarTipoVentaEvento(id, nombre, estado);
      response.put("data", resultados);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("message", "Error al realizar la búsqueda");
      response.put("error", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> crearTipoVentaEvento(
      @RequestParam String descripcion,
      @RequestParam String usuario) {
    Map<String, Object> response = new HashMap<>();
    try {
      String descTrim = descripcion != null ? descripcion.trim() : "";
      if (descTrim.isEmpty()) {
        response.put("message", "La descripción no puede estar vacía");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      if (!descTrim.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]+$")) {
        response.put("message", "La descripción no puede contener caracteres especiales");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      // Validar duplicado activo usando el método eficiente del service
      if (tipoVentaEventoService.existsByDescripcionActiva(descTrim)) {
        response.put("message", "Ya existe un tipo de evento de venta activo con esa descripción");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
      }
      TipoVentaEvento nuevoTipo = new TipoVentaEvento();
      nuevoTipo.setDescripcion(descTrim);
      nuevoTipo.setEstado(1);
      nuevoTipo.setCreateUser(usuario);
      nuevoTipo.setCreateDate(new Date());
      TipoVentaEvento savedTipo = tipoVentaEventoService.guardar(nuevoTipo);
      response.put("message", "Tipo de evento de venta creado exitosamente");
      response.put("data", savedTipo);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      response.put("message", "Error al crear el tipo de evento de venta");
      response.put("error", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/update")
  public ResponseEntity<?> actualizarTipoVentaEvento(
      @RequestParam Integer id,
      @RequestParam String descripcion,
      @RequestParam String usuario) {
    Map<String, Object> response = new HashMap<>();
    try {
      TipoVentaEvento tipoExistente = tipoVentaEventoService.buscarPorId(id);
      if (tipoExistente == null) {
        response.put("message", "El tipo de venta de evento no encontrado");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
      }
      String descTrim = descripcion != null ? descripcion.trim() : "";
      if (descTrim.isEmpty()) {
        response.put("message", "La descripción no puede estar vacía");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      if (!descTrim.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]+$")) {
        response.put("message", "La descripción no puede contener caracteres especiales");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      // Validar duplicado activo usando el método eficiente del service (excepto el mismo id)
      if (tipoVentaEventoService.existsByDescripcionActiva(descTrim)) {
        // Si el registro actual está activo y la descripción no cambió, permite actualizar
        if (!(tipoExistente.getEstado() == 1 && tipoExistente.getDescripcion().equalsIgnoreCase(descTrim))) {
          response.put("message", "Ya existe un tipo de evento de venta activo con esa descripción");
          return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
      }
      tipoExistente.setDescripcion(descTrim);
      tipoExistente.setUpdateUser(usuario);
      tipoExistente.setUpdateDate(new Date());
      TipoVentaEvento updatedTipo = tipoVentaEventoService.guardar(tipoExistente);
      response.put("message", "El tipo de venta de evento se actualizó satisfactoriamente");
      response.put("data", updatedTipo);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("message", "Error al actualizar el Tipo de Venta Evento");
      response.put("error", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/updateEstado")
  public ResponseEntity<?> cambiarEstadoTipoVentaEvento(
      @RequestParam Integer id,
      @RequestParam String usuario) {
    Map<String, Object> response = new HashMap<>();
    try {
      TipoVentaEvento tipoExistente = tipoVentaEventoService.buscarPorId(id);
      if (tipoExistente == null) {
        response.put("message", "No se encontró el tipo de venta de evento");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
      }
      // Si se va a activar, validar que no haya otro activo con la misma descripción
      if (tipoExistente.getEstado() == 2) { // Se va a activar
        String descTrim = tipoExistente.getDescripcion() != null ? tipoExistente.getDescripcion().trim() : "";
        if (descTrim.isEmpty()) {
          response.put("message", "La descripción no puede estar vacía");
          return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!descTrim.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]+$")) {
          response.put("message", "La descripción no puede contener caracteres especiales");
          return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (tipoVentaEventoService.existsByDescripcionActiva(descTrim)) {
          response.put("message", "Ya existe un tipo de evento de venta activo con esa descripción");
          return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
      }
      String cambio = tipoExistente.getEstado() == 1 ? "desactivó" : "activó";
      tipoExistente.setEstado(tipoExistente.getEstado() == 1 ? 2 : 1);
      tipoExistente.setUpdateUser(usuario);
      tipoExistente.setUpdateDate(new Date());
      TipoVentaEvento updatedTipo = tipoVentaEventoService.guardar(tipoExistente);
      response.put("message", "El registro se " + cambio + " satisfactoriamente.");
      response.put("data", updatedTipo);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("message", "Error al cambiar el estado del tipo de venta de evento");
      response.put("error", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}