package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Enfermedad;
import com.delta.deltanet.models.entity.TipoEnfermedad;
import com.delta.deltanet.models.service.EnfermedadServiceImpl;
import com.delta.deltanet.models.service.TipoEnfermedadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/enfermedad")
public class EnfermedadController {
    @Autowired
    private EnfermedadServiceImpl enfermedadService;

    @Autowired
    private TipoEnfermedadServiceImpl tipoEnfermedadService;

    /*------------------- ENFERMEDAD ------------------*/
    @GetMapping("/list")
    public ResponseEntity<?> getEnfermedades(@RequestParam(name = "idtipo", required = false) Integer idTipo){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Enfermedad> registros;
            if(idTipo!=null)
                registros = this.enfermedadService.listaByTipo(idTipo);
            else
                registros = this.enfermedadService.getAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de enfermedades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> showEnfermedad(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Enfermedad> registro = this.enfermedadService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion show de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?>grabaenfermedad(@RequestParam(name = "tipo") Integer tipo,
                                            @RequestParam(name = "nombre") String nombre,
                                            @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            TipoEnfermedad regTipo = tipoEnfermedadService.getById(tipo).get();
            Enfermedad registro = new Enfermedad();
            registro.setIdTipo(regTipo);
            registro.setNombre(nombre);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.enfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro creado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion save de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?>Updateenfermedad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "tipo") Integer tipo,
                                             @RequestParam(name = "nombre") String nombre,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoEnfermedad> reg1 = tipoEnfermedadService.getById(tipo);
            if (reg1.isEmpty()){
                response.put("mensaje","Tipo de enfermedad con id [" + tipo + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoEnfermedad regTipo = reg1.get();

            Optional<Enfermedad> reg2 = enfermedadService.getById(id);
            if(reg2.isEmpty()){
                response.put("mensaje","Enfermedad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Enfermedad registro = reg2.get();
            registro.setIdTipo(regTipo);
            registro.setNombre(nombre);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.enfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro actualizado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion update de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/updEstado")
    public ResponseEntity<?>ChangeEnfermedad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<Enfermedad> reg = enfermedadService.getById(id);
            if (reg.isEmpty()){
                response.put("mensaje","Enfermedad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Enfermedad registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "habilitado";
            } else {
                registro.setEstado(0);
                accion = "deshabilitado";
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.enfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion cambio de estado de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    /*------------------- TIPO ------------------*/
    @GetMapping("/tipo/list")
    public ResponseEntity<?> getTiposEnfermedades(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoEnfermedad> registros = this.tipoEnfermedadService.getAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de tipos de enfermedades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipo/show")
    public ResponseEntity<?> showTipoEnfermedad(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoEnfermedad> registro = this.tipoEnfermedadService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion show de tipo de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/save")
    public ResponseEntity<?>grabaTipoEnfermedad(@RequestParam(name = "nombre") String nombre,
                                            @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            TipoEnfermedad registro = new TipoEnfermedad();
            registro.setNombre(nombre);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.tipoEnfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro creado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion save de tpo de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/update")
    public ResponseEntity<?>UpdateTipoEnfermedad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "nombre") String nombre,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoEnfermedad> reg = tipoEnfermedadService.getById(id);
            if(reg.isEmpty()){
                response.put("mensaje","Tipo de enfermedad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            TipoEnfermedad registro = reg.get();
            registro.setNombre(nombre);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoEnfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro actualizado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion update de tipo de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipo/updEstado")
    public ResponseEntity<?>ChangeTipoEnfermedad(@RequestParam(name = "id") Integer id,
                                             @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<TipoEnfermedad> reg = tipoEnfermedadService.getById(id);
            if (reg.isEmpty()){
                response.put("mensaje","Tipo enfermedad con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoEnfermedad registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "habilitado";
            } else {
                registro.setEstado(0);
                accion = "deshabilitado";
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoEnfermedadService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion cambio de estado de enfermedad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
