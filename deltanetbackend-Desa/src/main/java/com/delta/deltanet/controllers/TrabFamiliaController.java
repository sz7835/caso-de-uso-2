package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.PersonaNatural;
import com.delta.deltanet.models.entity.TrabajadorFamilia;
import com.delta.deltanet.models.service.PersonaNaturalServiceImpl;
import com.delta.deltanet.models.service.TrabFamiliaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/trabFamilia")
public class TrabFamiliaController {

    @Autowired
    private TrabFamiliaServiceImpl familiaService;

    @Autowired
    private PersonaNaturalServiceImpl naturalService;

    @PostMapping("/save")
    public void save(@RequestParam(name="id") Long id,
                     @RequestParam(name="idPerNat") Long idPerNat,
                     @RequestParam(name="idContacto") Long idCto,
                     @RequestParam(name="flgConvive") Integer flgConv,
                     @RequestParam(name = "username") String username){
        TrabajadorFamilia reg;
        if(id!=null){
            reg = familiaService.findById(id);
            reg.setUpdateDate(new Date());
            reg.setUpdateUser(username);
        } else {
            reg = new TrabajadorFamilia();
            reg.setCreateDate(new Date());
            reg.setCreateUser(username);
        }

        PersonaNatural regPerNat = naturalService.findById(idPerNat);
        reg.setPerNat(regPerNat);

        reg.setConvivencia(flgConv);
        familiaService.save(reg);
    }

    @GetMapping("/listaCtos")
    public ResponseEntity<?> getFamiliares(@RequestParam(name = "idPerNat") Long idPerNat){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> datos = familiaService.listaFamilia(idPerNat);
            List<lstTrabFam> lstFinal = Collections.emptyList();
            if(!datos.isEmpty()) {
                Iterator<Object> it = datos.iterator();
                while (it.hasNext()) {
                    Object[] row = (Object[]) it.next();
                    lstTrabFam item = new lstTrabFam();
                    item.setId((Long) row[0]);
                    item.setTipoDoc(String.valueOf(row[1]));
                    item.setDocumento(String.valueOf(row[2]));
                    item.setNombre(String.valueOf(row[3]) + " " + String.valueOf(row[4]) + " " + String.valueOf(row[5]));
                    item.setMotivo(String.valueOf(row[6]));
                    item.setEstado((Integer) row[7]);
                    item.setConvivencia((Integer) row[8]);
                    lstFinal.add(item);
                }
            }
            response.put("data",lstFinal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en el servicio buscaPerNat");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

class lstTrabFam{
    private Long id;
    private String tipoDoc;
    private String documento;
    private String nombre;
    private String motivo;
    private Integer estado;
    private Integer convivencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getConvivencia() {
        return convivencia;
    }

    public void setConvivencia(Integer convivencia) {
        this.convivencia = convivencia;
    }
}