package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/ejecutores")
public class ServicioEjecutorController {

	@Autowired
	private ServicioEjecutorServiceImpl servicioEjecutorService;

	@Autowired
	private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;

	@Autowired
	private ICatalogoServicioService catalogoServicioService;

	@Autowired
	private ICatalogoServicioCustomService catalogoServicioCustomService;

	@Autowired
	private TelefonoServiceImpl telefonoService;

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private GerenciaServiceImpl gerenciaService;

	@Autowired
	private AreasServiceImpl areasService;

	@Autowired
	public IRelacionService relacionService;

	@Autowired
	public IRelacionService relService;

	@Autowired
	public ServicioHabilitadoServiceImpl servicioHabilitadoServiceImpl;

	@Autowired
	private IOrgAreaService orgAreaService;

	@GetMapping("/index")
	public ResponseEntity<?> listaJefes() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<ServicioEjecutor> list = servicioEjecutorService.buscaEjecutores();
			List<EjecutorDTO> registers = new ArrayList<>();
			for (ServicioEjecutor row : list) {
				Long idUser = row.getIdUsuario();
				AutenticacionUsuario pNat = autenticacionUsuarioService.findById(idUser).get();
				EjecutorDTO reg = new EjecutorDTO();
				reg.setId(row.getId());
				reg.setId_area((long) row.getArea().getId());
				reg.setId_usuario(idUser);
				reg.setEstado(row.getEstado());
				reg.setDesArea(row.getArea().getNombre());
				reg.setDesGerencia(row.getArea().getGerencia().getNombre());
				List<Telefono> fonos = telefonoService.findAllFonos(pNat.getPersona().getId()).stream()
						.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
				List<fonoDTO> regFonos = new ArrayList<>();
				for (Telefono r : fonos) {
					fonoDTO rf = new fonoDTO();
					rf.setNumero(r.getNumero());
					rf.setEstado(r.getEstado());
					regFonos.add(rf);
				}
				reg.setTelefonos(regFonos);
				List<Object> items = relacionService.busFiltrada2(pNat.getPersona().getId(), 1L);
				List<Relacion> regRelations = new ArrayList<>();
				for (Object re : items) {
					Object[] col = (Object[]) re;
					Relacion r = new Relacion();
					r.setIdRel((long) col[0]);
					r.setEstado((long) col[1]);
					r.setIdTipoRel((long) col[2]);
					r.setFecFin((Date) col[4]);
					r.setIdArea((long) col[5]);
					regRelations.add(r);
				}
				List<EMail> emails = emailService.findAllEmail(pNat.getPersona().getId()).stream()
						.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
				List<emailDTO> regCorreos = new ArrayList<>();
				for (EMail r : emails) {
					emailDTO re = new emailDTO();
					re.setEmail(r.getCorreo());
					re.setTipo(r.getTipo());
					re.setEstado(r.getEstado());
					regCorreos.add(re);
				}
				reg.setRelaciones(regRelations);
				reg.setCorreos(regCorreos);
				reg.setNombres(pNat.getPersona().getPerNat().getNombre());
				reg.setApe_paterno(pNat.getPersona().getPerNat().getApePaterno());
				reg.setApe_materno(pNat.getPersona().getPerNat().getApeMaterno());
				reg.setUsuario(row.getUsuario().getUsuario());
				registers.add(reg);
			}
			response.put("users", registers);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion list de jefe area");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/create")
	public ResponseEntity<?> listados() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Gerencia> regGen = gerenciaService.findAll();
			List<gralDTO> gerencias = new ArrayList<>();
			for (Gerencia r : regGen) {
				gralDTO reg = new gralDTO();
				reg.setValue(String.valueOf(r.getId()));
				reg.setText(r.getNombre());
				gerencias.add(reg);
			}

			List<Areas> regAreas = areasService.getAll();
			List<gralDTO> areas = new ArrayList<>();
			for (Areas r : regAreas) {
				gralDTO reg = new gralDTO();
				reg.setValue(String.valueOf(r.getId()));
				reg.setText(r.getNombre());
				areas.add(reg);
			}
			response.put("areas", areas);
			response.put("gerencias", gerencias);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion create");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<?> lstUsers(@RequestParam(name = "id") Long idArea) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Object> listado = autenticacionUsuarioService.buscaUsuarioIdArea(idArea);
			List<userDTO> registros = new ArrayList<>();
			Iterator<Object> it = listado != null ? listado.iterator() : null;
			while (it != null && it.hasNext()) {
				Object[] col = (Object[]) it.next();
				AutenticacionUsuario au = (AutenticacionUsuario) col[0];
				if (au.getTipoUsuarioDelta() == 1 && au.getCodEstUsuario() == 1 && au.getTipoUsuario() == 1) {
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
			}
			response.put("users", registros);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion create");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/store")
	public ResponseEntity<?> saveJefe(@RequestParam(name = "id_usuario") Long idUser,
			@RequestParam(name = "id_area") Integer idArea,
			@RequestParam(name = "username") String username) {
		Map<String, Object> response = new HashMap<>();
		Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUser);
		if (usuario.isEmpty()) {
			response.put("mensaje", "El id de usuario [" + idUser + "] no se encuentra");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Optional<Areas> area = areasService.getById(idArea);
		if (area.isEmpty()) {
			response.put("mensaje", "El id de area [" + idArea + "] no se encuentra");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		ServicioEjecutor user = servicioEjecutorService.findStatus(idUser);
		if (user != null) {
			response.put("mensaje", "El usuario ya esta asignado como ejecutor del area selecciona");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			ServicioEjecutor jefe = new ServicioEjecutor();
			jefe.setUsuario(usuario.get());
			jefe.setArea(area.get());
			jefe.setEstado(1);
			jefe.setCreateUser(username);
			jefe.setCreateDate(new Date());
			servicioEjecutorService.save(jefe);
			response.put("message", "Registro exitoso");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion store");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteJefe(@RequestParam(name = "id") Long id,
			@RequestParam(name = "username") String username) {
		Map<String, Object> response = new HashMap<>();
		String accion = "";
		Optional<ServicioEjecutor> jefe = servicioEjecutorService.getById(id);
		if (jefe.isEmpty()) {
			response.put("mensaje", "El id de jefe de area [" + id + "] no se encuentra");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		ServicioEjecutor registro = jefe.get();
		if (registro.getEstado() == 0) {
			registro.setEstado(1);
			accion = "habilitado";
		} else {
			registro.setEstado(0);
			accion = "deshabilitado";
		}
		try {
			registro.setUpdateUser(username);
			registro.setUpdateDate(new Date());
			servicioEjecutorService.save(registro);
			response.put("mensaje", "Registro " + accion);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion delete");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/services")
	public ResponseEntity<?> services(@RequestParam(name = "user_id") Long idUser,
			@RequestParam(name = "area_id") Long idArea) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<ServicioEjecutor> user = servicioEjecutorService.getById(idUser);
			if (user.isEmpty()) {
				response.put("mensaje", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(user.get().getIdUsuario());
			if (usuario.isEmpty()) {
				response.put("mensaje", "El id de usuario no se encuentra");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			List<ServicioHabilitado> listHab = servicioHabilitadoServiceImpl.buscarServicio(idUser.intValue());
			for(ServicioHabilitado ser: listHab) {
				if(ser.getEjecutor().getUsuario().getPersona().getArea() != null) {
					ser.getEjecutor().getUsuario().getPersona().getArea().setPuestos(null);
					ser.getEjecutor().getUsuario().getPersona().getArea().getGerencia().setOrgAreas(null);
					ser.getEjecutor().getArea().getGerencia().setOrgAreas(null);
				}
				if(ser.getEjecutor().getArea() != null) {
					ser.getEjecutor().getArea().getGerencia().setOrgAreas(null);
				}
			}
			//List<Object> listado = relService.busFiltrada3(usuario.get().getPersona().getId(), idArea, 1L);
			List<CatalogoServicio> lstResumen = new ArrayList<>();
			/*lstRelacion2 relation = new lstRelacion2();
			if (listado.size() > 0) {
				Iterator<Object> it = listado.iterator();
				while (it.hasNext()) {
					Object[] row = (Object[]) it.next();
					relation.setIdRel((Long) row[0]);
					relation.setEstado((Long) row[1]);
					relation.setIdTipoRel((Long) row[2]);
					relation.setDescrip(String.valueOf(row[3]));
					relation.setFecIni((Date) row[4]);
					relation.setIdArea(String.valueOf(row[5]));
				}*/
				//if (relation != null && relation.getIdArea() != "") {
					List<CatalogoServicio> list = catalogoServicioService
							.findAllByArea(idArea);
					Iterator<CatalogoServicio> it2 = list.iterator();
					while (it2.hasNext()) {
						CatalogoServicio row = it2.next();
						row.setArea(null);
						lstResumen.add(row);
					}
				//}
			//}
			response.put("list", lstResumen);
			//response.put("relation", relation);
			response.put("listHab", listHab);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/services_store")
	public ResponseEntity<?> services_store(@RequestParam(name = "id_usuario") Long idUser,
			@RequestParam(name = "services", required = false) String[] perfiles) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<ServicioEjecutor> user = servicioEjecutorService.getById(idUser);
			if (user.isEmpty()) {
				response.put("mensaje", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(user.get().getIdUsuario());
			if (usuario.isEmpty()) {
				response.put("mensaje", "El id de usuario no se encuentra");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			servicioHabilitadoServiceImpl.deleteService(idUser);
			if (perfiles != null && perfiles.length > 0) {
				for (String item : perfiles) {
					Long idPerfil = Long.valueOf(item);
					servicioHabilitadoServiceImpl.insert(idUser, idPerfil);
				}
			}
			response.put("message", "Se registraron correctamente los servicios del usuario");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles_store");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/masterServSearch")
	public ResponseEntity<?> listarCatalogoServiciosFiltrado(
			@RequestParam(value = "idArea", required = false) Long areaId,
			@RequestParam(value = "nombre", required = false) String nombre,
			@RequestParam(value = "estado", required = false) String estado) {
		try {
			List<CatalogoServicio> servicios = catalogoServicioCustomService.findByFilters(areaId, nombre, estado);
			return ResponseEntity.ok(servicios);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@GetMapping("/masterServIndex")
	public ResponseEntity<?> listarOrgAreas() {
		try {
			List<OrgAreas> orgAreas = orgAreaService.findAll();
			return ResponseEntity.ok(orgAreas);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("mensaje", "Error al obtener las áreas");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/masterServ/show")
	public ResponseEntity<?> showCatalogoServicio(@RequestParam Long id) {
		Map<String, Object> response = new HashMap<>();
		CatalogoServicio catalogo = catalogoServicioService.findById(id);
		if (catalogo == null) {
			response.put("message", "No existe un servicio maestro con id: " + id);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(catalogo);
	}

	@PostMapping("/masterServ/update")
	public ResponseEntity<?> updateCatalogoServicio(@RequestParam(value = "id", required = true) Long id,
													@RequestParam(value = "idArea", required = false)Long id_area,
													@RequestParam(value = "nombre", required = false) String nombre,
													@RequestParam(value = "username", required = true) String username) {
		Map<String, Object> response = new HashMap<>();
		CatalogoServicio catalogo = catalogoServicioService.findById(id);
		if (catalogo == null) {
			response.put("message", "No existe un servicio maestro con id: " + id);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		OrgAreas area = orgAreaService.findById(id_area);
		if (area == null) {
			response.put("message", "No existe un área con id: " + id_area);
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		catalogo.setArea(area);
		catalogo.setNombre(nombre);
		catalogo.setUsuEditado(username);
		catalogo.setFechaEditado(new java.util.Date());
		catalogoServicioService.save(catalogo);
		response.put("message", "Actualizado correctamente");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/masterServ/create")
	public ResponseEntity<?> createCatalogoServicio(@RequestParam(value = "idArea", required = true) Long id_area,
												   @RequestParam(value = "nombre", required = true)  String nombre,
												   @RequestParam(value = "username", required = true)  String username) {
		Map<String, Object> response = new HashMap<>();
		OrgAreas area = orgAreaService.findById(id_area);
		if (area == null) {
			response.put("message", "No existe OrgAreas con id: " + id_area);
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		CatalogoServicio nuevo = new CatalogoServicio();
		nuevo.setArea(area);
		nuevo.setNombre(nombre);
		nuevo.setUsuCreado(username);
		nuevo.setEstadoRegistro("A");
		nuevo.setFechaCreado(new java.util.Date());
		catalogoServicioService.save(nuevo);
		response.put("message", "Creado correctamente");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/masterServ/changeStatus")
	public ResponseEntity<?> changeStatus(@RequestParam Long id,
											@RequestParam("username") String usuarioActualizacion) {
		Map<String, Object> response = new HashMap<>();
		try {
			CatalogoServicio reg = catalogoServicioService.findById(id);
			if (reg == null) {
				response.put("message", "El id [" + id + "] no se encuentra registrado.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			String estadoAnterior = reg.getEstadoRegistro();
			String nuevoEstado = estadoAnterior != null && estadoAnterior.equals("A") ? "I" : "A";
			reg.setEstadoRegistro(nuevoEstado);
			reg.setUsuEditado(usuarioActualizacion);
			reg.setFechaEditado(new Date());
			catalogoServicioService.save(reg);
			String mensaje = nuevoEstado.equals("A") ? "El registro se activó satisfactoriamente." : "El registro se desactivó satisfactoriamente.";
			response.put("message", mensaje);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error al actualizar el estado del registro.");
			response.put("Error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}

class EjecutorDTO {
	private Long id;
	private Long id_area;
	private Long id_usuario;
	private Integer estado;
	private String desArea;
	private String desGerencia;
	private List<Relacion> relaciones;
	private List<fonoDTO> telefonos;
	private List<emailDTO> correos;
	private String nombres;
	private String ape_paterno;
	private String ape_materno;
	private String usuario;

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

	public List<Relacion> getRelaciones() {
		return relaciones;
	}

	public void setRelaciones(List<Relacion> relaciones) {
		this.relaciones = relaciones;
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