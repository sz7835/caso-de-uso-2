package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Modalidad;
import com.delta.deltanet.models.service.ModalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/modalidad")
public class ModalidadController {
    @Autowired
    private ModalidadService modalidadService;

    @GetMapping("/index")
    public List<Modalidad> index(@RequestParam(required = false) Integer estado,
                                 @RequestParam(required = false) Long id) {
        if (id != null) {
            Modalidad modalidad = modalidadService.findById(id);
            if (modalidad != null) return Collections.singletonList(modalidad);
            return new ArrayList<>();
        } else if (estado != null) {
            return modalidadService.findByEstado(estado);
        } else {
            return modalidadService.findAll();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam String nombre,
                                                    @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (nombre == null || username == null) {
            response.put("message", "El nombre y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: nombre no debe ser solo numérico ni contener caracteres especiales
        if (nombre.matches("\\d+")) {
            response.put("message", "El nombre no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s]+$")) {
            response.put("message", "El nombre contiene caracteres especiales no permitidos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: no debe existir otro con el mismo nombre y estado 1
        List<Modalidad> existentes = modalidadService.findByNombreAndEstadoLike(nombre, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe una modalidad activa con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Modalidad modalidad = new Modalidad();
        modalidad.setNombre(nombre);
        modalidad.setEstado(1);
        modalidad.setUsuCreado(username);
        modalidad.setFechaCreado(new Date());
        modalidadService.save(modalidad);
        response.put("message", "Modalidad creada correctamente");
        response.put("modalidad", modalidad);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMd(@RequestParam Long id,
                                                        @RequestParam(required = false) String nombre,
                                                        @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "El id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Modalidad modalidad = modalidadService.findById(id);
        if (modalidad == null) {
            response.put("message", "No existe modalidad con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nombre != null) {
            // Validación: nombre no debe ser solo numérico ni contener caracteres especiales
            if (nombre.matches("\\d+")) {
                response.put("message", "El nombre no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s]+$")) {
                response.put("message", "El nombre contiene caracteres especiales no permitidos");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validación: no debe existir otro con el mismo nombre y estado 1 (omitiendo el actual)
            List<Modalidad> existentes = modalidadService.findByNombreAndEstadoLike(nombre, 1);
            boolean existe = existentes.stream().anyMatch(m -> !m.getId().equals(id));
            if (existe) {
                response.put("message", "Ya existe una modalidad activa con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            modalidad.setNombre(nombre);
        }
        modalidad.setUsuEditado(username);
        modalidad.setFechaEditado(new Date());
        modalidadService.save(modalidad);
        response.put("message", "Modalidad actualizada correctamente");
        response.put("modalidad", modalidad);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id,
                                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "El id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Modalidad modalidad = modalidadService.findById(id);
        if (modalidad == null) {
            response.put("message", "No existe modalidad con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = modalidad.getEstado();
        switch (estadoAnterior) {
            case 1:
                modalidad.setEstado(0);
                response.put("message", "Modalidad desactivada correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y estado 1
                List<Modalidad> existentes = modalidadService.findByNombreAndEstadoLike(modalidad.getNombre(), 1);
                boolean existe = existentes.stream().anyMatch(m -> !m.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe una modalidad activa con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                modalidad.setEstado(1);
                response.put("message", "Modalidad activada correctamente");
                break;
            default:
                modalidad.setEstado(0);
                response.put("message", "Modalidad desactivada correctamente");
                break;
        }
        modalidad.setUsuEditado(username);
        modalidad.setFechaEditado(new Date());
        modalidadService.save(modalidad);
        response.put("modalidad", modalidad);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
