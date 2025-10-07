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

import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;
import com.delta.deltanet.models.service.IAdmCalendarioAnioService;
import com.delta.deltanet.models.service.IAdmCalendarioAnioEstadoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/calendario-anio")
public class AdmCalendarioAnioController {

    @Autowired
    private IAdmCalendarioAnioService calendarioAnioService;

    @Autowired
    private IAdmCalendarioAnioEstadoService calendarioAnioEstadoService;

    @GetMapping("/index")
    public ResponseEntity<?> index() {
        List<AdmCalendarioAnioEstado> estados = calendarioAnioService.index();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Lista de estados de calendario año obtenida con éxito");
        response.put("data", estados);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/listForCRUD")
    public ResponseEntity<?> listForCRUD() {
        Map<String, Object> response = new HashMap<>();
        boolean existePrincipalActivo = calendarioAnioEstadoService.existeEstadoPrincipalActivo();
        List<AdmCalendarioAnioEstado> estados = calendarioAnioEstadoService.buscarPorFiltros(null, 1);
        Map<String, Boolean> principalEstados = calendarioAnioEstadoService.obtenerEstadosPrincipalesActivos();
        if (!existePrincipalActivo) {
            response.put("message", "No hay estados principales de calendario año activos disponibles en este momento.");
            response.put("data", estados);
            response.put("principalEstados", principalEstados);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("data", estados);
        response.put("principalEstados", principalEstados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        Optional<AdmCalendarioAnio> calendarioOptional = calendarioAnioService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Calendario año encontrado con éxito");
        response.put("data", calendarioOptional.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam Integer idAnioEstado,
                                    @RequestParam String name,
                                    @RequestParam String username) {
        if (calendarioAnioService.existsByNombre(name)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un calendario año con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        AdmCalendarioAnio calendarioAnio = new AdmCalendarioAnio();
        Optional<AdmCalendarioAnioEstado> anioEstadoOptional = calendarioAnioEstadoService.findById(idAnioEstado.longValue());
        if (anioEstadoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el calendario año estado con ID: " + idAnioEstado);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        calendarioAnio.setAnioEstado(anioEstadoOptional.get());
        calendarioAnio.setNombre(name);
        calendarioAnio.setEstado(1);
        calendarioAnio.setCreateUser(username);
        calendarioAnio.setCreateDate(LocalDateTime.now());
        AdmCalendarioAnio savedCalendario = calendarioAnioService.save(calendarioAnio);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Calendario año creado con éxito");
        response.put("data", savedCalendario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Integer id,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer idAnioEstado,
                                    @RequestParam String username) {
        Optional<AdmCalendarioAnio> calendarioOptional = calendarioAnioService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AdmCalendarioAnio calendario = calendarioOptional.get();
        if (name != null && !name.equalsIgnoreCase(calendario.getNombre()) && calendarioAnioService.existsByNombre(name)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Ya existe un calendario año con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (name != null) {
        	calendario.setNombre(name);
        }
        if (idAnioEstado != null) {
            Optional<AdmCalendarioAnioEstado> anioEstadoOptional = calendarioAnioEstadoService.findById(idAnioEstado.longValue());
            if (anioEstadoOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No se encontró el calendario año estado con ID: " + idAnioEstado);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            calendario.setAnioEstado(anioEstadoOptional.get());
        }
        calendario.setUpdateUser(username);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmCalendarioAnio updatedCalendario = calendarioAnioService.update(calendario);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Calendario año actualizado con éxito");
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestParam Integer id,
                                          @RequestParam String username) {
        Optional<AdmCalendarioAnio> calendarioOptional = calendarioAnioService.findById(id);
        if (calendarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró el calendario año con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        AdmCalendarioAnio calendario = calendarioOptional.get();
        Integer newStatus = calendario.getEstado() == 1 ? 2 : 1;
        String message = newStatus == 1 ? "Calendario año activado con éxito" : "Calendario año desactivado con éxito";
        calendario.setUpdateUser(username);
        calendario.setUpdateDate(LocalDateTime.now());
        AdmCalendarioAnio updatedCalendario = calendarioAnioService.cambiarEstado(id, newStatus);
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", updatedCalendario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) Long idAnioEstado) {
        List<AdmCalendarioAnio> results = calendarioAnioService.buscarPorFiltros(
            name != null && !name.isEmpty() ? name : null,
            status != null && status != 0 ? status : null,
            idAnioEstado
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Búsqueda completada con éxito");
        response.put("data", results);
        return ResponseEntity.ok(response);
    }
}