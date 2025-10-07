package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.FormaPago;
import com.delta.deltanet.models.service.IFormaPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/forma-pago")
public class FormaPagoController {
    @Autowired
    private IFormaPagoService service;

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(required = false) Integer estado,
                                     @RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        if (id != null && id != 0) {
            FormaPago obj = service.findById(id);
            if (obj != null) {
                response.put("data", Collections.singletonList(obj));
            } else {
                response.put("data", new ArrayList<>());
                response.put("message", "No existe tipo de pago para el registro proporcionado");
            }
            return response;
        }
        if (estado != null && estado != 0) {
            List<FormaPago> filtrados = new ArrayList<>();
            for (FormaPago fp : service.findAll()) {
                if (fp.getEstado() != null && fp.getEstado().equals(estado)) {
                    filtrados.add(fp);
                }
            }
            response.put("data", filtrados);
            return response;
        }
        response.put("data", service.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestParam(name = "nombre") String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (nombre == null || username == null) {
            response.put("message", "nombre y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1
        List<FormaPago> existentes = service.findByDescripAndEstado(nombre, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de pago activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        FormaPago obj = new FormaPago();
        obj.setDescrip(nombre);
        obj.setEstado(1);
        obj.setUsrCreate(username);
        obj.setFecCreate(new Date());
        service.save(obj);
        response.put("message", "Tipo de pago creada correctamente");
        response.put("formaPago", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam Long id, @RequestParam(name = "nombre") String nombre, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || nombre == null || username == null) {
            response.put("message", "id, nombre y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        FormaPago obj = service.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de pago para el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1 (omitiendo el actual)
        List<FormaPago> existentes = service.findByDescripAndEstado(nombre, 1);
        boolean existe = existentes.stream().anyMatch(fp -> !fp.getId().equals(id));
        if (existe) {
            response.put("message", "Ya existe un tipo de pago activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        obj.setDescrip(nombre);
        obj.setUsrUpdate(username);
        obj.setFecUpdate(new Date());
        service.save(obj);
        response.put("message", "Tipo de pago actualizada correctamente");
        response.put("formaPago", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        FormaPago obj = service.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de pago para el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado() != null ? obj.getEstado() : 1;
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Tipo de pago desactivada correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y estado 1
                List<FormaPago> existentes = service.findByDescripAndEstado(obj.getDescrip(), 1);
                boolean existe = existentes.stream().anyMatch(fp -> !fp.getId().equals(id));
                if (existe) {
                    response.put("message", "Ya existe un tipo de pago activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Tipo de pago activada correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Tipo de pago desactivada correctamente");
                break;
        }
        obj.setUsrUpdate(username);
        obj.setFecUpdate(new Date());
        service.save(obj);
        response.put("formaPago", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
