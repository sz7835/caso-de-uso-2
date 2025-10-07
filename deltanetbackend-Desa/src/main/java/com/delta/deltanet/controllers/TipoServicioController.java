package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.TipoServicio;
import com.delta.deltanet.models.service.ITipoServicioService;
import com.delta.deltanet.models.entity.TipoContrato;
import com.delta.deltanet.models.service.TipoContrato2024ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tipo-servicio")
public class TipoServicioController {
    @Autowired
    private ITipoServicioService service;

    @Autowired
    private TipoContrato2024ServiceImpl tipoContratoService;

    @GetMapping("/index")
    public Map<String, Object> index(   @RequestParam(required = false) Integer estado,
                                        @RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        List<TipoContrato> contratos = tipoContratoService.findAll();
        response.put("contratos", contratos);
        if (id != null && id != 0) {
            TipoServicio obj = service.findById(id);
            if (obj != null) {
                if (obj.getEstado() != null && obj.getEstado() != 1) {
                    response.put("error", true);
                    response.put("message", "Acceso denegado, no se puede actualizar registro con estado Desactivado");
                    response.put("data", Collections.singletonList(obj));
                    return response;
                }
                response.put("data", Collections.singletonList(obj));
            } else {
                response.put("error", true);
                response.put("data", new ArrayList<>());
                response.put("message", "No existe tipo de servicio con el registro proporcionado");
            }
            return response;
        }
        if (estado != null && estado != 0) {
            List<TipoServicio> filtrados = new ArrayList<>();
            for (TipoServicio ts : service.findAll()) {
                if (ts.getEstado() != null && ts.getEstado().equals(estado)) {
                    filtrados.add(ts);
                }
            }
            response.put("data", filtrados);
            return response;
        }
        response.put("data", service.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(  @RequestParam(name = "descrip") String nombre,
                                                        @RequestParam(name = "contratoId") Long idTipo,
                                                        @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (nombre == null || username == null || idTipo == null) {
            response.put("message", "nombre, idTipo y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no sea solo números
        if (nombre.matches("^\\d+$")) {
            response.put("message", "El nombre no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no tenga caracteres especiales
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            response.put("message", "El nombre no puede contener caracteres especiales ni símbolos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1
        List<TipoServicio> existentes = service.findByDescripAndEstado(nombre, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de servicio activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoServicio obj = new TipoServicio();
        obj.setDescrip(nombre);
        TipoContrato tipoContrato = new TipoContrato();
        tipoContrato.setId(idTipo);
        obj.setTipo(tipoContrato);
        obj.setEstado(1);
        obj.setUsrCreate(username);
        obj.setFecCreate(new Date());
        service.save(obj);
        response.put("message", "Tipo de servicio creado correctamente");
        response.put("tipoServicio", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(  @RequestParam Long id,
                                                        @RequestParam(name = "descrip") String nombre,
                                                        @RequestParam(name = "contratoId") Long idTipo,
                                                        @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || nombre == null || username == null || idTipo == null) {
            response.put("message", "id, nombre, idTipo y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no sea solo números
        if (nombre.matches("^\\d+$")) {
            response.put("message", "El nombre no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no tenga caracteres especiales
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            response.put("message", "El nombre no puede contener caracteres especiales ni símbolos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoServicio obj = service.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de servicio con el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista otro con el mismo nombre y estado 1 (omitiendo el actual)
        List<TipoServicio> existentes = service.findByDescripAndEstado(nombre, 1);
        boolean existe = existentes.stream().anyMatch(ts -> !ts.getId().equals(id));
        if (existe) {
            response.put("message", "Ya existe un tipo de servicio activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        obj.setDescrip(nombre);
        if (obj.getTipo() == null) {
            obj.setTipo(new TipoContrato());
        }
        obj.getTipo().setId(idTipo);
        obj.setUsrUpdate(username);
        obj.setFecUpdate(new Date());
        service.save(obj);
        response.put("message", "Tipo de servicio actualizado correctamente");
        response.put("tipoServicio", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Long id,
                                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoServicio obj = service.findById(id);
        if (obj == null) {
            response.put("message", "No existe tipo de servicio con el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        int estadoAnterior = obj.getEstado() != null ? obj.getEstado() : 1;
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Tipo de servicio desactivado correctamente");
                break;
            case 0:
                // Validar que no exista otro con el mismo nombre y estado 1
                List<TipoServicio> existentes = service.findByDescripAndEstado(obj.getDescrip(), 1);
                boolean existe = existentes.stream().anyMatch(ts -> !ts.getId().equals(id));
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
        obj.setUsrUpdate(username);
        obj.setFecUpdate(new Date());
        service.save(obj);
        response.put("tipoServicio", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
