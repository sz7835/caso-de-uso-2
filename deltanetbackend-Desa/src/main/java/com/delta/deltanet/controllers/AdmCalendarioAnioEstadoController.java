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

import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;
import com.delta.deltanet.models.service.IAdmCalendarioAnioEstadoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/calendario-anio-estado")
public class AdmCalendarioAnioEstadoController {

    @Autowired
    private IAdmCalendarioAnioEstadoService calendarioAnioEstadoService;

    @PostMapping("/create")
    public ResponseEntity<?> crearCalendarioAnioEstado(@RequestParam String nombre,
                                                        @RequestParam String acronimo,
                                                        @RequestParam String createUser) {
        AdmCalendarioAnioEstado calendario = new AdmCalendarioAnioEstado();
        if (calendarioAnioEstadoService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (calendarioAnioEstadoService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        calendario.setNombre(nombre);
        calendario.setAcronimo(acronimo);
        calendario.setEstado(1);
        calendario.setCreateUser(createUser);
        calendario.setCreateDate(LocalDateTime.now());
        AdmCalendarioAnioEstado savedCalendario = calendarioAnioEstadoService.save(calendario);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado del calendario año creado con éxito");
        response.put("data", savedCalendario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Optional<AdmCalendarioAnioEstado> calendarioOptional = calendarioAnioEstadoService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el estado del calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado del calendario año encontrado con éxito");
        response.put("data", calendarioOptional.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizarCalendarioAnioEstado(@RequestParam Long id,
                                                            @RequestParam(required = false) String nombre,
                                                            @RequestParam(required = false) String acronimo,
                                                            @RequestParam String updateUser) {
        Optional<AdmCalendarioAnioEstado> calendarioOptional = calendarioAnioEstadoService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el estado del calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmCalendarioAnioEstado calendario = calendarioOptional.get();
        if (nombre != null && !nombre.equalsIgnoreCase(calendario.getNombre()) && calendarioAnioEstadoService.existsByNombre(nombre)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (acronimo != null && !acronimo.equalsIgnoreCase(calendario.getAcronimo()) && calendarioAnioEstadoService.existsByAcronimo(acronimo)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un registro con el mismo acrónimo");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (nombre != null) calendario.setNombre(nombre);
        if (acronimo != null) calendario.setAcronimo(acronimo);
        calendario.setUpdateUser(updateUser);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmCalendarioAnioEstado updatedCalendario = calendarioAnioEstadoService.update(calendario);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Estado del calendario año actualizado con éxito");
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> cambiarEstado(@RequestParam Long id,
                                            @RequestParam String username) {
        Optional<AdmCalendarioAnioEstado> calendarioOptional = calendarioAnioEstadoService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el estado del calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmCalendarioAnioEstado calendario = calendarioOptional.get();
        Integer nuevoEstado = calendario.getEstado() == 1 ? 2 : 1;
        String mensaje = nuevoEstado == 1 ? "Estado del calendario año activado con éxito" : "Estado del calendario año desactivado con éxito";
        calendario.setUpdateUser(username);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmCalendarioAnioEstado updatedCalendario = calendarioAnioEstadoService.cambiarEstado(id, nuevoEstado);
        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> buscarCalendarioAnioEstado(@RequestParam(required = false) String nombre,
                                                        @RequestParam(required = false) Integer estado) {
        List<AdmCalendarioAnioEstado> resultados = calendarioAnioEstadoService.buscarPorFiltros(
            nombre != null && !nombre.isEmpty() ? nombre : null,
            estado != null && estado != 0 ? estado : null
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }
}