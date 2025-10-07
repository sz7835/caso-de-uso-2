package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.Perfil;
import com.delta.deltanet.models.service.PerfilService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/perfil")
public class PerfilController {
	@Autowired
    private PerfilService perfilService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
        List<Perfil> resultados = perfilService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Perfil res = perfilService.findById(id);
        if (res == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el perfil con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil encontrado con éxito");
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
        // Validar duplicados activos
        List<Perfil> existentes = perfilService.buscarPorNombreYEstado(nombreTrim, 1);
        if (!existentes.isEmpty()) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Perfil reg = new Perfil();
        reg.setNombre(nombreTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = perfilService.save(reg);
        response.put("message", "Perfil creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String descripcion,
                                    @RequestParam String updateUser) {
        Perfil reg = perfilService.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el perfil con ID: " + id);
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
            // Validar duplicados activos (excepto el actual)
            List<Perfil> existentes = perfilService.buscarPorNombreYEstado(nombreTrim, 1);
            boolean duplicado = false;
            for (Perfil p : existentes) {
                if (!p.getId().equals(id)) {
                    duplicado = true;
                    break;
                }
            }
            if (duplicado) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = perfilService.save(reg);
        response.put("message", "Perfil se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(  @RequestParam Long id,
                                            @RequestParam String username) {
        Perfil reg = perfilService.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el perfil con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        Integer estadoActual = reg.getEstado();
        Integer nuevoEstado;
        String mensaje;
        switch (estadoActual) {
            case 1:
                nuevoEstado = 0;
                mensaje = "Perfil desactivado con éxito";
                break;
            case 0:
                nuevoEstado = 1;
                String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
                List<Perfil> existentes = perfilService.buscarPorNombreYEstado(nombreTrim, 1);
                boolean duplicado = false;
                for (Perfil p : existentes) {
                    if (!p.getId().equals(id)) {
                        duplicado = true;
                        break;
                    }
                }
                if (duplicado) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                mensaje = "Perfil activado con éxito";
                break;
            default:
                nuevoEstado = 0;
                mensaje = "Perfil desactivado con éxito";
                break;
        }
        reg = perfilService.changeEstado(id, nuevoEstado, username);
        response.put("message", mensaje);
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }
}