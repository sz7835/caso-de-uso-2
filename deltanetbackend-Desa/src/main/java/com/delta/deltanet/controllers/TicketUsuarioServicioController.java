package com.delta.deltanet.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.CatalogoServicio;
import com.delta.deltanet.models.entity.Estado;
import com.delta.deltanet.models.entity.Historial;
import com.delta.deltanet.models.entity.JefeArea;
import com.delta.deltanet.models.entity.OrgAreas;
import com.delta.deltanet.models.entity.ServicioEjecutor;
import com.delta.deltanet.models.entity.ServicioHabilitado;
import com.delta.deltanet.models.entity.Ticket;
import com.delta.deltanet.models.service.IAutenticacionUsuarioService;
import com.delta.deltanet.models.service.ICatalogoServicioService;
import com.delta.deltanet.models.service.IEmailService;
import com.delta.deltanet.models.service.IEstadoService;
import com.delta.deltanet.models.service.IHistorialService;
import com.delta.deltanet.models.service.IJefeAreaService;
import com.delta.deltanet.models.service.IOrgAreaService;
import com.delta.deltanet.models.service.ITicketService;
import com.delta.deltanet.models.service.ITipoAccionService;
import com.delta.deltanet.models.service.SendEmailService;
import com.delta.deltanet.models.service.ServicioHabilitadoServiceImpl;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/ticket")
public class TicketUsuarioServicioController {

	@Autowired
	private ITicketService ticketService;
	@Autowired
	private IEstadoService estadoService;
	@Autowired
	private ITipoAccionService tipoAccionService;
	@Autowired
	private IAutenticacionUsuarioService autenticacionUsuarioService;
	@Autowired
	private IHistorialService historialService;
	@Autowired
	private IOrgAreaService areaService;
	@Autowired
	private ICatalogoServicioService catalogoServicioService;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private SendEmailService sendEmailService;
	@Autowired
	public ServicioHabilitadoServiceImpl servicioHabilitadoServiceImpl;
	@Autowired
	private IJefeAreaService jefeAreaService;

	@Value("#{${url}}")
	private String url;

	// VariableEntorno
	@Value("#{${tablas}}")
	private Map<String, String> tablas;
	@Value("#{${acciones}}")
	private Map<String, String> acciones;
	@Value("#{${accionesNew}}")
	private Map<String, String> accionesNew;

	@GetMapping("/usuario/ReadUsuarioServicio/{id}")
	public ResponseEntity<?> leeUsuario(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<AutenticacionUsuario> usuario = null;
		try {
			usuario = autenticacionUsuarioService.findById(id);
			if (!usuario.isPresent()) {
				response.put("mensaje", "No se encontro el usuario.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			response.put("usuario", usuario.get());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error al buscar usuario.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/usuario/ReadAllUsuarioServicio")
	public ResponseEntity<?> listaUsuarios() {
		Map<String, Object> response = new HashMap<>();
		List<AutenticacionUsuario> usuarios = null;
		try {
			usuarios = autenticacionUsuarioService.findAll();
			if (usuarios == null) {
				response.put("mensaje", "No se encontraron usuarios.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			response.put("usuarios", usuarios);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error al listar usuarios.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/usuario/UpdateUsuarioServicio")
	public ResponseEntity<?> actUsuario(@RequestParam("idUsuario") Long idUser,
			@RequestParam("nombre") String nombre,
			@RequestParam("apellido") String apellido,
			@RequestParam("rol") char rol,
			@RequestParam("userActu") String userActu) {
		Map<String, Object> response = new HashMap<>();
		Optional<AutenticacionUsuario> user = null;
		try {
			user = autenticacionUsuarioService.findById(idUser);
			if (!user.isPresent()) {
				response.put("mensaje", "Usuario no encontrado.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.put("mensaje", "Error al buscar usuario.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(acciones.get("EDITARID"))));
		historial.setTabla(tablas.get("USUARIO"));
		historial.setTablaId(idUser);
		historial.setAccion(acciones.get("EDITAR"));
		historial.setUsuCreado(userActu);
		historial.setFechaCreado(new Date());

		AutenticacionUsuario userBack = new AutenticacionUsuario();
		userBack.setId(user.get().getId());
		///// userBack.setCatalogoServicios(user.get().getCatalogoServicios());//Revisar
		///// si esto no genera conflictos
		// userBack.setNombre(user.getNombre());
		// userBack.setApellidos(user.getApellidos());
		// userBack.setRol(user.getRol());
		// userBack.setUsuCreado(user.getUsuCreado());
		// userBack.setFechaCreado(user.getFechaCreado());
		// userBack.setUsuEditado(user.getUsuEditado());
		// userBack.setFechaEditado(user.getFechaEditado());
		// userBack.setEstadoRegistro(user.getEstadoRegistro());

		try {
			// user.setNombre(nombre);
			// user.setApellidos(apellido);
			// user.setRol(rol);
			// user.setUsuEditado(userActu);
			// user.setFechaEditado(new Date());
			autenticacionUsuarioService.save(user.get());

			try {
				historialService.save(historial);
			} catch (DataAccessException e) {
				autenticacionUsuarioService.save(userBack);

				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put("mensaje", "Error al actualizar usuario.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Se actualizo el usuario satisfactoriamente.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PutMapping("/usuario/DeleteUsuarioServicio")
	public ResponseEntity<?> delUsuario(@RequestParam("idUsuario") Long idUser,
			@RequestParam("userActu") String userActu) {
		Map<String, Object> response = new HashMap<>();
		Optional<AutenticacionUsuario> user = null;
		try {
			user = autenticacionUsuarioService.findById(idUser);
			if (!user.isPresent()) {
				response.put("mensaje", "Usuario no encontrado.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.put("mensaje", "Error al buscar usuario.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(acciones.get("ELIMINARID"))));
		historial.setTabla(tablas.get("USUARIO"));
		historial.setTablaId(idUser);
		historial.setAccion(acciones.get("ELIMINAR"));
		historial.setUsuCreado(userActu);
		historial.setFechaCreado(new Date());

		AutenticacionUsuario userBack = new AutenticacionUsuario();
		userBack.setId(user.get().getId());
		///// userBack.setCatalogoServicios(user.get().getCatalogoServicios());//Revisar
		///// si esto no genera conflictos
		// userBack.setNombre(user.getNombre());
		// userBack.setApellidos(user.getApellidos());
		// userBack.setRol(user.getRol());
		// userBack.setUsuCreado(user.getUsuCreado());
		// userBack.setFechaCreado(user.getFechaCreado());
		// userBack.setUsuEditado(user.getUsuEditado());
		// userBack.setFechaEditado(user.getFechaEditado());
		// userBack.setEstadoRegistro(user.getEstadoRegistro());

		try {
			// user.setEstadoRegistro("B");
			// user.setUsuEditado(userActu);
			// user.setFechaEditado(new Date());
			autenticacionUsuarioService.save(user.get());

			try {
				historialService.save(historial);
			} catch (DataAccessException e) {
				autenticacionUsuarioService.save(userBack);

				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put("mensaje", "Error al eliminar usuario.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Se elimino el usuario satisfactoriamente.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/verUsuariosPaginado/read")
	public ResponseEntity<?> PaginadoVerUsuarios(@RequestParam("area_id") Optional<Long> idArea,
			@RequestParam("usuario") Optional<String> usuario,
			@RequestParam("nombre") Optional<String> nombre,
			@RequestParam("apellidos") Optional<String> apellidos,
			@RequestParam("length") int length,
			@RequestParam("start") int start,
			@RequestParam("draw") int draw) {

		Map<String, Object> response = new HashMap<>();

		Map<String, Object> usuariosObject = new HashMap<>();

		try {
			usuariosObject = autenticacionUsuarioService.findAllFiltroPaginado(idArea.orElse(null),
					usuario.orElse(null),
					nombre.orElse(null),
					apellidos.orElse(null),
					length,
					start);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("usuarios", usuariosObject.get("usuarios"));
		response.put("recordsFiltered", usuariosObject.get("total"));
		response.put("recordsTotal", usuariosObject.get("total"));
		response.put("draw", draw);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PutMapping("/usuario/sacarArea")
	public ResponseEntity<?> SacarArea(@RequestParam("id") Long idUsuario) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUsuario);

			if (!usuario.isPresent()) {
				response.put("mensaje", "Error al realizar la consulta, no se encontro el usuario");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			// usuario.setA(null);
			///// usuario.get().setCatalogoServicios(null);

			autenticacionUsuarioService.save(usuario.get());

		} catch (Exception e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": "));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Envío exitoso");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PutMapping("/usuario/guardarArea")
	public ResponseEntity<?> GuardarArea(@RequestParam("idUsuario") Long idUsuario,
			@RequestParam("area_id") Long idArea,
			@RequestParam("idCatalogos") List<Long> idCatalogos) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUsuario);
			OrgAreas area = areaService.findById(idArea);
			List<CatalogoServicio> catalogos = new ArrayList<>();

			if (usuario == null) {
				response.put("mensaje", "Error al realizar la consulta, no se encontro el usuario");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			if (area == null) {
				response.put("mensaje", "Error al realizar la consulta, no se encontro el area");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			for (Long idCatalogo : idCatalogos) {
				CatalogoServicio catalogo = catalogoServicioService.findById(idCatalogo);
				if (catalogo == null) {
					response.put("mensaje",
							"Error al realizar la consulta, el catalogo con id: " + idCatalogo + "no existe");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
				catalogos.add(catalogo);
			}

			// usuario.setArea(area);
			///// usuario.get().setCatalogoServicios(catalogos);

			autenticacionUsuarioService.save(usuario.get());

		} catch (Exception e) {
			response.put("mensaje", "Error al realizar la actualizacion");
			response.put("error", e.getMessage().concat(": "));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Cambio de servicio exitoso");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/usuario/info")
	public ResponseEntity<?> ReadUsuarioInfo(@RequestParam("id") Long idUsuario) {
		Map<String, Object> response = new HashMap<>();

		List<OrgAreas> areas = new ArrayList<>();
		List<Long> services = new ArrayList<>();

		try {

			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUsuario);

			if (!usuario.isPresent()) {
				response.put("mensaje", "Error al realizar la consulta, no se encontro el usuario");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			areas = areaService.findAll();

			List<ServicioHabilitado> serviciosHabilitados = servicioHabilitadoServiceImpl
					.buscarServicio(usuario.get().getId().intValue());

			for (ServicioHabilitado servicio : serviciosHabilitados) {
				services.add(servicio.getId().getServicioId());
			}

			response.put("usuario", usuario);
			response.put("areas", areas);
			response.put("services", services);

		} catch (Exception e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": "));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/usuario-servicios/estados")
	public ResponseEntity<Map<String, List<Estado>>> getAllEstados() {
		List<Estado> estados = estadoService.findAll();
		Map<String, List<Estado>> response = new HashMap<>();
		response.put("estados", estados);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/usuario/servicios/{id}")
	public ResponseEntity<?> UsuarioService(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticket = null;
		AutenticacionUsuario creador = null;
		CatalogoServicio service = null;
		List<Estado> estados = new ArrayList<Estado>();
		try {
			estados = estadoService.findAll();
			ticket = ticketService.findById(id);
			if (ticket == null) {
				response.put("mensaje", "No se encontro el ticket.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			creador = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
			if(creador.getPersona().getArea() != null) {
				creador.getPersona().getArea().setPuestos(null);
				creador.getPersona().getArea().setGerencia(null);
			}
			service = catalogoServicioService.findById(ticket.getCatalogoServicio().getId());
			List<ServicioHabilitado> listHab = servicioHabilitadoServiceImpl
					.buscarporServicio(ticket.getCatalogoServicio().getId());
			JefeArea jefeArea = jefeAreaService.buscaJefeArea(ticket.getAreaDestino().getId());
			if (jefeArea != null) {
				boolean exists = false;
				for (int i = 0; i < listHab.size(); i++) {
					if (listHab.get(i).getEjecutor().getUsuario().getId().equals(jefeArea.getIdUsuario())) {
						exists = true;
					}
				}
				if (exists != true) {
					Optional<AutenticacionUsuario> usuarioJefeArea = autenticacionUsuarioService
							.findById(jefeArea.getIdUsuario());
					ServicioHabilitado ser = new ServicioHabilitado();
					ServicioEjecutor eje = new ServicioEjecutor();
					eje.setUsuario(usuarioJefeArea.get());
					eje.setEstado(1);
					ser.setEjecutor(eje);
					listHab.add(ser);
				}
			}
			for (ServicioHabilitado ha : listHab) {
				if (ha.getEjecutor().getArea() != null) {
					ha.getEjecutor().getArea().setGerencia(null);
				}
				if (ha.getEjecutor().getUsuario().getPersona().getArea() != null) {
					ha.getEjecutor().getUsuario().getPersona().getArea().setGerencia(null);
					ha.getEjecutor().getUsuario().getPersona().getArea().setPuestos(null);
				}
				if (ha.getEjecutor().getUsuario().getPersona().getPuesto() != null) {
					ha.getEjecutor().getUsuario().getPersona().setPuesto(null);
				}
			}
			response.put("listHab", listHab);
			response.put("ticket", ticket);
			response.put("service", service);
			response.put("estados", estados);
			response.put("creador", creador);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error al buscar el tic.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/usuario/asignar")
	public ResponseEntity<?> AsignarUsuario(@RequestParam("asignar") String usuario,
			@RequestParam("idTicket") Long id,
			@RequestParam("usuario") String userCrea) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticket = null;
		Optional<AutenticacionUsuario> user = null;
		AutenticacionUsuario userCreator = null;
		String typeTitle = "";
		Historial historial = new Historial();
		try {
			ticket = ticketService.findById(id);
			user = autenticacionUsuarioService.findById(Long.parseLong(usuario));
			try {
				userCreator = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
				String type = (userCrea.equals(user.get().getUsuario())) ? "AUTO_ASIGNAR" : "ASIGNAR";
				typeTitle = (userCrea.equals(user.get().getUsuario())) ? "AUTOASIGNADO" : "ASIGNADO";
				historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get(type))));
				historial.setUsuAsignado(user.get().getUsuario());
				historial.setTabla(tablas.get("TICKET"));
				historial.setTablaId(id);
				String messageHistory = ((userCrea.equals(user.get().getUsuario()))
						? (userCrea + " se auto asignó el ticket ")
						: (userCrea + " asignó a " + historial.getUsuAsignado() + " el ticket "));
				historial.setAccion(messageHistory);
				historial.setUsuCreado(userCrea);
				historial.setFechaCreado(new Date());
				historialService.save(historial);
				ticket.setAutenticacionUsuario(user.get());
				ticketService.save(ticket);
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put("mensaje", "Error al buscar el ticket.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Object> emailsByPersonForUser = this.emailService.findAllPer(user.get().getPersona().getId());
		Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
		String getEmailForUser = "";

		while (itForEmailUser != null && itForEmailUser.hasNext()) {
			Object[] row = (Object[]) itForEmailUser.next();
			getEmailForUser = String.valueOf(row[3]);
			break;
		}

		try {
			String title = "deltanet - ticket " + ticket.getId() + " " + typeTitle.toLowerCase();
			String message = historial.getAccion() + ticket.getId() + ", asunto: " + ticket.getTitulo();
			String address = url + "dashboard/ticket/detalle/" + ticket.getId();
			if (userCreator != null) {
				List<Object> emailsByPersonForUserCreator = this.emailService
						.findAllPer(userCreator.getPersona().getId());
				Iterator<Object> it = emailsByPersonForUserCreator != null ? emailsByPersonForUserCreator.iterator()
						: null;
				String getEmailForUserCreator = "";
				while (it != null && it.hasNext()) {
					Object[] row = (Object[]) it.next();
					getEmailForUserCreator = String.valueOf(row[3]);
					break;
				}
				sendEmailService.sendMailTicketMultiple(getEmailForUser, getEmailForUserCreator, title, typeTitle,
						"Estimado colaborador,", message, address);
			} else {
				sendEmailService.sendMailTicket(getEmailForUser, title, typeTitle, "Estimado colaborador,", message,
						address);
			}
			response.put("mensaje", "Se derivo al nuevo usuario el ticket #" + ticket.getId() + " exitosamente.");
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", "Se derivo al nuevo usuario el ticket #" + ticket.getId()
					+ " exitosamente, pero no se pudo enviar el correo.");
		}
		if (ticket.getAreaDestino().getGerencia() != null) {
			ticket.getAreaDestino().getGerencia().setOrgAreas(null);
		}
		ticket.getAreaDestino().setPuestos(null);
		ticket.getAreaOrigen().setPuestos(null);
		if (ticket.getAreaOrigen().getGerencia() != null) {
			ticket.getAreaOrigen().getGerencia().setOrgAreas(null);
		}
		response.put("ticket", ticket);
		response.put("usuario", user.get());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}