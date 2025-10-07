package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/Usuario")
public class UsuarioController {
    @Autowired
    private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;

    @Autowired
    private PersonaServiceImpl personaService;

    @Autowired
    private AutenticacionClaveServiceImpl autenticacionClaveService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthFuncServiceImpl authFuncService;

    @Autowired
    private AuthFuncPerfilServiceImpl authFuncPerfilService;

    @Autowired
    private AutorizacionModuloServiceImpl autorizacionModuloService;

    @Autowired
    private AuthHomeServiceImpl authHomeService;

    @Autowired
    private JefeAreaServiceImpl jefeAreaService;

    @Autowired
    private AuthPerfilUserServiceImpl authPerfilUserService;

    @Autowired
    private AuthPerfilServiceImpl authPerfilService;

    @Value("${ruc}")
    private String ruc;

    @GetMapping("/indexOutsourcing")
    public ResponseEntity<?> getFuncionalidades(){
        List<Object> users = autenticacionUsuarioService.listaUsuarios();
        List<usuarioUserDTO> listados = new ArrayList<>();
        Iterator<Object> it = users.iterator();
        Long idAnt = 0L;
        while (it.hasNext()){
            Object[] row = (Object[]) it.next();
            AutenticacionUsuario user = (AutenticacionUsuario) row[0];
            if (!Objects.equals(user.getId(), idAnt)){
                idAnt = user.getId();
                Persona persona = (Persona) row[1];

                usuarioUserDTO r = new usuarioUserDTO();
                r.setId(user.getId());
                r.setUsuario(user.getUsuario());
                r.setEstado(user.getCodEstUsuario());
                r.setTipdoc(persona.getTipoDoc().getNombre());
                r.setNrodoc(persona.getDocumento());
                r.setNombre(persona.getPerNat().getNombre());
                r.setApepat(persona.getPerNat().getApePaterno());
                r.setApemat(persona.getPerNat().getApeMaterno());
                if (persona.getArea() != null) {
                	persona.getArea().setGerencia(null);
                	persona.getArea().setPuestos(null);
                    r.setArea(persona.getArea());
                }
                if (persona.getPuesto() != null) {
                	persona.getPuesto().setArea(null);
                    r.setPuesto(persona.getPuesto());
                }
                List<usuarioMailDTO> x = new ArrayList<>();
                for (EMail correo : persona.getCorreos()) {
                    if (correo.getEstado() == 1) {
                        usuarioMailDTO umd = new usuarioMailDTO();
                        umd.setEmail(correo.getCorreo());
                        umd.setEstado(correo.getEstado());
                        x.add(umd);
                    }
                }
                r.setCorreos(x);

                List<usuarioRelDTO> z = new ArrayList<>();
                for (Relacion item : persona.getRelaciones()) {
                    if (item.getEstado() == 1) {
                        usuarioRelDTO urd = new usuarioRelDTO();
                        urd.setEstado(item.getEstado());
                        if (persona.getArea() != null) {
                            urd.setNombre(persona.getArea().getNombre());
                        }
                        z.add(urd);
                    }
                }

                r.setRelaciones(z);

                listados.add(r);
            }

        }

        return new ResponseEntity<>(listados, HttpStatus.OK);
    }

    @GetMapping("/buscarPersonasOutsourcing")
    public ResponseEntity<?> buscarPersonasOutsourcing(
        @RequestParam(name = "tiporel", required = false) Long tiporel,
        @RequestParam(name = "tiporelstaff", required = false) Long tiporelstaff) {

        Map<String, Object> response = new HashMap<>();
        List<Long> listaUsuarios = new ArrayList<>();

        List<AutenticacionUsuario> usuarios = autenticacionUsuarioService.findAll();
        for (AutenticacionUsuario k : usuarios) {
        	if(k.getCodEstUsuario() == 1) {
                listaUsuarios.add(k.getPersona().getId());
        	}
        }

        List<Persona> personas = new ArrayList<>();
        if (tiporelstaff != null) {
            personas = personaService.listaPersonasNatStaff(tiporelstaff);
        } else if (tiporel != null) {
            personas = personaService.listaPersonasNat(tiporel);
        }
        List<usuarioPerTO> listado = new ArrayList<>();
        if(personas.size() > 0) {
	        for(Persona per : personas){
	        	if (!listaUsuarios.contains(per.getId())){
	                usuarioPerTO x = new usuarioPerTO();
	                x.setId(per.getId());
	                x.setId_tipo_persona(per.getTipoPer().getIdTipoPer());
	                x.setId_tipo_documento(per.getTipoDoc().getIdTipDoc());
	                x.setDocumento(per.getDocumento());
	                x.setTratamiento(per.getAtencion().getId());
	                x.setId_datos_persona_natural(per.getPerNat().getIdPerNat());
	                x.setEstado(per.getEstado());
	                x.setVisible(per.getVisible());
	                x.setCreate_user(per.getCreUser());
	                x.setCreate_date(per.getCreDate());
	                x.setUpdate_user(per.getUpdUser());
	                x.setUpdate_date(per.getUpdDate());
	                if(per.getPuesto() != null) {
		                per.getPuesto().setArea(null);
		                x.setPuesto(per.getPuesto());
	                }
	                if(per.getArea() != null) {
		                per.getArea().getGerencia().setOrgAreas(null);
		                per.getArea().setPuestos(null);
		                x.setArea(per.getArea());
	                }
	                usuarioTipoDocDTO tipdoc = new usuarioTipoDocDTO();
	                tipdoc.setId(per.getTipoDoc().getIdTipDoc());
	                tipdoc.setNombre(per.getTipoDoc().getNombre());
	                x.setTipoDocumento(tipdoc);
	
	                usuarioPerNatDTO pernat = new usuarioPerNatDTO();
	                pernat.setId(per.getPerNat().getIdPerNat());
	                pernat.setId_Sexo(per.getPerNat().getSex().getIdSexo());
	                pernat.setNombres(per.getPerNat().getNombre());
	                pernat.setApe_paterno(per.getPerNat().getApePaterno());
	                pernat.setApe_materno(per.getPerNat().getApeMaterno());
	                pernat.setFec_nacimiento(per.getPerNat().getFecNacim());
	                pernat.setPeso(per.getPerNat().getPeso());
	                pernat.setEstatura(per.getPerNat().getEstatura());
	                //if(per.getPerNat().getEnfermedad()!=null) pernat.setId_enfermedad(per.getPerNat().getEnfermedad().getId());
	                //if(per.getPerNat().getDiscapacidad()!=null) pernat.setId_discapacidad(per.getPerNat().getDiscapacidad().getId());
	                x.setDatosNatural(pernat);
	
	                List<usuarioRelacDTO> rela = new ArrayList<>();
	                for(Relacion item : per.getRelaciones()){
	                    usuarioRelacDTO b = new usuarioRelacDTO();
	                    b.setId(item.getIdRel());
	                    b.setId_tipo_relacion(item.getTipoRel().getIdTipoRel());
	                    b.setId_persona(item.getPersona().getId());
	                    b.setId_area(item.getIdArea());
	                    b.setEstado(item.getEstado());
	                    b.setCreate_user(item.getCreateUser());
	                    b.setCreate_date(item.getCreateDate());
	                    b.setUpdate_user(item.getUpdateUser());
	                    b.setUpdate_date(item.getUpdateDate());
	                    rela.add(b);
	                }
	                x.setRelaciones(rela);
	
	                List<usuarioEmailDTO> emails = new ArrayList<>();
	                for(EMail item : per.getCorreos()){
	                    usuarioEmailDTO d = new usuarioEmailDTO();
	                    d.setId(item.getIdEMail());
	                    d.setId_persona(item.getPersona().getId());
	                    d.setEstado(item.getEstado());
	                    d.setTipo(item.getTipo());
	                    d.setEmail(item.getCorreo());
	                    d.setCreate_user(item.getUsuCreado());
	                    d.setCreate_date(item.getFechaCreado());
	                    d.setUpdate_user(item.getUsuUpdate());
	                    d.setUpdate_date(item.getFechaUpdate());
	                    emails.add(d);
	                }
	                x.setCorreos(emails);
	
	                List<usuarioFonoDTO> fonos = new ArrayList<>();
	                for(Telefono item : per.getTelefonos()){
	                    usuarioFonoDTO t = new usuarioFonoDTO();
	                    t.setId(item.getIdTelefono());
	                    t.setId_persona(item.getPersona().getId());
	                    t.setEstado(item.getEstado());
	                    t.setTipo(item.getTipo());
	                    t.setNumero(item.getNumero());
	                    t.setCreate_user(item.getUsuCreado());
	                    t.setCreate_date(item.getFechaCreado());
	                    t.setUpdate_user(item.getUsuUpdate());
	                    t.setUpdate_date(item.getFechaUpdate());
	                    fonos.add(t);
	                }
	                x.setTelefonos(fonos);
	                listado.add(x);
	            }
	        }
        }
        response.put("persons",listado);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validateUserOutsourcing")
    public ResponseEntity<?> validateUserOutsourcing(@RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        String mensaje = "";
        try {
            AutenticacionUsuario registro = autenticacionUsuarioService.findByUsuario(usuario);
            if(registro==null) mensaje = "Usuario disponible."; else mensaje = "Usuario no disponible";
            response.put("message",mensaje);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion validateUserOutsourcing");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/detailsOutsourcing")
    public ResponseEntity<?> detailsOutsourcing(@RequestParam(name = "id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario user = autenticacionUsuarioService.buscaUserDelta2(id);
            if(user==null){
                response.put("message","Usuario no existe");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            usuarioDetalleDTO registro = new usuarioDetalleDTO();
            registro.setId(user.getId());
            registro.setUsuario(user.getUsuario());
            registro.setId_persona(user.getPersona().getId());
            registro.setSeC_PASSWORD(user.getSecPassword());
            registro.setTipO_USUARIO(user.getTipoUsuario());
            registro.setTipO_USUARIO_DELTA(user.getTipoUsuarioDelta());
            registro.setNuM_INTENTOS(user.getNumIntentos());
            registro.setFeC_INGRESO(user.getFecIngreso());
            registro.setUsR_INGRESO(user.getUsrIngreso());
            registro.setFeC_ULT_MOD(user.getFecUltMod());
            registro.setUsR_ULT_MOD(user.getUsrUltMod());
            registro.setEnroladO_SERVICE(user.getEnroladoService());

            if(user.getPersona()!=null) {
                Persona per = user.getPersona();
                usuarioPersona p = new usuarioPersona();
                p.setId(per.getId());
                p.setId_tipo_persona(per.getTipoPer().getIdTipoPer());
                p.setId_tipo_documento(per.getTipoDoc().getIdTipDoc());
                p.setDocumento(per.getDocumento());
                p.setTratamiento(per.getAtencion().getId());
                p.setId_datos_persona_natural(per.getPerNat().getIdPerNat());
                p.setEstado(per.getEstado());
                p.setVisible(per.getVisible());
                p.setCreate_user(per.getCreUser());
                p.setCreate_date(per.getCreDate());
                p.setUpdate_user(per.getUpdUser());
                p.setUpdate_date(per.getUpdDate());
                if (per.getArea() != null) {
                    per.getArea().setPuestos(null);
                    per.getArea().getGerencia().setOrgAreas(null);
                    p.setArea(per.getArea());
                }
                if (per.getPuesto() != null) {
                    per.getPuesto().setArea(null);
                    p.setPuesto(per.getPuesto());
                }
                if(per.getRelaciones()!=null){
                    List<usuarioRelac> lrel = new ArrayList<>();
                    for(Relacion itr : per.getRelaciones()) {
                        usuarioRelac ur = new usuarioRelac();
                        ur.setId(itr.getIdRel());
                        ur.setId_tipo_relacion(itr.getIdTipoRel());
                        ur.setId_persona(itr.getPersona().getId());
                        ur.setEstado(itr.getEstado());
                        ur.setCreate_user(itr.getCreateUser());
                        ur.setCreate_date(itr.getCreateDate());
                        ur.setUpdate_user(itr.getUpdateUser());
                        ur.setUpdate_date(itr.getUpdateDate());

                        if(itr.getArea()!=null){
                            usuarioAreaDTO uad = new usuarioAreaDTO();
                            Integer flgEstado = Integer.valueOf(String.valueOf(itr.getEstado()));
                            uad.setId(itr.getArea().getId());
                            uad.setId_gerencia(itr.getArea().getIdGerencia());
                            uad.setNombre(itr.getArea().getNombre());
                            uad.setEstado(flgEstado);
                            ur.setArea(uad);
                        }

                        lrel.add(ur);
                    }
                    p.setRelaciones(lrel);
                } else p.setRelaciones(Collections.emptyList());

                if(per.getTipoDoc()!=null) {
                    TipoDocumento tc = per.getTipoDoc();
                    usuarioTipoDoc tipdoc = new usuarioTipoDoc();
                    tipdoc.setId(tc.getIdTipDoc());
                    tipdoc.setNombre(tc.getNombre());
                    p.setTipoDocumento(tipdoc);
                }

                if(per.getPerNat()!=null){
                    PersonaNatural pn = per.getPerNat();
                    usuarioPerNat pernat = new usuarioPerNat();
                    pernat.setId(pn.getIdPerNat());
                    pernat.setId_sexo(pn.getSex().getIdSexo());
                    pernat.setNombres(pn.getNombre());
                    pernat.setApe_paterno(pn.getApePaterno());
                    pernat.setApe_materno(pn.getApeMaterno());
                    pernat.setFec_nacimiento(pn.getFecNacim());
                    pernat.setPeso(pn.getPeso());
                    pernat.setEstatura(pn.getEstatura());
                    //if(pn.getEnfermedad()!=null) pernat.setId_enfermedad(pn.getEnfermedad().getId());
                    //if(pn.getDiscapacidad()!=null) pernat.setId_discapacidad(pn.getDiscapacidad().getId());

                    p.setDatosNatural(pernat);
                }

                if (per.getCorreos()!=null) {
                    List<usuarioEmails> em = new ArrayList<>();
                    for(EMail correo : per.getCorreos()){
                        usuarioEmails x = new usuarioEmails();
                        x.setId(correo.getIdEMail());
                        x.setId_persona(correo.getPersona().getId());
                        x.setEstado(correo.getEstado());
                        x.setTipo(correo.getTipo());
                        x.setEmail(correo.getCorreo());
                        x.setCreate_user(correo.getUsuCreado());
                        x.setCreate_date(correo.getFechaCreado());
                        x.setUpdate_user(correo.getUsuUpdate());
                        x.setUpdate_date(correo.getFechaUpdate());

                        em.add(x);
                    }
                    p.setCorreos(em);
                }

                if (per.getTelefonos() != null) {
                    List<usuarioFonos> tf = new ArrayList<>();
                    for(Telefono fono : per.getTelefonos()){
                        usuarioFonos t = new usuarioFonos();
                        t.setId(fono.getIdTelefono());
                        t.setId_persona(fono.getPersona().getId());
                        t.setEstado(fono.getEstado());
                        t.setTipo(fono.getTipo());
                        t.setNumero(fono.getNumero());
                        t.setCreate_user(fono.getUsuCreado());
                        t.setCreate_date(fono.getFechaCreado());
                        t.setUpdate_user(fono.getUsuUpdate());
                        t.setUpdate_date(fono.getFechaUpdate());

                        tf.add(t);
                    }
                    p.setTelefonos(tf);
                }

                registro.setPerson(p);
            }

            return new ResponseEntity<>(registro, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion detailsOutsourcing");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/Autentica")
    public ResponseEntity<?> autentica(@RequestParam(name = "usuario") String usuario,
                                       @RequestParam(name = "password") String password){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario registro = autenticacionUsuarioService.findByUsuario(usuario);
            if(registro==null){
                response.put("message","Usuario: " + usuario + " no encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AutenticacionClave reg = autenticacionClaveService.buscaUserClave(registro.getId(), registro.getSecPassword());
            if(reg==null){
                response.put("message","Usuario: " + usuario + " no tiene registro de clave.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                if(reg.getDesPassword().equals(hashWith256(password))){
                    response.put("message","Usuario autenticado");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else{
                    response.put("message","Password recibido es incorrecto.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            }

        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/InfoLogin")
    public ResponseEntity<?> InfoLogin(@RequestParam(name = "usuario") String usuario,
                                       @RequestParam(name = "password") String password){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario registro = autenticacionUsuarioService.findByUsuario(usuario);
            if(registro==null){
                response.put("message","Usuario: " + usuario + " no encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AutenticacionClave reg = autenticacionClaveService.buscaUserClave(registro.getId(), registro.getSecPassword());
            if(reg==null){
                response.put("message","Usuario: " + usuario + " no tiene registro de clave.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                if(reg.getDesPassword().equals(hashWith256(password))){
                    usuarioLogin2 u = new usuarioLogin2();
                    u.setUsuario_id(registro.getId());
                    u.setUsuario(registro.getUsuario());
                    u.setSeC_PASSWORD(registro.getSecPassword());
                    u.setTipO_USUARIO_DELTA(registro.getTipoUsuarioDelta());
                    u.setId_persona(registro.getPersona().getId());
                    u.setNombres(registro.getPersona().getPerNat().getNombre());
                    u.setApe_paterno(registro.getPersona().getPerNat().getApePaterno());
                    u.setApe_materno(registro.getPersona().getPerNat().getApeMaterno());
                    u.setFec_nacimiento(registro.getPersona().getPerNat().getFecNacim());
                    u.setId_sexo(registro.getPersona().getPerNat().getSex().getIdSexo());

                    usuarioSexo s = new usuarioSexo();
                    s.setId(registro.getPersona().getPerNat().getSex().getIdSexo());
                    s.setDescripcion(registro.getPersona().getPerNat().getSex().getDescripcion());
                    s.setAcronimo(registro.getPersona().getPerNat().getSex().getAcronimo());

                    Boolean escolabout = registro.getTipoUsuarioDelta()==2;

                    response.put("Usuario",u);
                    response.put("Sexo",s);
                    response.put("Escolabout",escolabout);
                    response.put("message","Obtención de la información exitosa.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else{
                    response.put("message","Password reicibido es incorrecto.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            }

        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/RegistraHome")
    public ResponseEntity<?> RegistraHome(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "id_funcionalidad") Long idFunc){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(username);
            if(user==null){
                response.put("message","Usuario no existe");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AuthFunc funcionalidad = authFuncService.buscaFuncApp(idFunc);
            if(funcionalidad==null){
                response.put("message","No se encuentra la funcionalidad");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Optional<AuthHome> home = authHomeService.findById(user.getPersona().getId());
            if(home.isEmpty()){
                AuthHome reg = new AuthHome();
                reg.setId(user.getPersona().getId());
                reg.setIdFunc(idFunc);
                reg.setRuta(funcionalidad.getRuta());
                reg.setRoute(funcionalidad.getRoute());
                reg.setUsrIngreso("SISTEMA");
                reg.setFecIngreso(new Date());
                authHomeService.save(reg);

                response.put("mensaje","nuevo");
                response.put("dato",reg.getRoute());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                AuthHome reg = home.get();
                reg.setIdFunc(idFunc);
                reg.setRuta(funcionalidad.getRuta());
                reg.setRoute(funcionalidad.getRoute());
                reg.setUsrUltMod("SISTEMA");
                reg.setFecUltMod(new Date());
                authHomeService.save(reg);

                response.put("mensaje","ya existe");
                response.put("dato",reg.getRuta());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion RegistraHome");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/createOutsourcing")
    public ResponseEntity<?> createOutsourcing(@RequestParam(name = "usuario") String usuario,
                                               @RequestParam(name = "id_persona") Long id_persona,
                                               @RequestParam(name = "usuario_crea") String usuario_crea){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(usuario);
            if(user!=null){
                response.put("message","Ya existe un usuario con el mismo nombre de usuario.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Persona persona = personaService.buscarId(id_persona);
            if(persona==null){
                response.put("message","No se encuentra la persona con id: [" + id_persona + "].");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AutenticacionUsuario newUser = new AutenticacionUsuario();
            newUser.setPersona(persona);
            newUser.setUsuario(usuario);
            newUser.setSecPassword(1);
            newUser.setNumIntentos(0);
            newUser.setCodEstUsuario(1);
            newUser.setUsrIngreso(usuario_crea);
            newUser.setFecIngreso(new Date());
            newUser.setEnroladoService(1);
            newUser.setTipoUsuario(1);
            newUser.setTipoUsuarioDelta(2);
            autenticacionUsuarioService.save(newUser);

           AutenticacionClave pass = new AutenticacionClave();
           pass.setId(newUser.getId());
           pass.setSecPassword(1);
           pass.setDesPassword(hashWith256(persona.getDocumento()));
           pass.setUsrIngreso(usuario_crea);
           pass.setFecIngreso(new Date());
           autenticacionClaveService.save(pass);

           response.put("id_user",newUser.getId());
           response.put("message","Usuario creado exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/changeStatusOutsourcing")
    public ResponseEntity<?> changeStatusOutsourcing(@RequestParam(name = "id") Long id,
                                                     @RequestParam(name = "usuario_modifica") String usuario_modifica){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutenticacionUsuario> user = autenticacionUsuarioService.findById(id);
            if(user.isEmpty()){
                response.put("message","Usuario no existe.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            AutenticacionUsuario usuario = user.get();
            if(usuario.getCodEstUsuario()==1) usuario.setCodEstUsuario(0); else usuario.setCodEstUsuario(1);
            usuario.setUsrUltMod(usuario_modifica);
            usuario.setFecUltMod(new Date());
            autenticacionUsuarioService.save(usuario);

            response.put("message","Se cambió exítosamente el estado del usuario");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/UsuarioShow")
    public ResponseEntity<?> UsuarioShow(@RequestParam(name = "username") String username,
                                         @RequestParam(name = "password") String password,
                                         @RequestParam(name = "application") Integer application){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(username);
            if(user==null){
                response.put("error","Usuario no tiene permisos para esta aplicación");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            AutenticacionClave regPwd = autenticacionClaveService.buscaUserClave(user.getId(),user.getSecPassword());
            if(regPwd==null){
                response.put("error","Usuario: " + username + " no tiene registro de clave.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                //AutenticacionClave clave = regPwd.get();
                if(!regPwd.getDesPassword().equals(hashWith256(password))) {
                    response.put("error","Contraseña incorrecta.");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            }

            Relacion relacion = user.getPersona().getRelaciones().get(0);
            if (relacion == null) {
                response.put("error", "Usuario no tiene relación");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (user.getPersona().getArea() == null) {
                response.put("error", "Usuario no tiene area");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            usuarioDetalleDTO usuario = new usuarioDetalleDTO();
            usuario.setId(user.getId());
            usuario.setUsuario(user.getUsuario());
            usuario.setId_persona(user.getPersona().getId());
            usuario.setSeC_PASSWORD(user.getSecPassword());
            usuario.setTipO_USUARIO(user.getTipoUsuario());
            usuario.setTipO_USUARIO_DELTA(user.getTipoUsuarioDelta());
            usuario.setNuM_INTENTOS(user.getNumIntentos());
            usuario.setFeC_INGRESO(user.getFecIngreso());
            usuario.setUsR_INGRESO(user.getUsrIngreso());
            usuario.setCoD_EST_USUARIO(user.getCodEstUsuario());
            usuario.setUsR_ULT_MOD(user.getUsrUltMod());
            usuario.setFeC_ULT_MOD(user.getFecUltMod());
            usuario.setAutorizado(user.getAutorizado());
            usuario.setEnroladO_SERVICE(user.getEnroladoService());

            Optional<AuthHome> homeOPT = authHomeService.findById(user.getPersona().getId());
            AuthHome home = new AuthHome();
            if(homeOPT.isPresent()) home = homeOPT.get(); else home = null;

            List<JefeArea> jefaturas = jefeAreaService.buscaJefes(user.getPersona().getId(),1);
            List<usuarioJefe> jefes = new ArrayList<>();
            for(JefeArea item : jefaturas) {
                usuarioJefe jefe = new usuarioJefe();
                jefe.setId(item.getId());
                jefe.setId_area(item.getIdArea());
                jefe.setId_usuario(item.getIdUsuario());
                jefe.setCreate_date(item.getCreateDate());
                jefe.setCreate_user(item.getCreateUser());
                jefe.setEstado(item.getEstado());
                jefe.setUpdate_user(item.getUpdateUser());
                jefe.setUpdate_date(item.getUpdateDate());

                if(item.getArea() != null) {
                    usuarioAreaDTO v = new usuarioAreaDTO();
                    Integer idArea = item.getArea().getId();
                    v.setId(idArea);
                    v.setId_gerencia(item.getArea().getIdGerencia());
                    v.setNombre(item.getArea().getNombre());
                    v.setEstado(item.getArea().getEstado());
                    jefe.setArea(v);
                }

                jefes.add(jefe);
            }
            usuario.setJefes(jefes);

            List<Object> regPerfiles = authPerfilUserService.findPerfiles(user.getId());
            List<usuarioPerfil> listado = new ArrayList<>();
            List<Long> funcionalidades = new ArrayList<>();
            Iterator<Object> it = regPerfiles.iterator();
            while (it.hasNext()){
                Object[] row = (Object[]) it.next();
                usuarioPerfil reg = new usuarioPerfil();
                Long idPerfil = Long.valueOf(String.valueOf(row[0]));
                Long idUsuario = (Long) row[1];

                List<Long> lstFuncs = authFuncPerfilService.listaFuncionalidades((Integer) row[0]);
                for(Long d : lstFuncs){
                    Boolean existe = funcionalidades.contains(d);
                    if(!existe){
                        funcionalidades.add(d);
                    }
                }

                reg.setiD_PERFIL(idPerfil);
                reg.setiD_USUARIO(idUsuario);

                Integer idpPerfil = (Integer) row[0];
                AutorizacionPerfil detPerfil = authPerfilService.findById(idpPerfil);

                usuarioPerfilDet x = new usuarioPerfilDet();
                Long idDet = Long.valueOf(String.valueOf(detPerfil.getId()));
                x.setiD_PERFIL(idDet);
                x.setDsC_NOM_PERFIL(detPerfil.getNombre());
                x.setDsC_DESCRIPCION(detPerfil.getDescripcion());
                x.setCoD_EST_PERFIL(detPerfil.getEstado());
                x.setFeC_INGRESO(detPerfil.getFecIngreso());
                x.setUsR_INGRESO(detPerfil.getUsrIngreso());
                x.setFeC_ULT_MOD(detPerfil.getFecUltMod());
                x.setUsR_ULT_MOD(detPerfil.getUsrUltMod());
                reg.setDetPerfil(x);

                listado.add(reg);
            }
            usuario.setPerfiles(listado);

            Optional<AutorizacionModulo> aplica = autorizacionModuloService.findById(application);
            if(aplica.isEmpty()){
                usuario.setRutas(null);
                usuario.setFuncionalidades(null);
            } else {
                if (!funcionalidades.isEmpty()) {
                    List<String> routes = new ArrayList<>();
                    List<AuthFunc> lstFuncsPadre = authFuncService.listaFuncs(funcionalidades,application);
                    List<FuncDTO> funcs = new ArrayList<>();
                    for (AuthFunc it1 : lstFuncsPadre) {
                        FuncDTO f = new FuncDTO();
                        f.setIdFunc(it1.getIdFunc());
                        f.setNomFunc(it1.getNomFunc());
                        f.setDscFunc(it1.getDscFunc());
                        f.setSecNivel(it1.getSecNivel());
                        f.setSecOrden(it1.getSecOrden());
                        f.setIdApp(it1.getIdApp());
                        f.setRuta(it1.getRuta());
                        f.setRoute(it1.getRoute());
                        routes.add(it1.getRoute());
                        f.setIcon(it1.getIcon());
                        f.setFlgMenu(it1.getFlgMenu());
                        f.setFlgControl(it1.getFlgControl());
                        f.setFecIngreso(it1.getFecIngreso());
                        f.setUsrIngreso(it1.getUsrIngreso());
                        f.setFecUltMod(it1.getFecUltMod());
                        f.setUsrUltMod(it1.getUsrUltMod());
                        f.setEstado(it1.getEstado());

                        List<AuthFunc> lstFuncsHijo = authFuncService.listaFuncs(it1.getIdFunc(), funcionalidades, application);
                        List<FuncDTO> hijos = new ArrayList<>();
                        for (AuthFunc it2 : lstFuncsHijo) {
                            FuncDTO h = new FuncDTO();
                            h.setIdFunc(it2.getIdFunc());
                            h.setNomFunc(it2.getNomFunc());
                            h.setDscFunc(it2.getDscFunc());
                            h.setSecNivel(it2.getSecNivel());
                            h.setSecOrden(it2.getSecOrden());
                            h.setIdApp(it2.getIdApp());
                            h.setRuta(it2.getRuta());
                            h.setRoute(it2.getRoute());
                            routes.add(it2.getRoute());
                            h.setIcon(it2.getIcon());
                            h.setFlgMenu(it2.getFlgMenu());
                            h.setFlgControl(it2.getFlgControl());
                            h.setFecIngreso(it2.getFecIngreso());
                            h.setUsrIngreso(it2.getUsrIngreso());
                            h.setFecUltMod(it2.getFecUltMod());
                            h.setUsrUltMod(it2.getUsrUltMod());
                            h.setEstado(it2.getEstado());

                            List<AuthFunc> lstFuncsNieto = authFuncService.listaFuncs(it2.getIdFunc(), funcionalidades, application);
                            List<FuncDTO> nietos = new ArrayList<>();
                            for (AuthFunc it3 : lstFuncsNieto) {
                                FuncDTO n = new FuncDTO();
                                n.setIdFunc(it3.getIdFunc());
                                n.setNomFunc(it3.getNomFunc());
                                n.setDscFunc(it3.getDscFunc());
                                n.setSecNivel(it3.getSecNivel());
                                n.setSecOrden(it3.getSecOrden());
                                n.setIdApp(it3.getIdApp());
                                n.setRuta(it3.getRuta());
                                n.setRoute(it3.getRoute());
                                routes.add(it3.getRoute());
                                n.setIcon(it3.getIcon());
                                n.setFlgMenu(it3.getFlgMenu());
                                n.setFlgControl(it3.getFlgControl());
                                n.setFecIngreso(it3.getFecIngreso());
                                n.setUsrIngreso(it3.getUsrIngreso());
                                n.setFecUltMod(it3.getFecUltMod());
                                n.setUsrUltMod(it3.getUsrUltMod());
                                n.setEstado(it3.getEstado());
                                nietos.add(n);
                            }
                            h.setChilds(nietos);
                            hijos.add(h);
                        }
                        f.setChilds(hijos);
                        funcs.add(f);
                    }
                    usuario.setFuncionalidades(funcs);
                    usuario.setRutas(routes);
                }
            }

            Persona p = user.getPersona();
            usuarioPersona per = new usuarioPersona();
            per.setId(p.getId());
            per.setId_tipo_persona(p.getTipoPer().getIdTipoPer());
            per.setId_tipo_documento(p.getTipoDoc().getIdTipDoc());
            per.setDocumento(p.getDocumento());
            per.setTratamiento(p.getAtencion().getId());
            per.setId_datos_persona_natural(p.getPerNat().getIdPerNat());
            per.setEstado(p.getEstado());
            per.setVisible(p.getVisible());
            per.setCreate_user(p.getCreUser());
            per.setCreate_date(p.getCreDate());
            per.setUpdate_user(p.getUpdUser());
            per.setUpdate_date(p.getUpdDate());
            per.setArea(p.getArea());
            usuarioTipoDoc td = new usuarioTipoDoc();
            td.setId(p.getTipoDoc().getIdTipDoc());
            td.setNombre(p.getTipoDoc().getNombre());
            per.setTipoDocumento(td);

            usuarioPerNat pn = new usuarioPerNat();
            pn.setId(p.getPerNat().getIdPerNat());
            pn.setId_sexo(p.getPerNat().getSex().getIdSexo());
            pn.setNombres(p.getPerNat().getNombre());
            pn.setApe_paterno(p.getPerNat().getApePaterno());
            pn.setApe_materno(p.getPerNat().getApeMaterno());
            pn.setFec_nacimiento(p.getPerNat().getFecNacim());
            pn.setPeso(p.getPerNat().getPeso());
            pn.setEstatura(p.getPerNat().getEstatura());
            //if(p.getPerNat().getEnfermedad()!=null) pn.setId_enfermedad(p.getPerNat().getEnfermedad().getId());
            //if(p.getPerNat().getDiscapacidad()!=null) pn.setId_discapacidad(p.getPerNat().getDiscapacidad().getId());

            if(p.getPerNat().getSex()!=null) {
                usuarioSexo us = new usuarioSexo();
                us.setId(p.getPerNat().getSex().getIdSexo());
                us.setDescripcion(p.getPerNat().getSex().getDescripcion());
                us.setAcronimo(p.getPerNat().getSex().getAcronimo());
                pn.setDatoSexo(us);
            }
            per.setDatosNatural(pn);

            List<usuarioRelac> relaciones = new ArrayList<>();
            // Validar que el RUC no sea nulo o vacío
            if (ruc == null || ruc.isEmpty()) {
                throw new IllegalStateException("El valor del RUC no está configurado en el archivo de propiedades.");
            }
            // Buscar el ID correspondiente al RUC
            Long idNodoDestino = jdbcTemplate.queryForObject(
                "SELECT id FROM per_persona WHERE documento = ?",
                (rs, rowNum) -> rs.getLong("id"),
                ruc
            );
            if (idNodoDestino == null) {
                throw new IllegalStateException("No se encontró un ID para el RUC proporcionado.");
            }
            // Consultar las relaciones desde la base de datos
            List<Map<String, Object>> resultados = jdbcTemplate.queryForList(
                "SELECT g.* FROM grafo_enlace AS g " +
                "INNER JOIN grafo_enlace_motivo AS gm ON g.id_enlace_motivo = gm.id " +
                "WHERE g.id_nodo_origen = ? AND gm.tipo = 1 AND g.id_nodo_destino = ?",
                p.getId(), idNodoDestino
            );
            // Mapear los resultados a objetos usuarioRelac
            for (Map<String, Object> fila : resultados) {
                usuarioRelac rc = new usuarioRelac();

                // Validar y asignar valores del Map
                rc.setId(fila.get("id") != null ? ((Number) fila.get("id")).longValue() : null);
                rc.setId_tipo_relacion(fila.get("id_enlace_motivo") != null ? ((Number) fila.get("id_enlace_motivo")).longValue() : null);
                rc.setId_persona(fila.get("id_nodo_origen") != null ? ((Number) fila.get("id_nodo_origen")).longValue() : null);
                rc.setEstado(fila.get("estado") != null ? ((Number) fila.get("estado")).longValue() : null);
                rc.setCreate_user((String) fila.get("create_user"));

                // Validar y convertir fechas
                if (fila.get("create_date") != null) {
                    LocalDateTime localCreateDate = (LocalDateTime) fila.get("create_date");
                    rc.setCreate_date(Date.from(localCreateDate.atZone(ZoneId.systemDefault()).toInstant()));
                }

                if (fila.get("update_date") != null) {
                    LocalDateTime localUpdateDate = (LocalDateTime) fila.get("update_date");
                    rc.setUpdate_date(Date.from(localUpdateDate.atZone(ZoneId.systemDefault()).toInstant()));
                }

                // Si hay un área asociada, mapearla
                if (fila.get("id_area") != null) {
                    usuarioAreaDTO a = new usuarioAreaDTO();
                    a.setId(fila.get("id_area") != null ? ((Number) fila.get("id_area")).intValue() : null);
                    a.setId_gerencia(fila.get("id_gerencia") != null ? ((Number) fila.get("id_gerencia")).intValue() : null);
                    a.setNombre((String) fila.get("nombre_area"));
                    a.setEstado(fila.get("estado_area") != null ? ((Number) fila.get("estado_area")).intValue() : null);
                    rc.setArea(a);
                }
                relaciones.add(rc);
            }
            per.setRelaciones(relaciones);

            List<EMail> lstCorreos = p.getCorreos();
            List<usuarioEmails> correos = new ArrayList<>();
            for(EMail em : lstCorreos){
                usuarioEmails ue = new usuarioEmails();
                ue.setId(em.getIdEMail());
                ue.setId_persona(em.getPersona().getId());
                ue.setEstado(em.getEstado());
                ue.setTipo(em.getTipo());
                ue.setEmail(em.getCorreo());
                ue.setCreate_user(em.getUsuCreado());
                ue.setCreate_date(em.getFechaCreado());
                ue.setUpdate_user(em.getUsuUpdate());
                ue.setUpdate_date(em.getFechaUpdate());
                correos.add(ue);
            }
            per.setCorreos(correos);

            List<Telefono> lstFonos = p.getTelefonos();
            List<usuarioFonos> fonos = new ArrayList<>();
            for(Telefono tel : lstFonos){
                usuarioFonos uf = new usuarioFonos();
                uf.setId(tel.getIdTelefono());
                uf.setId_persona(tel.getPersona().getId());
                uf.setEstado(tel.getEstado());
                uf.setTipo(tel.getTipo());
                uf.setNumero(tel.getNumero());
                uf.setCreate_user(tel.getUsuCreado());
                uf.setCreate_date(tel.getFechaCreado());
                uf.setUpdate_user(tel.getUsuUpdate());
                uf.setUpdate_date(tel.getFechaUpdate());
                fonos.add(uf);
            }
            per.setTelefonos(fonos);
            usuario.setPerson(per);
            response.put("user",usuario);
            response.put("home",home);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    //authFuncPerfilService
    @GetMapping("/profilesOutsourcing")
    public ResponseEntity<?> profilesOutsourcing(@RequestParam(name = "id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            List<AutorizacionPerfil> perfiles = authPerfilService.findByEstado(1);
            List<usuarioPerfilDet> lista = new ArrayList<>();
            for(AutorizacionPerfil perfil : perfiles){
                usuarioPerfilDet ap = new usuarioPerfilDet();
                Long idPerf= Long.valueOf(String.valueOf(perfil.getId()));
                ap.setiD_PERFIL(idPerf);
                ap.setDsC_DESCRIPCION(perfil.getDescripcion());
                ap.setDsC_NOM_PERFIL(perfil.getNombre());
                ap.setCoD_EST_PERFIL(perfil.getEstado());
                ap.setFeC_INGRESO(perfil.getFecIngreso());
                ap.setUsR_INGRESO(perfil.getUsrIngreso());
                ap.setFeC_ULT_MOD(perfil.getFecUltMod());
                ap.setUsrPerfil(perfil.getUsrUltMod());
                lista.add(ap);
            }
            response.put("profiles",lista);

            List<Integer> lst = authPerfilUserService.findListIdPerfil(id);

            response.put("usedProfiles",lst);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/saveProfilesOutsourcing")
    public ResponseEntity<?> saveProfilesOutsourcing(@RequestParam(name = "id") Long id,
                                                     @RequestParam(name = "profiles") String[] profiles){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutenticacionUsuario>user = autenticacionUsuarioService.findById(id);
            if(user.isEmpty()){
                response.put("message","Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Integer idUser = Integer.valueOf(String.valueOf(id));
            authPerfilUserService.delete(idUser);

            for(String item : profiles){
                Integer idPerfil = Integer.valueOf(item);
                authPerfilUserService.insert(id,idPerfil);
            }

            response.put("message","Se registraron correctamente los perfiles del usuario");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion autentica");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam(name = "id") Long id,
                                            @RequestParam(name = "password") String password){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AutenticacionUsuario> user = autenticacionUsuarioService.findById(id);
            if(user.isEmpty()){
                response.put("message","El usuario con id [" + id + "] no encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            AutenticacionUsuario us = user.get();
            Integer secuencia = us.getSecPassword() + 1;
            us.setSecPassword(secuencia);

            usuarioAuth ul = new usuarioAuth();
            ul.setId(us.getId());
            ul.setUsuario(us.getUsuario());
            ul.setId_persona(us.getPersona().getId());
            ul.setSeC_PASSWORD(secuencia);
            ul.setTipO_USUARIO(us.getTipoUsuario());
            ul.setTipO_USUARIO_DELTA(us.getTipoUsuarioDelta());
            ul.setNuM_INTENTOS(us.getNumIntentos());
            ul.setFeC_INGRESO(us.getFecIngreso());
            ul.setUsR_INGRESO(us.getUsrIngreso());
            ul.setCoD_EST_USUARIO(us.getCodEstUsuario());
            ul.setUsR_ULT_MOD(us.getUsrUltMod());
            ul.setFeC_ULT_MOD(us.getFecUltMod());
            ul.setAutorizado(us.getAutorizado());
            ul.setEnroladO_SERVICE(us.getEnroladoService());



            usuarioPerNat pn = new usuarioPerNat();
            pn.setId(us.getPersona().getId());
            pn.setId_sexo(us.getPersona().getPerNat().getSex().getIdSexo());
            pn.setNombres(us.getPersona().getPerNat().getNombre());
            pn.setApe_paterno(us.getPersona().getPerNat().getApePaterno());
            pn.setApe_paterno(us.getPersona().getPerNat().getApeMaterno());
            pn.setFec_nacimiento(us.getPersona().getPerNat().getFecNacim());

            String mensaje = us.getPersona().getPerNat().getNombre() + " " +
                           us.getPersona().getPerNat().getApePaterno() + " " +
                          us.getPersona().getPerNat().getApeMaterno() + ", se ha actualizado correctamente su contraseña.\n Por seguridad se recomienda mantener no visible su contraseña con terceros";

            usuarioSexo sx = new usuarioSexo();
            sx.setId(us.getPersona().getPerNat().getSex().getIdSexo());
            sx.setDescripcion(us.getPersona().getPerNat().getSex().getDescripcion());
            sx.setAcronimo(us.getPersona().getPerNat().getSex().getAcronimo());
            pn.setDatoSexo(sx);

            List<usuarioEmailDTO> lstCorreo = new ArrayList<>();
            String correoElectronico = "";
            for(EMail em : us.getPersona().getCorreos()){
                if(em.getTipo() == 1) {
                    usuarioEmailDTO correo = new usuarioEmailDTO();
                    correo.setId(em.getIdEMail());
                    correo.setId_persona(em.getPersona().getId());
                    correo.setEstado(em.getEstado());
                    correo.setTipo(em.getTipo());
                    correo.setEmail(em.getCorreo());
                    correoElectronico = em.getCorreo();
                    correo.setCreate_user(em.getUsuCreado());
                    correo.setCreate_date(em.getFechaCreado());
                    correo.setUpdate_user(em.getUsuUpdate());
                    correo.setUpdate_date(em.getFechaUpdate());
                    lstCorreo.add(correo);
                }
            }

            if(lstCorreo.isEmpty()){
                response.put("message","El correo no exise");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            autenticacionUsuarioService.save(us);
            autenticacionClaveService.insert(id,secuencia,hashWith256(password),"ChangePassword",new Date());

            response.put("mensaje","Contraseña por defecto cambiada.");
            response.put("data",ul);
            response.put("titulo","Cambiar contraseña por defecto");
            response.put("message",mensaje);
            response.put("correo",correoElectronico);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion changePassword");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam(name = "username") String username,
                                            @RequestParam(name = "email") String email){
        Map<String, Object> response = new HashMap<>();
        try {
            AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(username);
            if(user == null){
                response.put("message","El usuario [" + username + "] no encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            user.setSecPassword(1);
            user.setUsrIngreso("ResetPassword");
            user.setFecIngreso(new Date());
            user.setUsrUltMod("ResetPassword");
            user.setFecUltMod(new Date());
            autenticacionUsuarioService.save(user);

            autenticacionClaveService.delete(user.getId());
            AutenticacionClave clave = new AutenticacionClave();
            clave.setId(user.getId());
            clave.setSecPassword(1);
            clave.setDesPassword(hashWith256(user.getPersona().getDocumento()));
            clave.setUsrIngreso("ResetPassword");
            clave.setFecIngreso(new Date());
            autenticacionClaveService.save(clave);

            String mensaje = user.getPersona().getPerNat().getNombre() + " " +
                    user.getPersona().getPerNat().getApePaterno() + " " +
                    user.getPersona().getPerNat().getApeMaterno() + ", se ha reiniciado correctamente su contraseña.\n Utilizar su número de documento de identidad como contraseña default. \\n Por seguridad se recomienda cambiar inmediatamente su contraseña actual.";

            response.put("titulo","Reiniciar Contraseña");
            response.put("message",mensaje);
            response.put("mensaje","Contraseña por defecto cambiada.");
            response.put("correo",email);
            response.put("data",user);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion changePassword");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private String hashWith256(String textToHash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
        byte[] hashedByetArray = digest.digest(byteOfTextToHash);
        String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
        return encoded;
    }

    @GetMapping("/buscarRelacionesDetalladas")
    public ResponseEntity<?> buscarRelacionesDetalladas(
            @RequestParam(name = "idPersona") Long idPersona) {

        try {
            // Consulta SQL para obtener las relaciones
            String query = "SELECT g.* FROM grafo_enlace AS g " +
                        "INNER JOIN grafo_enlace_motivo AS gem ON gem.id = g.id_enlace_motivo " +
                        "WHERE g.id_nodo_destino = ? AND gem.tipo = 3;";

            // Ejecutar la consulta
            List<Map<String, Object>> relaciones = jdbcTemplate.queryForList(query, idPersona);

            // Procesar los resultados
            List<Map<String, Object>> respuesta = new ArrayList<>();
            for (Map<String, Object> fila : relaciones) {
                Map<String, Object> detalle = new HashMap<>();

                // Mapear los campos de grafo_enlace
                detalle.put("id", fila.get("id") != null ? ((Number) fila.get("id")).longValue() : null);
                detalle.put("idReverse", fila.get("id_reverse") != null ? ((Number) fila.get("id_reverse")).longValue() : null);
                detalle.put("idNodoOrigen", fila.get("id_nodo_origen") != null ? ((Number) fila.get("id_nodo_origen")).longValue() : null);
                detalle.put("idNodoDestino", fila.get("id_nodo_destino") != null ? ((Number) fila.get("id_nodo_destino")).longValue() : null);
                detalle.put("idMotivoTipo", fila.get("id_enlace_motivo_tipo") != null ? ((Number) fila.get("id_enlace_motivo_tipo")).longValue() : null);
                detalle.put("idMotivo", fila.get("id_enlace_motivo") != null ? ((Number) fila.get("id_enlace_motivo")).longValue() : null);
                detalle.put("idArea", fila.get("id_area") != null ? ((Number) fila.get("id_area")).longValue() : null);
                detalle.put("idCargo", fila.get("id_cargo") != null ? ((Number) fila.get("id_cargo")).longValue() : null);
                detalle.put("fecIni", fila.get("fec_inicio"));
                detalle.put("fecFin", fila.get("fec_fin"));
                detalle.put("estado", ((Number) fila.get("estado")).intValue());
                detalle.put("createUser", fila.get("create_user"));
                detalle.put("createDate", fila.get("create_date"));
                detalle.put("updateUser", fila.get("update_user"));
                detalle.put("updateDate", fila.get("update_date"));

                // Obtener la información de id_nodo_origen
                Long idNodoOrigen = ((Number) fila.get("id_nodo_origen")).longValue();
                if (idNodoOrigen != null) {
                    Map<String, Object> infoNodoOrigen = obtenerInfoNodoOrigen(idNodoOrigen);
                    detalle.put("infoNodoOrigen", infoNodoOrigen);
                }

                // Obtener la información de id_enlace_motivo
                Long idMotivo = ((Number) fila.get("id_enlace_motivo")).longValue();
                if (idMotivo != null) {
                    Map<String, Object> detalleMotivo = obtenerDetalleMotivo(idMotivo);
                    detalle.put("detalleMotivo", detalleMotivo);
                }

                respuesta.add(detalle);
            }

            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al buscar relaciones detalladas");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> obtenerDetalleMotivo(Long idMotivo) {
        String queryMotivo = "SELECT * FROM grafo_enlace_motivo WHERE id = ?";
        return jdbcTemplate.queryForObject(queryMotivo, new Object[]{idMotivo}, (rs, rowNum) -> {
            Map<String, Object> detalleMotivo = new HashMap<>();
            detalleMotivo.put("id", rs.getLong("id"));
            detalleMotivo.put("nodoTipoOrigenId", rs.getLong("nodo_tipo_origen_id"));
            detalleMotivo.put("nodoTipoDestinoId", rs.getLong("nodo_tipo_destino_id"));
            detalleMotivo.put("relacionTipoRegresoId", rs.getLong("relacion_tipo_regreso_id"));
            detalleMotivo.put("tipo", rs.getInt("tipo"));
            detalleMotivo.put("nombre", rs.getString("nombre"));
            detalleMotivo.put("estado", rs.getInt("estado"));
            detalleMotivo.put("createUser", rs.getString("create_user"));
            detalleMotivo.put("createDate", rs.getTimestamp("create_date"));
            detalleMotivo.put("updateUser", rs.getString("update_user"));
            detalleMotivo.put("updateDate", rs.getTimestamp("update_date"));
            return detalleMotivo;
        });
    }

    private Map<String, Object> obtenerInfoNodoOrigen(Long idNodoOrigen) {
        String queryPersona = "SELECT * FROM per_persona WHERE id = ?";
        return jdbcTemplate.queryForObject(queryPersona, new Object[]{idNodoOrigen}, (rs, rowNum) -> {
            Map<String, Object> infoNodoOrigen = new HashMap<>();
            infoNodoOrigen.put("id", rs.getLong("id"));
            infoNodoOrigen.put("idTipoDocumento", rs.getLong("id_tipo_documento"));
            infoNodoOrigen.put("documento", rs.getString("documento"));
            infoNodoOrigen.put("estado", rs.getInt("estado"));
            infoNodoOrigen.put("visible", rs.getInt("visible"));
            infoNodoOrigen.put("createUser", rs.getString("create_user"));
            infoNodoOrigen.put("createDate", rs.getTimestamp("create_date"));
            infoNodoOrigen.put("updateUser", rs.getString("update_user"));
            infoNodoOrigen.put("updateDate", rs.getTimestamp("update_date"));

            // Obtener el tipo de documento
            Long idTipoDoc = rs.getLong("id_tipo_documento");
            String queryTipoDoc = "SELECT * FROM per_persona_documento_tipo WHERE id = ?";
            if (idTipoDoc != null) {
                Map<String, Object> tipoDoc = jdbcTemplate.queryForObject(queryTipoDoc, new Object[]{idTipoDoc}, (rsTipoDoc, rowNumTipoDoc) -> {
                    Map<String, Object> tipoDocumento = new HashMap<>();
                    tipoDocumento.put("id", rsTipoDoc.getLong("id"));
                    tipoDocumento.put("nombre", rsTipoDoc.getString("nombre"));
                    tipoDocumento.put("estado", rsTipoDoc.getInt("estado"));
                    return tipoDocumento;
                });
                infoNodoOrigen.put("tipoDocumento", tipoDoc);
            }

            // Verificar si es PersonaNatural o PersonaJuridica
            Long idPerNat = rs.getObject("id_datos_persona_natural", Long.class);
            Long idPerJur = rs.getObject("id_datos_persona_juridica", Long.class);

            if (idPerNat != null && idPerNat > 0) {
                String queryPerNat = "SELECT * FROM per_nat WHERE id = ?";
                Map<String, Object> perNat = jdbcTemplate.queryForObject(queryPerNat, new Object[]{idPerNat}, (rsNat, rowNumNat) -> {
                    Map<String, Object> personaNatural = new HashMap<>();
                    personaNatural.put("id", rsNat.getLong("id"));
                    personaNatural.put("nombres", rsNat.getString("nombres"));
                    personaNatural.put("apePaterno", rsNat.getString("ape_paterno"));
                    personaNatural.put("apeMaterno", rsNat.getString("ape_materno"));
                    personaNatural.put("fecNacimiento", rsNat.getDate("fec_nacimiento"));
                    return personaNatural;
                });
                infoNodoOrigen.put("personaNatural", perNat);
            } else if (idPerJur != null && idPerJur > 0) {
                String queryPerJur = "SELECT * FROM per_jur WHERE id = ?";
                Map<String, Object> perJur = jdbcTemplate.queryForObject(queryPerJur, new Object[]{idPerJur}, (rsJur, rowNumJur) -> {
                    Map<String, Object> personaJuridica = new HashMap<>();
                    personaJuridica.put("id", rsJur.getLong("id"));
                    personaJuridica.put("razonSocial", rsJur.getString("razon_social"));
                    personaJuridica.put("razonComercial", rsJur.getString("razon_comercial"));
                    personaJuridica.put("fecInicioOper", rsJur.getDate("fec_inicio_oper"));
                    return personaJuridica;
                });
                infoNodoOrigen.put("personaJuridica", perJur);
            }

            return infoNodoOrigen;
        });
    }

}

class usuarioTipoDocDTO{
    private Long id;
    private String nombre;

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
}

class usuarioPerNatDTO{
    private Long id;
    private Long id_Sexo;
    private String nombres;
    private String ape_paterno;
    private String ape_materno;
    private Date fec_nacimiento;
    private Double peso;
    private Integer estatura;
    private Integer id_enfermedad;
    private Integer id_discapacidad;
    private Integer tratamiento;
    private String datoSexo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_Sexo() {
        return id_Sexo;
    }

    public void setId_Sexo(Long id_Sexo) {
        this.id_Sexo = id_Sexo;
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

    public Date getFec_nacimiento() {
        return fec_nacimiento;
    }

    public void setFec_nacimiento(Date fec_nacimiento) {
        this.fec_nacimiento = fec_nacimiento;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getEstatura() {
        return estatura;
    }

    public void setEstatura(Integer estatura) {
        this.estatura = estatura;
    }

    public Integer getId_enfermedad() {
        return id_enfermedad;
    }

    public void setId_enfermedad(Integer id_enfermedad) {
        this.id_enfermedad = id_enfermedad;
    }

    public Integer getId_discapacidad() {
        return id_discapacidad;
    }

    public void setId_discapacidad(Integer id_discapacidad) {
        this.id_discapacidad = id_discapacidad;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDatoSexo() {
        return datoSexo;
    }

    public void setDatoSexo(String datoSexo) {
        this.datoSexo = datoSexo;
    }
}

class usuarioAreaDTO{
    private Integer id;
    private Integer id_gerencia;
    private String nombre;
    private Integer estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_gerencia() {
        return id_gerencia;
    }

    public void setId_gerencia(Integer id_gerencia) {
        this.id_gerencia = id_gerencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

class usuarioRelacDTO{
    private Long id;
    private Long id_tipo_relacion;
    private Long id_persona;
    private Long id_area;
    private Long estado;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String person;
    private usuarioAreaDTO area;

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

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
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

    public usuarioAreaDTO getArea() {
        return area;
    }

    public void setArea(usuarioAreaDTO area) {
        this.area = area;
    }
}

class usuarioEmailDTO{
    private Long id;
    private Long id_persona;
    private Long id_contacto;
    private Integer estado;
    private Long tipo;
    private String email;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(Long id_contacto) {
        this.id_contacto = id_contacto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

class usuarioFonoDTO{
    private Long id;
    private Long id_persona;
    private Long id_contacto;
    private Integer estado;
    private Long tipo;
    private String numero;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(Long id_contacto) {
        this.id_contacto = id_contacto;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
}

class usuarioPerTO{
    private Long id;
    private Long id_tipo_persona;
    private long id_tipo_documento;
    private String documento;
    private Integer tratamiento;
    private Long id_datos_persona_natural;
    private Long id_datos_persona_juridica;
    private Integer estado;
    private Integer visible;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String user;
    private OrgAreas area;
    private Puestos puesto;
    private usuarioTipoDocDTO tipoDocumento;
    private usuarioPerNatDTO datosNatural;
    private usuarioPerNatDTO datosJuridica;
    private List<usuarioRelacDTO> relaciones;
    private List<usuarioEmailDTO> correos;
    private List<usuarioFonoDTO> telefonos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tipo_persona() {
        return id_tipo_persona;
    }

    public void setId_tipo_persona(Long id_tipo_persona) {
        this.id_tipo_persona = id_tipo_persona;
    }

    public long getId_tipo_documento() {
        return id_tipo_documento;
    }

    public void setId_tipo_documento(long id_tipo_documento) {
        this.id_tipo_documento = id_tipo_documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Long getId_datos_persona_natural() {
        return id_datos_persona_natural;
    }

    public void setId_datos_persona_natural(Long id_datos_persona_natural) {
        this.id_datos_persona_natural = id_datos_persona_natural;
    }

    public Long getId_datos_persona_juridica() {
        return id_datos_persona_juridica;
    }

    public void setId_datos_persona_juridica(Long id_datos_persona_juridica) {
        this.id_datos_persona_juridica = id_datos_persona_juridica;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public usuarioTipoDocDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(usuarioTipoDocDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public usuarioPerNatDTO getDatosNatural() {
        return datosNatural;
    }

    public void setDatosNatural(usuarioPerNatDTO datosNatural) {
        this.datosNatural = datosNatural;
    }

    public usuarioPerNatDTO getDatosJuridica() {
        return datosJuridica;
    }

    public void setDatosJuridica(usuarioPerNatDTO datosJuridica) {
        this.datosJuridica = datosJuridica;
    }

    public List<usuarioRelacDTO> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<usuarioRelacDTO> relaciones) {
        this.relaciones = relaciones;
    }

    public List<usuarioEmailDTO> getCorreos() {
        return correos;
    }

    public void setCorreos(List<usuarioEmailDTO> correos) {
        this.correos = correos;
    }

    public List<usuarioFonoDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<usuarioFonoDTO> telefonos) {
        this.telefonos = telefonos;
    }

	public OrgAreas getArea() {
		return area;
	}

	public void setArea(OrgAreas area) {
		this.area = area;
	}

	public Puestos getPuesto() {
		return puesto;
	}

	public void setPuesto(Puestos puesto) {
		this.puesto = puesto;
	}
    
}

class usuarioMailDTO{
    private String email;
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
}

class usuarioRelDTO{
    private String nombre;
    private Long estado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }
}

class usuarioUserDTO{
    private Long id;
    private String usuario;
    private Integer estado;
    private String tipdoc;
    private String nrodoc;
    private String nombre;
    private String apepat;
    private String apemat;
    private OrgAreas area;
    private Puestos puesto;
    private List<usuarioMailDTO> correos;
    private List<usuarioRelDTO> relaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getTipdoc() {
        return tipdoc;
    }

    public void setTipdoc(String tipdoc) {
        this.tipdoc = tipdoc;
    }

    public String getNrodoc() {
        return nrodoc;
    }

    public void setNrodoc(String nrodoc) {
        this.nrodoc = nrodoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApepat() {
        return apepat;
    }

    public void setApepat(String apepat) {
        this.apepat = apepat;
    }

    public String getApemat() {
        return apemat;
    }

    public void setApemat(String apemat) {
        this.apemat = apemat;
    }

    public List<usuarioMailDTO> getCorreos() {
        return correos;
    }

    public void setCorreos(List<usuarioMailDTO> correos) {
        this.correos = correos;
    }

    public List<usuarioRelDTO> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<usuarioRelDTO> relaciones) {
        this.relaciones = relaciones;
    }

	public OrgAreas getArea() {
		return area;
	}

	public void setArea(OrgAreas area) {
		this.area = area;
	}

	public Puestos getPuesto() {
		return puesto;
	}

	public void setPuesto(Puestos puesto) {
		this.puesto = puesto;
	}

}

class usuarioTipoDoc{
    private Long id;
    private String nombre;

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
}

class usuarioPerNat{
    private Long id;
    private Long id_sexo;
    private String nombres;
    private String ape_paterno;
    private String ape_materno;
    private Date fec_nacimiento;
    private Double peso;
    private Integer estatura;
    private Integer id_enfermedad;
    private Integer id_discapacidad;
    private Integer tratamiento;
    private usuarioSexo datoSexo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_sexo() {
        return id_sexo;
    }

    public void setId_sexo(Long id_sexo) {
        this.id_sexo = id_sexo;
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

    public Date getFec_nacimiento() {
        return fec_nacimiento;
    }

    public void setFec_nacimiento(Date fec_nacimiento) {
        this.fec_nacimiento = fec_nacimiento;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getEstatura() {
        return estatura;
    }

    public void setEstatura(Integer estatura) {
        this.estatura = estatura;
    }

    public Integer getId_enfermedad() {
        return id_enfermedad;
    }

    public void setId_enfermedad(Integer id_enfermedad) {
        this.id_enfermedad = id_enfermedad;
    }

    public Integer getId_discapacidad() {
        return id_discapacidad;
    }

    public void setId_discapacidad(Integer id_discapacidad) {
        this.id_discapacidad = id_discapacidad;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public usuarioSexo getDatoSexo() {
        return datoSexo;
    }

    public void setDatoSexo(usuarioSexo datoSexo) {
        this.datoSexo = datoSexo;
    }
}

class usuarioEmails{
    private Long id;
    private Long id_persona;
    private Long id_contacto;
    private Integer estado;
    private Long tipo;
    private String email;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(Long id_contacto) {
        this.id_contacto = id_contacto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

class usuarioFonos{
    private Long id;
    private Long id_persona;
    private Long id_contacto;
    private Integer estado;
    private Long tipo;
    private String numero;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(Long id_contacto) {
        this.id_contacto = id_contacto;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
}

class usuarioPersona{
    private Long id;
    private Long id_tipo_persona;
    private Long id_tipo_documento;
    private String documento;
    private Integer tratamiento;
    private Long id_datos_persona_natural;
    private Long id_datos_persona_juridica;
    private Integer estado;
    private Integer visible;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private Puestos puesto;
    private OrgAreas area;
    private usuarioTipoDoc tipoDocumento;
    private usuarioPerNat datosNatural;
    private usuarioPerNat datosJuridica;
    private List<usuarioRelac> relaciones;
    private List<usuarioEmails> correos;
    private List<usuarioFonos> telefonos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tipo_persona() {
        return id_tipo_persona;
    }

    public void setId_tipo_persona(Long id_tipo_persona) {
        this.id_tipo_persona = id_tipo_persona;
    }

    public Long getId_tipo_documento() {
        return id_tipo_documento;
    }

    public void setId_tipo_documento(Long id_tipo_documento) {
        this.id_tipo_documento = id_tipo_documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Long getId_datos_persona_natural() {
        return id_datos_persona_natural;
    }

    public void setId_datos_persona_natural(Long id_datos_persona_natural) {
        this.id_datos_persona_natural = id_datos_persona_natural;
    }

    public Long getId_datos_persona_juridica() {
        return id_datos_persona_juridica;
    }

    public void setId_datos_persona_juridica(Long id_datos_persona_juridica) {
        this.id_datos_persona_juridica = id_datos_persona_juridica;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
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

    public usuarioTipoDoc getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(usuarioTipoDoc tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public usuarioPerNat getDatosNatural() {
        return datosNatural;
    }

    public void setDatosNatural(usuarioPerNat datosNatural) {
        this.datosNatural = datosNatural;
    }

    public usuarioPerNat getDatosJuridica() {
        return datosJuridica;
    }

    public void setDatosJuridica(usuarioPerNat datosJuridica) {
        this.datosJuridica = datosJuridica;
    }

    public List<usuarioRelac> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<usuarioRelac> relaciones) {
        this.relaciones = relaciones;
    }

    public List<usuarioEmails> getCorreos() {
        return correos;
    }

    public void setCorreos(List<usuarioEmails> correos) {
        this.correos = correos;
    }

    public List<usuarioFonos> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<usuarioFonos> telefonos) {
        this.telefonos = telefonos;
    }

	public Puestos getPuesto() {
		return puesto;
	}

	public void setPuesto(Puestos puesto) {
		this.puesto = puesto;
	}

	public OrgAreas getArea() {
		return area;
	}

	public void setArea(OrgAreas area) {
		this.area = area;
	}
    
}

class usuarioRol{
    private Long iD_ROL;
    private String descripcion;
    private Integer estado;
    private String usR_INGRESO;
    private Date feC_INGRESO;
    private String usR_ULT_MOD;
    private Date feC_ULT_MOD;
    private String[] users;

    public Long getiD_ROL() {
        return iD_ROL;
    }

    public void setiD_ROL(Long iD_ROL) {
        this.iD_ROL = iD_ROL;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }
}

class usuarioPerfilLst{
    private Long iD_USUARIO;
    private Integer iD_PERFIL;

    public Long getiD_USUARIO() {
        return iD_USUARIO;
    }

    public void setiD_USUARIO(Long iD_USUARIO) {
        this.iD_USUARIO = iD_USUARIO;
    }

    public Integer getiD_PERFIL() {
        return iD_PERFIL;
    }

    public void setiD_PERFIL(Integer iD_PERFIL) {
        this.iD_PERFIL = iD_PERFIL;
    }
}

class usuarioPerfilDet{
    private Long iD_PERFIL;
    private String dsC_NOM_PERFIL;
    private String dsC_DESCRIPCION;
    private Integer coD_EST_PERFIL;
    private Date feC_INGRESO;
    private String usR_INGRESO;
    private Date feC_ULT_MOD;
    private String usR_ULT_MOD;
    private String usrPerfil;
    private List<usuarioPerfilLst> lstUsrPerfiles;

    public Long getiD_PERFIL() {
        return iD_PERFIL;
    }

    public void setiD_PERFIL(Long iD_PERFIL) {
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

    public List<usuarioPerfilLst> getLstUsrPerfiles() {
        return lstUsrPerfiles;
    }

    public void setLstUsrPerfiles(List<usuarioPerfilLst> lstUsrPerfiles) {
        this.lstUsrPerfiles = lstUsrPerfiles;
    }
}

class usuarioLogin{
    private Long id;
    private String usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

class usuarioPerfil{
    private Long iD_USUARIO;
    private Long iD_PERFIL;
    private usuarioPerfilDet detPerfil;
    private usuarioLogin usuario;

    public Long getiD_USUARIO() {
        return iD_USUARIO;
    }

    public void setiD_USUARIO(Long iD_USUARIO) {
        this.iD_USUARIO = iD_USUARIO;
    }

    public Long getiD_PERFIL() {
        return iD_PERFIL;
    }

    public void setiD_PERFIL(Long iD_PERFIL) {
        this.iD_PERFIL = iD_PERFIL;
    }

    public usuarioPerfilDet getDetPerfil() {
        return detPerfil;
    }

    public void setDetPerfil(usuarioPerfilDet detPerfil) {
        this.detPerfil = detPerfil;
    }

    public usuarioLogin getUsuario() {
        return usuario;
    }

    public void setUsuario(usuarioLogin usuario) {
        this.usuario = usuario;
    }
}

class usuarioJefe{
    private Long id;
    private Long id_area;
    private Long id_usuario;
    private Date create_date;
    private String create_user;
    private Integer estado;
    private Date update_date;
    private String update_user;
    private String user;
    private usuarioAreaDTO area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_area() {
        return id_area;
    }

    public void setId_area(Long id_area) {
        this.id_area = id_area;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public usuarioAreaDTO getArea() {
        return area;
    }

    public void setArea(usuarioAreaDTO area) {
        this.area = area;
    }
}

class usuarioDetalleDTO{
    private Long id;
    private String usuario;
    private Long id_persona;
    private Integer seC_PASSWORD;
    private Integer tipO_USUARIO;
    private Integer tipO_USUARIO_DELTA;
    private Integer nuM_INTENTOS;
    private Date feC_INGRESO;
    private String usR_INGRESO;
    private Integer coD_EST_USUARIO;
    private String usR_ULT_MOD;
    private Date feC_ULT_MOD;
    private String usR_SERVICIO;
    private Long iD_ROL;
    private Integer autorizado;
    private Integer enroladO_SERVICE;
    private Integer iD_USR_SERVICIO;
    private String password;
    private List<String> rutas;
    private List<FuncDTO> funcionalidades;
    private usuarioPersona person;
    private String typeUser;
    private usuarioRol rol;
    private List<usuarioJefe> jefes;
    private String tickets;
    private List<usuarioPerfil> perfiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Integer getSeC_PASSWORD() {
        return seC_PASSWORD;
    }

    public void setSeC_PASSWORD(Integer seC_PASSWORD) {
        this.seC_PASSWORD = seC_PASSWORD;
    }

    public Integer getTipO_USUARIO() {
        return tipO_USUARIO;
    }

    public void setTipO_USUARIO(Integer tipO_USUARIO) {
        this.tipO_USUARIO = tipO_USUARIO;
    }

    public Integer getTipO_USUARIO_DELTA() {
        return tipO_USUARIO_DELTA;
    }

    public void setTipO_USUARIO_DELTA(Integer tipO_USUARIO_DELTA) {
        this.tipO_USUARIO_DELTA = tipO_USUARIO_DELTA;
    }

    public Integer getNuM_INTENTOS() {
        return nuM_INTENTOS;
    }

    public void setNuM_INTENTOS(Integer nuM_INTENTOS) {
        this.nuM_INTENTOS = nuM_INTENTOS;
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

    public Integer getCoD_EST_USUARIO() {
        return coD_EST_USUARIO;
    }

    public void setCoD_EST_USUARIO(Integer coD_EST_USUARIO) {
        this.coD_EST_USUARIO = coD_EST_USUARIO;
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

    public String getUsR_SERVICIO() {
        return usR_SERVICIO;
    }

    public void setUsR_SERVICIO(String usR_SERVICIO) {
        this.usR_SERVICIO = usR_SERVICIO;
    }

    public Long getiD_ROL() {
        return iD_ROL;
    }

    public void setiD_ROL(Long iD_ROL) {
        this.iD_ROL = iD_ROL;
    }

    public Integer getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Integer autorizado) {
        this.autorizado = autorizado;
    }

    public Integer getEnroladO_SERVICE() {
        return enroladO_SERVICE;
    }

    public void setEnroladO_SERVICE(Integer enroladO_SERVICE) {
        this.enroladO_SERVICE = enroladO_SERVICE;
    }

    public Integer getiD_USR_SERVICIO() {
        return iD_USR_SERVICIO;
    }

    public void setiD_USR_SERVICIO(Integer iD_USR_SERVICIO) {
        this.iD_USR_SERVICIO = iD_USR_SERVICIO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRutas() {
        return rutas;
    }

    public void setRutas(List<String> rutas) {
        this.rutas = rutas;
    }

    public List<FuncDTO> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(List<FuncDTO> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    public usuarioPersona getPerson() {
        return person;
    }

    public void setPerson(usuarioPersona person) {
        this.person = person;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public usuarioRol getRol() {
        return rol;
    }

    public void setRol(usuarioRol rol) {
        this.rol = rol;
    }

    public List<usuarioJefe> getJefes() {
        return jefes;
    }

    public void setJefes(List<usuarioJefe> jefes) {
        this.jefes = jefes;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public List<usuarioPerfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<usuarioPerfil> perfiles) {
        this.perfiles = perfiles;
    }
}

class usuarioSexo{
    private Long id;
    private String descripcion;
    private String acronimo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }
}

class usuarioLogin2{
    private Long usuario_id;
    private String usuario;
    private Integer seC_PASSWORD;
    private Integer tipO_USUARIO_DELTA;
    private Long id_persona;
    private Long id_datos_persona_natural;
    private Long id;
    private String nombres;
    private String ape_paterno;
    private String ape_materno;
    private Date fec_nacimiento;
    private Long id_sexo;

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getSeC_PASSWORD() {
        return seC_PASSWORD;
    }

    public void setSeC_PASSWORD(Integer seC_PASSWORD) {
        this.seC_PASSWORD = seC_PASSWORD;
    }

    public Integer getTipO_USUARIO_DELTA() {
        return tipO_USUARIO_DELTA;
    }

    public void setTipO_USUARIO_DELTA(Integer tipO_USUARIO_DELTA) {
        this.tipO_USUARIO_DELTA = tipO_USUARIO_DELTA;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Long getId_datos_persona_natural() {
        return id_datos_persona_natural;
    }

    public void setId_datos_persona_natural(Long id_datos_persona_natural) {
        this.id_datos_persona_natural = id_datos_persona_natural;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getFec_nacimiento() {
        return fec_nacimiento;
    }

    public void setFec_nacimiento(Date fec_nacimiento) {
        this.fec_nacimiento = fec_nacimiento;
    }

    public Long getId_sexo() {
        return id_sexo;
    }

    public void setId_sexo(Long id_sexo) {
        this.id_sexo = id_sexo;
    }
}

class usuarioRelac{
    private Long id;
    private Long id_tipo_relacion;
    private Long id_persona;
    private Long id_area;
    private Long estado;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private String person;
    private usuarioAreaDTO area;
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

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
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

    public usuarioAreaDTO getArea() {
        return area;
    }

    public void setArea(usuarioAreaDTO area) {
        this.area = area;
    }

    public String getPlanillas() {
        return planillas;
    }

    public void setPlanillas(String planillas) {
        this.planillas = planillas;
    }
}

class FuncDTO {
    private Long idFunc;
    private Long idFuncPadre;
    private String nomFunc;
    private String dscFunc;
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
    private List<FuncDTO> childs;

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

    public List<FuncDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<FuncDTO> childs) {
        this.childs = childs;
    }
}

class usuarioAuth{
    private Long id;
    private String usuario;
    private Long id_persona;
    private Integer seC_PASSWORD;
    private Integer tipO_USUARIO;
    private Integer tipO_USUARIO_DELTA;
    private Integer nuM_INTENTOS;
    private Date feC_INGRESO;
    private String usR_INGRESO;
    private Integer coD_EST_USUARIO;
    private String usR_ULT_MOD;
    private Date feC_ULT_MOD;
    private String usR_SERVICIO;
    private Long iD_ROL;
    private Integer autorizado;
    private Integer enroladO_SERVICE;
    private Integer iD_USR_SERVICIO;
    private String password;
    private String rutas;
    private String funcionalidades;
    private usrPlantaPer person;
    private String typeUser;
    private usrPlantaRol rol;
    private String jefes;
    private String tickets;
    private List<perfil1DTO> perfiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public Integer getSeC_PASSWORD() {
        return seC_PASSWORD;
    }

    public void setSeC_PASSWORD(Integer seC_PASSWORD) {
        this.seC_PASSWORD = seC_PASSWORD;
    }

    public Integer getTipO_USUARIO() {
        return tipO_USUARIO;
    }

    public void setTipO_USUARIO(Integer tipO_USUARIO) {
        this.tipO_USUARIO = tipO_USUARIO;
    }

    public Integer getTipO_USUARIO_DELTA() {
        return tipO_USUARIO_DELTA;
    }

    public void setTipO_USUARIO_DELTA(Integer tipO_USUARIO_DELTA) {
        this.tipO_USUARIO_DELTA = tipO_USUARIO_DELTA;
    }

    public Integer getNuM_INTENTOS() {
        return nuM_INTENTOS;
    }

    public void setNuM_INTENTOS(Integer nuM_INTENTOS) {
        this.nuM_INTENTOS = nuM_INTENTOS;
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

    public Integer getCoD_EST_USUARIO() {
        return coD_EST_USUARIO;
    }

    public void setCoD_EST_USUARIO(Integer coD_EST_USUARIO) {
        this.coD_EST_USUARIO = coD_EST_USUARIO;
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

    public String getUsR_SERVICIO() {
        return usR_SERVICIO;
    }

    public void setUsR_SERVICIO(String usR_SERVICIO) {
        this.usR_SERVICIO = usR_SERVICIO;
    }

    public Long getiD_ROL() {
        return iD_ROL;
    }

    public void setiD_ROL(Long iD_ROL) {
        this.iD_ROL = iD_ROL;
    }

    public Integer getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Integer autorizado) {
        this.autorizado = autorizado;
    }

    public Integer getEnroladO_SERVICE() {
        return enroladO_SERVICE;
    }

    public void setEnroladO_SERVICE(Integer enroladO_SERVICE) {
        this.enroladO_SERVICE = enroladO_SERVICE;
    }

    public Integer getiD_USR_SERVICIO() {
        return iD_USR_SERVICIO;
    }

    public void setiD_USR_SERVICIO(Integer iD_USR_SERVICIO) {
        this.iD_USR_SERVICIO = iD_USR_SERVICIO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRutas() {
        return rutas;
    }

    public void setRutas(String rutas) {
        this.rutas = rutas;
    }

    public String getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(String funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    public usrPlantaPer getPerson() {
        return person;
    }

    public void setPerson(usrPlantaPer person) {
        this.person = person;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public usrPlantaRol getRol() {
        return rol;
    }

    public void setRol(usrPlantaRol rol) {
        this.rol = rol;
    }

    public String getJefes() {
        return jefes;
    }

    public void setJefes(String jefes) {
        this.jefes = jefes;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public List<perfil1DTO> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<perfil1DTO> perfiles) {
        this.perfiles = perfiles;
    }
}