package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.TipoContrato;
import com.delta.deltanet.models.service.TipoContrato2024ServiceImpl;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/tipoContrato")
public class TipoContratoController {

    @Autowired
    private TipoContrato2024ServiceImpl tipoContrato;

    @GetMapping("/list")
    public ResponseEntity<?> getTipoContratos(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoContrato> registros = this.tipoContrato.findAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de tipo contratos");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?>grabaTipo(@RequestParam(name = "descrip") String descrip,
                                      @RequestParam(name = "vencimiento") Integer vencimiento,
                                      @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar duplicados
            List<TipoContrato> existentes = tipoContrato.findByDescripAndEstado(descrip, 1);
            if (!existentes.isEmpty()) {
                response.put("message", "Ya existe un tipo de contrato activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar solo números
            if (descrip.matches("^\\d+$")) {
                response.put("message", "El nombre no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar caracteres especiales
            if (!descrip.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
                response.put("message", "El nombre no puede contener caracteres especiales ni símbolos");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar rango de vencimiento (0 a 99999)
            if (vencimiento == null || vencimiento < 0 || vencimiento > 99999) {
                response.put("message", "El valor de alerta vencimiento debe estar entre 0 y 99999");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoContrato registro = new TipoContrato();
            registro.setDescrip(descrip);
            registro.setEstado(1);
            registro.setVencimiento(vencimiento);
            registro.setUsrCreate(usuario);
            registro.setFecCreate(new Date());
            this.tipoContrato.save(registro);

            response.put("dato",registro);
            response.put("message","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de tipo contrato");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/updateEstado")
    public ResponseEntity<?>ChangeTipo(@RequestParam(name = "id") Long id,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<TipoContrato> rpta = tipoContrato.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoContrato registro = rpta.get();
            int estadoAnterior = registro.getEstado() != null ? registro.getEstado() : 1;
            switch (estadoAnterior) {
                case 1:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
                case 0:
                    // Validar duplicados al activar
                    List<TipoContrato> existentes = tipoContrato.findByDescripAndEstado(registro.getDescrip(), 1);
                    boolean existe = existentes.stream().anyMatch(tc -> !tc.getId().equals(id));
                    if (existe) {
                        response.put("message", "Ya existe un tipo de contrato activo con ese nombre");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    registro.setEstado(1);
                    accion = "activado";
                    break;
                default:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
            }
            registro.setUsrUpdate(usuario);
            registro.setFecUpdate(new Date());
            this.tipoContrato.save(registro);

            response.put("dato",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de tipo contrato");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> showTipo(@RequestParam(name = "id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoContrato> registro = this.tipoContrato.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de tipo contrato");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?>UpdateTipo(@RequestParam(name = "id") Long id,
                                       @RequestParam(name = "descrip") String descrip,
                                       @RequestParam(name = "vencimiento") Integer vencimiento,
                                      @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoContrato> rpta = tipoContrato.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicados (omitiendo el actual)
            List<TipoContrato> existentes = tipoContrato.findByDescripAndEstado(descrip, 1);
            boolean existe = existentes.stream().anyMatch(tc -> !tc.getId().equals(id));
            if (existe) {
                response.put("message", "Ya existe un tipo de contrato activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar solo números
            if (descrip.matches("^\\d+$")) {
                response.put("message", "El nombre no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar caracteres especiales
            if (!descrip.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
                response.put("message", "El nombre no puede contener caracteres especiales ni símbolos");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar rango de vencimiento (0 a 99999)
            if (vencimiento == null || vencimiento < 0 || vencimiento > 99999) {
                response.put("message", "El valor de alerta vencimiento debe estar entre 0 y 99999");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoContrato registro = rpta.get();
            registro.setDescrip(descrip);
            registro.setVencimiento(vencimiento);
            registro.setUsrUpdate(usuario);
            registro.setFecUpdate(new Date());
            this.tipoContrato.save(registro);

            response.put("dato",registro);
            response.put("message","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la función update de tipo contrato");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/index")
    public ResponseEntity<?> indexTipo(@RequestParam(required = false) Integer estado,
                                       @RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        if (id != null && id != 0) {
            Optional<TipoContrato> obj = tipoContrato.getById(id);
            if (obj.isPresent()) {
                if (obj.get().getEstado() != null && obj.get().getEstado() != 1) {
                    response.put("error", true);
                    response.put("message", "Acceso denegado, no se puede actualizar un registro con estado Desactivado");
                    response.put("data", Collections.singletonList(obj.get()));
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                if (estado == null || estado == 0 || (obj.get().getEstado() != null && obj.get().getEstado().equals(estado))) {
                    response.put("data", Collections.singletonList(obj.get()));
                } else {
                    response.put("data", new ArrayList<>());
                }
            } else {
                response.put("error", true);
                response.put("data", new ArrayList<>());
                response.put("message", "No existe tipo de contrato para el registro proporcionado");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (estado != null && estado != 0) {
            List<TipoContrato> filtrados = new ArrayList<>();
            for (TipoContrato tc : tipoContrato.findAll()) {
                if (tc.getEstado() != null && tc.getEstado().equals(estado)) {
                    filtrados.add(tc);
                }
            }
            response.put("data", filtrados);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("data", tipoContrato.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
