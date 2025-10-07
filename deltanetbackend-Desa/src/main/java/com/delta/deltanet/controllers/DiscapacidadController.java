package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Discapacidad;
import com.delta.deltanet.models.entity.TipoDiscapacidad;
import com.delta.deltanet.models.service.DiscapacidadServiceImpl;
import com.delta.deltanet.models.service.TipoDiscapacidadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/discapacidad")
public class DiscapacidadController {
    @Autowired
    private DiscapacidadServiceImpl discapacidadService;

    @Autowired
    private TipoDiscapacidadServiceImpl tipoDiscapacidadService;

    /*------------------- DISCAPACIDAD ------------------*/
    @GetMapping("/list")
    public ResponseEntity<?> getDiscapacidades(@RequestParam(name = "idtipo", required = false) Integer idTipo){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Discapacidad> registros;
            if(idTipo!=null)
                registros = this.discapacidadService.listaByTipo(idTipo);
            else
                registros = this.discapacidadService.getAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de discapacidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> showiscapacidad(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Discapacidad> registro = this.discapacidadService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion show de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?>grabaDiscapacidad(@RequestParam(name = "tipo") Integer tipo,
                                            @RequestParam(name = "nombre") String nombre,
                                            @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoDiscapacidad> reg = tipoDiscapacidadService.getById(tipo);
            if(reg.isEmpty()){
                response.put("mensaje","Tipo de discapacidad con id [" + tipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoDiscapacidad regTipo = reg.get();
            Discapacidad registro = new Discapacidad();
            registro.setIdTipo(regTipo);
            registro.setNombre(nombre);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.discapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro creado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion save de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?>UpdateDiscapacidad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "tipo") Integer tipo,
                                             @RequestParam(name = "nombre") String nombre,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoDiscapacidad> reg1 = tipoDiscapacidadService.getById(tipo);
            if (reg1.isEmpty()){
                response.put("mensaje","Tipo de discapacidad con id [" + tipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoDiscapacidad regTipo = reg1.get();

            Optional<Discapacidad> reg2 = discapacidadService.getById(id);
            if(reg2.isEmpty()){
                response.put("mensaje","Discapacidad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Discapacidad registro = reg2.get();
            registro.setIdTipo(regTipo);
            registro.setNombre(nombre);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.discapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro actualizado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion update de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/updEstado")
    public ResponseEntity<?>ChangeDiscapacidad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<Discapacidad> reg = discapacidadService.getById(id);
            if (reg.isEmpty()){
                response.put("mensaje","Discapacidad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Discapacidad registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "habilitado";
            } else {
                registro.setEstado(0);
                accion = "deshabilitado";
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.discapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion cambio de estado de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    /*------------------- TIPO ------------------*/
    @GetMapping("/tipo/list")
    public ResponseEntity<?> getTiposDiscapacidades(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoDiscapacidad> registros = this.tipoDiscapacidadService.getAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de tipos de discapacidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipo/show")
    public ResponseEntity<?> showTipoDiscapacidad(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoDiscapacidad> registro = this.tipoDiscapacidadService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion show de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/save")
    public ResponseEntity<?>grabaTipoDiscapacidad(@RequestParam(name = "nombre") String nombre,
                                              @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            TipoDiscapacidad registro = new TipoDiscapacidad();
            registro.setNombre(nombre);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.tipoDiscapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro creado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion save de tipo de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/update")
    public ResponseEntity<?>UpdateTipoDiscapacidad(@RequestParam(name = "id") Integer id,
                                               @RequestParam(name = "nombre") String nombre,
                                               @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoDiscapacidad> reg = tipoDiscapacidadService.getById(id);
            if (reg.isEmpty()){
                response.put("mensaje","Tipo de discapacidad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoDiscapacidad registro = reg.get();
            registro.setNombre(nombre);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoDiscapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro actualizado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion update de tipo de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/updEstado")
    public ResponseEntity<?>ChangeTipoDiscapacidad(@RequestParam(name = "id") Integer id,
                                               @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<TipoDiscapacidad> reg = tipoDiscapacidadService.getById(id);
            if (reg.isEmpty()){
                response.put("mensaje","Tipo de discapacidad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoDiscapacidad registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "habilitado";
            } else {
                registro.setEstado(0);
                accion = "deshabilitado";
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoDiscapacidadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion updEstado de tipo de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
