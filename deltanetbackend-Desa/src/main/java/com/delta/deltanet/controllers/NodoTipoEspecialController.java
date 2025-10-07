package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.NodoTipo;
import com.delta.deltanet.models.service.INodoTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/nodo-tipo/especial")
@CrossOrigin(origins = {"*"})
public class NodoTipoEspecialController {

    @Autowired
    private INodoTipoService nodoTipoService;

    @GetMapping("/index")
    public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Long id,
                                           @RequestParam(required = false) Integer estado) {
        Map<String, Object> response = new HashMap<>();
        List<NodoTipo> registros = new ArrayList<>();
        if (id != null) {
            NodoTipo reg = nodoTipoService.findById(id);
            if (reg == null) {
                response.put("error", true);
                response.put("message", "No existe el registro proporcionado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (reg.getEstado() == 0) {
                response.put("error", true);
                response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
                response.put("data", Collections.singletonList(reg));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (estado == null || reg.getEstado() == estado) {
                registros.add(reg);
            }
        } else if (estado != null) {
            for (NodoTipo reg : nodoTipoService.findAll()) {
                if (reg.getEstado() == estado) {
                    registros.add(reg);
                }
            }
        } else {
            registros = nodoTipoService.findAll();
        }
        response.put("data", registros);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEspecial(@RequestParam String nombre,
                                            @RequestParam(required = false) String acronimo,
                                            @RequestParam String username) {
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
        if (nodoTipoService.existsNombreActivo(nombreTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String acronimoTrim = acronimo == null ? null : acronimo.trim();
        if (acronimoTrim != null && !acronimoTrim.isEmpty() && nodoTipoService.existsAcronimoActivo(acronimoTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        NodoTipo nuevo = new NodoTipo();
        nuevo.setNombre(nombreTrim);
        if (acronimoTrim != null && !acronimoTrim.isEmpty()) nuevo.setAbrev(acronimoTrim);
        nuevo.setEstado(1);
        nuevo.setCreateUser(username);
        nuevo.setCreateDate(new java.util.Date());
        nodoTipoService.save(nuevo);
        response.put("message", "Registro creado correctamente");
        response.put("registro", nuevo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEspecial(@RequestParam Long id,
                                            @RequestParam(required = false) String nombre,
                                            @RequestParam(required = false) String acronimo,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        NodoTipo reg = nodoTipoService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
            if (nodoTipoService.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        String acronimoTrim = acronimo == null ? null : acronimo.trim();
        if (acronimoTrim != null && !acronimoTrim.isEmpty() && nodoTipoService.existsAcronimoActivo(acronimoTrim, id)) {
            response.put("error", true);
            response.put("message", "Ya existe otro registro activo con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (acronimoTrim != null && !acronimoTrim.isEmpty()) reg.setAbrev(acronimoTrim);
        reg.setUpdateUser(username);
        reg.setUpdateDate(new java.util.Date());
        nodoTipoService.save(reg);
        response.put("message", "Registro actualizado correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatusEspecial(@RequestParam Long id,
                                                  @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        NodoTipo reg = nodoTipoService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (reg.getEstado() == 1) {
            reg.setEstado(0);
            reg.setUpdateUser(username);
            reg.setUpdateDate(new java.util.Date());
            response.put("message", "Registro desactivado correctamente");
        } else {
            String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
            String acronimoTrim = reg.getAbrev() == null ? null : reg.getAbrev().trim();
            if (acronimoTrim != null && !acronimoTrim.isEmpty() && nodoTipoService.existsAcronimoActivo(acronimoTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese acrónimo");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nodoTipoService.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setEstado(1);
            reg.setUpdateUser(username);
            reg.setUpdateDate(new java.util.Date());
            response.put("message", "Registro activado correctamente");
        }
        nodoTipoService.save(reg);
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
