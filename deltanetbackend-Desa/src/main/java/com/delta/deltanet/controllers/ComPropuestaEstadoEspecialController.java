package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.ComPropuestaEstado;
import com.delta.deltanet.models.service.ComPropuestaEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/propuesta-estado/especial")
@CrossOrigin(origins = {"*"})
public class ComPropuestaEstadoEspecialController {

    @Autowired
    private ComPropuestaEstadoService estadoService;

    @GetMapping("/index")
    public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Integer id,
                                           @RequestParam(required = false) Integer estado) {
        Map<String, Object> response = new HashMap<>();
        List<ComPropuestaEstado> registros = new ArrayList<>();
        if (id != null) {
            Optional<ComPropuestaEstado> opt = estadoService.findById(id);
            if (!opt.isPresent()) {
                response.put("error", true);
                response.put("message", "No existe el registro proporcionado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            ComPropuestaEstado reg = opt.get();
            if (reg.getEstado() != 1) {
                response.put("error", true);
                response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
                response.put("data", Collections.singletonList(reg));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (estado == null || estado == 0 || reg.getEstado().equals(estado)) {
                registros.add(reg);
            }
        } else if (estado != null) {
            for (ComPropuestaEstado reg : estadoService.findAll()) {
                if (reg.getEstado().equals(estado)) {
                    registros.add(reg);
                }
            }
        } else {
            registros = estadoService.findAll();
        }
        response.put("data", registros);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEspecial(@RequestParam String descripcion,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        descripcion = descripcion.trim();
        // Validación: no caracteres especiales ni solo números
        if (descripcion.isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descripcion.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (estadoService.existsDescripcionActiva(descripcion, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ComPropuestaEstado nuevo = new ComPropuestaEstado();
        nuevo.setDescripcion(descripcion);
        nuevo.setEstado(1);
        nuevo.setCreateUser(username);
        nuevo.setCreateDate(LocalDateTime.now());
        estadoService.save(nuevo);
        response.put("message", "Registro creado correctamente");
        response.put("registro", nuevo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEspecial(@RequestParam Integer id,
                                            @RequestParam(required = false) String descripcion,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Optional<ComPropuestaEstado> opt = estadoService.findById(id);
        if (!opt.isPresent()) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ComPropuestaEstado reg = opt.get();
        if (descripcion != null) {
            descripcion = descripcion.trim();
            if (descripcion.isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripcion.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (estadoService.existsDescripcionActiva(descripcion, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDescripcion(descripcion);
        }
        reg.setUpdateUser(username);
        reg.setUpdateDate(LocalDateTime.now());
        estadoService.save(reg);
        response.put("message", "Registro actualizado correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatusEspecial(@RequestParam Integer id,
                                                  @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Optional<ComPropuestaEstado> opt = estadoService.findById(id);
        if (!opt.isPresent()) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ComPropuestaEstado reg = opt.get();
        String mensaje;
        switch (reg.getEstado()) {
            case 1:
                reg.setEstado(0);
                reg.setUpdateUser(username);
                reg.setUpdateDate(LocalDateTime.now());
                estadoService.save(reg);
                mensaje = "Registro desactivado correctamente";
                break;
            case 0:
                // Validar descripcion antes de activar
                String descripcion = reg.getDescripcion();
                if (descripcion == null || descripcion.trim().isEmpty()) {
                    response.put("error", true);
                    response.put("message", "El campo 'nombre' es obligatorio");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                    response.put("error", true);
                    response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                if (descripcion.matches("^\\d+$")) {
                    response.put("error", true);
                    response.put("message", "El campo 'nombre' no puede ser completamente numérico");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                if (estadoService.existsDescripcionActiva(descripcion, id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                reg.setEstado(1);
                reg.setUpdateUser(username);
                reg.setUpdateDate(LocalDateTime.now());
                estadoService.save(reg);
                mensaje = "Registro activado correctamente";
                break;
            default:
                reg.setEstado(0);
                reg.setUpdateUser(username);
                reg.setUpdateDate(LocalDateTime.now());
                estadoService.save(reg);
                mensaje = "Registro desactivado correctamente";
                break;
        }
        response.put("message", mensaje);
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
