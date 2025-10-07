package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Atencion;
import com.delta.deltanet.models.service.IAtencionService;
import com.delta.deltanet.models.service.ITipoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/atencion-tipo")
public class AtencionController {
    @Autowired
    private IAtencionService atencionService;

    @Autowired
    private ITipoPersonaService tipoPersonaService;

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(required = false) Integer estado,
                                     @RequestParam(required = false) Integer id,
                                     @RequestParam(required = false) Integer idTipoPer) {
        Map<String, Object> response = new HashMap<>();
        List<Atencion> listado;
        if (id != null) {
            Optional<Atencion> obj = atencionService.findById(id);
            listado = (obj.isPresent()) ? Collections.singletonList(obj.get()) : new ArrayList<>();
        } else if (estado != null) {
            listado = atencionService.findByEstado(estado);
        } else if (idTipoPer != null) {
            listado = atencionService.findByIdTipoPer(idTipoPer);
        } else {
            listado = atencionService.findAll();
        }
        response.put("data", listado);
        response.put("tiposPersona", tipoPersonaService.findAll());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAt(@RequestParam(name = "abreviacion") String abrev,
                                        @RequestParam(name = "descripcion") String descrip,
                                        @RequestParam(name = "idTipoPersona") Integer idTipoPer,
                                        @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (abrev == null || descrip == null || username == null || idTipoPer == null) {
            response.put("message", "abreviacion, descripcion, idTipoPer y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación abreviación: solo letras y punto final obligatorio
        if (!abrev.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+\\.$")) {
            response.put("message", "La abreviación solo puede contener letras y debe terminar con un punto final");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación descripción: solo letras (sin números ni símbolos)
        if (!descrip.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            response.put("message", "La descripción solo puede contener letras y espacios, sin números ni símbolos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: no debe existir otro con la misma descripción y tipo persona y estado 1
        List<Atencion> existentes = atencionService.findByDescripAndIdTipoPerAndEstado(descrip, idTipoPer, 1);
        if (!existentes.isEmpty()) {
            response.put("message", "Ya existe un tipo de atención activo con esa descripción para el mismo tipo de persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Atencion obj = new Atencion();
        obj.setAbrev(abrev);
        obj.setDescrip(descrip);
        obj.setIdTipoPer(idTipoPer);
        obj.setEstado(1);
        obj.setCreUser(username);
        obj.setCreDate(new Date());
        atencionService.save(obj);
        response.put("message", "Atención creada correctamente");
        response.put("atencion", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAt(@RequestParam Integer id,
                                        @RequestParam(name = "abreviacion") String abrev,
                                        @RequestParam(name = "descripcion") String descrip,
                                        @RequestParam(name = "idTipoPersona") Integer idTipoPer,
                                        @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || abrev == null || descrip == null || username == null || idTipoPer == null) {
            response.put("message", "id, abreviacion, descripcion, idTipoPer y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Optional<Atencion> opt = atencionService.findById(id);
        if (!opt.isPresent()) {
            response.put("message", "No existe atención con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación abreviación: solo letras y punto final obligatorio
        if (!abrev.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+\\.$")) {
            response.put("message", "La abreviación solo puede contener letras y debe terminar con un punto final");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación descripción: solo letras (sin números ni símbolos)
        if (!descrip.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
            response.put("message", "La descripción solo puede contener letras y espacios, sin números ni símbolos");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación: no debe existir otro con la misma descripción y tipo persona y estado 1 (omitiendo el actual)
        List<Atencion> existentes = atencionService.findByDescripAndIdTipoPerAndEstado(descrip, idTipoPer, 1);
        boolean existe = existentes.stream().anyMatch(a -> a.getId() != id);
        if (existe) {
            response.put("message", "Ya existe un tipo de atención activo con esa descripción para el mismo tipo de persona");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Atencion obj = opt.get();
        obj.setAbrev(abrev);
        obj.setDescrip(descrip);
        obj.setIdTipoPer(idTipoPer);
        obj.setUpdUser(username);
        obj.setUpdDate(new Date());
        atencionService.save(obj);
        response.put("message", "Atención actualizada correctamente");
        response.put("atencion", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestParam Integer id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        if (id == null || username == null) {
            response.put("message", "El id y username son obligatorios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Optional<Atencion> opt = atencionService.findById(id);
        if (!opt.isPresent()) {
            response.put("message", "No existe atención con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Atencion obj = opt.get();
        int estadoAnterior = obj.getEstado();
        switch (estadoAnterior) {
            case 1:
                obj.setEstado(0);
                response.put("message", "Atención desactivada correctamente");
                break;
            case 0:
                // Validar que no exista otro con la misma descripción y tipo persona y estado 1
                List<Atencion> existentes = atencionService.findByDescripAndIdTipoPerAndEstado(obj.getDescrip(), obj.getIdTipoPer(), 1);
                boolean existe = existentes.stream().anyMatch(a -> a.getId() != id);
                if (existe) {
                    response.put("message", "Ya existe un tipo de atención activo con esa descripción para el mismo tipo de persona");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                obj.setEstado(1);
                response.put("message", "Atención activada correctamente");
                break;
            default:
                obj.setEstado(0);
                response.put("message", "Atención desactivada correctamente");
                break;
        }
        obj.setUpdUser(username);
        obj.setUpdDate(new Date());
        atencionService.save(obj);
        response.put("atencion", obj);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

