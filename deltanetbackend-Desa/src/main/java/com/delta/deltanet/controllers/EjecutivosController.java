package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.Ejecutivos;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.service.IEjecutivosService;
import com.delta.deltanet.models.service.IPersonaService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/ejecutivos")
public class EjecutivosController {

    @Autowired
    private IEjecutivosService ejecutivosService;
    
    @Autowired
    private IPersonaService perService;

    @GetMapping("/index")
    public ResponseEntity<?> findAll() {
    	List<Ejecutivos> resultados = ejecutivosService.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
    	Ejecutivos ejecutivos = ejecutivosService.findById(id);
    	if (ejecutivos == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró el ejecutivo con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Ejecutivo encontrado con éxito");
    	response.put("data", ejecutivos);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam Long idPer,
                                    @RequestParam String nombrePuesto,
                                    @RequestParam String createUser) {
        Ejecutivos ejecutivos = new Ejecutivos();
        Persona persona = perService.buscarId(idPer);
        Map<String, Object> response = new HashMap<>();
        if (persona == null) {
            response.put("message", "No se encontró la persona con ID: " + idPer);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (persona.getPerNat() == null) {
            response.put("message", "La persona con ID: " + idPer + " no es una persona natural.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // Validaciones de nombrePuesto
        String nombrePuestoTrim = nombrePuesto == null ? "" : nombrePuesto.trim();
        if (nombrePuestoTrim.isEmpty()) {
            response.put("message", "El campo 'nombrePuesto' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nombrePuestoTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _\\-/]+$")) {
            response.put("message", "El campo 'nombrePuesto' solo permite letras, números, espacios, guion y slash");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nombrePuestoTrim.matches("^\\d+$")) {
            response.put("message", "El campo 'nombrePuesto' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación de duplicado (nombrePuesto puede repetirse pero no con el mismo idPer y estado=1)
        if (ejecutivosService.existsNombrePuestoActivoPorPersona(nombrePuestoTrim, idPer, null)) {
            response.put("message", "Ya existe un ejecutivo activo con ese nombre de puesto para la misma persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ejecutivos.setPersona(persona);
        ejecutivos.setNombre(persona.getPerNat().getNombre() + " " + persona.getPerNat().getApePaterno() + " " + persona.getPerNat().getApeMaterno());
        ejecutivos.setNombrePuesto(nombrePuestoTrim);
        ejecutivos.setEstado(1);
        ejecutivos.setCreateUser(createUser);
        ejecutivos.setCreateDate(new Date());
        ejecutivos = ejecutivosService.save(ejecutivos);
        response.put("message", "Ejecutivo creado con éxito");
        response.put("data", ejecutivos);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam(required = false) Long idPer,
                                    @RequestParam(required = false) String nombrePuesto,
                                    @RequestParam String updateUser) {
        Ejecutivos ejecutivo = ejecutivosService.findById(id);
        Persona persona = null;
        Map<String, Object> response = new HashMap<>();
        if (idPer != null) persona = perService.buscarId(idPer);
        if (idPer != null && persona == null) {
            response.put("message", "No se encontró la persona con ID: " + idPer);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (idPer != null && persona.getPerNat() == null) {
            response.put("message", "La persona con ID: " + idPer + " no es una persona natural.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (ejecutivo == null) {
            response.put("message", "No se encontró el ejecutivo con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        // Validaciones de nombrePuesto
        String nombrePuestoTrim = nombrePuesto != null ? nombrePuesto.trim() : ejecutivo.getNombrePuesto();
        if (nombrePuesto != null) {
            if (nombrePuestoTrim.isEmpty()) {
                response.put("message", "El campo 'nombrePuesto' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombrePuestoTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _\\-/]+$")) {
                response.put("message", "El campo 'nombrePuesto' solo permite letras, números, espacios, guion y slash");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombrePuestoTrim.matches("^\\d+$")) {
                response.put("message", "El campo 'nombrePuesto' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        // Validación de duplicado
        Long idPersonaVal = idPer != null ? idPer : (ejecutivo.getPersona() != null ? ejecutivo.getPersona().getId() : null);
        if (ejecutivosService.existsNombrePuestoActivoPorPersona(nombrePuestoTrim, idPersonaVal, id)) {
            response.put("message", "Ya existe un ejecutivo activo con ese nombre de puesto para la misma persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (idPer != null) {
            ejecutivo.setPersona(persona);
            ejecutivo.setNombre(persona.getPerNat().getNombre() + " " + persona.getPerNat().getApePaterno() + " " + persona.getPerNat().getApeMaterno());
        }
        if (nombrePuesto != null) ejecutivo.setNombrePuesto(nombrePuestoTrim);
        ejecutivo.setUpdateUser(updateUser);
        ejecutivo.setUpdateDate(new Date());
        ejecutivo = ejecutivosService.save(ejecutivo);
        response.put("message", "Ejecutivo se ha actualizado con éxito");
        response.put("data", ejecutivo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
        Ejecutivos ejecutivo = ejecutivosService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (ejecutivo == null) {
            response.put("message", "No se encontró el ejecutivo con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Integer nuevoEstado = ejecutivo.getEstado() == 1 ? 0 : 1;
        String mensaje = nuevoEstado == 1 ? "Ejecutivo activado con éxito" : "Ejecutivo desactivado con éxito";
        // Si se va a activar, validar duplicado
        if (nuevoEstado == 1) {
            String nombrePuestoTrim = ejecutivo.getNombrePuesto() == null ? "" : ejecutivo.getNombrePuesto().trim();
            Long idPersonaVal = ejecutivo.getPersona() != null ? ejecutivo.getPersona().getId() : null;
            if (ejecutivosService.existsNombrePuestoActivoPorPersona(nombrePuestoTrim, idPersonaVal, id)) {
                response.put("message", "Ya existe un ejecutivo activo con ese nombre de puesto para la misma persona");
                response.put("data", ejecutivo);
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        ejecutivo = ejecutivosService.changeEstado(id, nuevoEstado, username);
        response.put("message", mensaje);
        response.put("data", ejecutivo);
        return ResponseEntity.ok(response);
    }

}