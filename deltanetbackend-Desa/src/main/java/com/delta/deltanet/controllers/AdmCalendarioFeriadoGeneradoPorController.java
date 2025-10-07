package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmCalendarioFeriadoGeneradoPor;
import com.delta.deltanet.models.service.IAdmCalendarioFeriadoGeneradoPorService;
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
@RequestMapping("/origen-feriado")
public class AdmCalendarioFeriadoGeneradoPorController {

    @Autowired
    private IAdmCalendarioFeriadoGeneradoPorService calendarioFeriadoGeneradoPorService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String descripcion,
                                    @RequestParam String acronimo,
                                    @RequestParam String username) {
        if (calendarioFeriadoGeneradoPorService.existsByDescripcion(descripcion)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (calendarioFeriadoGeneradoPorService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor = new AdmCalendarioFeriadoGeneradoPor();
        calendarioFeriadoGeneradoPor.setDescripcion(descripcion);
        calendarioFeriadoGeneradoPor.setAcronimo(acronimo);
        calendarioFeriadoGeneradoPor.setEstado(1);            calendarioFeriadoGeneradoPor.setCreateUser(username);
        calendarioFeriadoGeneradoPor.setCreateDate(LocalDateTime.now());
        AdmCalendarioFeriadoGeneradoPor saved = calendarioFeriadoGeneradoPorService.save(calendarioFeriadoGeneradoPor);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Feriado generado por creado con éxito");            response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Optional<AdmCalendarioFeriadoGeneradoPor> feriadoOptional = calendarioFeriadoGeneradoPorService.findById(id);
        if(feriadoOptional.isEmpty()){
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el feriado generado por con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Feriado generado por encontrado con éxito");
        response.put("data", feriadoOptional.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam Long id,
            @RequestParam String descripcion,
            @RequestParam String acronimo,
            @RequestParam String username) {
        Optional<AdmCalendarioFeriadoGeneradoPor> calendarioFeriadoGeneradoPorOptional = calendarioFeriadoGeneradoPorService.findById(id);
        if (calendarioFeriadoGeneradoPorOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el feriado generado por con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor = calendarioFeriadoGeneradoPorOptional.get();
        if (calendarioFeriadoGeneradoPorService.existsByDescripcion(descripcion) && !calendarioFeriadoGeneradoPor.getDescripcion().equalsIgnoreCase(descripcion)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (calendarioFeriadoGeneradoPorService.existsByAcronimo(acronimo) && !calendarioFeriadoGeneradoPor.getAcronimo().equalsIgnoreCase(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (descripcion != null) calendarioFeriadoGeneradoPor.setDescripcion(descripcion);
        if (acronimo != null) calendarioFeriadoGeneradoPor.setAcronimo(acronimo);
        calendarioFeriadoGeneradoPor.setUpdateUser(username);
        calendarioFeriadoGeneradoPor.setUpdateDate(LocalDateTime.now());
        AdmCalendarioFeriadoGeneradoPor updated = calendarioFeriadoGeneradoPorService.update(calendarioFeriadoGeneradoPor);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Feriado generado por actualizado con éxito");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(
            @RequestParam Long id,
            @RequestParam String username) {
        AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor = calendarioFeriadoGeneradoPorService.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el feriado generado por con ID: " + id));
        Integer nuevoEstado = calendarioFeriadoGeneradoPor.getEstado() == 1 ? 2 : 1;
        String mensaje = nuevoEstado == 1 ? "Feriado generado por activado con éxito" : "Feriado generado por desactivado con éxito";
        
        calendarioFeriadoGeneradoPor.setUpdateUser(username);
        calendarioFeriadoGeneradoPor.setUpdateDate(LocalDateTime.now());
        AdmCalendarioFeriadoGeneradoPor updated = calendarioFeriadoGeneradoPorService.cambiarEstado(id, nuevoEstado);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", updated);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) Integer estado) {
        try {
            if (estado != null && estado == 0) {
                estado = null;
            }
            if (descripcion != null && descripcion.isEmpty()) {
                descripcion = null;
            }
            List<AdmCalendarioFeriadoGeneradoPor> feriados = calendarioFeriadoGeneradoPorService.buscarPorFiltros(descripcion, estado);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Búsqueda realizada con éxito");
            response.put("data", feriados);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al realizar la búsqueda");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}