package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.Gerencia;
import com.delta.deltanet.models.entity.Jefatura;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.service.IJefaturaService;
import com.delta.deltanet.models.service.IGerenciaService;
import com.delta.deltanet.models.service.IPersonaService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/jefatura")
public class JefaturaController {

    @Autowired
    private IJefaturaService jefaturaService;
    
    @Autowired
    private IPersonaService perService;
    
    @Autowired
    private IGerenciaService gerenciaService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
    	List<Jefatura> resultados = jefaturaService.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
    	Jefatura res = jefaturaService.findById(id);
    	if (res == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró la jefatura con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Jefatura encontrado con éxito");
    	response.put("data", res);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam Long idPer,
									@RequestParam Long idGerencia,
									@RequestParam String nombre,
                                    @RequestParam String nombrePuesto,
                                    @RequestParam String createUser) {
    Jefatura reg = new Jefatura();
    Persona persona = perService.buscarId(idPer);
    Gerencia gerencia = gerenciaService.findById(idGerencia).orElse(null);
    Map<String, Object> response = new HashMap<>();
    // Validaciones de existencia
    if (gerencia == null) {
      response.put("message", "No se encontró la gerencia con ID: " + idGerencia);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    if (persona == null) {
      response.put("message", "No se encontró la persona con ID: " + idPer);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    if (persona.getPerNat() == null) {
      response.put("message", "La persona con ID: " + idPer + " no es una persona natural.");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    // Validaciones de nombre
    String nombreTrim = nombre == null ? "" : nombre.trim();
    if (nombreTrim.isEmpty()) {
      response.put("message", "El campo 'nombre' es obligatorio");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
      response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (nombreTrim.matches("^\\d+$")) {
      response.put("message", "El campo 'nombre' no puede ser completamente numérico");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
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
    // Validación de duplicado
    if (jefaturaService.existsCombinacionActiva(nombreTrim, nombrePuestoTrim, idGerencia, idPer, null)) {
      response.put("message", "Ya existe una jefatura activa con la misma combinación de datos");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    reg.setPersona(persona);
    reg.setGerencia(gerencia);
    reg.setNombre(nombreTrim);
    reg.setNombrePuesto(nombrePuestoTrim);
    reg.setEstado(1);
    reg.setCreateUser(createUser);
    reg.setCreateDate(new Date());
    reg = jefaturaService.save(reg);
    response.put("message", "Jefatura creado con éxito");
    response.put("data", reg);
    return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam(required = false) Long idPer,
                                    @RequestParam(required = false) Long idGerencia,
                                    @RequestParam(required = false) String nombre,
                                    @RequestParam(required = false) String nombrePuesto,
                                    @RequestParam String updateUser) {
    Jefatura reg = jefaturaService.findById(id);
    Persona persona = null;
    Gerencia gerencia = null;
    Map<String, Object> response = new HashMap<>();
    if (idPer != null) persona = perService.buscarId(idPer);
    if (idGerencia != null) gerencia = gerenciaService.findById(idGerencia).orElse(null);
    if (idGerencia != null && gerencia == null) {
      response.put("message", "No se encontró la gerencia con ID: " + idGerencia);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    if (idPer != null && persona == null) {
      response.put("message", "No se encontró la persona con ID: " + idPer);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    if (idPer != null && persona.getPerNat() == null) {
      response.put("message", "La persona con ID: " + idPer + " no es una persona natural.");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    if (reg == null) {
      response.put("message", "No se encontró la jefatura con ID: " + id);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    // Validaciones de nombre
    String nombreTrim = nombre != null ? nombre.trim() : reg.getNombre();
    if (nombre != null) {
      if (nombreTrim.isEmpty()) {
        response.put("message", "El campo 'nombre' es obligatorio");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
      if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
        response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
      if (nombreTrim.matches("^\\d+$")) {
        response.put("message", "El campo 'nombre' no puede ser completamente numérico");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    // Validaciones de nombrePuesto
    String nombrePuestoTrim = nombrePuesto != null ? nombrePuesto.trim() : reg.getNombrePuesto();
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
    Long idGer = idGerencia != null ? idGerencia : (reg.getGerencia() != null ? reg.getGerencia().getId() : null);
    Long idPerVal = idPer != null ? idPer : (reg.getPersona() != null ? reg.getPersona().getId() : null);
    if (jefaturaService.existsCombinacionActiva(nombreTrim, nombrePuestoTrim, idGer, idPerVal, id)) {
      response.put("message", "Ya existe una jefatura activa con la misma combinación de datos");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (idPer != null) reg.setPersona(persona);
    if (idGerencia != null) reg.setGerencia(gerencia);
    if (nombre != null) reg.setNombre(nombreTrim);
    if (nombrePuesto != null) reg.setNombrePuesto(nombrePuestoTrim);
    reg.setUpdateUser(updateUser);
    reg.setUpdateDate(new Date());
    reg = jefaturaService.save(reg);
    response.put("message", "Jefatura se ha actualizado con éxito");
    response.put("data", reg);
    return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
    Jefatura reg = jefaturaService.findById(id);
    Map<String, Object> response = new HashMap<>();
    if (reg == null) {
      response.put("message", "No se encontró la jefatura con ID: " + id);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    Integer nuevoEstado = reg.getEstado() == 1 ? 0 : 1;
    String mensaje = nuevoEstado == 1 ? "Jefatura activada con éxito" : "Jefatura desactivada con éxito";
    // Si se va a activar, validar duplicado
    if (nuevoEstado == 1) {
      String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
      String nombrePuestoTrim = reg.getNombrePuesto() == null ? "" : reg.getNombrePuesto().trim();
      Long idGer = reg.getGerencia() != null ? reg.getGerencia().getId() : null;
      Long idPerVal = reg.getPersona() != null ? reg.getPersona().getId() : null;
      if (jefaturaService.existsCombinacionActiva(nombreTrim, nombrePuestoTrim, idGer, idPerVal, id)) {
        response.put("message", "Ya existe una jefatura activa con la misma combinación de datos");
        response.put("data", reg);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    reg = jefaturaService.changeEstado(id, nuevoEstado, username);
    response.put("message", mensaje);
    response.put("data", reg);
    return ResponseEntity.ok(response);
    }
    
    @GetMapping("/gerencias")
    public ResponseEntity<?> getGerencias() {
    	List<Gerencia> resultados = gerenciaService.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }

}