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
@RequestMapping("/trabSueldo")
public class TrabSueldoController {
    @Autowired
    private TrabSueldoServiceImpl sueldoService;

    @Autowired
    private PersonaNaturalServiceImpl naturalService;

    @Autowired
    private SbsAfpServiceImpl afpService;

    @Autowired
    private SbsBancaServiceImpl bancaService;

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

            TrabajadorSueldo reg = sueldoService.buscaByPerNat(idPerNat);
            if (reg==null) {
                response.put("message","No se encontro registro para el idPerNat [" + idPerNat + "]");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("data",reg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error inesperado en el servicio buscaPerNat");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(name = "id", required = false) Long id,
                                  @RequestParam(name = "idPerNat") Long idPerNat,
                                  @RequestParam(name = "idSbs", required = false) Integer idSbs,
                                  @RequestParam(name = "idBanca", required = false) Integer idBanca,
                                  @RequestParam(name = "ctaAfp", required = false) String ctaAfp,
                                  @RequestParam(name = "ctaBanco", required = false) String ctaBanco,
                                  @RequestParam(name = "cci", required = false) String cci,
                                  @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            TrabajadorSueldo reg;
            String strMensaje = "";
            if(id!=null){
                reg = sueldoService.findById(id);
                reg.setUpdateUser(username);
                reg.setUpdateDate(new Date());
                strMensaje = "Se actualizaron los datos exitosamente.";
            } else {
                reg = new TrabajadorSueldo();
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

            if(idSbs!=null){
                SbsAfp regAfp = afpService.getById(idSbs).orElse(null);
                if(regAfp==null){
                    response.put("message","No se encuentra la afp [" + idSbs + "]");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                reg.setAfp(regAfp);
            }
            if(ctaAfp!=null) reg.setNroAfp(ctaAfp);

            if(idBanca!=null){
                SbsBanca regBanca = bancaService.getById(idBanca).orElse(null);
                if(regBanca==null){
                    response.put("message","No se encuentra la banca [" + idBanca + "]");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                reg.setBanca(regBanca);
            }
            if(ctaBanco!=null) reg.setNroCuenta(ctaBanco);
            if(cci!=null) reg.setNroCCI(cci);
            TrabajadorSueldo ts = sueldoService.save(reg);

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
