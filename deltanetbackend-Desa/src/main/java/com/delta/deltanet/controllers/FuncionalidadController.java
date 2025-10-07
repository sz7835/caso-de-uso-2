package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AutorizacionModulo;
import com.delta.deltanet.models.entity.Funcionalidad;
import com.delta.deltanet.models.service.AutorizacionModuloServiceImpl;
import com.delta.deltanet.models.service.FuncionalidadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/funcionalidad")
public class FuncionalidadController {
    @Autowired
    private FuncionalidadServiceImpl funcionalidadService;

    @Autowired
    private AutorizacionModuloServiceImpl autorizacionModuloService;

    @GetMapping("/index")
    public ResponseEntity<?> getFuncionalidades(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Funcionalidad> regs = funcionalidadService.getAll();
            return new ResponseEntity<>(regs, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de funcionalidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/show")
    public ResponseEntity<?> getFuncionalidad(@RequestParam(name = "id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Funcionalidad> reg = funcionalidadService.getById(id);
            if(reg.isEmpty()){
                response.put("message", "El ID de la funcionalidad [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Funcionalidad registro = reg.get();
            List<Object> ap = autorizacionModuloService.listaAplicaciones();
            List<MaestraDTO> apps = new ArrayList<>();
            Iterator<Object> it = ap.iterator();
            while (it.hasNext()){
                Object[] row = (Object[]) it.next();
                MaestraDTO regDTO = new MaestraDTO();
                regDTO.setValue(String.valueOf(row[0]));
                regDTO.setText(String.valueOf(row[1]));
                apps.add((regDTO));
            }

            List<Object> fn = funcionalidadService.listaFunciones();
            List<MaestraDTO> funcs = new ArrayList<>();
            it = fn.iterator();
            while (it.hasNext()){
                Object[] row = (Object[]) it.next();
                MaestraDTO regDTO = new MaestraDTO();
                regDTO.setValue(String.valueOf(row[0]));
                regDTO.setText(String.valueOf(row[1]));
                funcs.add(regDTO);
            }

            response.put("functionality",registro);
            response.put("aplications",apps);
            response.put("fathers",funcs);
            response.put("message","Busqueda exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de funcionalidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> create() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> ap = autorizacionModuloService.listaAplicaciones();
            List<MaestraDTO> apps = new ArrayList<>();
            Iterator<Object> it = ap.iterator();
            while (it.hasNext()){
                Object[] row = (Object[]) it.next();
                MaestraDTO regDTO = new MaestraDTO();
                regDTO.setValue(String.valueOf(row[0]));
                regDTO.setText(String.valueOf(row[1]));
                apps.add((regDTO));
            }

            List<Object> fn = funcionalidadService.listaFunciones();
            List<MaestraDTO> funcs = new ArrayList<>();
            it = fn.iterator();
            while (it.hasNext()){
                Object[] row = (Object[]) it.next();
                MaestraDTO regDTO = new MaestraDTO();
                regDTO.setValue(String.valueOf(row[0]));
                regDTO.setText(String.valueOf(row[1]));
                funcs.add(regDTO);
            }

            response.put("aplications",apps);
            response.put("fathers",funcs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de funcionalidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/store")
    public ResponseEntity<?> saveFuncionalidad(@RequestParam(name = "name") String name,
                                               @RequestParam(name = "description") String description,
                                               @RequestParam(name = "level") Integer level,
                                               @RequestParam(name = "order") Integer order,
                                               @RequestParam(required = false, name = "id_father") Long idFather,
                                               @RequestParam(name = "id_aplication") Integer idApp,
                                               @RequestParam(required = false, name = "ruta") String ruta,
                                               @RequestParam(required = false, name = "route") String route,
                                               @RequestParam(required = false,name = "icon") String icon,
                                               @RequestParam(name = "menu") String flgMenu,
                                               @RequestParam(name = "control") String flgControl,
                                               @RequestParam(name = "username") String username) {
        Map<String, Object> response = new HashMap<>();
        if(ruta.isEmpty()) ruta=null;
        if(route.isEmpty()) ruta=null;
        if(icon.isEmpty()) icon=null;
        try {
            Optional<AutorizacionModulo> aplicacion = autorizacionModuloService.getById(idApp);
            if (aplicacion.isEmpty()){
                response.put("message","El id de aplicacion: [" + idApp + "] no existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Optional<Funcionalidad> padre = Optional.empty();
            if(idFather!=null) {
                padre = funcionalidadService.getById(idFather);
                if (padre.isEmpty()) {
                    response.put("message", "El id de funcionalidad padre [" + idFather + "] no existe");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }

            Funcionalidad registro = new Funcionalidad();
            if(idFather!=null) registro.setFather(padre.get()); else registro.setFather(null);
            registro.setNoM_FUNCIONALIDAD(name);
            registro.setDesC_FUNCIONALIDAD(description);
            registro.setSeC_NIVEL(level);
            registro.setSeC_ORDEN(order);
            registro.setAplication(aplicacion.get());
            registro.setRuta(ruta);
            registro.setRoute(route);
            registro.setIcon(icon);
            registro.setFlG_MENU(flgMenu);
            registro.setFlG_CONTROL(flgControl);
            registro.setEstado(1);
            registro.setUsR_INGRESO(username);
            registro.setFeC_INGRESO(new Date());
            funcionalidadService.save(registro);

            response.put("functionality",registro);
            response.put("message","Registro exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion store de funcionalidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFuncionalidad(@RequestParam(name = "id") Long id,
                                               @RequestParam(required = false, name = "name") String name,
                                               @RequestParam(required = false, name = "description") String description,
                                               @RequestParam(required = false, name = "level") Integer level,
                                               @RequestParam(required = false, name = "order") Integer order,
                                               @RequestParam(required = false, name = "id_father") Long idFather,
                                               @RequestParam(required = false, name = "id_aplication") Integer idApp,
                                               @RequestParam(required = false, name = "ruta") String ruta,
                                               @RequestParam(required = false, name = "route") String route,
                                               @RequestParam(required = false, name = "icon") String icon,
                                               @RequestParam(required = false, name = "menu") String flgMenu,
                                               @RequestParam(required = false, name = "control") String flgControl,
                                               @RequestParam(name = "username") String username) {
        Map<String, Object> response = new HashMap<>();
        if(ruta.isEmpty()) ruta=null;
        if(route.isEmpty()) route=null;
        if(icon.isEmpty()) icon=null;
        Optional<Funcionalidad> reg = funcionalidadService.getById(id);
        if(reg.isEmpty()){
            response.put("message","El id funcionalidad [" + id + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        Optional<AutorizacionModulo> regApp = Optional.empty();
        if(idApp!=null) {
            regApp = autorizacionModuloService.getById(idApp);
            if(regApp.isEmpty()){
                response.put("message","El id de la aplicacion [" + idApp + "] no existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        Optional<Funcionalidad> padre = Optional.empty();
        if(idFather!=null){
            padre = funcionalidadService.getById(idFather);
            if(padre.isEmpty()){
                response.put("message","El id de la funcionalidad padre [" + idFather + "] no existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        try {
            Funcionalidad registro = reg.get();
            if (name != null) registro.setNoM_FUNCIONALIDAD(name);
            if (description != null) registro.setDesC_FUNCIONALIDAD(description);
            if (level != null) registro.setSeC_NIVEL(level);
            if (order != null) registro.setSeC_ORDEN(order);
            if (idFather != null) registro.setFather(padre.get()); else registro.setFather(null);
            if (idApp != null) registro.setAplication(regApp.get());
            registro.setRuta(ruta);
            if (route != null) registro.setRoute(route);
            registro.setIcon(icon);
            if (flgMenu != null) registro.setFlG_MENU(flgMenu);
            if (flgControl != null) registro.setFlG_CONTROL(flgControl);
            registro.setUsR_ULT_MOD(username);
            registro.setFeC_ULT_MOD(new Date());

            funcionalidadService.save(registro);
            response.put("functionality", registro);
            response.put("mensaje", "Actualización exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion store de funcionalidades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> changeFuncionalidad(@RequestParam(name = "id") Long id,
                                                 @RequestParam(name = "username") String username) {
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<Funcionalidad> reg = funcionalidadService.getById(id);
            if(reg.isEmpty()){
                response.put("mensaje","El id funcionalidad [" + id + "] no se encuentra");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Funcionalidad registro = reg.get();
            if (registro.getEstado()==0) {
                registro.setEstado(1);
                accion = "activado";
            } else {
                registro.setEstado(0);
                accion = "desactivado";
            }
            registro.setUsR_ULT_MOD(username);
            registro.setFeC_ULT_MOD(new Date());
            funcionalidadService.save(registro);

            response.put("functionality",registro);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion updEstado de funcionalidad");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/listarmaestros")
    public ResponseEntity<?> listarMaestros() {
        Map<String, Object> response = new HashMap<>();
        try {
            Funcionalidad padre = funcionalidadService.getByRoute("dashboard.masters");
            if (padre == null) {
                response.put("data", Collections.emptyList());
                response.put("message", "No se encontró el padre");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            List<Funcionalidad> hijos = funcionalidadService.getByPadreId(padre.getiD_FUNCIONALIDAD());
            response.put("data", hijos);
            response.put("message", "Filtrado exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la función listarMaestros");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

class MaestraDTO{
    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}