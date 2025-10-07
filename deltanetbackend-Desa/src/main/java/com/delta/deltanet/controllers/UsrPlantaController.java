package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/usrplanta")
public class UsrPlantaController {

	@Autowired
	private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;

	@Autowired
	private AutenticacionClaveServiceImpl autenticacionClaveService;

	@Autowired
	private AuthPerfilUserServiceImpl authPerfilUserService;

	@Autowired
	private PersonaServiceImpl personaService;

	@Autowired
	private AuthPerfilServiceImpl authPerfilService;

	@Autowired
	private JefeAreaServiceImpl jefeAreaService;

	@Autowired
	private GerenciaServiceImpl gerenciaService;

	@Autowired
	private RelacionServiceImpl relacionService;

	@Autowired
	private ITicketService ticketService;

	@GetMapping("/index")
	public ResponseEntity<?> getFuncionalidades() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object> usuarios = autenticacionUsuarioService.listaUsuariosDelta(1, 1);
			List<listaDTO> listado = new ArrayList<>();
			Iterator<Object> it = usuarios.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				listaDTO reg = new listaDTO();
				AutenticacionUsuario fil = (AutenticacionUsuario) row[0];
				reg.setId(fil.getId());
				reg.setUsuario(fil.getUsuario());
				reg.setCoD_EST_USUARIO(fil.getCodEstUsuario());
				reg.setTipdoc(fil.getPersona().getTipoDoc().getNombre());
				reg.setNrodoc(fil.getPersona().getDocumento());
				reg.setNombre(fil.getPersona().getPerNat().getNombre());
				reg.setApepat(fil.getPersona().getPerNat().getApePaterno());
				reg.setApemat(fil.getPersona().getPerNat().getApeMaterno());
				if (fil.getPersona().getPuesto() != null) {
					fil.getPersona().getPuesto().setArea(null);
					reg.setPuesto(fil.getPersona().getPuesto());
				}
				if (fil.getPersona().getArea() != null) {
					fil.getPersona().getArea().getGerencia().setOrgAreas(null);
					fil.getPersona().getArea().setPuestos(null);
					reg.setArea(fil.getPersona().getArea());
				}
				List<EMail> lstEmails = fil.getPersona().getCorreos();
				List<correoDTO> correos = new ArrayList<>();
				for (EMail item : lstEmails) {
					if (item.getEstado() == 1) {
						correoDTO reg1 = new correoDTO();
						reg1.setEstado(item.getEstado());
						reg1.setEmail(item.getCorreo());
						correos.add(reg1);
					}
				}
				reg.setCorreos(correos);

				List<Relacion> lstRelaciones = fil.getPersona().getRelaciones();
				List<relacionDTO> relaciones = new ArrayList<>();
				for (Relacion item : lstRelaciones) {
					if (item.getEstado() == 1) {
						relacionDTO reg1 = new relacionDTO();
						reg1.setEstado(1);
						reg1.setNombre("");
						relaciones.add(reg1);
					}
				}
				reg.setRelaciones(relaciones);

				listado.add(reg);
			}
			response.put("users", listado);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion index");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/virtual/index")
	public ResponseEntity<?> getVirtuales() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object> usuarios = autenticacionUsuarioService.listaUsuariosDelta(1, 2);
			List<listaDTO> listado = new ArrayList<>();
			Iterator<Object> it = usuarios.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				listaDTO reg = new listaDTO();
				AutenticacionUsuario fil = (AutenticacionUsuario) row[0];
				reg.setId(fil.getId());
				reg.setUsuario(fil.getUsuario());
				reg.setCoD_EST_USUARIO(fil.getCodEstUsuario());
				reg.setTipdoc(fil.getPersona().getTipoDoc().getNombre());
				reg.setNrodoc(fil.getPersona().getDocumento());
				reg.setNombre(fil.getPersona().getPerNat().getNombre());
				reg.setApepat(fil.getPersona().getPerNat().getApePaterno());
				reg.setApemat(fil.getPersona().getPerNat().getApeMaterno());
				if (fil.getPersona().getArea() != null) {
					reg.setArea(fil.getPersona().getArea());
					reg.getArea().getGerencia().setOrgAreas(null);
				}
				if (fil.getPersona().getPuesto() != null) {
					reg.getArea().setPuestos(null);
					reg.setPuesto(fil.getPersona().getPuesto());
					reg.getPuesto().getArea().setPuestos(null);
				}

				List<EMail> lstEmails = fil.getPersona().getCorreos();
				List<correoDTO> correos = new ArrayList<>();
				for (EMail item : lstEmails) {
					if (item.getEstado() == 1) {
						correoDTO reg1 = new correoDTO();
						reg1.setEstado(item.getEstado());
						reg1.setEmail(item.getCorreo());
						correos.add(reg1);
					}
				}
				reg.setCorreos(correos);

				List<Relacion> lstRelaciones = fil.getPersona().getRelaciones();
				List<relacionDTO> relaciones = new ArrayList<>();
				for (Relacion item : lstRelaciones) {
					if (item.getEstado() == 1 && item.getArea() != null) {
						relacionDTO reg1 = new relacionDTO();
						reg1.setEstado(1);
						reg1.setNombre(item.getArea().getNombre());
						relaciones.add(reg1);
					}
				}
				reg.setRelaciones(relaciones);

				listado.add(reg);
			}
			response.put("users", listado);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion index");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/index2")
	public ResponseEntity<?> getFuncionalidades2() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Relacion> lista = autenticacionUsuarioService.listaRelaciones();
			List<lstRelDTO> listado = new ArrayList<>();
			for (Relacion item : lista) {
				lstRelDTO reg = new lstRelDTO();
				reg.setId(item.getIdRel());
				reg.setId_tipo_relacion(item.getTipoRel().getIdTipoRel());
				reg.setId_persona(item.getPersona().getId());
				reg.setId_area(item.getIdArea());
				reg.setEstado(item.getEstado());
				reg.setCreate_user(item.getCreateUser());
				reg.setCreate_date(item.getCreateDate());
				reg.setUpdate_user(item.getUpdateUser());
				reg.setUpdate_date(item.getUpdateDate());

				Areas regArea = item.getArea();
				usrPlantaAreaDTO reg1 = new usrPlantaAreaDTO();
				Long idArea = Long.valueOf(String.valueOf(regArea.getId()));
				reg1.setId(regArea.getId());
				reg1.setId_gerencia(regArea.getIdGerencia());
				reg1.setNombre(regArea.getNombre());
				reg1.setEstado(regArea.getEstado());
				reg1.setCreate_user(regArea.getUsrCreate());
				reg1.setCreate_date(regArea.getFecCreate());
				reg1.setUpdate_user(regArea.getUsrUpdate());
				reg1.setUpdate_date(regArea.getFecUpdate());

				List<JefeArea> lstjefes = jefeAreaService.buscaJefes(idArea);
				List<usrPlantaJefeDTO> jefes = new ArrayList<>();
				for (JefeArea itemJefe : lstjefes) {
					usrPlantaJefeDTO regJefe = new usrPlantaJefeDTO();
					regJefe.setIdUser(itemJefe.getIdUsuario());
					regJefe.setNombres(itemJefe.getUsuario().getPersona().getPerNat().getNombre());
					regJefe.setApePaterno(itemJefe.getUsuario().getPersona().getPerNat().getApePaterno());
					regJefe.setApeMaterno(itemJefe.getUsuario().getPersona().getPerNat().getApeMaterno());
					jefes.add(regJefe);
				}
				reg1.setJefes(jefes);

				Long idGer = Long.valueOf(String.valueOf(regArea.getIdGerencia()));
				Optional<Gerencia> gerente = gerenciaService.findById(idGer);
				if (gerente.isPresent()) {
					Gerencia x = gerente.get();
					usrPlantaGerDTO regGerente = new usrPlantaGerDTO();
					regGerente.setId(x.getId());
					regGerente.setNombre(x.getNombre());
					regGerente.setEstado(x.getEstado());
					reg1.setDatoGerente(regGerente);
				}

				List<Relacion> lstRelaciones = relacionService.buscaPorArea(idArea);
				List<usrPlantaRelDTO> listaRels = new ArrayList<>();
				for (Relacion itRel : lstRelaciones) {
					usrPlantaRelDTO y = new usrPlantaRelDTO();
					y.setIdRel(itRel.getIdRel());
					y.setIdTipoRel(itRel.getIdTipoRel());
					y.setDescrip(itRel.getTipoRel().getDescrip());
					listaRels.add(y);
				}
				reg1.setRelaciones(listaRels);

				reg.setArea(reg1);

				listado.add(reg);
			}
			response.put("datos", listado);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion index2");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/show")
	public ResponseEntity<?> muestra(@RequestParam(name = "username") String usuario) {
		Map<String, Object> response = new HashMap<>();
		try {
			AutenticacionUsuario registro = autenticacionUsuarioService.findByUsuario(usuario);
			usrPlantaUser regUser = new usrPlantaUser();
			Long idUser = registro.getId();

			regUser.setId(registro.getId());
			regUser.setUsuario(registro.getUsuario());
			regUser.setId_persona(registro.getPersona().getId());
			regUser.setSeC_PASSWORD(registro.getSecPassword());
			regUser.setTipO_USUARIO(registro.getTipoUsuario());
			regUser.setTipO_USUARIO_DELTA(registro.getTipoUsuarioDelta());
			regUser.setNuM_INTENTOS(registro.getNumIntentos());
			regUser.setFeC_INGRESO(registro.getFecIngreso());
			regUser.setUsR_INGRESO(registro.getUsrIngreso());
			regUser.setCoD_EST_USUARIO(registro.getCodEstUsuario());
			regUser.setUsR_ULT_MOD(registro.getUsrUltMod());
			regUser.setFeC_ULT_MOD(registro.getFecUltMod());
			regUser.setAutorizado(registro.getAutorizado());
			regUser.setEnroladO_SERVICE(registro.getEnroladoService());

			usrPlantaPer per = new usrPlantaPer();
			per.setId(registro.getPersona().getId());
			per.setId_tipo_persona(registro.getPersona().getTipoPer().getIdTipoPer());
			per.setId_tipo_documento(registro.getPersona().getTipoDoc().getIdTipDoc());
			per.setDocumento(registro.getPersona().getDocumento());
			if (registro.getPersona().getAtencion() != null)
				per.setTratamiento(registro.getPersona().getAtencion().getId());
			if (registro.getPersona().getPerNat() != null)
				per.setId_datos_persona_natural(registro.getPersona().getPerNat().getIdPerNat());
			if (registro.getPersona().getPerJur() != null)
				per.setId_datos_persona_juridica(registro.getPersona().getPerJur().getIdPerJur());
			per.setEstado(registro.getCodEstUsuario());
			per.setVisible(registro.getPersona().getVisible());
			per.setCreate_user(registro.getPersona().getCreUser());
			per.setCreate_date(registro.getPersona().getCreDate());
			per.setUpdate_user(registro.getPersona().getUpdUser());
			per.setUpdate_date(registro.getPersona().getUpdDate());

			usrPlantaTipoDoc td = new usrPlantaTipoDoc();
			td.setId(registro.getPersona().getTipoDoc().getIdTipDoc());
			td.setNombre(registro.getPersona().getTipoDoc().getNombre());
			per.setTipoDocumento(td);

			usrPlantaPerNat pn = new usrPlantaPerNat();
			pn.setId(registro.getPersona().getPerNat().getIdPerNat());
			pn.setId_sexo(registro.getPersona().getPerNat().getSex().getIdSexo());
			pn.setNombres(registro.getPersona().getPerNat().getNombre());
			pn.setApe_paterno(registro.getPersona().getPerNat().getApePaterno());
			pn.setApe_materno(registro.getPersona().getPerNat().getApeMaterno());
			pn.setFec_nacimiento(registro.getPersona().getPerNat().getFecNacim());
			per.setDatosNatural(pn);

			List<usrPlantaEmails> ce = new ArrayList<>();
			for (EMail correo : registro.getPersona().getCorreos()) {
				usrPlantaEmails z = new usrPlantaEmails();
				z.setId(correo.getIdEMail());
				z.setId_persona(correo.getPersona().getId());
				z.setEstado(correo.getEstado());
				z.setTipo(correo.getTipo());
				z.setEmail(correo.getCorreo());
				z.setCreate_user(correo.getUsuCreado());
				z.setCreate_date(correo.getFechaCreado());
				z.setUpdate_user(correo.getUsuUpdate());
				z.setUpdate_date(correo.getFechaUpdate());
				ce.add(z);
			}
			per.setCorreos(ce);

			List<usrPlantaFonos> nt = new ArrayList<>();
			for (Telefono fono : registro.getPersona().getTelefonos()) {
				usrPlantaFonos t = new usrPlantaFonos();
				t.setId(fono.getIdTelefono());
				t.setId_persona(fono.getPersona().getId());
				t.setEstado(fono.getEstado());
				t.setTipo(fono.getTipo());
				t.setNumero(fono.getNumero());
				t.setCreate_user(fono.getUsuCreado());
				t.setCreate_date(fono.getFechaCreado());
				t.setUpdate_user(fono.getUsuUpdate());
				t.setUpdate_date(fono.getFechaUpdate());
				nt.add(t);
			}
			per.setTelefonos(nt);

			regUser.setPerson(per);
			List<Object> regPerfiles = authPerfilUserService.findPerfiles(idUser);
			List<perfil1DTO> listado = new ArrayList<>();
			Iterator<Object> it = regPerfiles.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				perfil1DTO reg = new perfil1DTO();
				Long idPerfil = Long.valueOf(String.valueOf(row[0]));
				Long idUsuario = (Long) row[1];
				reg.setiD_PERFIL(idPerfil);
				reg.setiD_USUARIO(idUsuario);

				Integer idpPerfil = (Integer) row[0];
				AutorizacionPerfil detPerfil = authPerfilService.findById(idpPerfil);

				usrPlantaDetDTO x = new usrPlantaDetDTO();
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
			regUser.setPerfiles(listado);

			response.put("user", regUser);
			response.put("message", "Busqueda exitosa");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion show");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/detail")
	public ResponseEntity<?> detalle(@RequestParam(name = "username") String usuario) {
		Map<String, Object> response = new HashMap<>();
		try {
			AutenticacionUsuario registro = autenticacionUsuarioService.findByUsuario(usuario);
			response.put("user", registro);
			response.put("message", "Busqueda exitosa");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion detail");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/store")
	public ResponseEntity<?> store(@RequestParam(name = "idusuario") Long idUser,
			@RequestParam(name = "usuario") String usuario, @RequestParam(name = "username") String username,
			@RequestParam(name = "type", required = false) int type) {
		Map<String, Object> response = new HashMap<>();
		try {
			Persona persona = personaService.buscarId(idUser);
			List<Relacion> relaciones = persona.getRelaciones();
			Boolean valida = false;
			for (Relacion item : relaciones) {
				if (item.getIdTipoRel() == 4)
					valida = true;
			}
			if (!valida) {
				response.put("message", "El usuario no esta autorizado. Su relacion no es de trabajador.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			AutenticacionUsuario regUser = autenticacionUsuarioService.findByUsuario(usuario);
			if (regUser == null) {
				AutenticacionUsuario registro = new AutenticacionUsuario();
				registro.setUsuario(usuario);
				registro.setPersona(persona);
				registro.setSecPassword(1);
				if (type > 0) {
					registro.setTipoUsuario(type);
				} else {
					registro.setTipoUsuario(1);
				}
				registro.setTipoUsuarioDelta(1);
				registro.setCodEstUsuario(1);
				registro.setNumIntentos(0);
				registro.setFecIngreso(new Date());
				registro.setUsrIngreso(username);
				registro.setEnroladoService(1);
				autenticacionUsuarioService.save(registro);

				AutenticacionClave regClave = new AutenticacionClave();
				regClave.setId(registro.getId());
				regClave.setSecPassword(1);
				regClave.setDesPassword(hashWith256(registro.getPersona().getDocumento()));
				regClave.setUsrIngreso(username);
				regClave.setFecIngreso(new Date());
				autenticacionClaveService.save(regClave);

				usrPlantaUser x = new usrPlantaUser();
				x.setId(registro.getId());
				x.setUsuario(registro.getUsuario());
				x.setId_persona(registro.getPersona().getId());
				x.setSeC_PASSWORD(registro.getSecPassword());
				x.setTipO_USUARIO(registro.getTipoUsuario());
				x.setTipO_USUARIO_DELTA(registro.getTipoUsuarioDelta());
				x.setNuM_INTENTOS(registro.getNumIntentos());
				x.setFeC_INGRESO(registro.getFecIngreso());
				x.setUsR_INGRESO(registro.getUsrIngreso());
				x.setCoD_EST_USUARIO(registro.getCodEstUsuario());
				x.setUsR_ULT_MOD(registro.getUsrUltMod());
				x.setFeC_ULT_MOD(registro.getFecUltMod());

				usrPlantaPer y = new usrPlantaPer();
				y.setId(registro.getPersona().getId());
				y.setId_tipo_persona(registro.getPersona().getTipoPer().getIdTipoPer());
				y.setId_tipo_documento(registro.getPersona().getTipoDoc().getIdTipDoc());
				y.setDocumento(registro.getPersona().getDocumento());
				if (registro.getPersona().getAtencion() != null)
					y.setTratamiento(registro.getPersona().getAtencion().getId());
				if (registro.getPersona().getPerNat() != null)
					y.setId_datos_persona_natural(registro.getPersona().getPerNat().getIdPerNat());
				if (registro.getPersona().getPerJur() != null)
					y.setId_datos_persona_juridica(registro.getPersona().getPerJur().getIdPerJur());
				y.setEstado(registro.getCodEstUsuario());
				y.setVisible(registro.getPersona().getVisible());
				y.setCreate_user(registro.getPersona().getCreUser());
				y.setCreate_date(registro.getPersona().getCreDate());
				y.setUpdate_user(registro.getPersona().getUpdUser());
				y.setUpdate_date(registro.getPersona().getUpdDate());
				if (registro.getPersona().getPerNat() != null) {
					usrPlantaPerNat r = new usrPlantaPerNat();
					r.setId(registro.getPersona().getPerNat().getIdPerNat());
					r.setId_sexo(registro.getPersona().getPerNat().getSex().getIdSexo());
					r.setNombres(registro.getPersona().getPerNat().getNombre());
					r.setApe_paterno(registro.getPersona().getPerNat().getApePaterno());
					r.setApe_materno(registro.getPersona().getPerNat().getApeMaterno());
					r.setFec_nacimiento(registro.getPersona().getPerNat().getFecNacim());
					y.setDatosNatural(r);
				}
				y.setRelaciones(registro.getPersona().getRelaciones());
				y.setRelaciones(null);
				x.setPerson(y);
				response.put("aplication", x);

			} else {
				response.put("message", "El usuario ya existe.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			response.put("message", "Registro exitoso");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion store");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/verifica")
	public ResponseEntity<?> verifica(@RequestParam(name = "username") String username) {
		Map<String, Object> response = new HashMap<>();
		try {
			AutenticacionUsuario regUser = autenticacionUsuarioService.findByUsuario(username);
			if (regUser == null) {
				response.put("message", "Usuario disponible.");
			} else {
				response.put("message", "El usuario ya existe.");
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion verifica");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam(name = "id") Long idUser,
			@RequestParam(name = "username") String username) {
		Map<String, Object> response = new HashMap<>();
		String accion = "";
		Optional<AutenticacionUsuario> regUsuario = autenticacionUsuarioService.findById(idUser);
		if (regUsuario.isPresent()) {
			AutenticacionUsuario reg = regUsuario.get();
			if (reg.getCodEstUsuario() == 0) {
				reg.setCodEstUsuario(1);
				accion = "habilitado";
			} else {
				List<Ticket> list = ticketService.findAllNoResueltosByUsuarioCreador(idUser);
				if (list.size() > 0) {
					response.put("mensaje", "No se puede deshabilitar un usuario con uno o mas tickets sin resolver");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				reg.setCodEstUsuario(0);
				accion = "deshabilitado";
			}
			try {
				reg.setUsrUltMod(username);
				reg.setFecUltMod(new Date());
				autenticacionUsuarioService.save(reg);
				usrPlantaUser f = new usrPlantaUser();
				f.setId(reg.getId());
				f.setUsuario(reg.getUsuario());
				f.setId_persona(reg.getPersona().getId());
				f.setSeC_PASSWORD(reg.getSecPassword());
				f.setTipO_USUARIO(reg.getTipoUsuario());
				f.setTipO_USUARIO_DELTA(reg.getTipoUsuarioDelta());
				f.setNuM_INTENTOS(reg.getNumIntentos());
				f.setFeC_INGRESO(reg.getFecIngreso());
				f.setUsR_INGRESO(reg.getUsrIngreso());
				f.setCoD_EST_USUARIO(reg.getCodEstUsuario());
				f.setFeC_ULT_MOD(reg.getFecUltMod());
				f.setUsR_ULT_MOD(reg.getUsrUltMod());
				f.setAutorizado(reg.getAutorizado());
				f.setEnroladO_SERVICE(reg.getEnroladoService());
				response.put("user", f);
				response.put("mensaje", "Registro " + accion);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.put("mensaje", "Error inesperado en la funcion delete");
				response.put("debug", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		} else {
			response.put("message", "Error di de usuario no encontrado");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/perfiles")
	public ResponseEntity<?> perfiles(@RequestParam(name = "user_id") Long idUser) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object> regPerfiles = authPerfilUserService.findPerfiles(idUser);
			List<perfil1DTO> listado = new ArrayList<>();
			Iterator<Object> it = regPerfiles.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				perfil1DTO reg = new perfil1DTO();
				Long idPerfil = Long.valueOf(String.valueOf(row[0]));
				Long idUsuario = (Long) row[1];
				reg.setiD_PERFIL(idPerfil);
				reg.setiD_USUARIO(idUsuario);

				Integer idpPerfil = (Integer) row[0];
				AutorizacionPerfil detPerfil = authPerfilService.findById(idpPerfil);

				usrPlantaDetDTO x = new usrPlantaDetDTO();
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

				Optional<AutenticacionUsuario> login = autenticacionUsuarioService.findById(idUsuario);
				if (login.isPresent()) {
					AutenticacionUsuario user = login.get();
					usrPlantaLogin log = new usrPlantaLogin();
					log.setId(user.getId());
					log.setUsuario(user.getUsuario());
					reg.setUsuario(log);
				}

				listado.add(reg);
			}
			response.put("perfiles", listado);

			List<AutorizacionPerfil> lst = authPerfilService.getAll();
			List<usrPlantaDetDTO> listas = new ArrayList<>();
			for (AutorizacionPerfil item : lst) {
				usrPlantaDetDTO x = new usrPlantaDetDTO();
				Long idPerfil = Long.valueOf(String.valueOf(item.getId()));
				x.setiD_PERFIL(idPerfil);
				x.setDsC_NOM_PERFIL(item.getNombre());
				x.setDsC_DESCRIPCION(item.getDescripcion());
				x.setCoD_EST_PERFIL(item.getEstado());
				x.setFeC_INGRESO(item.getFecIngreso());
				x.setUsR_INGRESO(item.getUsrIngreso());
				x.setFeC_ULT_MOD(item.getFecUltMod());
				x.setUsR_ULT_MOD(item.getUsrUltMod());

				Optional<AutorizacionPerfilUsuario> regPerf = authPerfilUserService.buscaIdUsrIdPerfil(idUser,
						item.getId());
				if (regPerf.isPresent()) {
					AutorizacionPerfilUsuario apu = regPerf.get();
					List<usrPlantalstPerfiles> y = new ArrayList<>();

					usrPlantalstPerfiles a = new usrPlantalstPerfiles();
					a.setiD_PERFIL(apu.getIdPerfil());
					a.setiD_USUARIO(apu.getIdUser());
					y.add(a);

					x.setLstUsrPerfiles(y);
				}

				listas.add(x);
			}
			response.put("lists", listas);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/perfiles_store")
	public ResponseEntity<?> perfiles_store(@RequestParam(name = "id_usuario") Long idUser,
			@RequestParam(name = "perfiles") String[] perfiles) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUser);
			if (usuario.isEmpty()) {
				response.put("message", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			Integer id = Integer.valueOf(String.valueOf(idUser));
			if (perfiles.length > 0) {
				authPerfilUserService.delete(id);
				for (String item : perfiles) {
					Integer idPerfil = Integer.valueOf(item);
					authPerfilUserService.insert(idUser, idPerfil);
				}
			} else {
				response.put("message", "No se enviaron los perfiles");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			response.put("message", "Se registraron correctamente los perfiles del usuario");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles_store");
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
}

class usrPlantalstPerfiles {
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

class usrPlantaDetDTO {
	private Long iD_PERFIL;
	private String dsC_NOM_PERFIL;
	private String dsC_DESCRIPCION;
	private Integer coD_EST_PERFIL;
	private Date feC_INGRESO;
	private String usR_INGRESO;
	private Date feC_ULT_MOD;
	private String usR_ULT_MOD;
	private String usrPerfil;
	private List<usrPlantalstPerfiles> lstUsrPerfiles;

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

	public List<usrPlantalstPerfiles> getLstUsrPerfiles() {
		return lstUsrPerfiles;
	}

	public void setLstUsrPerfiles(List<usrPlantalstPerfiles> lstUsrPerfiles) {
		this.lstUsrPerfiles = lstUsrPerfiles;
	}
}

class usrPlantaJefeDTO {
	private Long idUser;
	private String nombres;
	private String apePaterno;
	private String apeMaterno;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApePaterno() {
		return apePaterno;
	}

	public void setApePaterno(String apePaterno) {
		this.apePaterno = apePaterno;
	}

	public String getApeMaterno() {
		return apeMaterno;
	}

	public void setApeMaterno(String apeMaterno) {
		this.apeMaterno = apeMaterno;
	}
}

class usrPlantaGerDTO {
	private Long id;
	private String nombre;
	private Integer estado;

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

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
}

class usrPlantaRelDTO {
	private Long idRel;
	private Long idTipoRel;
	private String descrip;

	public Long getIdRel() {
		return idRel;
	}

	public void setIdRel(Long idRel) {
		this.idRel = idRel;
	}

	public Long getIdTipoRel() {
		return idTipoRel;
	}

	public void setIdTipoRel(Long idTipoRel) {
		this.idTipoRel = idTipoRel;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
}

class usrPlantaLogin {
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

class perfil1DTO {
	private Long iD_USUARIO;
	private Long iD_PERFIL;
	private usrPlantaDetDTO detPerfil;
	private usrPlantaLogin usuario;

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

	public usrPlantaDetDTO getDetPerfil() {
		return detPerfil;
	}

	public void setDetPerfil(usrPlantaDetDTO detPerfil) {
		this.detPerfil = detPerfil;
	}

	public usrPlantaLogin getUsuario() {
		return usuario;
	}

	public void setUsuario(usrPlantaLogin usuario) {
		this.usuario = usuario;
	}
}

class relacionDTO {
	private String nombre;
	private Integer estado;

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

class correoDTO {
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

class listaDTO {
	private Long id;
	private String usuario;
	private Integer coD_EST_USUARIO;
	private String tipdoc;
	private String nrodoc;
	private String nombre;
	private String apepat;
	private String apemat;
	private OrgAreas area;
	private Puestos puesto;
	private List<correoDTO> correos;
	private List<relacionDTO> relaciones;

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

	public Integer getCoD_EST_USUARIO() {
		return coD_EST_USUARIO;
	}

	public void setCoD_EST_USUARIO(Integer coD_EST_USUARIO) {
		this.coD_EST_USUARIO = coD_EST_USUARIO;
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

	public List<correoDTO> getCorreos() {
		return correos;
	}

	public void setCorreos(List<correoDTO> correos) {
		this.correos = correos;
	}

	public List<relacionDTO> getRelaciones() {
		return relaciones;
	}

	public void setRelaciones(List<relacionDTO> relaciones) {
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

class usrPlantaAreaDTO {
	private Integer id;
	private Integer id_gerencia;
	private String nombre;
	private Integer estado;
	private String create_user;
	private Date create_date;
	private String update_user;
	private Date update_date;
	private List<usrPlantaJefeDTO> jefes;
	private usrPlantaGerDTO datoGerente;
	private List<usrPlantaRelDTO> relaciones;

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

	public List<usrPlantaJefeDTO> getJefes() {
		return jefes;
	}

	public void setJefes(List<usrPlantaJefeDTO> jefes) {
		this.jefes = jefes;
	}

	public usrPlantaGerDTO getDatoGerente() {
		return datoGerente;
	}

	public void setDatoGerente(usrPlantaGerDTO datoGerente) {
		this.datoGerente = datoGerente;
	}

	public List<usrPlantaRelDTO> getRelaciones() {
		return relaciones;
	}

	public void setRelaciones(List<usrPlantaRelDTO> relaciones) {
		this.relaciones = relaciones;
	}
}

class lstRelDTO {
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
	private String planillas;
	private usrPlantaAreaDTO area;

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

	public String getPlanillas() {
		return planillas;
	}

	public void setPlanillas(String planillas) {
		this.planillas = planillas;
	}

	public usrPlantaAreaDTO getArea() {
		return area;
	}

	public void setArea(usrPlantaAreaDTO area) {
		this.area = area;
	}
}

class usrPlantaTipoDoc {
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

class usrPlantaPerNat {
	private Long id;
	private Long id_sexo;
	private String nombres;
	private String ape_paterno;
	private String ape_materno;
	private Date fec_nacimiento;
	private String datoSexo;

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

	public String getDatoSexo() {
		return datoSexo;
	}

	public void setDatoSexo(String datoSexo) {
		this.datoSexo = datoSexo;
	}
}

class usrPlantaEmails {
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

class usrPlantaFonos {
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

class usrPlantaPer {
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
	private usrPlantaTipoDoc tipoDocumento;
	private usrPlantaPerNat datosNatural;
	private usrPlantaPerNat datosJuridica;
	private List<Relacion> relaciones;
	private List<usrPlantaEmails> correos;
	private List<usrPlantaFonos> telefonos;

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

	public usrPlantaTipoDoc getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(usrPlantaTipoDoc tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public usrPlantaPerNat getDatosNatural() {
		return datosNatural;
	}

	public void setDatosNatural(usrPlantaPerNat datosNatural) {
		this.datosNatural = datosNatural;
	}

	public usrPlantaPerNat getDatosJuridica() {
		return datosJuridica;
	}

	public void setDatosJuridica(usrPlantaPerNat datosJuridica) {
		this.datosJuridica = datosJuridica;
	}

	public List<Relacion> getRelaciones() {
		return relaciones;
	}

	public void setRelaciones(List<Relacion> relaciones) {
		this.relaciones = relaciones;
	}

	public List<usrPlantaEmails> getCorreos() {
		return correos;
	}

	public void setCorreos(List<usrPlantaEmails> correos) {
		this.correos = correos;
	}

	public List<usrPlantaFonos> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<usrPlantaFonos> telefonos) {
		this.telefonos = telefonos;
	}
}

class usrPlantaRol {
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

class usrPlantaUser {
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
