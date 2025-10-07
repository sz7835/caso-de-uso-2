package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.ComProdServTipo;
import com.delta.deltanet.models.service.ComProdServTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tipo-producto-servicio")
@CrossOrigin(origins = {"*"})
public class ComProdServTipoEspecialController {

    @Autowired
    private ComProdServTipoService tipoService;

    @GetMapping("/index")
    public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Integer id,
                                           @RequestParam(required = false) Integer estado) {
        Map<String, Object> response = new HashMap<>();
        List<ComProdServTipo> registros = new ArrayList<>();
        if (id != null) {
            Optional<ComProdServTipo> opt = tipoService.findById(id);
            if (!opt.isPresent()) {
                response.put("error", true);
                response.put("message", "No existe el registro proporcionado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            ComProdServTipo reg = opt.get();
            if (reg.getEstado() != 1) {
                response.put("error", true);
                response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
                response.put("data", Collections.singletonList(reg));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (estado == null || estado == 0 || reg.getEstado() == estado) {
                registros.add(reg);
            }
        } else if (estado != null) {
            for (ComProdServTipo reg : tipoService.findAll()) {
                if (reg.getEstado() == estado) {
                    registros.add(reg);
                }
            }
        } else {
            registros = tipoService.findAll();
        }
        response.put("data", registros);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEspecial(@RequestParam String descripcion,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        descripcion = descripcion.trim();
        // Validar solo números
        if (descripcion.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "La descripción no puede ser completamente numérica");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar caracteres especiales
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            response.put("error", true);
            response.put("message", "La descripción no puede contener caracteres especiales ni símbolos");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (tipoService.existsDescripcionActiva(descripcion, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con esa descripción");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ComProdServTipo nuevo = new ComProdServTipo();
        nuevo.setDescripcion(descripcion);
        nuevo.setEstado(1);
        nuevo.setCreateUser(username);
        nuevo.setCreateDate(new Date());
        tipoService.save(nuevo);
        response.put("message", "Registro creado correctamente");
        response.put("registro", nuevo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEspecial(@RequestParam Integer id,
                                            @RequestParam(required = false) String descripcion,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Optional<ComProdServTipo> opt = tipoService.findById(id);
        if (!opt.isPresent()) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ComProdServTipo reg = opt.get();
        if (descripcion != null) {
            descripcion = descripcion.trim();
            // Validar solo números
            if (descripcion.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "La descripción no puede ser completamente numérica");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // Validar caracteres especiales
            if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
                response.put("error", true);
                response.put("message", "La descripción no puede contener caracteres especiales ni símbolos");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (tipoService.existsDescripcionActiva(descripcion, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            reg.setDescripcion(descripcion);
        }
        reg.setUpdateUser(username);
        reg.setUpdateDate(new Date());
        tipoService.save(reg);
        response.put("message", "Registro actualizado correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatusEspecial(@RequestParam Integer id,
                                                  @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        Optional<ComProdServTipo> opt = tipoService.findById(id);
        if (!opt.isPresent()) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        ComProdServTipo reg = opt.get();
        Integer estadoAnterior = reg.getEstado();
        String accion = "";
        switch (estadoAnterior) {
            case 1:
                reg.setEstado(0);
                accion = "desactivado";
                break;
            case 0:
                // Validar solo números
                if (reg.getDescripcion() != null && reg.getDescripcion().matches("^\\d+$")) {
                    response.put("error", true);
                    response.put("message", "La descripción no puede ser completamente numérica");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                // Validar caracteres especiales
                if (reg.getDescripcion() != null && !reg.getDescripcion().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
                    response.put("error", true);
                    response.put("message", "La descripción no puede contener caracteres especiales ni símbolos");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (tipoService.existsDescripcionActiva(reg.getDescripcion(), id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                reg.setEstado(1);
                accion = "activado";
                break;
            default:
                reg.setEstado(0);
                accion = "desactivado";
                break;
        }
        reg.setUpdateUser(username);
        reg.setUpdateDate(new Date());
        tipoService.save(reg);
        response.put("message", "Registro " + accion + " correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
