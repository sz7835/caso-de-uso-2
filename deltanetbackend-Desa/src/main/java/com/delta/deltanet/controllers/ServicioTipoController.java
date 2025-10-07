package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.ServicioTipo;
import com.delta.deltanet.models.service.ISrvTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/servicio-tipo")
public class ServicioTipoController {
    @Autowired
    private ISrvTipoService service;

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(required = false) Integer estado,
                                     @RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        if (id != null && id != 0) {
            ServicioTipo obj = service.buscaById(id);
            if (obj != null) {
                response.put("data", Collections.singletonList(obj));
            } else {
                response.put("data", new ArrayList<>());
                response.put("message", "No existe tipo de servicio con el id proporcionado");
            }
            return response;
        }
        if (estado != null && estado != 0) {
            response.put("data", service.findByEstado(estado));
            return response;
        }
        response.put("data", service.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (nombre == null || username == null) {
            response.put("message", "nombre y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1
        List<ServicioTipo> existentes = service.findByNombreAndEstado(nombre, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de servicio activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ServicioTipo obj = new ServicioTipo();
        obj.setNombre(nombre);
        obj.setEstado(1);
        obj.setCreateUser(username);
        obj.setCreateDate(new Date());
        service.save(obj);
        response.put("message", "Tipo de servicio creado correctamente");
        response.put("servicioTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam Long id, @RequestParam String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || nombre == null || username == null) {
            response.put("message", "id, nombre y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ServicioTipo obj = service.buscaById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de servicio con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1 (omitiendo el actual)
        List<ServicioTipo> existentes = service.findByNombreAndEstado(nombre, 1);
        boolean existe = existentes.stream().anyMatch(s -> !s.getId().equals(id));
        if (existe) {
            response.put("message", "Ya existe un tipo de servicio activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        obj.setNombre(nombre);
        obj.setUpdateUser(username);
        obj.setUpdateDate(new Date());
        service.save(obj);
        response.put("message", "Tipo de servicio actualizado correctamente");
        response.put("servicioTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ServicioTipo obj = service.buscaById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de servicio con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado();
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Tipo de servicio desactivado correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y estado 1
                List<ServicioTipo> existentes = service.findByNombreAndEstado(obj.getNombre(), 1);
                boolean existe = existentes.stream().anyMatch(s -> !s.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe un tipo de servicio activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Tipo de servicio activado correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Tipo de servicio desactivado correctamente");
                break;
        }
        obj.setUpdateUser(username);
        obj.setUpdateDate(new Date());
        service.save(obj);
        response.put("servicioTipo", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
