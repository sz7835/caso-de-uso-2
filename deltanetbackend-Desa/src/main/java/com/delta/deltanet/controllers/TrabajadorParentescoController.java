package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.TrabajadorParentesco;
import com.delta.deltanet.models.service.ITrabajadorParentescoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/trabajador-parentesco")
public class TrabajadorParentescoController {

	@Autowired
    private ITrabajadorParentescoService trabajadorParentescoService;

    @GetMapping("/index")
    public ResponseEntity<?> getAll() {
    	List<TrabajadorParentesco> resultados = trabajadorParentescoService.findAll();
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Búsqueda completada con éxito");
    	response.put("data", resultados);
    	return ResponseEntity.ok(response);
    }
    
    @GetMapping("/show")
    public ResponseEntity<?> findById(@RequestParam Long id) {
    	TrabajadorParentesco res = trabajadorParentescoService.findById(id);
    	if (res == null) {
    		Map<String, Object> response = new HashMap<>();
    		response.put("message", "No se encontró el parentesco del trabajador con ID: " + id);
    		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	}
    	Map<String, Object> response = new HashMap<>();
    	response.put("message", "Parentesco de trabajador encontrado con éxito");
    	response.put("data", res);
    	return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam String descripcion,
                                    @RequestParam String createUser) {
        Map<String, Object> response = new HashMap<>();
        String descTrim = descripcion == null ? "" : descripcion.trim();
        if (descTrim.isEmpty()) {
            response.put("message", "El campo 'descripcion' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("message", "El campo 'descripcion' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descTrim.matches("^\\d+$")) {
            response.put("message", "El campo 'descripcion' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar duplicado solo en activos
        List<TrabajadorParentesco> duplicados = trabajadorParentescoService.findAll();
        boolean existe = duplicados.stream().anyMatch(p -> p.getDescripcion() != null && p.getDescripcion().trim().equalsIgnoreCase(descTrim) && p.getEstado() == 1);
        if (existe) {
            response.put("message", "Ya existe un registro activo con esa descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TrabajadorParentesco reg = new TrabajadorParentesco();
        reg.setDescripcion(descripcion);
        reg.setEstado(1);
        reg.setCreateUser(createUser);
        reg.setCreateDate(new Date());
        reg = trabajadorParentescoService.save(reg);
        response.put("message", "Parentesco de trabajador creado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam String descripcion,
                                    @RequestParam String updateUser) {
        TrabajadorParentesco reg = trabajadorParentescoService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (reg == null) {
            response.put("message", "No se encontró el parentesco de trabajador con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (descripcion != null) {
            String descTrim = descripcion.trim();
            if (descTrim.isEmpty()) {
                response.put("message", "El campo 'descripcion' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!descTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("message", "El campo 'descripcion' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descTrim.matches("^\\d+$")) {
                response.put("message", "El campo 'descripcion' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicado solo en activos, ignorando el propio id
            List<TrabajadorParentesco> duplicados = trabajadorParentescoService.findAll();
            boolean existe = duplicados.stream().anyMatch(p -> p.getDescripcion() != null && p.getDescripcion().trim().equalsIgnoreCase(descTrim) && p.getEstado() == 1 && !p.getId().equals(id));
            if (existe) {
                response.put("message", "Ya existe otro registro activo con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDescripcion(descTrim);
        }
        reg.setUpdateUser(updateUser);
        reg.setUpdateDate(new Date());
        reg = trabajadorParentescoService.save(reg);
        response.put("message", "Parentesco de trabajador se ha actualizado con éxito");
        response.put("data", reg);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long id,
                                          @RequestParam String username) {
        TrabajadorParentesco reg = trabajadorParentescoService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (reg == null) {
            response.put("message", "No se encontró el parentesco de trabajador con ID: " + id);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        String mensaje;
        TrabajadorParentesco regResult = reg;
        switch (reg.getEstado()) {
            case 1: {
                regResult = trabajadorParentescoService.changeEstado(id, 0, username);
                mensaje = "Parentesco de trabajador desactivada con éxito";
                break;
            }
            case 0: {
                // Validar duplicado antes de activar
                String descTrim = reg.getDescripcion() == null ? "" : reg.getDescripcion().trim();
                List<TrabajadorParentesco> duplicados = trabajadorParentescoService.findAll();
                boolean existe = duplicados.stream().anyMatch(p -> p.getDescripcion() != null && p.getDescripcion().trim().equalsIgnoreCase(descTrim) && p.getEstado() == 1 && !p.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    response.put("data", reg);
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                regResult = trabajadorParentescoService.changeEstado(id, 1, username);
                mensaje = "Parentesco de trabajador activada con éxito";
                break;
            }
            default: {
                regResult = trabajadorParentescoService.changeEstado(id, 0, username);
                mensaje = "Parentesco de trabajador desactivada con éxito";
                break;
            }
        }
        response.put("message", mensaje);
        response.put("data", regResult);
        return ResponseEntity.ok(response);
    }

}