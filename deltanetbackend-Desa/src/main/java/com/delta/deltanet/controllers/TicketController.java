package com.delta.deltanet.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.dao.IOrgAreasDao;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private ITicketService ticketService;
	@Autowired
	private IPrioridadService prioridadService;
	@Autowired
	private IOrgAreaService areaService;
	// @Autowired
	// private IAreaService areaService;
	@Autowired
	private IArchivoService archivoService;
	@Autowired
	private ICategoriaService categoriaService;
	@Autowired
	private IComentarioService comentarioService;
	@Autowired
	private IEstadoService estadoService;
	@Autowired
	private ITipoAccionService tipoAccionService;
	@Autowired
	private ICatalogoServicioService catalogoServicioService;
	@Autowired
	private IHistorialService historialService;
	@Autowired
	private IAutenticacionUsuarioService autenticacionUsuarioService;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private SendEmailService sendEmailService;
	@Autowired
	private IJefeAreaService jefeAreaService;
	@Autowired
	public ISisParamService parametroService;
	@Autowired
	public ServicioHabilitadoServiceImpl servicioHabilitadoServiceImpl;
	@Autowired
	private ServicioEjecutorServiceImpl servicioEjecutorService;
	@Autowired
	private CalendarioServiceImpl calendarioService;
	@Autowired
	private IOutFeriadoService feriadoService;

	// VariableEntorno
	@Value("#{${tablas}}")
	private Map<String, String> tablas;
	@Value("#{${acciones}}")
	private Map<String, String> acciones;
	@Value("#{${accionesNew}}")
	private Map<String, String> accionesNew;
	@Value("#{${url}}")
	private String url;

	// Variables Properties del correo
	@Value("${spring.mail.host}")
	private String envHost;
	@Value("${spring.mail.port}")
	private String envPort;
	@Value("${spring.mail.username}")
	private String envUsername;
	@Value("${spring.mail.password}")
	private String envPassword;
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String envAuth;
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String envStartTls;

	// TICKET
	@PostMapping("/create")
	public ResponseEntity<?> CreateTicket(@RequestParam("titulo") String titulo,
			@RequestParam("descripcion") String descripcion, @RequestParam("tipoUsuario") String tipoUsuario,
			@RequestParam("idPrioridad") Long idPrioridad, @RequestParam("idCategoria") Long idCategoria,
			@RequestParam("idCatalogoServicio") Long idCatalogoServicio,
			@RequestParam("idAreaOrigen") Long idAreaOrigen, @RequestParam("idAreaDestino") Long idAreaDestino,
			@RequestParam("usuCrea") String usuCrea) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticket = new Ticket();
		Prioridad prioridad = prioridadService.findById(idPrioridad);
		Categoria categoria = categoriaService.findById(idCategoria);
		CatalogoServicio catalogoServicio = catalogoServicioService.findById(idCatalogoServicio);
		OrgAreas areaOrigen = areaService.findById(idAreaOrigen);
		OrgAreas areaDestino = areaService.findById(idAreaDestino);
		Estado estado = estadoService.findById(Long.valueOf(1));
		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("CREAR"))));
		historial.setTabla(tablas.get("TICKET"));
		String messageHistory = usuCrea + " creó el ticket ";
		historial.setAccion(messageHistory);
		historial.setUsuCreado(usuCrea);
		historial.setFechaCreado(new Date());
		Ticket ticketCreado = new Ticket();
		try {
			ticket.setTitulo(titulo);
			ticket.setDescripcion(descripcion);
			ticket.setUsuarioCreador(usuCrea);
			ticket.setTipoUsuarioCreador(tipoUsuario);
			ticket.setAreaDestino(areaDestino);
			ticket.setAreaOrigen(areaOrigen);
			ticket.setCatalogoServicio(catalogoServicio);
			ticket.setCategoria(categoria);
			ticket.setPrioridad(prioridad);
			ticket.setEstado(estado);
			ticket.setUsuCreado(usuCrea);
			ticket.setFechaCreado(new Date());
			JefeArea jefeArea = jefeAreaService.buscaJefeArea(ticket.getAreaDestino().getId());
			AutenticacionUsuario autenticacionUsuario = null;
			if (jefeArea != null) {
				Optional<AutenticacionUsuario> usuarioJefeArea = autenticacionUsuarioService
						.findById(jefeArea.getIdUsuario());
				ticket.setAutenticacionUsuario(usuarioJefeArea.get());
			} else {
				List<ServicioHabilitado> listHab = servicioHabilitadoServiceImpl
						.buscarporServicio((catalogoServicio.getId()));
				if (listHab.size() > 0) {
					ServicioHabilitado first = listHab.get(0);
					Optional<ServicioEjecutor> userOp = servicioEjecutorService
							.getById(first.getId().getEjecutorId().longValue());
					ServicioEjecutor user = null;
					if (!userOp.isEmpty()) {
						user = userOp.get();
						autenticacionUsuario = user.getUsuario();
					}
					if (autenticacionUsuario == null) {
						response.put("mensaje",
								"Error, no existe un supervisor o ejecutor para el servicio seleccionado");
						return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					} else {
						ticket.setAutenticacionUsuario(autenticacionUsuario);
					}
				} else {
					response.put("mensaje", "Error, no existe un supervisor o ejecutor para el servicio seleccionado");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

			ticketCreado = ticketService.save(ticket);
			try {
				historial.setTablaId(ticketCreado.getId());
				historialService.save(historial);
				Historial historialAsignado = new Historial();
				historialAsignado.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ASIGNAR"))));
				historialAsignado.setTabla(tablas.get("TICKET"));
				historialAsignado.setTablaId(ticketCreado.getId());
				historialAsignado.setUsuCreado(usuCrea);
				historialAsignado.setFechaCreado(new Date());
				messageHistory = "El sistema asignó a " + ticketCreado.getAutenticacionUsuario().getUsuario()
						+ " el ticket ";
				historialAsignado.setAccion(messageHistory);
				historialService.save(historialAsignado);
			} catch (DataAccessException e) {
				estadoService.delete(ticketCreado.getId());
				response.put("mensaje", "Error al realizar el insert en la base de datos 1");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos 2");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			List<Object> emailsByPersonForUser = this.emailService
					.findAllPer(ticket.getAutenticacionUsuario().getPersona().getId());
			Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
			String getEmailForUser = "";

			while (itForEmailUser != null && itForEmailUser.hasNext()) {
				Object[] row = (Object[]) itForEmailUser.next();
				getEmailForUser = String.valueOf(row[3]);
				break;
			}

			String message = "ticket " + ticket.getId() + " asignado, asunto: " + ticket.getTitulo();
			String address = url + "dashboard/ticket/detalle/" + ticket.getId();
			sendEmailService.sendMailTicket(getEmailForUser, "deltanet - ticket " + ticket.getId() + " asignado",
					"ASIGNADO", "Estimado colaborador,", message, address);
			response.put("mensaje", "Ticket " + ticket.getId() + " creado");
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", "Ticket " + ticket.getId() + " creado, no se pudo enviar correo");
		}
		ticketCreado.getAreaDestino().setGerencia(null);
		ticketCreado.getAreaDestino().setPuestos(null);
		ticketCreado.getAreaOrigen().setGerencia(null);
		ticketCreado.getAreaOrigen().setPuestos(null);
		response.put("ticket", ticketCreado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/read/{id}")
	public ResponseEntity<?> ReadTicket(@PathVariable Long id) {
		Ticket ticket = null;
		AutenticacionUsuario creador = null;
		List<Archivo> archivosTicket = null;
		List<Historial> histories = new ArrayList<>();
		List<Comentario> comentarios = null;
		Map<String, Object> response = new HashMap<>();
		Map<Long, Object> comentArchivos = new HashMap<>();

		try {
			ticket = ticketService.findById(id);
			if (ticket == null) {
				response.put("mensaje",
						"El ticket ID: ".concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			creador = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
			if(creador.getPersona().getArea() != null) {
				creador.getPersona().getArea().setPuestos(null);
				creador.getPersona().getArea().setGerencia(null);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			archivosTicket = archivoService.findByTablaAndTablaId("ticket", ticket.getId());
			comentarios = comentarioService.findAllByTicket(id);

			for (Comentario comentario : comentarios) {
				List<Archivo> archivosComentario = archivoService.findByTablaAndTablaId("comentario",
						comentario.getId());
				comentArchivos.put(comentario.getId(), archivosComentario);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			histories = historialService.findAllByItem("ticket", ticket.getId());
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(ticket.getAreaDestino().getGerencia() != null) {
			ticket.getAreaDestino().getGerencia().setOrgAreas(null);
		}
		ticket.getAreaDestino().setPuestos(null);
		ticket.getAreaOrigen().setPuestos(null);
		if(ticket.getAreaOrigen().getGerencia() != null) {
			ticket.getAreaOrigen().getGerencia().setOrgAreas(null);
		}
		response.put("histories", histories);
		response.put("creador", creador);
		response.put("ticket", ticket);
		response.put("archivosTicket", archivosTicket);
		response.put("comentarios", comentarios);
		response.put("archivosComentarios", comentArchivos);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/read")
	public ResponseEntity<?> ReadAllTicket(@RequestParam("idAreaOrigen") Optional<Long> idAreaOrigen,
			@RequestParam("idAreaDestino") Optional<Long> idAreaDestino,
			@RequestParam("tipoUsuario") Optional<String> tipoUsuario,
			@RequestParam("idPrioridad") Optional<Long> idPrioridad,
			@RequestParam("idCategoria") Optional<Long> idCategoria,
			@RequestParam("idCatalogoServicio") Optional<Long> idCatalogoServicio,
			@RequestParam("usuCrea") String usuarioCrea) {
		Map<String, Object> response = new HashMap<>();

		List<Ticket> tickets = new ArrayList<>();

		try {
			tickets = ticketService.findAllFiltro(idAreaOrigen.orElse(null), idAreaDestino.orElse(null),
					tipoUsuario.orElse(null), idPrioridad.orElse(null), idCategoria.orElse(null),
					idCatalogoServicio.orElse(null), usuarioCrea);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("tickets", tickets);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> UpdateTicket(@PathVariable Long id, @RequestParam("descripcion") String descripcion,
			@RequestParam("idPrioridad") Long idPrioridad, @RequestParam("idCatalogoServicio") Long idCatalogoServicio,
			@RequestParam("usuario") String usuarioActualizacion) {
		Map<String, Object> response = new HashMap<>();

		Ticket ticketActual = ticketService.findById(id);
		Prioridad prioridad = prioridadService.findById(idPrioridad);
		CatalogoServicio catalogoServicio = catalogoServicioService.findById(idCatalogoServicio);
		AutenticacionUsuario autenticacionUsuario = null;
		if (ticketActual == null) {
			response.put("mensaje", "Error: no se puede editar, el ticket ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		Ticket ticketBack = new Ticket();
		ticketBack.setId(ticketActual.getId());
		ticketBack.setAreaDestino(ticketActual.getAreaDestino());
		ticketBack.setAreaOrigen(ticketActual.getAreaOrigen());
		ticketBack.setCatalogoServicio(ticketActual.getCatalogoServicio());
		ticketBack.setCategoria(ticketActual.getCategoria());
		ticketBack.setPrioridad(ticketActual.getPrioridad());
		ticketBack.setAutenticacionUsuario(ticketActual.getAutenticacionUsuario());
		ticketBack.setUsuarioCreador(ticketActual.getUsuarioCreador());
		ticketBack.setTipoUsuarioCreador(ticketActual.getTipoUsuarioCreador());
		ticketBack.setTitulo(ticketActual.getTitulo());
		ticketBack.setDescripcion(ticketActual.getDescripcion());
		ticketBack.setUsuCreado(ticketActual.getUsuCreado());
		ticketBack.setFechaCreado(ticketActual.getFechaCreado());
		ticketBack.setUsuEditado(ticketActual.getUsuEditado());
		ticketBack.setFechaEditado(ticketActual.getFechaEditado());

		Historial historial = new Historial();
		String messageHistory = "";
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("EDITAR_TICKET"))));
		historial.setTabla(tablas.get("TICKET"));
		historial.setTablaId(id);
		messageHistory = usuarioActualizacion + " editó el ticket ";
		historial.setAccion(messageHistory);
		historial.setUsuCreado(usuarioActualizacion);
		historial.setFechaCreado(new Date());
		switch (ticketActual.getEstado().getId().intValue()) {
			case 6:
				Estado estado = estadoService.findById(Long.valueOf(1));
				ticketActual.setEstado(estado);
				break;
			default:
				ticketActual.setEstado(ticketActual.getEstado());
				break;
		}
		try {
			ticketActual.setDescripcion(descripcion);
			ticketActual.setPrioridad(prioridad);
			if (catalogoServicio.getId() != ticketBack.getCatalogoServicio().getId()) {
				JefeArea jefeArea = jefeAreaService.buscaJefeArea(ticketBack.getAreaDestino().getId());
				if (jefeArea != null) {
					Optional<AutenticacionUsuario> usuarioJefeArea = autenticacionUsuarioService
							.findById(jefeArea.getIdUsuario());
					ticketActual.setAutenticacionUsuario(usuarioJefeArea.get());
				} else {
					List<ServicioHabilitado> listHab = servicioHabilitadoServiceImpl
							.buscarporServicio((catalogoServicio.getId()));
					ServicioHabilitado first = listHab.get(0);
					Optional<ServicioEjecutor> userOp = servicioEjecutorService
							.getById(first.getId().getEjecutorId().longValue());
					ServicioEjecutor user = null;
					if (!userOp.isEmpty()) {
						user = userOp.get();
						autenticacionUsuario = user.getUsuario();
					}
					if (autenticacionUsuario == null) {
						response.put("mensaje", "Error, no existen usuarios para el catalogo seleccionado");
						return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					} else {
						ticketActual.setAutenticacionUsuario(autenticacionUsuario);
					}
				}
				ticketActual.setCatalogoServicio(catalogoServicio);
			}
			ticketActual.setFechaEditado(new Date());
			ticketActual.setUsuEditado(usuarioActualizacion);

			ticketService.save(ticketActual);

			try {
				historialService.save(historial);
			} catch (DataAccessException e) {
				ticketService.save(ticketBack);

				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el ticket en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (ticketBack.getAutenticacionUsuario().getUsuario() != ticketActual.getAutenticacionUsuario().getUsuario()) {
			try {
				Historial historialAsignado = new Historial();
				historialAsignado.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ASIGNAR"))));
				historialAsignado.setTabla(tablas.get("TICKET"));
				historialAsignado.setTablaId(ticketActual.getId());
				historialAsignado.setUsuCreado(usuarioActualizacion);
				historialAsignado.setFechaCreado(new Date());
				messageHistory = "El sistema asignó a " + ticketActual.getAutenticacionUsuario().getUsuario()
						+ " el ticket ";
				historialAsignado.setAccion(messageHistory);
				historialService.save(historialAsignado);
				List<Object> emailsByPersonForUser = this.emailService
						.findAllPer(ticketActual.getAutenticacionUsuario().getPersona().getId());
				Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator()
						: null;
				String getEmailForUser = "";

				while (itForEmailUser != null && itForEmailUser.hasNext()) {
					Object[] row = (Object[]) itForEmailUser.next();
					getEmailForUser = String.valueOf(row[3]);
					break;
				}
				String message = "Ticket " + ticketActual.getId() + " asignado, asunto: " + ticketActual.getTitulo();
				String address = url + "dashboard/ticket/detalle/" + ticketActual.getId();
				sendEmailService.sendMailTicket(getEmailForUser,
						"deltanet - ticket " + ticketActual.getId() + " asignado", "ASIGNADO", "Estimado colaborador,",
						message, address);
			} catch (Exception e) {
				response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
				response.put("mensaje", "Ticket actualizado, no se envió el correo");
			}
		} else {
			response.put("mensaje", "Ticket actualizado");
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteTicket(@PathVariable Long id, @RequestParam("usuario") String usuarioActualizacion) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticketActual = ticketService.findById(id);
		if (ticketActual == null) {
			response.put("mensaje", "Error: no se puede editar, el ticket ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		Estado estado = estadoService.findById(Long.valueOf(7));
		ticketActual.setEstado(estado);
		ticketActual.setUsuEditado(usuarioActualizacion);
		ticketActual.setFechaEditado(new Date());
		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ELIMINAR_TICKET"))));
		historial.setTabla(tablas.get("TICKET"));
		historial.setTablaId(id);
		String messageHistory = usuarioActualizacion + " eliminó el ticket ";
		historial.setAccion(messageHistory);
		historial.setUsuCreado(usuarioActualizacion);
		historial.setFechaCreado(new Date());
		Comentario comentario = new Comentario();
		comentario.setTicket(ticketActual);
		comentario.setUsuario(usuarioActualizacion);
		comentario.setDescripcion(usuarioActualizacion + " eliminó el ticket");
		comentario.setVisibilidad('S');
		comentario.setUsuCreado(usuarioActualizacion);
		comentario.setFechaCreado(new Date());
		comentarioService.save(comentario);
		try {
			historialService.save(historial);
			try {
				ticketService.save(ticketActual);
			} catch (DataAccessException e) {
				historialService.delete(historial.getId());

				response.put("mensaje", "Error al realizar el delete en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			response.put("mensaje", "No se elimino el ticket.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try {
			List<Object> emailsByPersonForUser = this.emailService
					.findAllPer(ticketActual.getAutenticacionUsuario().getPersona().getId());
			Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
			String getEmailForUser = "";
			while (itForEmailUser != null && itForEmailUser.hasNext()) {
				Object[] row = (Object[]) itForEmailUser.next();
				getEmailForUser = String.valueOf(row[3]);
				break;
			}
			String message = "Ticket " + ticketActual.getId() + " eliminado, asunto: " + ticketActual.getTitulo();
			String address = url + "dashboard/ticket/detalle/" + ticketActual.getId();
			sendEmailService.sendMailTicket(getEmailForUser, "deltanet - ticket " + ticketActual.getId() + " eliminado",
					"ELIMINADO", "Estimado colaborador,", message, address);
			response.put("mensaje", "Ticket eliminado satisfactoriamente.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Ticket eliminado satisfactoriamente, pero no se pudo enviar el correo.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
	}

	// MI VISTA
	@GetMapping("/mivista/read")
	public ResponseEntity<?> ReadMiVista(@RequestParam("usuario") String usuario,
			@RequestParam("idUsuario") Optional<Long> idUsuario,
			@RequestParam("idArea") Optional<Long> idArea,
			@RequestParam("rol") Optional<Long> rol) {

		Map<String, Object> response = new HashMap<>();

		List<Ticket> ticketsAsignados = new ArrayList<>();
		List<Ticket> ticketsNoAsignados = new ArrayList<>();
		List<Ticket> ticketsReportadosPorMi = new ArrayList<>();
		List<Ticket> ticketsResueltos = new ArrayList<>();
		List<Ticket> ticketsModificados = new ArrayList<>();

		ticketsReportadosPorMi = ticketService.findAllByUsuarioCreador(usuario);

		for (Ticket ticket : ticketsReportadosPorMi) {
			ticket.getAutenticacionUsuario().getPersona().setArea(null);
			ticket.getAutenticacionUsuario().getPersona().setPuesto(null);
			if(ticket.getAreaDestino() != null) {
				ticket.getAreaDestino().setPuestos(null);
				ticket.getAreaDestino().setGerencia(null);
			}
			if(ticket.getAreaOrigen() != null) {
				ticket.getAreaOrigen().setPuestos(null);
				ticket.getAreaOrigen().setGerencia(null);
			}
		}
		if (idUsuario.isPresent()) {
			List<Ticket> ticketsModAll = ticketService.findAllModificados(usuario, idUsuario.get());
			ticketsAsignados = ticketService.findAllByAutenticacionUsuario(idUsuario.get());
			for (Ticket ticket : ticketsAsignados) {
				ticket.getAutenticacionUsuario().getPersona().setArea(null);
				ticket.getAutenticacionUsuario().getPersona().setPuesto(null);
				if(ticket.getAreaDestino() != null) {
					ticket.getAreaDestino().setPuestos(null);
					ticket.getAreaDestino().setGerencia(null);
				}
				if(ticket.getAreaOrigen() != null) {
					ticket.getAreaOrigen().setPuestos(null);
					ticket.getAreaOrigen().setGerencia(null);
				}
			}
			ticketsNoAsignados = ticketService.findAllByAutenticacionUsuarioNull();
			ticketsResueltos = ticketService.findAllResueltos(usuario, idUsuario.get(), idArea.get());

			for (Ticket ticket : ticketsResueltos) {
				ticket.getAutenticacionUsuario().getPersona().setArea(null);
				ticket.getAutenticacionUsuario().getPersona().setPuesto(null);
				if(ticket.getAreaDestino() != null) {
					ticket.getAreaDestino().setPuestos(null);
					ticket.getAreaDestino().setGerencia(null);
				}
				if(ticket.getAreaOrigen() != null) {
					ticket.getAreaOrigen().setPuestos(null);
					ticket.getAreaOrigen().setGerencia(null);
				}
			}
			for (Ticket ticket : ticketsModAll) {
				if(ticket.getAreaDestino() != null) {
					ticket.getAreaDestino().setPuestos(null);
					ticket.getAreaDestino().setGerencia(null);
				}
				if(ticket.getAreaOrigen() != null) {
					ticket.getAreaOrigen().setPuestos(null);
					ticket.getAreaOrigen().setGerencia(null);
				}
				if (ticket.getFechaEditado().after(
						Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
					ticketsModificados.add(ticket);
				}
			}
		} else {
			List<Ticket> ticketsModAll = ticketService.findAllModificados();
			ticketsResueltos = ticketService.findAllResueltosByUsuarioCreador(usuario);

			for (Ticket ticket : ticketsResueltos) {
				ticket.getAutenticacionUsuario().getPersona().setArea(null);
				ticket.getAutenticacionUsuario().getPersona().setPuesto(null);
				if(ticket.getAreaDestino() != null) {
					ticket.getAreaDestino().setPuestos(null);
					ticket.getAreaDestino().setGerencia(null);
				}
				if(ticket.getAreaOrigen() != null) {
					ticket.getAreaOrigen().setPuestos(null);
					ticket.getAreaOrigen().setGerencia(null);
				}
			}
			for (Ticket ticket : ticketsModAll) {
				if(ticket.getAreaDestino() != null) {
					ticket.getAreaDestino().setPuestos(null);
					ticket.getAreaDestino().setGerencia(null);
				}
				if(ticket.getAreaOrigen() != null) {
					ticket.getAreaOrigen().setPuestos(null);
					ticket.getAreaOrigen().setGerencia(null);
				}
				if (ticket.getFechaEditado().after(
						Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
					if (ticket.getUsuarioCreador() == usuario) {
						ticketsModificados.add(ticket);
					}
				}
			}
		}
		response.put("ticketsAsignados", ticketsAsignados);
		response.put("ticketsNoAsignados", ticketsNoAsignados);
		response.put("ticketsReportadosPorMi", ticketsReportadosPorMi);
		response.put("ticketsResueltos", ticketsResueltos);
		response.put("ticketsModificados", ticketsModificados);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/mivista/read/historial")
	public ResponseEntity<?> ReadMiVistaHistorial(@RequestParam("usuario") String usuario,
			@RequestParam("idUsuario") Optional<Long> idUsuario,
			@RequestParam("idArea") Optional<Long> idArea,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "50") int size) {

		// Obtener el valor del parámetro de paginación desde la base de datos
		SisParam param = parametroService.buscaEtiqueta("TICKET_HISTORY_PAGINATION");
		if (param != null) {
			try {
				size = Integer.parseInt(param.getValor());
			} catch (NumberFormatException e) {
				size = 50;
			}
		}
		Set<Long> ticketIds = new HashSet<>();

		// Añadir IDs de tickets creados por el usuario
		ticketService.findAllByUsuarioCreador(usuario)
				.forEach(ticket -> ticketIds.add(ticket.getId()));

		if (idUsuario.isPresent()) {
			ticketService.findAllByAutenticacionUsuario(idUsuario.get())
					.forEach(ticket -> ticketIds.add(ticket.getId()));

			/*
			 * ticketService.findAllByAutenticacionUsuarioNull()
			 * .forEach(ticket -> ticketIds.add(ticket.getId()));
			 */

			ticketService.findAllResueltos(usuario, idUsuario.get(), idArea.get())
					.forEach(ticket -> ticketIds.add(ticket.getId()));
		} else {
			ticketService.findAllResueltosByUsuarioCreador(usuario)
					.forEach(ticket -> ticketIds.add(ticket.getId()));
		}

		// Get historial de tickets
		List<Historial> historialAll = new ArrayList<>();
		for (Long ticketId : ticketIds) {
			historialAll.addAll(historialService.findHistorialTicket(ticketId));
		}
		historialAll.sort(Comparator.comparing(Historial::getFechaCreado).reversed());

		// Aplicar pagination
		int totalElements = historialAll.size();
		size = Math.min(size, totalElements);
		int start = Math.min(page * size, totalElements);
		int end = Math.min(start + size, totalElements);

		List<Historial> historialPaginadoContent = (start < end) ? historialAll.subList(start, end) : new ArrayList<>();

		Map<String, Object> historialPaginado = new HashMap<>();
		historialPaginado.put("content", historialPaginadoContent);
		historialPaginado.put("totalElements", totalElements);
		historialPaginado.put("totalPages", (int) Math.ceil((double) totalElements / size));
		historialPaginado.put("currentPage", page);
		historialPaginado.put("pageSize", size);

		Map<String, Object> response = new HashMap<>();
		response.put("historial", historialPaginado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	// GENERAR TICKET READ
	@GetMapping("/generarTicket/read")
	public ResponseEntity<?> ReadGenerarTicket() {

		Map<String, Object> response = new HashMap<>();

		List<OrgAreas> areas = areaService.findAll();
		List<Prioridad> prioridades = prioridadService.findAll();
		List<Categoria> categorias = categoriaService.findAll();

		for (OrgAreas area : areas) {
			area.setGerencia(null);
			area.setPuestos(null);
		}
		response.put("areas", areas);
		response.put("prioridades", prioridades);
		response.put("categorias", categorias);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	// EDITAR TICKET
	@GetMapping("/editarTicket/read/{id}")
	public ResponseEntity<?> ListarEditarTicket(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		Ticket ticket = ticketService.findById(id);
		List<Prioridad> prioridades = prioridadService.findAll();
		if (ticket == null) {
			response.put("mensaje", "Error: no se puede ver, el ticket ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		List<CatalogoServicio> servicios = catalogoServicioService.findByArea(ticket.getAreaDestino());
		List<OrgAreas> areas = areaService.findAll();

		for (CatalogoServicio are : servicios) {
			if(are.getArea() != null) {
				are.getArea().getGerencia().setOrgAreas(null);
				are.getArea().setPuestos(null);
			}
		}
		for (OrgAreas area : areas) {
			area.setGerencia(null);
			area.setPuestos(null);
		}
		response.put("areas", areas);
		response.put("ticket", ticket);
		response.put("prioridades", prioridades);
		response.put("servicios", servicios);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	// VER TICKETS
	@GetMapping("/verTickets/read")
	public ResponseEntity<?> ListarVerTickets() {

		Map<String, Object> response = new HashMap<>();

		List<AutenticacionUsuario> usuariosDeServicio = autenticacionUsuarioService.findAll();
		for(AutenticacionUsuario user: usuariosDeServicio) {
			if(user.getPersona().getArea() != null) {
				user.getPersona().getArea().getGerencia().setOrgAreas(null);
				user.getPersona().getArea().setPuestos(null);
			}
			if(user.getPersona().getPuesto() != null) {
				user.getPersona().getPuesto().getArea().setPuestos(null);
			}
		}
		List<OrgAreas> areas = areaService.findAll();
		for(OrgAreas area: areas) {
			area.getGerencia().setOrgAreas(null);
			area.setPuestos(null);
			
		}
		List<Prioridad> prioridades = prioridadService.findAll();
		List<Categoria> categorias = categoriaService.findAll();

		response.put("usuariosDeServicio", usuariosDeServicio);
		response.put("areas", areas);
		response.put("prioridades", prioridades);
		response.put("categorias", categorias);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/verTicketsPaginado/read")
	public ResponseEntity<?> PaginadoVerTickets(@RequestParam("idAreaOrigen") Optional<Long> idAreaOrigen,
			@RequestParam("idAreaDestino") Optional<Long> idAreaDestino,
			@RequestParam("usuarioServicioId") Optional<Long> usuarioServicio,
			@RequestParam("idPrioridad") Optional<Long> idPrioridad,
			@RequestParam("idCategoria") Optional<Long> idCategoria,
			@RequestParam("idCatalogoServicio") Optional<Long> idCatalogoServicio,
			@RequestParam("idTicket") Optional<Long> idTicket, @RequestParam("usuCrea") Optional<String> usuarioCrea,
			@RequestParam("startDate") Optional<String> startDate, @RequestParam("endDate") Optional<String> endDate) {

		Map<String, Object> response = new HashMap<>();

		Map<String, Object> ticketsObject = new HashMap<>();

		try {
			ticketsObject = ticketService.findAllFiltroPaginado(idAreaOrigen.orElse(null), idAreaDestino.orElse(null),
					usuarioServicio.orElse(null), idPrioridad.orElse(null), idCategoria.orElse(null),
					idCatalogoServicio.orElse(null), idTicket.orElse(null), usuarioCrea.orElse(null),
					startDate.orElse(null), endDate.orElse(null));
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("tickets", ticketsObject.get("tickets"));
		response.put("recordsFiltered", ticketsObject.get("total"));
		response.put("recordsTotal", ticketsObject.get("total"));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/reassign/{id}")
	public ResponseEntity<?> ReasignarArea(@PathVariable Long id,
			@RequestParam("idAreaDestino") Long idAreaDestino,
			@RequestParam("idCatalogoServicio") Long idCatalogoServicio,
			@RequestParam("usuEditado") String usuEditado) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticket = ticketService.findById(id);
		try {
			if(ticket != null) {
				OrgAreas areaDestino = areaService.findById(idAreaDestino);
				CatalogoServicio catalogoServicio = catalogoServicioService.findById(idCatalogoServicio);
				ticket.setAreaDestino(areaDestino);
				ticket.setCatalogoServicio(catalogoServicio);
				ticket.setUsuEditado(usuEditado);
				ticket.setFechaEditado(new Date());
				JefeArea jefeArea = jefeAreaService.buscaJefeArea(ticket.getAreaDestino().getId());
				AutenticacionUsuario autenticacionUsuario = null;
				if (jefeArea != null) {
					Optional<AutenticacionUsuario> usuarioJefeArea = autenticacionUsuarioService
							.findById(jefeArea.getIdUsuario());
					ticket.setAutenticacionUsuario(usuarioJefeArea.get());
				} else {
					List<ServicioHabilitado> listHab = servicioHabilitadoServiceImpl
							.buscarporServicio((catalogoServicio.getId()));
					if (listHab.size() > 0) {
						ServicioHabilitado first = listHab.get(0);
						Optional<ServicioEjecutor> userOp = servicioEjecutorService
								.getById(first.getId().getEjecutorId().longValue());
						ServicioEjecutor user = null;
						if (!userOp.isEmpty()) {
							user = userOp.get();
							autenticacionUsuario = user.getUsuario();
						}
						if (autenticacionUsuario == null) {
							response.put("mensaje", "Error, no existe un supervisor o ejecutor para el servicio seleccionado");
							return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
						} else {
							ticket.setAutenticacionUsuario(autenticacionUsuario);
						}
					}else {
						response.put("mensaje", "Error, no existe un supervisor o ejecutor para el servicio seleccionado");
						return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}else {
				response.put("mensaje", "El ticket no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String message = usuEditado + " reasignó a " + ticket.getAutenticacionUsuario().getUsuario() + " el ticket";

		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(16L));
		historial.setTabla(tablas.get("TICKET"));
		historial.setTablaId(id);
		historial.setAccion(message);
		historial.setUsuCreado(usuEditado);
		historial.setFechaCreado(new Date());
		historialService.save(historial);
		List<Object> emailsByPersonForUser = this.emailService
				.findAllPer(ticket.getAutenticacionUsuario().getPersona().getId());
		Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
		String getEmailForUser = "";
		while (itForEmailUser != null && itForEmailUser.hasNext()) {
			Object[] row = (Object[]) itForEmailUser.next();
			getEmailForUser = String.valueOf(row[3]);
			break;
		}
		String address = url + "dashboard/ticket/detalle/" + ticket.getId();
		String type = "reasignado";
		try {
			if (ticket.getAutenticacionUsuario() != null) {
				List<Object> emailsByPersonForUserCreator = this.emailService.findAllPer(ticket.getAutenticacionUsuario().getPersona().getId());
				Iterator<Object> it = emailsByPersonForUserCreator != null ? emailsByPersonForUserCreator.iterator(): null;
				String getEmailForUserCreator = "";
				while (it != null && it.hasNext()) {
					Object[] row = (Object[]) it.next();
					getEmailForUserCreator = String.valueOf(row[3]);
					break;
				}
				sendEmailService.sendMailTicketMultiple(getEmailForUser, getEmailForUserCreator, "deltanet - ticket " + ticket.getId() + " " + type.toLowerCase(), type, "Estimado colaborador,", message+ " "+ticket.getId(), address);
			} else {
				sendEmailService.sendMailTicket(getEmailForUser, "deltanet - ticket " + ticket.getId() + " " + type.toLowerCase(), type, "Estimado colaborador,",message+ " "+ticket.getId(), address);
			}
			response.put("mensaje", message);
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", message + ", pero no se pudo enviar el correo!");
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/cerrar")
	public ResponseEntity<?> CerrerTicket(@RequestParam("usuario") String usuario, @RequestParam("idTicket") Long id,
			@RequestParam("estadoId") Long estadoId, @RequestParam("descripcion") String descripcion) {
		Map<String, Object> response = new HashMap<>();
		Ticket ticket = null;
		AutenticacionUsuario user = null;
		Estado status = null;
		String message = "";
		String action = "";
		String type = "";
		String message_response = "";
		try {
			ticket = ticketService.findById(id);
			user = autenticacionUsuarioService.findByUsuario(usuario);
			Long ids = (long) 0;
			// Estados del ticket
			String messageHistory = usuario + " aceptó el ticket ";
			switch (estadoId.intValue()) {
				case 2:
					ids = Long.valueOf(accionesNew.get("ACEPTAR"));
					action = usuario + " aceptó el ticket ";
					message = usuario + " aceptó el ticket " + ticket.getId() + ", asunto: " + ticket.getTitulo();
					message_response = usuario + " aceptó el ticket " + ticket.getId();
					type = "ACEPTADO";
					break;
				case 4:
					ids = Long.valueOf(accionesNew.get("RESOLVER"));
					action = usuario + " resolvió el ticket ";
					message = usuario + " resolvió el ticket " + ticket.getId() + ", asunto: " + ticket.getTitulo();
					message_response = usuario + " resolvió el ticket " + ticket.getId();
					type = "RESUELTO";
					break;
				case 5:
					ids = Long.valueOf(accionesNew.get("CERRAR_MANUALMENTE"));
					action = usuario + " cerró manualmente el ticket ";
					message = usuario + " cerró manualmente el ticket " + ticket.getId() + ", asunto: "
							+ ticket.getTitulo();
					message_response = usuario + " cerró manualmente el ticket " + ticket.getId();
					type = "CERRADO MANUALMENTE";
					break;
				case 6:
					ids = Long.valueOf(accionesNew.get("RECHAZAR"));
					action = usuario + " rechazó el ticket ";
					message = usuario + " rechazó el ticket " + ticket.getId() + ", asunto: " + ticket.getTitulo();
					message_response = usuario + " rechazó el ticket " + ticket.getId();
					type = "RECHAZADO";
					estadoId = (ticket.getEstado().getId().intValue() == 4) ? ((long) 2) : ((long) 6);
					break;
				case 7:
					ids = Long.valueOf(accionesNew.get("REABRIR"));
					action = usuario + " reabrió el ticket ";
					message = usuario + " reabrió el ticket " + ticket.getId() + ", asunto: " + ticket.getTitulo();
					message_response = usuario + " reabrió el ticket " + ticket.getId();
					type = "REABIERTO";
					estadoId = (long) 1;
					break;
				default:
					ids = Long.valueOf(accionesNew.get("RECHAZAR"));
					action = usuario + " rechazó el ticket ";
					message = usuario + " rechazó el ticket " + ticket.getId() + ", asunto: " + ticket.getTitulo();
					message_response = usuario + " rechazó el ticket " + ticket.getId();
					type = "RECHAZADO";
			}
			status = estadoService.findById(estadoId);
			try {
				Historial historial = new Historial();
				historial.setTipoAccionId(tipoAccionService.findById(ids));
				historial.setTabla(tablas.get("TICKET"));
				historial.setTablaId(id);
				historial.setAccion(action);
				historial.setUsuCreado(usuario);
				historial.setFechaCreado(new Date());
				historialService.save(historial);
				Comentario comentario = new Comentario();
				comentario.setTicket(ticket);
				comentario.setUsuario(usuario);
				comentario.setDescripcion(descripcion);
				comentario.setVisibilidad('S');
				comentario.setUsuCreado(usuario);
				comentario.setFechaCreado(new Date());
				comentarioService.save(comentario);
				ticket.setEstado(status);
				ticket.setUsuEditado(usuario);
				ticket.setFechaEditado(new Date());
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
		try {
			String address = url + "dashboard/ticket/detalle/" + ticket.getId();
			AutenticacionUsuario userCreator = null;
			userCreator = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
			List<Object> emailsByPersonForUser = this.emailService
					.findAllPer(ticket.getAutenticacionUsuario().getPersona().getId());
			Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
			String getEmailForUser = "";
			while (itForEmailUser != null && itForEmailUser.hasNext()) {
				Object[] row = (Object[]) itForEmailUser.next();
				getEmailForUser = String.valueOf(row[3]);
				break;
			}
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
				sendEmailService.sendMailTicketMultiple(getEmailForUser, getEmailForUserCreator,
						"deltanet - ticket " + ticket.getId() + " " + type.toLowerCase(), type, "Estimado colaborador,",
						message, address);
			} else {
				sendEmailService.sendMailTicket(getEmailForUser,
						"deltanet - ticket " + ticket.getId() + " " + type.toLowerCase(), type, "Estimado colaborador,",
						message, address);
			}
			response.put("mensaje", message_response);
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", message_response + ", pero no se pudo enviar el correo!");
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Scheduled(cron = "0 50 23 * * *") // Todos los dias a las 23:59   CR-BSG-03
	@GetMapping("/avisoporcerrar")
	public ResponseEntity<?> avisoPorCerrar() {
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
		Integer mes = calendar.get(Calendar.MONTH) + 1;
		Integer yyy = calendar.get(Calendar.YEAR);
		String strFecHoy = String.format("%04d", yyy) + "-" + String.format("%02d", mes) + "-"
				+ String.format("%02d", dia);
		Date fecHoy = new Date();
		Date fecDesde = new Date();
		Date fecHasta = new Date();
		SisParam param;
		Integer diasPlazo;
		try {
			fecHoy = formato.parse(strFecHoy);
			Calendario now = calendarioService.findDate(fecHoy);
			OutFeriado date = feriadoService.buscaFechaFeriado(fecHoy);
			if (date != null || now.getIdSemanaDia().equals(6L)  || now.getIdSemanaDia().equals(7L)) {
				response.put("mensaje", "No se procesa nada hoy por que es feriado");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			String strEtiqueta = "TICKET_DIAS_PLAZO_MENSAJE";
			param = parametroService.buscaEtiqueta(strEtiqueta);
			if (param == null) {
				response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			Integer dias = 0;
			try {
				dias = Integer.valueOf(param.getValor()) * (-1);
				diasPlazo = Integer.valueOf(param.getValor());
			} catch (Exception x) {
				response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			calendar.setTime(fecHoy);
			calendar.add(Calendar.DATE, dias);
			fecDesde = calendar.getTime();

		} catch (Exception e) {
			response.put("mensaje", "Error al obtener fecha limite");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String strEtiqueta = "TICKET_DIAS_PLAZO_CIERRE";
		param = parametroService.buscaEtiqueta(strEtiqueta);
		if (param == null) {
			response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Integer diasCierre;
		try {
			diasCierre = Integer.valueOf(param.getValor());
		} catch (Exception o) {
			response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Integer difDias = diasCierre - diasPlazo;
		Pageable limit = PageRequest.of(0, difDias+1);
		List<Calendario> resultados = calendarioService.buscaRangoFecha(fecHoy, limit);
		if(resultados.size() == 0) {
			response.put("mensaje", "No existen fechas par");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    Calendario ultimo = resultados.get(resultados.size() - 1);
		calendar.setTime(fecDesde);
		calendar.add(Calendar.DATE, 1);
		fecHasta = calendar.getTime();
		List<Ticket> listado = ticketService.findAllporCerrar2(ultimo.getFecha());	
		StringBuilder cuerpo = new StringBuilder("");
		Long idPerAnt = 0L;
		Boolean nuevo = false;
		String username = "";
		String usernameAnt = "";
		for (int x = 0; x < listado.size(); x++) {
			// Long idPer = listado.get(x).getAutenticacionUsuario().getPersona().getId();
			Long idPer;
			try {
				username = listado.get(x).getUsuarioCreador();
				idPer = autenticacionUsuarioService.findByUsuario(username).getId();
				if (idPer > 0) {
					nuevo = idPer != idPerAnt;
					if (nuevo) {
						if (idPerAnt != 0L) {
							cuerpo.append("</tbody></table><br><p>"
									+ "En el caso que la atención haya sido conforme, ingresar al sistema y cambiar el estado del ticket a cerrado."
									+ "En el caso que la atención no haya sido conforme, ingresar al sistema y cambiar el estado del ticket a asignado. "
									+ "Caso contrario el ticket será cerrado automáticamente por el sistema en "
									+ difDias + " dia"+((difDias > 1) ? "s":"")+".<br><br><br>"
									+ "Saludos cordiales, <br> Sistema Deltanet</p>");
							envioCorreo(cuerpo, idPerAnt,
									"Usuario: " + usernameAnt + " - tickets resueltos por vencer", listado);
						}
						idPerAnt = idPer;
						usernameAnt = username;
						cuerpo = new StringBuilder(
								"<html><style>table {border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;}table td {padding: 15px;}\n");
						cuerpo.append(
								"table thead td {background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;}\n");
						cuerpo.append("table tbody td {color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;}\n");
						cuerpo.append("table tbody tr {background-color: #f9fafb;text-align:center;}\n");
						cuerpo.append("table tbody tr:nth-child(odd) {background-color: #ffffff;}</style>\n");
						cuerpo.append("<body><p>Estimado usuario: " + username
								+ ", <br><br> Por medio de la presente se le informa que los siguientes tickets han sido atendidos. <br></p><br>");
						cuerpo.append(
								"<table style=\"border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;\">\n");
						cuerpo.append("<thead>" + "<tr>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Id</td>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Titulo</td>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Estado</td>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Fecha Act.</td>");
						cuerpo.append("</tr></thead><tbody>\n");
					}
					cuerpo.append(
							"<tr style=\"background-color: #f9fafb;text-align:center;\"><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"5%\">"
									+ listado.get(x).getId()
									+ "</td><td style=\"text-align: left; color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"40%\">"
									+ listado.get(x).getTitulo()
									+ "</td><td style=\"text-align: left; color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"10%\">"
									+ "Resuelto"
									+ "</td><td style=\"color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;\" width=\"15%\">"
									+ listado.get(x).getFechaEditado().toString() + "</td></tr>");
				}
			} catch (Exception d) {}
		}
		if (listado.size() > 0) {
			if (idPerAnt > 0) {
				cuerpo.append("</tbody></table><br><p>"
						+ "En el caso que la atención haya sido conforme, ingresar al sistema y cambiar el estado del ticket a cerrado."
						+ "En el caso que la atención no haya sido conforme, ingresar al sistema y cambiar el estado del ticket a asignado. "
						+ "Caso contrario el ticket será cerrado automáticamente por el sistema en "
						+ difDias + " dia"+((difDias > 1) ? "s":"")+".<br><br><br>"
						+ "Saludos cordiales, <br> Sistema Deltanet</p>");
				envioCorreo(cuerpo, idPerAnt, "Usuario: " + usernameAnt + " - tickets resueltos por vencer", listado);
			}
		}

		response.put("mensaje", "correos enviados.");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Scheduled(cron = "0 55 23 * * *") // Todos los dias a las 23:59   CR-BSG-04
	@GetMapping("/avisocierre")
	public ResponseEntity<?> avisoCierre() {
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
		Integer mes = calendar.get(Calendar.MONTH) + 1;
		Integer yyy = calendar.get(Calendar.YEAR);
		String strFecHoy = String.format("%04d", yyy) + "-" + String.format("%02d", mes) + "-"
				+ String.format("%02d", dia);
		Date fecHoy = new Date();
		Date fechaIni = new Date();
		Date fechaFin = new Date();
		SisParam param;
		Integer diasPlazo;
		Integer diasCierre;
		try {
			fecHoy = formato.parse(strFecHoy);
			Calendario now = calendarioService.findDate(fecHoy);
			OutFeriado date = feriadoService.buscaFechaFeriado(fecHoy);
			if (date != null || now.getIdSemanaDia().equals(6L)  || now.getIdSemanaDia().equals(7L)) {
				response.put("mensaje", "No se procesa nada hoy por que es feriado");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			String strEtiqueta = "TICKET_DIAS_PLAZO_MENSAJE";
			param = parametroService.buscaEtiqueta(strEtiqueta);
			if (param == null) {
				response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Integer diasMsg;
			try {
				diasMsg = Integer.valueOf(param.getValor());
			} catch (Exception x) {
				response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			strEtiqueta = "TICKET_DIAS_PLAZO_CIERRE";
			param = parametroService.buscaEtiqueta(strEtiqueta);
			if (param == null) {
				response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			try {
				diasCierre = Integer.valueOf(param.getValor());
			} catch (Exception x) {
				response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			calendar.setTime(fecHoy);
			calendar.add(Calendar.DATE, diasCierre * (-1));
			fechaIni = calendar.getTime();

			calendar.setTime(fechaIni);
			calendar.add(Calendar.DATE, 1);
			fechaFin = calendar.getTime();
		} catch (Exception e) {
			response.put("mensaje", "Error al obtener plazo");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Pageable limit = PageRequest.of(0, (diasCierre + 1));
		List<Calendario> resultados = calendarioService.buscaRangoFecha(fecHoy, limit);
		if(resultados.size() == 0) {
			response.put("mensaje", "No existen fechas par");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    Calendario ultimo = resultados.get(resultados.size() - 1);
		List<Ticket> listado = ticketService.findAllporCerrar2(ultimo.getFecha());	
		//List<Ticket> listado = ticketService.findAllporCerrar(fechaIni, fechaFin);
		StringBuilder cuerpo = new StringBuilder("");
		Long idPersonaAnt = 0L;
		Boolean nuevo = false;
		Estado vEst = estadoService.findById(5L);
		Ticket registro;
		String username = "";
		String usernameAnt = "";
		for (int x = 0; x < listado.size(); x++) {
			// Long idPer = listado.get(x).getAutenticacionUsuario().getPersona().getId();
			Long idPersona;
			try {
				username = listado.get(x).getUsuarioCreador();
				// idPer = autenticacionUsuarioService.findByUsuario(username).getId();
				// Obtenemos el ID de persona en lugar del ID de autenticación
				idPersona = autenticacionUsuarioService.findByUsuario(username).getPersona().getId();
				if (idPersona > 0) {
					registro = listado.get(x);
					registro.setEstado(vEst);
					registro.setFechaEditado(new Date());
					registro.setUsuEditado("sistema");
					ticketService.save(registro);
					Historial historial = new Historial();
					historial.setTipoAccionId(
							tipoAccionService.findById(Long.valueOf(accionesNew.get("CERRAR_AUTOMATICAMENTE"))));
					historial.setTabla(tablas.get("TICKET"));
					historial.setTablaId(registro.getId());
					String messageHistory = "El sistema cerró automáticamente el ticket ";
					historial.setAccion(messageHistory);
					historial.setUsuCreado("sistema");
					historial.setFechaCreado(new Date());
					historialService.save(historial);
					nuevo = idPersona != idPersonaAnt;
					if (nuevo) {
						if (idPersonaAnt != 0L) {
							cuerpo.append("</tbody></table><br><br><p>Saludos cordiales, <br>Sistema Deltanet</p>");
							final Long currentPersonaId = idPersonaAnt;

							List<Ticket> userTickets = listado.stream()
								.filter(ticket -> {
									try {
										String ticketUsername = ticket.getUsuarioCreador();
										Long ticketIdPersona = autenticacionUsuarioService.findByUsuario(ticketUsername).getPersona().getId();
										return ticketIdPersona.equals(currentPersonaId);
									} catch (Exception e) {
										return false;
									}
								})
								.collect(Collectors.toList());

							envioCorreo(cuerpo, idPersonaAnt, "deltanet - tickets cerrados automáticamente por el sistema", userTickets);
						}
						idPersonaAnt = idPersona;
						usernameAnt = username;
						cuerpo = new StringBuilder(
								"<html><style>table {border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;}table td {padding: 15px;}\n");
						cuerpo.append(
								"table thead td {background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;}\n");
						cuerpo.append("table tbody td {color: #636363;font-size: 10px;\tborder: 1px solid #dddfe1;}\n");
						cuerpo.append("table tbody tr {background-color: #f9fafb;text-align:center;}\n");
						cuerpo.append("table tbody tr:nth-child(odd) {background-color: #ffffff;}</style>\n");
						cuerpo.append(
								"<body><p>Estimado colaborador,<br><br>Debido al vencimiento de tiempo de espera de su conformidad a los tickets generados,<br>el sistema ha procedido a cerrarlos automáticamente:</p><br><br>");
						cuerpo.append(
								"<table style=\"border-collapse: collapse;font-family: Tahoma, Geneva, sans-serif;\">\n");
						cuerpo.append("<thead>" + "<tr>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Ticket</td>"
								+ "<td style=\"text-align: center; background-color: #54585d;color: #ffffff;font-weight: bold;font-size: 12px;\tborder: 1px solid #54585d;\">Asunto del ticket</td>");
						cuerpo.append("</tr></thead><tbody>\n");
					}
					cuerpo.append(
							"<tr style=\"background-color: #f9fafb;\"><td style=\"color: #636363;font-size: 10px;text-align:right !important;padding-right: 10px !important;\tborder: 1px solid #dddfe1;\" width=\"5%\">"
									+ listado.get(x).getId()
									+ "</td><td style=\"text-align: left; color: #636363;font-size: 10px;padding-left: 10px !important;\tborder: 1px solid #dddfe1;\" width=\"40%\">"
									+ listado.get(x).getTitulo() + "</td></tr>");
				}
			} catch (Exception d) {
				registro = listado.get(x);
				registro.setEstado(vEst);
				registro.setFechaEditado(new Date());
				registro.setUsuEditado("sistema");
				ticketService.save(registro);
			}
		}
		if (listado.size() > 0) {
			if (idPersonaAnt > 0) {
				cuerpo.append(
						"</tbody></table><br><p>Por favor no responder a este correo informativo</p><br><p>Saludos cordiales, <br>Sistema Deltanet</p>");
				final Long currentPersonaId = idPersonaAnt;

				List<Ticket> userTickets = listado.stream()
					.filter(ticket -> {
						try {
							String ticketUsername = ticket.getUsuarioCreador();
							Long ticketIdPersona = autenticacionUsuarioService.findByUsuario(ticketUsername).getPersona().getId();
							return ticketIdPersona.equals(currentPersonaId);
						} catch (Exception e) {
							return false;
						}
					})
					.collect(Collectors.toList());
				envioCorreo(cuerpo, idPersonaAnt, "deltanet - tickets cerrados automáticamente por el sistema", userTickets);
			}
		}
		response.put("mensaje", "tickets cerrados y correos enviados.");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	public void envioCorreo(StringBuilder mensaje, Long idPer, String title, List<Ticket> tickets) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", envHost);
		props.put("mail.smtp.user", envUsername);
		props.put("mail.smtp.password", envPassword);
		props.put("mail.smtp.starttls.enable", envStartTls);
		props.put("mail.smtp.port", envPort);
		props.put("mail.smtp.auth", envAuth);
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(envUsername, envPassword);
			}
		});

		List<Object> destinos = emailService.findByCorreosPer(1, idPer);
		Iterator<Object> it = destinos != null ? destinos.iterator() : null;
		String correo = "";
		while (it != null && it.hasNext()) {
			Object[] col = (Object[]) it.next();
			if (correo.equals("") || correo.isEmpty()) {
				correo = String.valueOf(col[3]);
			}
		}

		if (correo.equals("") || correo.isEmpty()) {
			String aviso = "Error al enviar correo";
			return;
		}

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(envUsername, "Deltanet"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
			msg.setSubject(title);

			// Create the message parts
			Multipart multipart = new MimeMultipart();

			// Create the HTML part
			MimeBodyPart htmlPart = new MimeBodyPart();
			mensaje.append("</tbody></table></body></html>\n");
			htmlPart.setContent(mensaje.toString(), "text/html;charset=utf-8");
			multipart.addBodyPart(htmlPart);

			// Create the Excel attachment part
			MimeBodyPart excelPart = new MimeBodyPart();
			// Determinar el tipo de reporte basado en el título
			boolean isAvisoPorCerrar = title.contains("tickets resueltos por vencer");
			byte[] excelFile = createExcelFile(tickets, isAvisoPorCerrar);
			DataSource source = new ByteArrayDataSource(excelFile,
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			excelPart.setDataHandler(new DataHandler(source));
			excelPart.setFileName("tickets_reporte.xlsx");
			multipart.addBodyPart(excelPart);

			msg.setContent(multipart);
			Transport.send(msg);

		} catch (Exception s) {
			String aviso = "Error al enviar correo";
		}
	}

	private byte[] createExcelFile(List<Ticket> tickets, boolean isAvisoPorCerrar) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Tickets");

		// Create header row
		Row headerRow = sheet.createRow(0);
		if (isAvisoPorCerrar) {
			headerRow.createCell(0).setCellValue("Id");
			headerRow.createCell(1).setCellValue("Titulo");
			headerRow.createCell(2).setCellValue("Estado");
			headerRow.createCell(3).setCellValue("Fecha Act.");
		} else {
			headerRow.createCell(0).setCellValue("Ticket");
			headerRow.createCell(1).setCellValue("Asunto del ticket");
		}

		// Create date format for avisoPorCerrar
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Create data rows
		int rowNum = 1;
		for (Ticket ticket : tickets) {
			Row row = sheet.createRow(rowNum++);
			if (isAvisoPorCerrar) {
				row.createCell(0).setCellValue(ticket.getId());
				row.createCell(1).setCellValue(ticket.getTitulo());
				row.createCell(2).setCellValue("Resuelto");

				// Format the date
				String formattedDate = outputFormat.format(ticket.getFechaEditado());
				row.createCell(3).setCellValue(formattedDate);
			} else {
				row.createCell(0).setCellValue(ticket.getId());
				row.createCell(1).setCellValue(ticket.getTitulo());
			}
		}

		// Autosize columns
		for (int i = 0; i < (isAvisoPorCerrar ? 4 : 2); i++) {
			sheet.autoSizeColumn(i);
		}

		// Write to ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		return outputStream.toByteArray();
	}
}
