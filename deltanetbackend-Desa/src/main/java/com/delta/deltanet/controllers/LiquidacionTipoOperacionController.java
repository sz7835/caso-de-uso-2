package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.LiquidacionTipoOperacion;
import com.delta.deltanet.models.service.LiquidacionTipoOperacionService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/liquidacion-tipo-operacion")
public class LiquidacionTipoOperacionController {

    @Autowired
    private LiquidacionTipoOperacionService liquidacionTipoOperacionService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        List<LiquidacionTipoOperacion> resultados = liquidacionTipoOperacionService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        LiquidacionTipoOperacion res = liquidacionTipoOperacionService.findById(id);
        if (res == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo de liquidación operación con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo de liquidación operación encontrado con éxito");
        response.put("data", res);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String nombre,
                                    @RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        String nombreTrim = nombre == null ? "" : nombre.trim();
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
        if (liquidacionTipoOperacionService.existsNombreActivo(nombreTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        LiquidacionTipoOperacion reg = new LiquidacionTipoOperacion();
        reg.setNombre(nombreTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = liquidacionTipoOperacionService.save(reg);
        response.put("message", "Tipo de liquidación operación creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String updateUser) {
        LiquidacionTipoOperacion reg = liquidacionTipoOperacionService.findById(id);

        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo de liquidación operación con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        if (nombre != null) {
            String nombreTrim = nombre.trim();
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
            if (liquidacionTipoOperacionService.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = liquidacionTipoOperacionService.save(reg);
        response.put("message", "Tipo de liquidación operación se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
            @RequestParam String username) {
        LiquidacionTipoOperacion reg = liquidacionTipoOperacionService.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo de liquidación operación con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        Integer estadoActual = reg.getEstado();
        Integer nuevoEstado;
        String mensaje;
        switch (estadoActual) {
            case 1:
                nuevoEstado = 0;
                mensaje = "Tipo de liquidación operación desactivado con éxito";
                break;
            case 0:
                nuevoEstado = 1;
                String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
                if (liquidacionTipoOperacionService.existsNombreActivo(nombreTrim, id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                mensaje = "Tipo de liquidación operación activado con éxito";
                break;
            default:
                nuevoEstado = 0;
                mensaje = "Tipo de liquidación operación desactivado con éxito";
                break;
        }
        reg = liquidacionTipoOperacionService.changeEstado(id, nuevoEstado, username);
        response.put("message", mensaje);
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

}