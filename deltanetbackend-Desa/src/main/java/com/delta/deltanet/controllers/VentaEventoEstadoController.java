package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.VentaEventoEstado;
import com.delta.deltanet.models.service.IVentaEventoEstadoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/venta-evento-estado")
public class VentaEventoEstadoController {

	@Autowired
    private IVentaEventoEstadoService service;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        List<VentaEventoEstado> resultados = service.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        VentaEventoEstado res = service.findById(id);
        if (res == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró estado de venta evento con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado de venta evento encontrado con éxito");
        response.put("data", res);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String descripcion,
                                    @RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        String descripcionTrim = descripcion == null ? "" : descripcion.trim();
        if (descripcionTrim.isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'descripcion' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descripcionTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'descripcion' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descripcionTrim.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'descripcion' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (service.existsDescripcionActivo(descripcionTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con esa descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        VentaEventoEstado reg = new VentaEventoEstado();
        reg.setDescripcion(descripcionTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = service.save(reg);
        response.put("message", "Estado de venta evento creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
						            @RequestParam String descripcion,
						            @RequestParam String updateUser) {
        VentaEventoEstado reg = service.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró estado de venta evento con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        if (descripcion != null) {
            String descripcionTrim = descripcion.trim();
            if (descripcionTrim.isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'descripcion' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!descripcionTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'descripcion' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripcionTrim.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'descripcion' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (service.existsDescripcionActivo(descripcionTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDescripcion(descripcionTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = service.save(reg);
        response.put("message", "Estado de venta evento se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
        VentaEventoEstado reg = service.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró skill de postulante con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        Integer estadoActual = reg.getEstado();
        Integer nuevoEstado;
        String mensaje;
        switch (estadoActual) {
            case 1:
                nuevoEstado = 0;
                mensaje = "Estado de venta evento desactivado con éxito";
                break;
            case 0:
                nuevoEstado = 1;
                String descripcionTrim = reg.getDescripcion() == null ? "" : reg.getDescripcion().trim();
                if (service.existsDescripcionActivo(descripcionTrim, id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                mensaje = "Estado de venta evento activado con éxito";
                break;
            default:
                nuevoEstado = 0;
                mensaje = "Estado de venta evento desactivado con éxito";
                break;
        }
        reg = service.changeEstado(id, nuevoEstado, username);
        response.put("message", mensaje);
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }
}