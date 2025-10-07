package com.delta.deltanet.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.TipoPago;
import com.delta.deltanet.models.service.TipoPagoServiceImpl;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/tipoPago")
public class TipoPagoController {

  @Autowired
  private TipoPagoServiceImpl tipoPagoService;

  @GetMapping("/list")
  public ResponseEntity<?> getTipoPagos(){
    Map<String, Object> response = new HashMap<>();
        try {
            List<TipoPago> registros = this.tipoPagoService.findAll();
            response.put("data",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de tipo pago");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

  @PostMapping("/save")
  public ResponseEntity<?>grabaTipo(@RequestParam(name = "descrip") String descrip,
                                    @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
          TipoPago registro = new TipoPago();
            registro.setNombre(descrip);
            registro.setEstado(1);
            registro.setCreateUser(usuario);
            registro.setCreateDate(new Date());
            this.tipoPagoService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro creado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion save de tipo pago");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/updateEstado")
    public ResponseEntity<?>ChangeTipo(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<TipoPago> rpta = tipoPagoService.getById(id);
            if (rpta.isEmpty()){
                response.put("mensaje","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoPago registro = rpta.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "activado";
            } else {
                registro.setEstado(0);
                accion = "desactivado";
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoPagoService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion updateEstado de tipo pago");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> showTipo(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoPago> registro = this.tipoPagoService.getById(id);
            response.put("data",registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion show de tipo pago");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?>UpdateTipo(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "descrip") String descrip,
                                      @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<TipoPago> rpta = tipoPagoService.getById(id);
            if (rpta.isEmpty()){
                response.put("mensaje","Registro con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoPago registro = rpta.get();
            registro.setNombre(descrip);
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            this.tipoPagoService.save(registro);

            response.put("dato",registro);
            response.put("mensaje","Registro actualizado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion update de tipo pago");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
