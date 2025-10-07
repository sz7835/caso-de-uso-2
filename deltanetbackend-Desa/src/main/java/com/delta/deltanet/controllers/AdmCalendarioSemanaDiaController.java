package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;
import com.delta.deltanet.models.service.IAdmCalendarioSemanaDiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/dia-semana")
public class AdmCalendarioSemanaDiaController {

    @Autowired
    private IAdmCalendarioSemanaDiaService calendarioSemanaDiaService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String nombre,
                                    @RequestParam String acronimo,
                                    @RequestParam String username) {
        if (calendarioSemanaDiaService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un día de la semana con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (calendarioSemanaDiaService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un día de la semana con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        AdmCalendarioSemanaDia calendarioSemanaDia = new AdmCalendarioSemanaDia();
        calendarioSemanaDia.setNombre(nombre);
        calendarioSemanaDia.setAcronimo(acronimo);
        calendarioSemanaDia.setEstado(1);
        calendarioSemanaDia.setCreateUser(username);
        calendarioSemanaDia.setCreateDate(LocalDateTime.now());
        AdmCalendarioSemanaDia saved = calendarioSemanaDiaService.save(calendarioSemanaDia);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Día de la semana creado con éxito");
        response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        try {
            AdmCalendarioSemanaDia dia = calendarioSemanaDiaService.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el día de la semana con ID: " + id));
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Día de la semana encontrado con éxito");
            response.put("data", dia);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al buscar el día de la semana");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String acronimo,
                                    @RequestParam String username) {
        Optional<AdmCalendarioSemanaDia> calendarioSemanaDiaOpt = calendarioSemanaDiaService.findById(id);
        if (calendarioSemanaDiaOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el día de la semana con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmCalendarioSemanaDia calendarioSemanaDia = calendarioSemanaDiaOpt.get();
        if (nombre != null && !nombre.equalsIgnoreCase(calendarioSemanaDia.getNombre()) && calendarioSemanaDiaService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un día de la semana con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (acronimo != null && !acronimo.equalsIgnoreCase(calendarioSemanaDia.getAcronimo()) && calendarioSemanaDiaService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un día de la semana con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (nombre != null) calendarioSemanaDia.setNombre(nombre);
        if (acronimo != null) calendarioSemanaDia.setAcronimo(acronimo);
        calendarioSemanaDia.setUpdateUser(username);
        calendarioSemanaDia.setUpdateDate(LocalDateTime.now());
        AdmCalendarioSemanaDia updated = calendarioSemanaDiaService.update(calendarioSemanaDia);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Día de la semana actualizado con éxito");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(
            @RequestParam Long id,
            @RequestParam String username) {
        AdmCalendarioSemanaDia calendarioSemanaDia = calendarioSemanaDiaService.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el día de la semana con ID: " + id));
        Integer nuevoEstado = calendarioSemanaDia.getEstado() == 1 ? 2 : 1;
        String mensaje = nuevoEstado == 1 ? "Día de la semana activado con éxito" : "Día de la semana desactivado con éxito";
        
        calendarioSemanaDia.setUpdateUser(username);
        calendarioSemanaDia.setUpdateDate(LocalDateTime.now());
        AdmCalendarioSemanaDia updated = calendarioSemanaDiaService.cambiarEstado(id, nuevoEstado);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", updated);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer estado) {
        try {
            if (estado != null && estado == 0) {
                estado = null;
            }
            if (nombre != null && nombre.isEmpty()) {
                nombre = null;
            }
            List<AdmCalendarioSemanaDia> dias = calendarioSemanaDiaService.buscarPorFiltros(nombre, estado);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Búsqueda realizada con éxito");
            response.put("data", dias);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al realizar la búsqueda");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}