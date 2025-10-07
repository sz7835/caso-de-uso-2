package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.service.TarifarioMonedaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tarifario-moneda")
public class TarifarioMonedaController {
    @Autowired
    private TarifarioMonedaService service;

    @GetMapping("/index")
    public List<TarifarioMoneda> index(@RequestParam(required = false) Integer estado,
                                       @RequestParam(required = false) Long id) {
        if (id != null) {
            TarifarioMoneda obj = service.buscaId(id);
            return (obj != null) ? Collections.singletonList(obj) : new ArrayList<>();
        } else if (estado != null) {
            List<TarifarioMoneda> all = service.findAll();
            List<TarifarioMoneda> filtered = new ArrayList<>();
            for (TarifarioMoneda m : all) {
                if (m.getEstado() == estado) filtered.add(m);
            }
            return filtered;
        } else {
            return service.findAll();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (nombre == null) {
            response.put("message", "El nombre es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1
        List<TarifarioMoneda> existentes = service.findByNombreAndEstado(nombre, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe una moneda activa con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TarifarioMoneda obj = new TarifarioMoneda();
        obj.setNombre(nombre);
        obj.setEstado(1);
        obj.setCreateUser(username);
        obj.setCreateDate(new java.util.Date());
        service.save(obj);
        response.put("message", "Moneda creada correctamente");
        response.put("moneda", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam Long id, @RequestParam String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || nombre == null) {
            response.put("message", "id y nombre son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TarifarioMoneda obj = service.buscaId(id);
        if (obj == null) {
            response.put("message", "No existe moneda con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1 (omitiendo el actual)
        List<TarifarioMoneda> existentes = service.findByNombreAndEstado(nombre, 1);
        boolean existe = existentes.stream().anyMatch(m -> !m.getId().equals(id));
        if (existe) {
            response.put("message", "Ya existe una moneda activa con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        obj.setNombre(nombre);
        obj.setUpdateUser(username);
        obj.setUpdateDate(new java.util.Date());
        service.save(obj);
        response.put("message", "Moneda actualizada correctamente");
        response.put("moneda", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null) {
            response.put("message", "El id es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TarifarioMoneda obj = service.buscaId(id);
        if (obj == null) {
            response.put("message", "No existe moneda con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado();
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Moneda desactivada correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y estado 1
                List<TarifarioMoneda> existentes = service.findByNombreAndEstado(obj.getNombre(), 1);
                boolean existe = existentes.stream().anyMatch(m -> !m.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe una moneda activa con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Moneda activada correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Moneda desactivada correctamente");
                break;
        }
        obj.setUpdateUser(username);
        obj.setUpdateDate(new java.util.Date());
        service.save(obj);
        response.put("moneda", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
