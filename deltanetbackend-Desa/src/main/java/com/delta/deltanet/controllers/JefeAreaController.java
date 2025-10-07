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
@RequestMapping("/api/jefearea")
public class JefeAreaController {
    @Autowired
    private JefeAreaServiceImpl jefeAreaService;

    @Autowired
    private TelefonoServiceImpl telefonoService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;

    @Autowired
    private AreasServiceImpl areasService;

    @Autowired
    private GerenciaServiceImpl gerenciaService;

    @GetMapping("/index")
    public ResponseEntity<?> listaJefes(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<JefeArea> jefes = jefeAreaService.getAll();
            List<jefeDTO> registros = new ArrayList<>();

            for(JefeArea row: jefes){
                Long idUser = row.getIdUsuario();
                AutenticacionUsuario pNat = autenticacionUsuarioService.findById(idUser).get();
                jefeDTO reg = new jefeDTO();
                reg.setId(Math.toIntExact(row.getId()));
                reg.setId_area(row.getArea().getId());
                reg.setId_usuario(Math.toIntExact(idUser));
                reg.setEstado(row.getEstado());
                reg.setDesArea(row.getArea().getNombre());
                reg.setDesGerencia(row.getArea().getGerencia().getNombre());

                List<Telefono> fonos = telefonoService.findAllFonos(pNat.getPersona().getId());
                List<fonoDTO> regFonos = new ArrayList<>();
                for(Telefono r : fonos){
                    fonoDTO rf = new fonoDTO();
                    rf.setNumero(r.getNumero());
                    rf.setEstado(r.getEstado());
                    regFonos.add(rf);
                }
                reg.setTelefonos(regFonos);

                List<EMail> emails = emailService.findAllEmail(pNat.getPersona().getId());
                List<emailDTO> regCorreos = new ArrayList<>();
                for(EMail r : emails){
                    emailDTO re = new emailDTO();
                    re.setEmail(r.getCorreo());
                    re.setEstado(r.getEstado());
                    regCorreos.add(re);
                }
                reg.setCorreos(regCorreos);
                reg.setNombres(pNat.getPersona().getPerNat().getNombre());
                reg.setApe_paterno(pNat.getPersona().getPerNat().getApePaterno());
                reg.setApe_materno(pNat.getPersona().getPerNat().getApeMaterno());
                reg.setUsuario(row.getUsuario().getUsuario());
                registros.add(reg);
            }
            response.put("users",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de jefe area");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/index2")
    public ResponseEntity<?> listaAreas(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Areas> regAreas = areasService.getAll();
            List<areaDTO> registros = new ArrayList<>();
            for(Areas ra : regAreas){
                areaDTO reg = new areaDTO();
                reg.setDesArea(ra.getNombre());
                reg.setDesGerencia(ra.getGerencia().getNombre());
                registros.add(reg);
            }
            response.put("areas",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion list de jefe area");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> listados(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Gerencia> regGen = gerenciaService.findAll();
            List<gralDTO> gerencias = new ArrayList<>();
            for(Gerencia r : regGen){
                gralDTO reg = new gralDTO();
                reg.setValue(String.valueOf(r.getId()));
                reg.setText(r.getNombre());
                gerencias.add(reg);
            }

            List<AutenticacionUsuario> regUser = autenticacionUsuarioService.findAll();
            List<gralDTO> usuarios = new ArrayList<>();
            for(AutenticacionUsuario r : regUser){
                gralDTO reg = new gralDTO();
                reg.setValue(String.valueOf(r.getId()));
                reg.setText(r.getUsuario());
                usuarios.add(reg);
            }

            List<Areas> regAreas = areasService.getAll();
            List<gralDTO> areas = new ArrayList<>();
            for(Areas r : regAreas){
                gralDTO reg = new gralDTO();
                reg.setValue(String.valueOf(r.getId()));
                reg.setText(r.getNombre());
                areas.add(reg);
            }
            response.put("areas",areas);
            response.put("users",usuarios);
            response.put("gerencias",gerencias);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion create");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/lstAreas")
    public ResponseEntity<?> lstAreas(@RequestParam(name = "id") Integer idGerencia){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Areas> areas = areasService.getAreasGer(idGerencia);
            List<gralDTO> registros = new ArrayList<>();
            for(Areas ra : areas){
                gralDTO reg = new gralDTO();
                reg.setValue(String.valueOf(ra.getId()));
                reg.setText(ra.getNombre());
                registros.add(reg);
            }
            response.put("areas",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion create");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/lstUsers")
    public ResponseEntity<?> lstUsers(@RequestParam(name = "id") Long idArea){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> listado = autenticacionUsuarioService.buscaUsuarioIdArea(idArea);
            List<userDTO> registros = new ArrayList<>();
            Iterator<Object> it = listado != null ? listado.iterator() : null;
            while (it != null && it.hasNext()){
                Object[] col=(Object[]) it.next();
                AutenticacionUsuario au = (AutenticacionUsuario) col[0];
                PersonaNatural pn = (PersonaNatural) col[2];
                Relacion r = (Relacion) col[3];

                areaUnica rau = new areaUnica();
                rau.setId(r.getIdRel());
                rau.setId_tipo_relacion(r.getTipoRel().getIdTipoRel());
                rau.setId_persona(r.getPersona().getId());
                rau.setId_area(r.getIdArea());
                rau.setEstado(Math.toIntExact(r.getEstado()));
                rau.setCreate_user(r.getCreateUser());
                rau.setCreate_date(r.getCreateDate());
                rau.setUpdate_user(r.getUpdateUser());
                rau.setUpdate_date(r.getUpdateDate());

                userDTO reg = new userDTO();
                reg.setId(au.getId());
                reg.setNombre(pn.getNombre() + " " + pn.getApePaterno() + " " + pn.getApeMaterno());
                reg.setArea(rau);
                registros.add(reg);
            }
            response.put("users",registros);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion create");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/store")
    public ResponseEntity<?> saveJefe(@RequestParam(name = "id_usuario") Long idUser,
                                      @RequestParam(name = "id_area") Integer idArea,
                                      @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUser);
        if(usuario.isEmpty()){
            response.put("mensaje", "El id de usuario [" + idUser + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Optional<Areas> area = areasService.getById(idArea);
        if(area.isEmpty()){
            response.put("mensaje", "El id de area [" + idArea + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        JefeArea exists = jefeAreaService.buscaBoss(idUser, (long)(idArea));
        if(exists != null){
        	if(exists.getEstado() == 1) {
                response.put("mensaje", "El usuario ya es supervisor para el área seleccionada");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        	}else {
                response.put("mensaje", "El usuario ha sido supervisor para el área seleccionada y esta inactivo solo necesita activarlo");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        	}
        }

        try {
            JefeArea jefe= new JefeArea();
            jefe.setUsuario(usuario.get());
            jefe.getUsuario().getPersona().setArea(null);
            jefe.getUsuario().getPersona().setPuesto(null);
            Areas newArea = area.get();
            newArea.getGerencia().setOrgAreas(null);
            jefe.setArea(newArea);
            jefe.setEstado(1);
            jefe.setCreateUser(username);
            jefe.setCreateDate(new Date());
            jefeAreaService.save(jefe);

            response.put("item",jefe);
            response.put("message","Registro exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion store");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteJefe(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "username") String username){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        Optional<JefeArea> jefe = jefeAreaService.getById(id);
        if(jefe.isEmpty()){
            response.put("mensaje", "El id de jefe de area [" + id + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        JefeArea registro = jefe.get();
        if (registro.getEstado()==0) {
            registro.setEstado(1);
            accion = "habilitado";
        } else {
            registro.setEstado(0);
            accion = "deshabilitado";
        }

        try {
            registro.setUpdateUser(username);
            registro.setUpdateDate(new Date());
            jefeAreaService.save(registro);

            registro.getUsuario().getPersona().setArea(null);
            registro.getUsuario().getPersona().setPuesto(null);
            registro.getArea().getGerencia().setOrgAreas(null);
            response.put("functionality",registro);
            response.put("mensaje","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error inesperado en la funcion delete");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

class areaUnica{
    private Long id;
    private Long id_tipo_relacion;
    private Long id_persona;
    private Long id_area;
    private Integer estado;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String person;
    private String area;
    private String planillas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tipo_relacion() {
        return id_tipo_relacion;
    }

    public void setId_tipo_relacion(Long id_tipo_relacion) {
        this.id_tipo_relacion = id_tipo_relacion;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_area() {
        return id_area;
    }

    public void setId_area(Long id_area) {
        this.id_area = id_area;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPlanillas() {
        return planillas;
    }

    public void setPlanillas(String planillas) {
        this.planillas = planillas;
    }
}

class userDTO{
    private Long id;
    private String nombre;
    private areaUnica area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public areaUnica getArea() {
        return area;
    }

    public void setArea(areaUnica area) {
        this.area = area;
    }
}

class gralDTO{
    private String text;
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class areaDTO{
    private String desArea;
    private String desGerencia;

    public String getDesArea() {
        return desArea;
    }

    public void setDesArea(String desArea) {
        this.desArea = desArea;
    }

    public String getDesGerencia() {
        return desGerencia;
    }

    public void setDesGerencia(String desGerencia) {
        this.desGerencia = desGerencia;
    }
}

class fonoDTO{
    private String numero;
    private Integer estado;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

class emailDTO{
    private String email;
    private Long tipo;
    private Integer estado;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

	public Long getTipo() {
		return tipo;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}
    
}

class  jefeDTO{
    private Integer id;
    private Integer id_area;
    private Integer id_usuario;
    private Integer estado;
    private String desArea;
    private String desGerencia;
    private List<fonoDTO> telefonos;
    private List<emailDTO> correos;
    private String nombres;
    private String ape_paterno;
    private String ape_materno;
    private String usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_area() {
        return id_area;
    }

    public void setId_area(Integer id_area) {
        this.id_area = id_area;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getDesArea() {
        return desArea;
    }

    public void setDesArea(String desArea) {
        this.desArea = desArea;
    }

    public String getDesGerencia() {
        return desGerencia;
    }

    public void setDesGerencia(String desGerencia) {
        this.desGerencia = desGerencia;
    }

    public List<fonoDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<fonoDTO> telefonos) {
        this.telefonos = telefonos;
    }

    public List<emailDTO> getCorreos() {
        return correos;
    }

    public void setCorreos(List<emailDTO> correos) {
        this.correos = correos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApe_paterno() {
        return ape_paterno;
    }

    public void setApe_paterno(String ape_paterno) {
        this.ape_paterno = ape_paterno;
    }

    public String getApe_materno() {
        return ape_materno;
    }

    public void setApe_materno(String ape_materno) {
        this.ape_materno = ape_materno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}