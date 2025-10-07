package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.TipoActividad;
import com.delta.deltanet.models.service.TipoActividad2ServiceImpl;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/tipo-actividad")
public class TipoActividad2Controller {

	@Autowired
    private TipoActividad2ServiceImpl tipoActividadService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        List<TipoActividad> resultados = tipoActividadService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        TipoActividad res = tipoActividadService.findById(id);
        if (res == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo actividad con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo actividad encontrado con éxito");
        response.put("data", res);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String descripcion,
                                    @RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        String nombreTrim = descripcion == null ? "" : descripcion.trim();
        if (nombreTrim.isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nombreTrim.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (tipoActividadService.existsNombreActivo(nombreTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoActividad reg = new TipoActividad();
        reg.setNombre(nombreTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = tipoActividadService.save(reg);
        response.put("message", "Tipo actividad creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String descripcion,
                                    @RequestParam String updateUser) {
        TipoActividad reg = tipoActividadService.findById(id);

        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo actividad con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        if (descripcion != null) {
            String nombreTrim = descripcion.trim();
            if (nombreTrim.isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (tipoActividadService.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = tipoActividadService.save(reg);
        response.put("message", "Tipo actividad actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
        TipoActividad reg = tipoActividadService.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo actividad con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        Integer estadoActual = reg.getEstado();
        Integer nuevoEstado;
        String mensaje;
        switch (estadoActual) {
            case 1:
                nuevoEstado = 0;
                mensaje = "Tipo actividad desactivado con éxito";
                break;
            case 0:
                nuevoEstado = 1;
                String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
                if (tipoActividadService.existsNombreActivo(nombreTrim, id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                mensaje = "Tipo actividad activado con éxito";
                break;
            default:
                nuevoEstado = 0;
                mensaje = "Tipo actividad desactivado con éxito";
                break;
        }
        reg = tipoActividadService.changeEstado(id, nuevoEstado, username);
        response.put("message", mensaje);
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

}