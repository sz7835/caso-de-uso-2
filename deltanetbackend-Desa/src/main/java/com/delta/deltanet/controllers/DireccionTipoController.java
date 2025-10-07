package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.DireccionTipo;
import com.delta.deltanet.models.service.DireccionTipoService;
import com.delta.deltanet.models.service.ITipoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/direccion-tipo")
public class DireccionTipoController {
    @Autowired
    private DireccionTipoService direccionTipoService;

    @Autowired
    private ITipoPersonaService tipoPersonaService;

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(required = false) Integer estado,
                                     @RequestParam(required = false) Long id,
                                     @RequestParam(required = false) Long idTipoPersona) {
        Map<String, Object> response = new HashMap<>();
        List<DireccionTipo> listado;
        if (id != null) {
            DireccionTipo obj = direccionTipoService.findById(id);
            listado = (obj != null) ? Collections.singletonList(obj) : new ArrayList<>();
        } else if (estado != null) {
            listado = direccionTipoService.findByEstado(estado);
        } else if (idTipoPersona != null) {
            listado = direccionTipoService.findByIdTipoPersona(idTipoPersona);
        } else {
            listado = direccionTipoService.findAll();
        }
        response.put("data", listado);
        response.put("tiposPersona", tipoPersonaService.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam String descripcion, @RequestParam Long idTipoPersona, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (descripcion == null || username == null || idTipoPersona == null) {
            response.put("message", "descripcion, idTipoPersona y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: descripcion no debe ser solo numérico ni contener caracteres especiales
        if (descripcion.matches("\\d+")) {
            response.put("message", "La descripción no puede ser completamente numérica");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s]+$")) {
            response.put("message", "La descripción contiene caracteres especiales no permitidos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: no debe existir otro con el mismo nombre y idTipoPersona y estado 1
        List<DireccionTipo> existentes = direccionTipoService.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de dirección activo con ese nombre para el mismo tipo de persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        DireccionTipo obj = new DireccionTipo();
        obj.setDescripcion(descripcion);
        obj.setIdTipoPersona(idTipoPersona);
        obj.setEstado(1);
        obj.setCreateUser(username);
        obj.setCreateDate(new Date());
        direccionTipoService.save(obj);
        response.put("message", "Tipo de dirección creado correctamente");
        response.put("direccionTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam Long id, @RequestParam(required = false) String descripcion, @RequestParam Long idTipoPersona, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null || idTipoPersona == null) {
            response.put("message", "id, idTipoPersona y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        DireccionTipo obj = direccionTipoService.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de dirección con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descripcion != null) {
            // Validación: descripcion no debe ser solo numérico ni contener caracteres especiales
            if (descripcion.matches("\\d+")) {
                response.put("message", "La descripción no puede ser completamente numérica");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s]+$")) {
                response.put("message", "La descripción contiene caracteres especiales no permitidos");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validación: no debe existir otro con el mismo nombre y idTipoPersona y estado 1 (omitiendo el actual)
            List<DireccionTipo> existentes = direccionTipoService.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, 1);
            boolean existe = existentes.stream().anyMatch(d -> !d.getId().equals(id));
            if (existe) {
                response.put("message", "Ya existe un tipo de dirección activo con ese nombre para el mismo tipo de persona");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            obj.setDescripcion(descripcion);
        }
        obj.setIdTipoPersona(idTipoPersona);
        obj.setUpdateUser(username);
        obj.setUpdateDate(new Date());
        direccionTipoService.save(obj);
        response.put("message", "Tipo de dirección actualizado correctamente");
        response.put("direccionTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "El id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        DireccionTipo obj = direccionTipoService.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de dirección con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado();
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Tipo de dirección desactivado correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y idTipoPersona y estado 1
                List<DireccionTipo> existentes = direccionTipoService.findByDescripcionAndIdTipoPersonaAndEstado(obj.getDescripcion(), obj.getIdTipoPersona(), 1);
                boolean existe = existentes.stream().anyMatch(d -> !d.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe un tipo de dirección activo con ese nombre para el mismo tipo de persona");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Tipo de dirección activado correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Tipo de dirección desactivado correctamente");
                break;
        }
        obj.setUpdateUser(username);
        obj.setUpdateDate(new Date());
        direccionTipoService.save(obj);
        response.put("direccionTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
