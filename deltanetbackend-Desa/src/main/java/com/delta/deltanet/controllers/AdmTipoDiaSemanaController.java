package com.delta.deltanet.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmTipoDiaSemana;
import com.delta.deltanet.models.service.IAdmTipoDiaSemanaService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/tipo-dia-semana")
public class AdmTipoDiaSemanaController {

    @Autowired
    private IAdmTipoDiaSemanaService tipoDiaSemanaService;

    @PostMapping("/create")
    public ResponseEntity<?> crearTipoDiaSemana(@RequestParam String nombre,
                                                @RequestParam String acronimo,
                                                @RequestParam String createUser){
        if (tipoDiaSemanaService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un tipo de día de la semana con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        if (tipoDiaSemanaService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un tipo de día de la semana con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        AdmTipoDiaSemana calendario = new AdmTipoDiaSemana();
        calendario.setNombre(nombre);
        calendario.setAcronimo(acronimo);
        calendario.setEstado(1);
        calendario.setCreateUser(createUser);
        calendario.setCreateDate(LocalDateTime.now());

        AdmTipoDiaSemana savedCalendario = tipoDiaSemanaService.save(calendario);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo día semana creado con éxito");
        response.put("data", savedCalendario);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Optional<AdmTipoDiaSemana> tipoDiaSemanaOptional = tipoDiaSemanaService.findById(id);

        if (tipoDiaSemanaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo día semana con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo día semana encontrado con éxito");
        response.put("data", tipoDiaSemanaOptional.get());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizarTipoDiaSemana(@RequestParam Long id,
                                                    @RequestParam(required = false) String nombre,
                                                    @RequestParam(required = false) String acronimo,
                                                    @RequestParam String username) {
        Optional<AdmTipoDiaSemana> tipoDiaSemanaOptional = tipoDiaSemanaService.findById(id);
        if (tipoDiaSemanaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo día semana con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmTipoDiaSemana calendario = tipoDiaSemanaOptional.get();
        if (tipoDiaSemanaService.existsByNombre(nombre) && !calendario.getNombre().equalsIgnoreCase(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un tipo de día de la semana con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (tipoDiaSemanaService.existsByAcronimo(acronimo) && !calendario.getAcronimo().equalsIgnoreCase(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un tipo de día de la semana con ese acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (nombre != null) calendario.setNombre(nombre);
        if (acronimo != null) calendario.setAcronimo(acronimo);
        calendario.setUpdateUser(username);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmTipoDiaSemana updatedCalendario = tipoDiaSemanaService.update(calendario);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipo día semana actualizado con éxito");
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> cambiarEstado(
            @RequestParam Long id,
            @RequestParam String username) {
        Optional<AdmTipoDiaSemana> tipoDiaSemanaOptional = tipoDiaSemanaService.findById(id);

        if (tipoDiaSemanaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el tipo día semana con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        AdmTipoDiaSemana calendario = tipoDiaSemanaOptional.get();
        Integer nuevoEstado = calendario.getEstado() == 1 ? 2 : 1;
        String mensaje = nuevoEstado == 1 ? "Tipo día semana activado con éxito" : "Tipo día semana desactivado con éxito";

        calendario.setUpdateUser(username);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmTipoDiaSemana updatedCalendario = tipoDiaSemanaService.cambiarEstado(id, nuevoEstado);

        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", updatedCalendario);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> buscarTipoDiaSemana(   @RequestParam(required = false) String nombre,
                                                    @RequestParam(required = false) Integer estado) {
        List<AdmTipoDiaSemana> resultados = tipoDiaSemanaService.buscarPorFiltros(
            nombre != null && !nombre.isEmpty() ? nombre : null,
            estado != null && estado != 0 ? estado : null
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);

        return ResponseEntity.ok(response);
    }
}