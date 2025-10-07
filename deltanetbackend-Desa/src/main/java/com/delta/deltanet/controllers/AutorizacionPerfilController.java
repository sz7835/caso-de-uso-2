package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/perfil")
public class AutorizacionPerfilController {

    @Autowired
    private AuthPerfilServiceImpl authPerfilService;

    @Autowired
    private AutorizacionModuloServiceImpl autorizacionModuloService;

    @Autowired
    private AuthFuncServiceImpl authFuncService;

    @Autowired
    private AuthFuncPerfilServiceImpl authFuncPerfilService;

    @GetMapping("/index")
    ResponseEntity<?> listaPerfiles(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<AutorizacionPerfil> regs = authPerfilService.getAll();
            List<perfilDTO> registros = new ArrayList<>();
            for(AutorizacionPerfil perfil : regs){
                perfilDTO r = new perfilDTO();
                r.setiD_PERFIL(perfil.getId());
                r.setDsC_NOM_PERFIL(perfil.getNombre());
                r.setDsC_DESCRIPCION(perfil.getDescripcion());
                r.setCoD_EST_PERFIL(perfil.getEstado());
                r.setFeC_INGRESO(perfil.getFecIngreso());
                r.setUsR_INGRESO(perfil.getUsrIngreso());
                r.setFeC_ULT_MOD(perfil.getFecUltMod());
                r.setUsR_ULT_MOD(perfil.getUsrUltMod());

                registros.add(r);
            }
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion list de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/show")
    ResponseEntity<?> muestraPerfil(@RequestParam(name = "id") int id){
        Map<String, Object> response = new HashMap<>();
        try {
            AutorizacionPerfil perfil = authPerfilService.findById(id);
            if(perfil==null){
                response.put("message", "El id de perfil [" + id + "] no encontrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            perfilDTO r = new perfilDTO();
            r.setiD_PERFIL(perfil.getId());
            r.setDsC_NOM_PERFIL(perfil.getNombre());
            r.setDsC_DESCRIPCION(perfil.getDescripcion());
            r.setCoD_EST_PERFIL(perfil.getEstado());
            r.setFeC_INGRESO(perfil.getFecIngreso());
            r.setUsR_INGRESO(perfil.getUsrIngreso());
            r.setFeC_ULT_MOD(perfil.getFecUltMod());
            r.setUsR_ULT_MOD(perfil.getUsrUltMod());
            response.put("perfil",r);
            response.put("message","Busqueda exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion show de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/store")
    ResponseEntity<?> grabaPerfil(@RequestParam(name = "name") String name,
                                  @RequestParam(name = "description") String desc,
                                  @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            AutorizacionPerfil perfil = authPerfilService.getByName(name);
            if(perfil!=null){
                response.put("message", "El perfil [" + name + "] ya esta registrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            AutorizacionPerfil reg = new AutorizacionPerfil();
            reg.setNombre(name);
            reg.setDescripcion(desc);
            reg.setEstado(1);
            reg.setUsrIngreso(username);
            reg.setFecIngreso(new Date());
            reg.setUsrUltMod(username);
            reg.setFecUltMod(new Date());
            perfil = authPerfilService.save(reg);

            perfilDTO r = new perfilDTO();
            r.setiD_PERFIL(perfil.getId());
            r.setDsC_NOM_PERFIL(perfil.getNombre());
            r.setDsC_DESCRIPCION(perfil.getDescripcion());
            r.setCoD_EST_PERFIL(perfil.getEstado());
            r.setFeC_INGRESO(perfil.getFecIngreso());
            r.setUsR_INGRESO(perfil.getUsrIngreso());
            r.setFeC_ULT_MOD(perfil.getFecUltMod());
            r.setUsR_ULT_MOD(perfil.getUsrUltMod());
            response.put("perfil",r);
            response.put("message","Registro exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion save de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    ResponseEntity<?> actPerfil(@RequestParam(name = "id") Integer id,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "description") String desc,
                                @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            AutorizacionPerfil reg = authPerfilService.findById(id);
            if(reg==null){
                response.put("message", "El perfil con id [" + id + "] no se encuentra");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            reg.setNombre(name);
            reg.setDescripcion(desc);
            reg.setUsrUltMod(username);
            reg.setFecUltMod(new Date());
            AutorizacionPerfil perfil = authPerfilService.save(reg);

            perfilDTO r = new perfilDTO();
            r.setiD_PERFIL(perfil.getId());
            r.setDsC_NOM_PERFIL(perfil.getNombre());
            r.setDsC_DESCRIPCION(perfil.getDescripcion());
            r.setCoD_EST_PERFIL(perfil.getEstado());
            r.setFeC_INGRESO(perfil.getFecIngreso());
            r.setUsR_INGRESO(perfil.getUsrIngreso());
            r.setFeC_ULT_MOD(perfil.getFecUltMod());
            r.setUsR_ULT_MOD(perfil.getUsrUltMod());
            response.put("perfil",r);
            response.put("message","Actualización exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion update de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/delete")
    ResponseEntity<?> delPerfil(@RequestParam(name = "id") Integer id,
                                @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            AutorizacionPerfil reg = authPerfilService.findById(id);
            if(reg==null){
                response.put("message", "El perfil con id [" + id + "] no se encuentra");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (reg.getEstado()==0) {
                reg.setEstado(1);
                accion = "activado";
            } else {
                reg.setEstado(0);
                accion = "desactivado";
            }

            reg.setUsrUltMod(username);
            reg.setFecUltMod(new Date());
            AutorizacionPerfil perfil = authPerfilService.save(reg);

            perfilDTO r = new perfilDTO();
            r.setiD_PERFIL(perfil.getId());
            r.setDsC_NOM_PERFIL(perfil.getNombre());
            r.setDsC_DESCRIPCION(perfil.getDescripcion());
            r.setCoD_EST_PERFIL(perfil.getEstado());
            r.setFeC_INGRESO(perfil.getFecIngreso());
            r.setUsR_INGRESO(perfil.getUsrIngreso());
            r.setFeC_ULT_MOD(perfil.getFecUltMod());
            r.setUsR_ULT_MOD(perfil.getUsrUltMod());
            response.put("perfil",r);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion delete de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/functionality")
    ResponseEntity<?> functionality(@RequestParam(name = "id") Integer id,
                                @RequestParam(required = false, name = "id_aplicacion", defaultValue = "") String idAplicacion){
        Map<String, Object> response = new HashMap<>();
        Integer idApp;
        if(idAplicacion.isEmpty()) idApp=0; else idApp= Integer.valueOf(idAplicacion);

        try {
            AutorizacionPerfil perfil = authPerfilService.findById(id);
            if(perfil==null){
                response.put("message", "El perfil con id [" + id + "] no se encuentra");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            perfilDTO r = new perfilDTO();
            r.setiD_PERFIL(perfil.getId());
            r.setDsC_NOM_PERFIL(perfil.getNombre());
            r.setDsC_DESCRIPCION(perfil.getDescripcion());
            r.setCoD_EST_PERFIL(perfil.getEstado());
            r.setFeC_INGRESO(perfil.getFecIngreso());
            r.setUsR_INGRESO(perfil.getUsrIngreso());
            r.setFeC_ULT_MOD(perfil.getFecUltMod());
            r.setUsR_ULT_MOD(perfil.getUsrUltMod());
            response.put("perfil",r);

            List<Object> listado = autorizacionModuloService.listaAplicaciones();
            List<apcDTO> registros = new ArrayList<>();
            Iterator<Object> it = listado != null ? listado.iterator() : null;
            while (it != null && it.hasNext()){
                Object[] col=(Object[]) it.next();
                apcDTO reg = new apcDTO();
                reg.setValue((Integer) col[0]);
                reg.setText(String.valueOf(col[1]));
                registros.add(reg);
            }
            response.put("aplicaciones",registros);

            registros = new ArrayList<>();
            List<Object> listaSel = authFuncPerfilService.listaFunciones(id,idApp);
            /*List<String> resSelect = new ArrayList<>();
            Iterator<Object> itSel = listaSel != null ? listaSel.iterator() : null;
            while (itSel != null && itSel.hasNext()){
                Object[] col=(Object[]) itSel.next();
                String dato = String.valueOf(col[0]);
                resSelect.add(dato);
            }*/

            response.put("selected",listaSel);

            List<AuthFunc> lstApps = authFuncService.listaApp(idApp);
            List<FuncPerfilRecurDTO> listaFuncionalities = new ArrayList<>();
            for(AuthFunc regA : lstApps){
                Long idNvl0 = regA.getIdFunc();
                FuncPerfilRecurDTO item = new FuncPerfilRecurDTO();
                item.setIdFunc(regA.getIdFunc());
                item.setIdFuncPadre(regA.getIdFuncPadre());
                item.setNomFunc(regA.getNomFunc());
                item.setDscFunc(regA.getDscFunc());
                item.setSecNivel(regA.getSecNivel());
                item.setSecOrden(regA.getSecOrden());
                item.setIdApp(regA.getIdApp());
                item.setRuta(regA.getRuta());
                item.setRoute(regA.getRoute());
                item.setIcon(regA.getIcon());
                item.setFlgMenu(regA.getFlgMenu());
                item.setFlgControl(regA.getFlgControl());
                item.setEstado(regA.getEstado());
                item.setUsrIngreso(regA.getUsrIngreso());
                item.setFecIngreso(regA.getFecIngreso());
                item.setUsrUltMod(regA.getUsrUltMod());
                item.setFecUltMod(regA.getFecUltMod());

                List<FuncPerfilRecurDTO> hijos = new ArrayList<>();
                List<AuthFunc> ListaHijos = authFuncService.listaApp(idApp,idNvl0);
                for(AuthFunc regB : ListaHijos){
                    Long idNvl1 = regB.getIdFunc();
                    FuncPerfilRecurDTO hijo = new FuncPerfilRecurDTO();
                    hijo.setIdFunc(regB.getIdFunc());
                    hijo.setIdFuncPadre(regB.getIdFuncPadre());
                    hijo.setNomFunc(regB.getNomFunc());
                    hijo.setDscFunc(regB.getDscFunc());
                    hijo.setSecNivel(regB.getSecNivel());
                    hijo.setSecOrden(regB.getSecOrden());
                    hijo.setIdApp(regB.getIdApp());
                    hijo.setRuta(regB.getRuta());
                    hijo.setRoute(regB.getRoute());
                    hijo.setIcon(regB.getIcon());
                    hijo.setFlgMenu(regB.getFlgMenu());
                    hijo.setFlgControl(regB.getFlgControl());
                    hijo.setEstado(regB.getEstado());
                    hijo.setUsrIngreso(regB.getUsrIngreso());
                    hijo.setFecIngreso(regB.getFecIngreso());
                    hijo.setUsrUltMod(regB.getUsrUltMod());
                    hijo.setFecUltMod(regB.getFecUltMod());

                    List<AuthFunc> ListaNietos = authFuncService.listaApp(idApp,idNvl1);
                    List<FuncPerfilRecurDTO> nietos = new ArrayList<>();
                    for(AuthFunc regC : ListaNietos){
                        FuncPerfilRecurDTO nieto = new FuncPerfilRecurDTO();
                        nieto.setIdFunc(regC.getIdFunc());
                        nieto.setIdFuncPadre(regC.getIdFuncPadre());
                        nieto.setNomFunc(regC.getNomFunc());
                        nieto.setDscFunc(regC.getDscFunc());
                        nieto.setSecNivel(regC.getSecNivel());
                        nieto.setSecOrden(regC.getSecOrden());
                        nieto.setIdApp(regC.getIdApp());
                        nieto.setRuta(regC.getRuta());
                        nieto.setRoute(regC.getRoute());
                        nieto.setIcon(regC.getIcon());
                        nieto.setFlgMenu(regC.getFlgMenu());
                        nieto.setFlgControl(regC.getFlgControl());
                        nieto.setEstado(regC.getEstado());
                        nieto.setUsrIngreso(regC.getUsrIngreso());
                        nieto.setFecIngreso(regC.getFecIngreso());
                        nieto.setUsrUltMod(regC.getUsrUltMod());
                        nieto.setFecUltMod(regC.getFecUltMod());
                        nietos.add(nieto);
                    }
                    hijo.setChilds(nietos);
                    hijos.add(hijo);
                }
                item.setChilds(hijos);
                listaFuncionalities.add(item);
            }
            response.put("funcionalidades",listaFuncionalities);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion functionality de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/functionality_store")
    ResponseEntity<?> functionality_store(@RequestParam(name = "id") Integer id,
                                          @RequestParam(name = "aplication_id") Integer idApp,
                                          @RequestParam(name = "functionalities") String[] funcs){
        Map<String, Object> response = new HashMap<>();
        AutorizacionPerfil regPerfil = authPerfilService.findById(id);
        if (regPerfil == null) {
            response.put("message", "El perfil con id [" + id + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            authFuncPerfilService.deleteFunc(id);

            Long idFuncion;
            Integer idPerfil = id;
            for(String item : funcs){
                idFuncion = Long.valueOf(item);

                //AuthFunc exist = authFuncService.buscaFuncApp(idFuncion,idApp);
                AuthFunc exist = authFuncService.buscaFuncApp(idFuncion);
                if(exist!=null) {
                    Long father = exist.getIdFuncPadre();
                    Optional<AuthFuncPerfil> app = authFuncPerfilService.busca(idPerfil, father);
                    if(app.isEmpty()){
                        if(exist.getIdFuncPadre() != null){
                            Optional<AuthFuncPerfil> flag = authFuncPerfilService.busca(idPerfil,idFuncion);
                            if(flag.isEmpty()) {
                                authFuncPerfilService.insertFunc(idPerfil, father);
                            }
                        }
                    }

                    Optional<AuthFuncPerfil> flag = authFuncPerfilService.busca(idPerfil,idFuncion);
                    if(flag.isEmpty()) {
                        authFuncPerfilService.insertFunc(idPerfil, idFuncion);
                    }
                }
            }
            response.put("message", "Registro satisfactorio");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion functionality de perfiles");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

class apcDTO{
    private String text;
    private Integer value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

class perfilDTO{
    private Integer iD_PERFIL;
    private String dsC_NOM_PERFIL;
    private String dsC_DESCRIPCION;
    private Integer coD_EST_PERFIL;
    private Date feC_INGRESO;
    private String usR_INGRESO;
    private Date feC_ULT_MOD;
    private String usR_ULT_MOD;
    private String usrPerfil;
    private String lstUsrPerfiles;

    public Integer getiD_PERFIL() {
        return iD_PERFIL;
    }

    public void setiD_PERFIL(Integer iD_PERFIL) {
        this.iD_PERFIL = iD_PERFIL;
    }

    public String getDsC_NOM_PERFIL() {
        return dsC_NOM_PERFIL;
    }

    public void setDsC_NOM_PERFIL(String dsC_NOM_PERFIL) {
        this.dsC_NOM_PERFIL = dsC_NOM_PERFIL;
    }

    public String getDsC_DESCRIPCION() {
        return dsC_DESCRIPCION;
    }

    public void setDsC_DESCRIPCION(String dsC_DESCRIPCION) {
        this.dsC_DESCRIPCION = dsC_DESCRIPCION;
    }

    public Integer getCoD_EST_PERFIL() {
        return coD_EST_PERFIL;
    }

    public void setCoD_EST_PERFIL(Integer coD_EST_PERFIL) {
        this.coD_EST_PERFIL = coD_EST_PERFIL;
    }

    public Date getFeC_INGRESO() {
        return feC_INGRESO;
    }

    public void setFeC_INGRESO(Date feC_INGRESO) {
        this.feC_INGRESO = feC_INGRESO;
    }

    public String getUsR_INGRESO() {
        return usR_INGRESO;
    }

    public void setUsR_INGRESO(String usR_INGRESO) {
        this.usR_INGRESO = usR_INGRESO;
    }

    public Date getFeC_ULT_MOD() {
        return feC_ULT_MOD;
    }

    public void setFeC_ULT_MOD(Date feC_ULT_MOD) {
        this.feC_ULT_MOD = feC_ULT_MOD;
    }

    public String getUsR_ULT_MOD() {
        return usR_ULT_MOD;
    }

    public void setUsR_ULT_MOD(String usR_ULT_MOD) {
        this.usR_ULT_MOD = usR_ULT_MOD;
    }

    public String getUsrPerfil() {
        return usrPerfil;
    }

    public void setUsrPerfil(String usrPerfil) {
        this.usrPerfil = usrPerfil;
    }

    public String getLstUsrPerfiles() {
        return lstUsrPerfiles;
    }

    public void setLstUsrPerfiles(String lstUsrPerfiles) {
        this.lstUsrPerfiles = lstUsrPerfiles;
    }


}

class FuncPerfilRecurDTO{
    private Long idFunc;
    private Long idFuncPadre;
    private String nomFunc ;
    private String dscFunc ;
    private int secNivel;
    private int secOrden;
    private int idApp;
    private String ruta;
    private String route;
    private String icon;
    private String flgMenu;
    private String flgControl;
    private int estado;
    private String usrIngreso;
    private Date fecIngreso;
    private String usrUltMod;
    private Date fecUltMod;
    private List<FuncPerfilRecurDTO> childs;

    public Long getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(Long idFunc) {
        this.idFunc = idFunc;
    }

    public Long getIdFuncPadre() {
        return idFuncPadre;
    }

    public void setIdFuncPadre(Long idFuncPadre) {
        this.idFuncPadre = idFuncPadre;
    }

    public String getNomFunc() {
        return nomFunc;
    }

    public void setNomFunc(String nomFunc) {
        this.nomFunc = nomFunc;
    }

    public String getDscFunc() {
        return dscFunc;
    }

    public void setDscFunc(String dscFunc) {
        this.dscFunc = dscFunc;
    }

    public int getSecNivel() {
        return secNivel;
    }

    public void setSecNivel(int secNivel) {
        this.secNivel = secNivel;
    }

    public int getSecOrden() {
        return secOrden;
    }

    public void setSecOrden(int secOrden) {
        this.secOrden = secOrden;
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFlgMenu() {
        return flgMenu;
    }

    public void setFlgMenu(String flgMenu) {
        this.flgMenu = flgMenu;
    }

    public String getFlgControl() {
        return flgControl;
    }

    public void setFlgControl(String flgControl) {
        this.flgControl = flgControl;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUsrIngreso() {
        return usrIngreso;
    }

    public void setUsrIngreso(String usrIngreso) {
        this.usrIngreso = usrIngreso;
    }

    public Date getFecIngreso() {
        return fecIngreso;
    }

    public void setFecIngreso(Date fecIngreso) {
        this.fecIngreso = fecIngreso;
    }

    public String getUsrUltMod() {
        return usrUltMod;
    }

    public void setUsrUltMod(String usrUltMod) {
        this.usrUltMod = usrUltMod;
    }

    public Date getFecUltMod() {
        return fecUltMod;
    }

    public void setFecUltMod(Date fecUltMod) {
        this.fecUltMod = fecUltMod;
    }

    public List<FuncPerfilRecurDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<FuncPerfilRecurDTO> childs) {
        this.childs = childs;
    }
}