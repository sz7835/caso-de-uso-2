package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmCalendarioMes;
import com.delta.deltanet.models.service.IAdmCalendarioMesService;
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
@RequestMapping("/calendario-mes")
public class AdmCalendarioMesController {

    @Autowired
    private IAdmCalendarioMesService calendarioMesService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String nombre,
                                    @RequestParam String acronimo,
                                    @RequestParam String username) {
        if (calendarioMesService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un mes con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (calendarioMesService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un mes con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        AdmCalendarioMes calendarioMes = new AdmCalendarioMes();
        calendarioMes.setNombre(nombre);
        calendarioMes.setAcronimo(acronimo);
        calendarioMes.setEstado(1);
        calendarioMes.setCreateUser(username);
        calendarioMes.setCreateDate(LocalDateTime.now());
        AdmCalendarioMes saved = calendarioMesService.save(calendarioMes);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mes creado con éxito");
        response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Optional<AdmCalendarioMes> mesOptional = calendarioMesService.findById(id);
        if (mesOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el mes con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mes encontrado con éxito");
        response.put("data", mesOptional.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam(required = false) String nombre,
                                    @RequestParam(required = false) String acronimo,
                                    @RequestParam String username) {
        Optional<AdmCalendarioMes> calendarioMesOpt = calendarioMesService.findById(id);
        if (calendarioMesOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el mes con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmCalendarioMes calendarioMes = calendarioMesOpt.get();
        if (nombre != null && !nombre.equalsIgnoreCase(calendarioMes.getNombre())
                && calendarioMesService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un mes con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (acronimo != null && !acronimo.equalsIgnoreCase(calendarioMes.getAcronimo())
                && calendarioMesService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un mes con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (nombre != null) calendarioMes.setNombre(nombre);
        if (acronimo != null) calendarioMes.setAcronimo(acronimo);
        calendarioMes.setUpdateUser(username);
        calendarioMes.setUpdateDate(LocalDateTime.now());
        AdmCalendarioMes updatedCalendario = calendarioMesService.update(calendarioMes);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mes actualizado con éxito");
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                            @RequestParam String username) {
        Optional<AdmCalendarioMes> calendarioMesOptional = calendarioMesService.findById(id);
        if (calendarioMesOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el mes con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmCalendarioMes calendarioMes = calendarioMesOptional.get();
        Integer nuevoEstado = (calendarioMes.getEstado() == 1) ? 2 : 1;
        String mensaje = (nuevoEstado == 1) ? "Mes activado con éxito" : "Mes desactivado con éxito";
        calendarioMes.setUpdateUser(username);
        calendarioMes.setUpdateDate(LocalDateTime.now());
        AdmCalendarioMes updatedCalendarioMes = calendarioMesService.cambiarEstado(id, nuevoEstado);
        calendarioMes.setEstado(nuevoEstado);
        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", updatedCalendarioMes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String nombre,
                                    @RequestParam(required = false) Integer estado) {
        List<AdmCalendarioMes> resultados = calendarioMesService.buscarPorFiltros(
            nombre != null && !nombre.isEmpty() ? nombre : null,
            estado != null && estado != 0 ? estado : null
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda realizada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }
}