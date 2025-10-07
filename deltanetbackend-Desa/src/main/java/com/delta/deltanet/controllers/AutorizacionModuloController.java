package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AutorizacionModulo;
import com.delta.deltanet.models.service.AutorizacionModuloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/aplicacion")
public class AutorizacionModuloController {
    @Autowired
    private AutorizacionModuloServiceImpl autorizacionModuloService;

    @GetMapping("/index")
    public ResponseEntity<?> getAplicaciones(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<AutorizacionModulo> regs = autorizacionModuloService.getAll();
            List<AplicacionDTO> registros = new ArrayList<>();
            for (AutorizacionModulo row : regs) {
                AplicacionDTO reg = new AplicacionDTO();
                reg.setiD_APLICACION(row.getId());
                reg.setNoM_APLICACION(row.getNombre());
                reg.setEstado(row.getEstado());
                reg.setUsR_INGRESO(row.getUsrIngreso());
                reg.setFeC_INGRESO(row.getFecIngreso());
                reg.setUsR_ULT_MOD(row.getUsrUltMod());
                reg.setFeC_ULT_MOD(row.getFecUltMod());

                registros.add(reg);
            }

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de aplicaciones");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("show")
    public ResponseEntity<?> showAplicacion(@RequestParam(name = "id") Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutorizacionModulo> reg = autorizacionModuloService.getById(id);

            AplicacionDTO registro = new AplicacionDTO();
            registro.setiD_APLICACION(reg.get().getId());
            registro.setNoM_APLICACION(reg.get().getNombre());
            registro.setEstado(reg.get().getEstado());
            registro.setUsR_INGRESO(reg.get().getUsrIngreso());
            registro.setFeC_INGRESO(reg.get().getFecIngreso());
            registro.setUsR_ULT_MOD(reg.get().getUsrUltMod());
            registro.setFeC_ULT_MOD(reg.get().getFecUltMod());

            response.put("aplication",registro);
            response.put("message","Busqueda exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de discapacidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/store")
    public ResponseEntity<?> saveAplicacion(@RequestParam(name = "name") String name,
                                            @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutorizacionModulo> regApp = autorizacionModuloService.getByNombre(name);
            if(regApp.isPresent()){
                response.put("message","El nombre de la aplicacion ya existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e){
            response.put("message", "Error inesperado en la existencia de la aplicacion");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            AutorizacionModulo reg = new AutorizacionModulo();
            reg.setNombre(name);
            reg.setEstado(1);
            reg.setUsrIngreso(username);
            reg.setFecIngreso(new Date());
            autorizacionModuloService.save(reg);

            AplicacionDTO registro = new AplicacionDTO();
            registro.setiD_APLICACION(reg.getId());
            registro.setNoM_APLICACION(reg.getNombre());
            registro.setEstado(reg.getEstado());
            registro.setUsR_INGRESO(reg.getUsrIngreso());
            registro.setFeC_INGRESO(reg.getFecIngreso());
            registro.setUsR_ULT_MOD(reg.getUsrUltMod());
            registro.setFeC_ULT_MOD(reg.getFecUltMod());

            response.put("aplication",registro);
            response.put("message","Registro exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de aplicacion");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAplicacion(@RequestParam(name = "id") Integer id,
                                              @RequestParam(name = "name") String name,
                                              @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutorizacionModulo> reg = autorizacionModuloService.getById(id);
            if(reg.isEmpty()){
                response.put("message","El ID de la aplicacion no existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            AutorizacionModulo regis = reg.get();
            regis.setNombre(name);
            regis.setUsrUltMod(username);
            regis.setFecUltMod(new Date());
            autorizacionModuloService.save(regis);

            AplicacionDTO registro = new AplicacionDTO();
            registro.setiD_APLICACION(regis.getId());
            registro.setNoM_APLICACION(regis.getNombre());
            registro.setEstado(regis.getEstado());
            registro.setUsR_INGRESO(regis.getUsrIngreso());
            registro.setFeC_INGRESO(regis.getFecIngreso());
            registro.setUsR_ULT_MOD(regis.getUsrUltMod());
            registro.setFeC_ULT_MOD(regis.getFecUltMod());

            response.put("aplication",registro);
            response.put("message","Actualización exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e){
            response.put("message", "Error inesperado en la actualizacion de la aplicacion");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> changeAplicacion(@RequestParam(name = "id") Integer id,
                                              @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<AutorizacionModulo> reg = autorizacionModuloService.getById(id);
            if(reg.isEmpty()){
                response.put("message","Aplicacion con id [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            AutorizacionModulo registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "activado";
            } else {
                registro.setEstado(0);
                accion = "desactivado";
            }
            registro.setUsrUltMod(username);
            registro.setFecUltMod(new Date());
            autorizacionModuloService.save(registro);

            AplicacionDTO regDTO = new AplicacionDTO();
            regDTO.setNoM_APLICACION(registro.getNombre());
            regDTO.setEstado(registro.getEstado());
            regDTO.setUsR_INGRESO(registro.getUsrIngreso());
            regDTO.setFeC_INGRESO(registro.getFecIngreso());
            regDTO.setUsR_ULT_MOD(registro.getUsrUltMod());
            regDTO.setFeC_ULT_MOD(registro.getFecUltMod());
            regDTO.setiD_APLICACION(registro.getId());

            response.put("aplication",regDTO);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de aplicacion");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

class AplicacionDTO{
    private Integer iD_APLICACION;
    private String noM_APLICACION;
    private Integer estado;
    private String usR_INGRESO;
    private Date feC_INGRESO;
    private String usR_ULT_MOD;
    private Date feC_ULT_MOD;
    private String funcionalidades;

    public Integer getiD_APLICACION() {
        return iD_APLICACION;
    }

    public void setiD_APLICACION(Integer iD_APLICACION) {
        this.iD_APLICACION = iD_APLICACION;
    }

    public String getNoM_APLICACION() {
        return noM_APLICACION;
    }

    public void setNoM_APLICACION(String noM_APLICACION) {
        this.noM_APLICACION = noM_APLICACION;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getUsR_INGRESO() {
        return usR_INGRESO;
    }

    public void setUsR_INGRESO(String usR_INGRESO) {
        this.usR_INGRESO = usR_INGRESO;
    }

    public Date getFeC_INGRESO() {
        return feC_INGRESO;
    }

    public void setFeC_INGRESO(Date feC_INGRESO) {
        this.feC_INGRESO = feC_INGRESO;
    }

    public String getUsR_ULT_MOD() {
        return usR_ULT_MOD;
    }

    public void setUsR_ULT_MOD(String usR_ULT_MOD) {
        this.usR_ULT_MOD = usR_ULT_MOD;
    }

    public Date getFeC_ULT_MOD() {
        return feC_ULT_MOD;
    }

    public void setFeC_ULT_MOD(Date feC_ULT_MOD) {
        this.feC_ULT_MOD = feC_ULT_MOD;
    }

    public String getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(String funcionalidades) {
        this.funcionalidades = funcionalidades;
    }
}