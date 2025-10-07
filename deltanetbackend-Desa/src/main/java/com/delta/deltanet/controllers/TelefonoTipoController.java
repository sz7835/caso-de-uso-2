package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.TelefonoTipo;
import com.delta.deltanet.models.service.TelefonoTipoService;
import com.delta.deltanet.models.service.ITipoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/telefono-tipo")
public class TelefonoTipoController {
    @Autowired
    private TelefonoTipoService telefonoTipoService;

    @Autowired
    private ITipoPersonaService tipoPersonaService;

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(required = false) Integer estado,
                                     @RequestParam(required = false) Long id,
                                     @RequestParam(required = false) Long idTipoPersona) {
        Map<String, Object> response = new HashMap<>();
        List<TelefonoTipo> listado;
        if (id != null) {
            TelefonoTipo obj = telefonoTipoService.findById(id);
            listado = (obj != null) ? Collections.singletonList(obj) : new ArrayList<>();
        } else if (estado != null) {
            listado = telefonoTipoService.findByEstado(estado);
        } else if (idTipoPersona != null) {
            listado = telefonoTipoService.findByIdTipoPersona(idTipoPersona);
        } else {
            listado = telefonoTipoService.findAll();
        }
        response.put("data", listado);
        response.put("tiposPersona", tipoPersonaService.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam String descripcion,
                                                    @RequestParam Long idTipoPersona,
                                                    @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (descripcion == null || username == null || idTipoPersona == null) {
            response.put("message", "descripcion, idTipoPersona y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validación: descripcion no debe ser solo numérico ni contener caracteres especiales
        if (descripcion.matches("\\d+")) {
            response.put("message", "La descripción no puede ser completamente numérica");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s]+$")) {
            response.put("message", "La descripción contiene caracteres especiales no permitidos");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validación: no debe existir otro con el mismo nombre y idTipoPersona y estado 1
        List<TelefonoTipo> existentes = telefonoTipoService.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de teléfono activo con ese nombre para el mismo tipo de persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TelefonoTipo obj = new TelefonoTipo();
        obj.setDescripcion(descripcion);
        obj.setIdTipoPersona(idTipoPersona);
        obj.setEstado(1);
        obj.setUsuCreado(username);
        obj.setFechaCreado(new Date());
        telefonoTipoService.save(obj);
        response.put("message", "Tipo de teléfono creado correctamente");
        response.put("telefonoTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam Long id,
                                                    @RequestParam(required = false) String descripcion,
                                                    @RequestParam Long idTipoPersona,
                                                    @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null || idTipoPersona == null) {
            response.put("message", "id, idTipoPersona y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        TelefonoTipo obj = telefonoTipoService.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de teléfono para el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
            List<TelefonoTipo> existentes = telefonoTipoService.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, 1);
            boolean existe = existentes.stream().anyMatch(t -> !t.getId().equals(id));
            if (existe) {
                response.put("message", "Ya existe un tipo de teléfono activo con ese nombre para el mismo tipo de persona");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            obj.setDescripcion(descripcion);
        }
        obj.setIdTipoPersona(idTipoPersona);
        obj.setUsuUpdate(username);
        obj.setFechaUpdate(new Date());
        telefonoTipoService.save(obj);
        response.put("message", "Tipo de teléfono actualizado correctamente");
        response.put("telefonoTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id,
                                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "El id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TelefonoTipo obj = telefonoTipoService.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de teléfono para el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado();
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Tipo de teléfono desactivado correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y idTipoPersona y estado 1
                List<TelefonoTipo> existentes = telefonoTipoService.findByDescripcionAndIdTipoPersonaAndEstado(obj.getDescripcion(), obj.getIdTipoPersona(), 1);
                boolean existe = existentes.stream().anyMatch(t -> !t.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe un tipo de teléfono activo con ese nombre para el mismo tipo de persona");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Tipo de teléfono activado correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Tipo de teléfono desactivado correctamente");
                break;
        }
        obj.setUsuUpdate(username);
        obj.setFechaUpdate(new Date());
        telefonoTipoService.save(obj);
        response.put("telefonoTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}