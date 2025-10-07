package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/adjunto")
public class AdjuntosController {
    @Autowired
    private IRepArchivoFormatoService formatoService;

    @GetMapping("/tipoArchivo/index")
    public ResponseEntity<?> formatoIndex(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<RepArchivoFormato> listado = formatoService.findAll();
            response.put("message", "Se retorna listado de registros");
            response.put("formatos", listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipoArchivo/show")
    public ResponseEntity<?> formatoShow(@RequestParam(name = "idFormato") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            RepArchivoFormato registro = formatoService.findById(id);
            if (registro == null) {
                response.put("message", "El formato con id [" + id + "] no se encuentra registrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("message", "Se retorna el registro requerido");
            response.put("formato", registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoArchivo/create")
    public ResponseEntity<?> formatoCreate(@RequestParam(name = "nombre") String nombre,
                                           @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "La descripción del formato de archivo es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción del formato de archivo no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Verificar duplicados entre activos
            int duplicados = formatoService.countByNombreAndEstado(nombreTrim, 1);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            RepArchivoFormato reg = new RepArchivoFormato();
            reg.setNombre(nombreTrim);
            reg.setEstado(1);
            reg.setUsrCreate(usuario);
            reg.setFecCreate(new Date());
            formatoService.save(reg);
            response.put("message", "Registro activado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Ocurrió un error al registrar el formato de archivo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoArchivo/update")
    public ResponseEntity<?> formatoUpdate(@RequestParam(name = "idFormato") Long id,
                                           @RequestParam(name = "nombre") String nombre,
                                           @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            RepArchivoFormato reg = formatoService.findById(id);
            if (reg == null) {
                response.put("message", "No se encontró el formato de archivo con el id proporcionado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "La descripción del formato de archivo es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción del formato de archivo no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Verificar duplicados entre activos, excluyendo el actual
            int duplicados = formatoService.countByNombreAndEstadoExcluyendoId(nombreTrim, 1, id);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nombreTrim);
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            formatoService.save(reg);
            response.put("message", "Registro actualizado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Ocurrió un error al actualizar el formato de archivo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoArchivo/chgEstado")
    public ResponseEntity<?> formatoChgEstado(@RequestParam(name = "idFormato") Long id,
                                              @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            RepArchivoFormato reg = formatoService.findById(id);
            if (reg == null) {
                response.put("message", "No se encontró el formato de archivo con el id proporcionado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = "";
            switch (reg.getEstado()) {
                case 0:
                    int duplicados = formatoService.countByNombreAndEstadoExcluyendoId(reg.getNombre().trim(), 1, reg.getIdArchivoFormato());
                    if (duplicados > 0) {
                        response.put("message", "Ya existe otro registro activo con la misma descripción");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    reg.setEstado(1);
                    cambio = "activado";
                    break;
                case 1:
                    reg.setEstado(0);
                    cambio = "desactivado";
                    break;
                default:
                    reg.setEstado(0);
                    cambio = "desactivado";
                    break;
            }
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            formatoService.save(reg);
            response.put("message", "Registro " + cambio);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Ocurrió un error al cambiar el estado del formato de archivo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
