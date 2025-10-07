package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.PostulanteFormacion;
import com.delta.deltanet.models.service.IPostulanteFormacionService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/postulante-formacion")
public class PostulanteFormacionController {

	@Autowired
    private IPostulanteFormacionService service;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
    	List<PostulanteFormacion> resultados = service.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
    	PostulanteFormacion res = service.findById(id);
    	if (res == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró la formacion de postulante con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Formacion de postulante encontrada con éxito");
    	response.put("data", res);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String nombre,
    		@RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        String nombreTrim = nombre == null ? "" : nombre.trim();
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
        // Verificar duplicados activos con el servicio
        if (service.existsNombreActivo(nombreTrim, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        PostulanteFormacion reg = new PostulanteFormacion();
        reg.setNombre(nombreTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = service.save(reg);
        response.put("message", "Formacion de postulante creada con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String updateUser) {
        PostulanteFormacion reg = service.findById(id);

        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró la formacion de postulante con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        if (nombre != null) {
            String nombreTrim = nombre.trim();
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
            // Verificar duplicados activos con el servicio (excepto el actual)
            if (service.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = service.save(reg);
        response.put("message", "Formacion de postulante se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(  @RequestParam Long id,
                                            @RequestParam String username) {
        PostulanteFormacion reg = service.findById(id);
        if (reg == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No se encontró la formacion de postulante con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        Integer nuevoEstado = reg.getEstado() == 1 ? 0 : 1;
        if (nuevoEstado == 1) { // Se va a activar
            String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
            if (service.existsNombreActivo(nombreTrim, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        reg = service.changeEstado(id, nuevoEstado, username);
        String mensaje = nuevoEstado == 1 ? "Formacion de postulante activada con éxito" : "Formacion de postulante desactivada con éxito";
        response.put("message", mensaje);
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

}