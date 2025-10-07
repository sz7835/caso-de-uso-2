package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.Jefatura;
import com.delta.deltanet.models.entity.JefaturaPuesto;
import com.delta.deltanet.models.service.IJefaturaService;
import com.delta.deltanet.models.service.IJefaturaPuestoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/jefatura-puesto")
public class JefaturaPuestoController {

	@Autowired
    private IJefaturaPuestoService jefaturaPuestoService;
	
    @Autowired
    private IJefaturaService jefaturaService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
    	List<JefaturaPuesto> resultados = jefaturaPuestoService.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
    	JefaturaPuesto res = jefaturaPuestoService.findById(id);
    	if (res == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró el puesto de jefatura con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Puesto de jefatura encontrado con éxito");
    	response.put("data", res);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam Long idJefatura,
									@RequestParam String nombre,
                                    @RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        Jefatura jefatura = jefaturaService.findById(idJefatura);
        if(jefatura == null) {
            response.put("message", "No se encontró la jefatura con ID: " + idJefatura);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
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
        if (jefaturaPuestoService.existsNombreActivoPorJefatura(nombreTrim, idJefatura, null)) {
            response.put("message", "Ya existe un registro activo con ese nombre para la misma jefatura");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        JefaturaPuesto reg = new JefaturaPuesto();
        reg.setJefatura(jefatura);
        reg.setNombre(nombreTrim);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = jefaturaPuestoService.save(reg);
        response.put("message", "Puesto de jefatura creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam(required = false) Long idJefatura,
                                    @RequestParam(required = false) String nombre,
                                    @RequestParam String updateUser) {
    	JefaturaPuesto reg = jefaturaPuestoService.findById(id);
    	
    	Jefatura jefatura = null;
    	
    	if(idJefatura != null) jefatura = jefaturaService.findById(idJefatura);
        
    	if(idJefatura != null && jefatura == null) {
        	Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró la jefatura con ID: " + idJefatura);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    	if (reg == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró el puesto de jefatura con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}

        if (idJefatura != null) {
            reg.setJefatura(jefatura);
        }
        if (nombre != null) {
            String nombreTrim = nombre.trim();
            if (nombreTrim.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "El campo 'nombre' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^\\d+$")) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "El campo 'nombre' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Long jefId = idJefatura != null ? idJefatura : (reg.getJefatura() != null ? reg.getJefatura().getId() : null);
            if (jefaturaPuestoService.existsNombreActivoPorJefatura(nombreTrim, jefId, id)) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Ya existe otro registro activo con ese nombre para la misma jefatura");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = jefaturaPuestoService.save(reg);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Puesto de jefatura se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
    	JefaturaPuesto reg = jefaturaPuestoService.findById(id);
    	if (reg == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró el puesto de jefatura con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	
        String mensaje;
        JefaturaPuesto regResult = reg;
        switch (reg.getEstado()) {
            case 1: {
                regResult = jefaturaPuestoService.changeEstado(id, 0, username);
                mensaje = "Puesto de jefatura desactivada con éxito";
                break;
            }
            case 0: {
                // Validar duplicado antes de activar
                String nombreTrim = reg.getNombre() == null ? "" : reg.getNombre().trim();
                Long jefId = reg.getJefatura() != null ? reg.getJefatura().getId() : null;
                if (jefaturaPuestoService.existsNombreActivoPorJefatura(nombreTrim, jefId, id)) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Ya existe otro registro activo con ese nombre para la misma jefatura");
                    response.put("data", reg);
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                regResult = jefaturaPuestoService.changeEstado(id, 1, username);
                mensaje = "Puesto de jefatura activada con éxito";
                break;
            }
            default: {
                regResult = jefaturaPuestoService.changeEstado(id, 0, username);
                mensaje = "Puesto de jefatura desactivada con éxito";
                break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", mensaje);
        response.put("data", regResult);
        return ResponseEntity.ok(response);
    }

}