package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.PuestoOcupacional;
import com.delta.deltanet.models.service.PuestoOcupacionalEspecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/puesto-ocupacional/especial")
@CrossOrigin(origins = {"*"})
public class PuestoOcupacionalEspecialController {

    @Autowired
    private PuestoOcupacionalEspecialService puestoService;

    @GetMapping("/index")
    public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Long id,
                                           @RequestParam(required = false) Integer estado) {
        Map<String, Object> response = new HashMap<>();
        List<PuestoOcupacional> registros = new ArrayList<>();
        if (id != null) {
            PuestoOcupacional reg = puestoService.findById(id);
            if (reg == null) {
                response.put("error", true);
                response.put("message", "No existe el registro proporcionado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (reg.getEstado() == 0) {
                response.put("error", true);
                response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
                response.put("data", Collections.singletonList(reg));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (estado == null || reg.getEstado().equals(estado)) {
                registros.add(reg);
            }
        } else if (estado != null) {
            for (PuestoOcupacional reg : puestoService.findAll()) {
                if (reg.getEstado().equals(estado)) {
                    registros.add(reg);
                }
            }
        } else {
            registros = puestoService.findAll();
        }
        response.put("data", registros);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEspecial(@RequestParam String nombrePuesto,
                                            @RequestParam String unidadOrganica,
                                            @RequestParam String dependenciaJerarquicaFuncional,
                                            @RequestParam(required = false, defaultValue = "No aplica") String puestoQueSupervisa,
                                            @RequestParam String misionDelPuesto,
                                            @RequestParam String funcionesDelPuesto,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        nombrePuesto = nombrePuesto == null ? "" : nombrePuesto;
        unidadOrganica = unidadOrganica == null ? "" : unidadOrganica;
        dependenciaJerarquicaFuncional = dependenciaJerarquicaFuncional == null ? "" : dependenciaJerarquicaFuncional;
        puestoQueSupervisa = puestoQueSupervisa == null ? "" : puestoQueSupervisa;
        // Validaciones campos obligatorios y solo espacios
        if (nombrePuesto.trim().isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'nombre puesto' es obligatorio y no puede ser solo espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nombrePuesto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre puesto' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nombrePuesto.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre puesto' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (unidadOrganica.trim().isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'unidad orgánica' es obligatorio y no puede ser solo espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!unidadOrganica.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'unidad orgánica' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (unidadOrganica.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'unidad orgánica' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (dependenciaJerarquicaFuncional.trim().isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'dependencia jerárquica funcional' es obligatorio y no puede ser solo espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!dependenciaJerarquicaFuncional.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'dependencia jerárquica funcional' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (dependenciaJerarquicaFuncional.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'dependencia jerárquica funcional' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (puestoQueSupervisa.trim().isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'puesto que supervisa' no puede ser solo espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!puestoQueSupervisa.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'puesto que supervisa' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (puestoQueSupervisa.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'puesto que supervisa' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validación de duplicado: mismo nombre y misma unidad orgánica activos
        if (puestoService.existsNombreUnidadOrganicaActivo(nombrePuesto, unidadOrganica, null)) {
            response.put("error", true);
            response.put("message", "Ya existe un registro activo con ese nombre");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        PuestoOcupacional nuevo = new PuestoOcupacional();
        nuevo.setNombrePuesto(nombrePuesto);
        nuevo.setUnidadOrganica(unidadOrganica);
        nuevo.setDependenciaJerarquicaFuncional(dependenciaJerarquicaFuncional);
        nuevo.setPuestoQueSupervisa(puestoQueSupervisa);
        nuevo.setMisionDelPuesto(misionDelPuesto);
        nuevo.setFuncionesDelPuesto(funcionesDelPuesto);
        nuevo.setEstado(1);
        nuevo.setCreateUser(username);
        nuevo.setCreateDate(new Date());
        puestoService.save(nuevo);
        response.put("message", "Registro creado correctamente");
        response.put("registro", nuevo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEspecial(@RequestParam Long id,
                                            @RequestParam(required = false) String nombrePuesto,
                                            @RequestParam(required = false) String unidadOrganica,
                                            @RequestParam(required = false) String dependenciaJerarquicaFuncional,
                                            @RequestParam(required = false) String puestoQueSupervisa,
                                            @RequestParam(required = false) String misionDelPuesto,
                                            @RequestParam(required = false) String funcionesDelPuesto,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        PuestoOcupacional reg = puestoService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nombrePuesto != null && unidadOrganica != null) {
            if (nombrePuesto.trim().isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'nombre puesto' es obligatorio y no puede ser solo espacios");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombrePuesto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre puesto' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombrePuesto.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre puesto' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (unidadOrganica.trim().isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'unidad orgánica' es obligatorio y no puede ser solo espacios");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!unidadOrganica.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'unidad orgánica' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (unidadOrganica.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'unidad orgánica' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (puestoService.existsNombreUnidadOrganicaActivo(nombrePuesto, unidadOrganica, id)) {
                response.put("error", true);
                response.put("message", "Ya existe otro registro activo con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombrePuesto(nombrePuesto);
            reg.setUnidadOrganica(unidadOrganica);
        }
        if (dependenciaJerarquicaFuncional != null) {
            if (dependenciaJerarquicaFuncional.trim().isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'dependencia jerárquica funcional' es obligatorio y no puede ser solo espacios");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!dependenciaJerarquicaFuncional.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'dependencia jerárquica funcional' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (dependenciaJerarquicaFuncional.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'dependencia jerárquica funcional' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDependenciaJerarquicaFuncional(dependenciaJerarquicaFuncional);
        }
        if (puestoQueSupervisa != null) {
            if (puestoQueSupervisa.trim().isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'puesto que supervisa' no puede ser solo espacios");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!puestoQueSupervisa.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'puesto que supervisa' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (puestoQueSupervisa.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'puesto que supervisa' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setPuestoQueSupervisa(puestoQueSupervisa);
        }
        if (misionDelPuesto != null) reg.setMisionDelPuesto(misionDelPuesto);
        if (funcionesDelPuesto != null) reg.setFuncionesDelPuesto(funcionesDelPuesto);
        reg.setUpdateUser(username);
        reg.setUpdateDate(new Date());
        puestoService.save(reg);
        response.put("message", "Registro actualizado correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatusEspecial(@RequestParam Long id,
                                                  @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        PuestoOcupacional reg = puestoService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String mensaje;
        switch (reg.getEstado()) {
            case 1:
                reg.setEstado(0);
                reg.setUpdateUser(username);
                reg.setUpdateDate(new Date());
                mensaje = "Registro desactivado correctamente";
                break;
            case 0:
                if (puestoService.existsNombreUnidadOrganicaActivo(reg.getNombrePuesto(), reg.getUnidadOrganica(), id)) {
                    response.put("error", true);
                    response.put("message", "Ya existe otro registro activo con ese nombre");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                reg.setEstado(1);
                reg.setUpdateUser(username);
                reg.setUpdateDate(new Date());
                mensaje = "Registro activado correctamente";
                break;
            default:
                reg.setEstado(0);
                reg.setUpdateUser(username);
                reg.setUpdateDate(new Date());
                mensaje = "Registro desactivado correctamente";
                break;
        }
        puestoService.save(reg);
        response.put("message", mensaje);
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
