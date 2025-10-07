package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/trabSalud")
public class TrabSaludController {
    @Autowired
    private TrabSaludServiceImpl saludService;

    @Autowired
    private PersonaNaturalServiceImpl naturalService;

    @Autowired
    private DiscapacidadServiceImpl discapacidadService;

    @Autowired
    private EnfermedadServiceImpl enfermedadService;

    @Autowired
    private PersonaServiceImpl personaService;

    @GetMapping("/buscaPerNat")
    public ResponseEntity<?> getByPerNat(@RequestParam(name = "idPer") Long idPer){
        Map<String, Object> response = new HashMap<>();
        try {
            Persona regPer = personaService.buscarId(idPer);
            if(regPer==null){
                response.put("message","No se encontro registro de la persona [" + idPer + "]");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Long idPerNat = regPer.getPerNat().getIdPerNat();
            TrabajadorSalud reg = saludService.buscaByPerNat(idPerNat);
            if (reg==null) {
                response.put("message","No se encontro registro para el idPerNat [" + idPerNat + "]");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("data",reg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en el servicio buscaPerNat");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(name = "id", required = false) Long id,
                                  @RequestParam(name = "idPerNat") Long idPerNat,
                                  @RequestParam(name = "peso", required = false) Double peso,
                                  @RequestParam(name = "estatura", required = false) Integer estatura,
                                  @RequestParam(name = "idDisca", required = false) Integer idDisca,
                                  @RequestParam(name = "ajuste", required = false) String ajuste,
                                  @RequestParam(name = "idEnfer", required = false) Integer idEnfer,
                                  @RequestParam(name = "tratam", required = false) String tratam,
                                  @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            String strMensaje = "";
            TrabajadorSalud reg;
            if(id!=null) {
                reg = saludService.findById(id);
                reg.setUpdateUser(username);
                reg.setUpdateDate(new Date());
                strMensaje = "Se actualizaron los datos exitosamente.";
            }
            else {
                reg = new TrabajadorSalud();
                reg.setEstado(1);
                reg.setCreateUser(username);
                reg.setCreateDate(new Date());
                strMensaje = "Se crearon los datos exitosamente.";
            }

            PersonaNatural regPerNat = naturalService.findById(idPerNat);
            if(regPerNat==null){
                response.put("message","No se encuentra el idPerNat [" + idPerNat + "]");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            reg.setPerNat(regPerNat);
            if (peso!=null || estatura !=null){
                if(peso!=null) regPerNat.setPeso(peso);
                if(estatura!=null) regPerNat.setEstatura(estatura);
                naturalService.save(regPerNat);
            }

            if(idDisca!=null){
                Discapacidad regDisca = discapacidadService.getById(idDisca).orElse(null);
                if(regDisca==null){
                    response.put("message","No se encuentra la discapacidad [" + idDisca + "]");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                reg.setFlagDiscap(1);
                reg.setDiscapacidad(regDisca);
                reg.setAjusteDiscap(ajuste);
            } else{
                reg.setFlagDiscap(0);
                reg.setDiscapacidad(null);
                reg.setAjusteDiscap(null);
            }

            if(idEnfer!=null){
                Enfermedad regEnfer = enfermedadService.getById(idEnfer).orElse(null);
                if(regEnfer==null){
                    response.put("message","No se encuentra la enfermedad [" + idEnfer + "]");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                reg.setFlagEnfer(1);
                reg.setEnfermedad(regEnfer);
                reg.setTratamEnfer(tratam);
            } else{
                reg.setFlagEnfer(0);
                reg.setEnfermedad(null);
                reg.setTratamEnfer(null);
            }

            TrabajadorSalud ts = saludService.save(reg);
            response.put("data",ts);
            response.put("message",strMensaje);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error inesperado en el servicio save");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
