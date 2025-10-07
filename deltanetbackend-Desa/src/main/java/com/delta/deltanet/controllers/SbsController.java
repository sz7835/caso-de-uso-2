package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.SbsAfp;
import com.delta.deltanet.models.entity.SbsBanca;
import com.delta.deltanet.models.entity.SbsSeguro;
import com.delta.deltanet.models.entity.SbsTipo;
import com.delta.deltanet.models.service.SbsAfpServiceImpl;
import com.delta.deltanet.models.service.SbsBancaServiceImpl;
import com.delta.deltanet.models.service.SbsSeguroServiceImpl;
import com.delta.deltanet.models.service.SbsTipoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/sbs")
public class SbsController {

    @Autowired
    private SbsAfpServiceImpl afpService;

    @Autowired
    private SbsTipoServiceImpl tipoService;

    @Autowired
    private SbsBancaServiceImpl bancaService;

    @Autowired
    private SbsSeguroServiceImpl seguroService;

    /*------------------- TIPO ------------------*/
    @GetMapping("/tipo/list")
    public ResponseEntity<?> getTipos(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<SbsTipo> registros =this.tipoService.getTipos();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de tipo sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipo/show")
    public ResponseEntity<?> showTipo(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> registro = this.tipoService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de tipo sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/save")
    public ResponseEntity<?>grabaTipo(@RequestParam(name = "nombre") String nombre,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = tipoService.countByNombreAndEstado(nombreTrim, 1);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo registro = new SbsTipo();
            registro.setNombre(nombreTrim);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.tipoService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de tipo sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/update")
    public ResponseEntity<?>UpdateTipo(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "nombre") String nombre,
                                      @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> rpta = tipoService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = tipoService.countByNombreAndEstadoAndIdNot(nombreTrim, 1, id);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo registro = rpta.get();
            registro.setNombre(nombreTrim);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion update de tipo sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/updEstado")
    public ResponseEntity<?>ChangeTipo(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<SbsTipo> rpta = tipoService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo registro = rpta.get();
            switch (registro.getEstado()) {
                case 0:
                    // Activar: validar duplicados
                    int duplicados = tipoService.countByNombreAndEstadoAndIdNot(registro.getNombre().trim(), 1, registro.getId());
                    if (duplicados > 0) {
                        response.put("message", "Ya existe otro registro con ese nombre");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    registro.setEstado(1);
                    accion = "activado";
                    break;
                case 1:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
                default:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de tipo sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /*------------------- AFP ------------------*/
    @GetMapping("/afp/list")
    public ResponseEntity<?> getAfps(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<SbsAfp> registros =this.afpService.getAfps();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de afp sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/afp/show")
    public ResponseEntity<?> showAfp(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsAfp> registro = this.afpService.getById(id);
            if(registro.isEmpty()){
                response.put("message", "El registro con Id: [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de afp sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/afp/save")
    public ResponseEntity<?>grabaAfp(@RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                     @RequestParam(required = false, name = "nombre") String nombre,
                                     @RequestParam(required = false, name = "acronimo") String acronimo,
                                     @RequestParam(required = false, name = "codsbs") String codsbs,
                                     @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> rpta = tipoService.getById(idSbsTipo);
            if (rpta.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = rpta.get();
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = afpService.countByNombreAndEstado(nombreTrim, 1);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = afpService.countByCodsbsAndEstado(codsbs.trim(), 1);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsAfp registro = new SbsAfp();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCodsbs(codsbs);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.afpService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de afp sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/afp/update")
    public ResponseEntity<?>UpdateAfp(@RequestParam(name = "id") Integer id,
                                      @RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                      @RequestParam(required = false, name = "nombre") String nombre,
                                      @RequestParam(required = false, name = "acronimo") String acronimo,
                                      @RequestParam(required = false, name = "codsbs") String codsbs,
                                      @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> regTipo = tipoService.getById(idSbsTipo);
            if (regTipo.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = regTipo.get();
            Optional<SbsAfp> rpta = afpService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = afpService.countByNombreAndEstadoAndIdNot(nombreTrim, 1, id);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = afpService.countByCodsbsAndEstadoAndIdNot(codsbs.trim(), 1, id);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsAfp registro = rpta.get();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCodsbs(codsbs);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.afpService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion update de afp sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/afp/updEstado")
    public ResponseEntity<?>ChangeAfp(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<SbsAfp> rpta = afpService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsAfp registro = rpta.get();
            switch (registro.getEstado()) {
                case 0:
                    int duplicados = afpService.countByNombreAndEstadoAndIdNot(registro.getNombre().trim(), 1, registro.getId());
                    if (duplicados > 0) {
                        response.put("message", "Ya existe otro registro con ese nombre");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    if (registro.getCodsbs() != null && !registro.getCodsbs().trim().isEmpty()) {
                        int codsbsDuplicados = afpService.countByCodsbsAndEstadoAndIdNot(registro.getCodsbs().trim(), 1, registro.getId());
                        if (codsbsDuplicados > 0) {
                            response.put("message", "Ya existe otro registro con ese código");
                            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    registro.setEstado(1);
                    accion = "activado";
                    break;
                case 1:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
                default:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.afpService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de afp sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /*------------------- BANCA ------------------*/
    @GetMapping("/banca/list")
    public ResponseEntity<?> getBancas(@RequestParam(name = "sueldo", required = false) Integer sueldo){
        Map<String, Object> response = new HashMap<>();
        try {
            List<SbsBanca> registros;
            if(sueldo==null) {
                registros = this.bancaService.getBancas();
            } else{
                registros = this.bancaService.listaCtaSueldo();
            }
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de banca sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/banca/show")
    public ResponseEntity<?> showBanca(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsBanca> registro = this.bancaService.getById(id);
            if(registro.isEmpty()){
                response.put("message", "El registro con Id: [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de banca sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/banca/save")
    public ResponseEntity<?>grabaBanca(@RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                       @RequestParam(required = false, name = "nombre") String nombre,
                                       @RequestParam(required = false, name = "acronimo") String acronimo,
                                       @RequestParam(required = false, name = "ctasldflag") Integer ctaSldFlg,
                                       @RequestParam(required = false, name = "codsbs") String codsbs,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> rpta = tipoService.getById(idSbsTipo);
            if (rpta.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = rpta.get();
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = bancaService.countByNombreAndEstado(nombreTrim, 1);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = bancaService.countByCodsbsAndEstado(codsbs.trim(), 1);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsBanca registro = new SbsBanca();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCtaSueldoFlg(ctaSldFlg);
            registro.setCodsbs(codsbs);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.bancaService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de banca sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/banca/update")
    public ResponseEntity<?>UpdateBanca(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                        @RequestParam(required = false, name = "nombre") String nombre,
                                        @RequestParam(required = false, name = "acronimo") String acronimo,
                                        @RequestParam(required = false, name = "ctasldflag") Integer ctaSldFlg,
                                        @RequestParam(required = false, name = "codsbs") String codsbs,
                                        @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> regTipo = tipoService.getById(idSbsTipo);
            if (regTipo.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = regTipo.get();
            Optional<SbsBanca> rpta = bancaService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = bancaService.countByNombreAndEstadoAndIdNot(nombreTrim, 1, id);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = bancaService.countByCodsbsAndEstadoAndIdNot(codsbs.trim(), 1, id);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsBanca registro = rpta.get();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCtaSueldoFlg(ctaSldFlg);
            registro.setCodsbs(codsbs);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.bancaService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion update de banca sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/banca/updEstado")
    public ResponseEntity<?>ChangeBanca(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<SbsBanca> rpta = bancaService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsBanca registro = rpta.get();
            switch (registro.getEstado()) {
                case 0:
                    int duplicados = bancaService.countByNombreAndEstadoAndIdNot(registro.getNombre().trim(), 1, registro.getId());
                    if (duplicados > 0) {
                        response.put("message", "Ya existe otro registro con ese nombre");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    if (registro.getCodsbs() != null && !registro.getCodsbs().trim().isEmpty()) {
                        int codsbsDuplicados = bancaService.countByCodsbsAndEstadoAndIdNot(registro.getCodsbs().trim(), 1, registro.getId());
                        if (codsbsDuplicados > 0) {
                            response.put("message", "Ya existe otro registro con ese código");
                            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    registro.setEstado(1);
                    accion = "activado";
                    break;
                case 1:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
                default:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.bancaService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de banca sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /*------------------- SEGURO ------------------*/
    @GetMapping("/seguro/list")
    public ResponseEntity<?> getSeguros(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<SbsSeguro> registros =this.seguroService.getSeguros();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de seguro sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/seguro/show")
    public ResponseEntity<?> showSeguro(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsSeguro> registro = this.seguroService.getById(id);
            if(registro.isEmpty()){
                response.put("message", "El registro con Id: [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de seguro sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/seguro/save")
    public ResponseEntity<?>grabaSeguro(@RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                       @RequestParam(required = false, name = "nombre") String nombre,
                                       @RequestParam(required = false, name = "acronimo") String acronimo,
                                       @RequestParam(required = false, name = "codsbs") String codsbs,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> rpta = tipoService.getById(idSbsTipo);
            if (rpta.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = rpta.get();
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = seguroService.countByNombreAndEstado(nombreTrim, 1);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = seguroService.countByCodsbsAndEstado(codsbs.trim(), 1);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsSeguro registro = new SbsSeguro();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCodsbs(codsbs);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.seguroService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de seguro sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/seguro/update")
    public ResponseEntity<?>UpdateSeguro(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "idSbsTipo") Integer idSbsTipo,
                                        @RequestParam(required = false, name = "nombre") String nombre,
                                        @RequestParam(required = false, name = "acronimo") String acronimo,
                                        @RequestParam(required = false, name = "codsbs") String codsbs,
                                        @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SbsTipo> regTipo = tipoService.getById(idSbsTipo);
            if (regTipo.isEmpty()){
                response.put("message","El id_cod_sbs [" + idSbsTipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsTipo tipoSbs = regTipo.get();
            Optional<SbsSeguro> rpta = seguroService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String nombreTrim = nombre != null ? nombre.trim() : "";
            if (nombreTrim.isEmpty()) {
                response.put("message", "El nombre es obligatorio y no puede estar vacío");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombreTrim.matches("^[0-9]+$") || nombreTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "El nombre no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            int duplicados = seguroService.countByNombreAndEstadoAndIdNot(nombreTrim, 1, id);
            if (duplicados > 0) {
                response.put("message", "Ya existe otro registro con ese nombre");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (codsbs != null && !codsbs.trim().isEmpty()) {
                int codsbsDuplicados = seguroService.countByCodsbsAndEstadoAndIdNot(codsbs.trim(), 1, id);
                if (codsbsDuplicados > 0) {
                    response.put("message", "Ya existe otro registro con ese código");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SbsSeguro registro = rpta.get();
            registro.setSbsTipo(tipoSbs);
            registro.setNombre(nombreTrim);
            registro.setAcronimo(acronimo);
            registro.setCodsbs(codsbs);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.seguroService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion update de seguro sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/seguro/updEstado")
    public ResponseEntity<?>ChangeSeguro(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<SbsSeguro> rpta = seguroService.getById(id);
            if (rpta.isEmpty()){
                response.put("message","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SbsSeguro registro = rpta.get();
            switch (registro.getEstado()) {
                case 0:
                    int duplicados = seguroService.countByNombreAndEstadoAndIdNot(registro.getNombre().trim(), 1, registro.getId());
                    if (duplicados > 0) {
                        response.put("message", "Ya existe otro registro con ese nombre");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    if (registro.getCodsbs() != null && !registro.getCodsbs().trim().isEmpty()) {
                        int codsbsDuplicados = seguroService.countByCodsbsAndEstadoAndIdNot(registro.getCodsbs().trim(), 1, registro.getId());
                        if (codsbsDuplicados > 0) {
                            response.put("message", "Ya existe otro registro con ese código");
                            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                    }
                    registro.setEstado(1);
                    accion = "activado";
                    break;
                case 1:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
                default:
                    registro.setEstado(0);
                    accion = "desactivado";
                    break;
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.seguroService.save(registro);
            response.put("dato",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de seguro sbs");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
