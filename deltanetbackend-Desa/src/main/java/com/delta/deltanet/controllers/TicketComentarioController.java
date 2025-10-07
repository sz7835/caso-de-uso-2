package com.delta.deltanet.controllers;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.Archivo;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.Comentario;
import com.delta.deltanet.models.entity.Historial;
import com.delta.deltanet.models.entity.Ticket;
import com.delta.deltanet.models.service.IArchivoService;
import com.delta.deltanet.models.service.IAutenticacionUsuarioService;
import com.delta.deltanet.models.service.IComentarioService;
import com.delta.deltanet.models.service.IEmailService;
import com.delta.deltanet.models.service.IHistorialService;
import com.delta.deltanet.models.service.ITicketService;
import com.delta.deltanet.models.service.ITipoAccionService;
import com.delta.deltanet.models.service.SendEmailService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/ticket")
public class TicketComentarioController {

	@Autowired
	private ITicketService ticketService;
	@Autowired
	private ITipoAccionService tipoAccionService;
	@Autowired
	private IHistorialService historialService;
	@Autowired
	private IAutenticacionUsuarioService autenticacionUsuarioService;
	@Autowired
	private IComentarioService comentarioService;
	@Autowired
	private IArchivoService archivoService;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private SendEmailService sendEmailService;

	// VariableEntorno
	@Value("#{${tablas}}")
	private Map<String, String> tablas;
	@Value("#{${acciones}}")
	private Map<String, String> acciones;
	@Value("#{${accionesNew}}")
	private Map<String, String> accionesNew;
	@Value("#{${url}}")
	private String url;

	// COMENTARIO
	@PostMapping("/comentario/create")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> CreateComentario(@RequestParam("descripcion") String descripcion,
			@RequestParam("visibilidad") char visibilidad, @RequestParam("idTicket") Long idTicket,
			@RequestParam("usuario") String usuarioCreacion) {
		Map<String, Object> response = new HashMap<>();

		Ticket ticket = ticketService.findById(idTicket);
		if (ticket == null) {
			response.put("mensaje",
					"El ticket ID: ".concat(idTicket.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		Comentario comentario = new Comentario();
		comentario.setTicket(ticket);
		comentario.setUsuario(usuarioCreacion);
		comentario.setDescripcion(descripcion);
		comentario.setVisibilidad(visibilidad);
		comentario.setUsuCreado(usuarioCreacion);
		comentario.setFechaCreado(new Date());

		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("COMENTAR"))));
		historial.setTabla(tablas.get("TICKET"));
		String messageHistory = usuarioCreacion + " realizó un comentario sobre el ticket ";
		historial.setAccion(messageHistory);
		historial.setUsuCreado(usuarioCreacion);
		historial.setFechaCreado(new Date());

		AutenticacionUsuario userCreator = null;
		AutenticacionUsuario user = null;
		Comentario comentarioCreado = new Comentario();
		try {
			comentarioCreado = comentarioService.save(comentario);
			try {
				historial.setTablaId(idTicket);
				historialService.save(historial);
			} catch (DataAccessException e) {
				comentarioService.delete(comentarioCreado.getId());
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		userCreator = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
		user = ticket.getAutenticacionUsuario();

		List<Object> emailsByPersonForUser = this.emailService.findAllPer(user.getPersona().getId());
		Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
		String getEmailForUser = "";

		while (itForEmailUser != null && itForEmailUser.hasNext()) {
			Object[] row = (Object[]) itForEmailUser.next();
			getEmailForUser = String.valueOf(row[4]);
			break;
		}

		try {
			String title = "Estimado colaborador,";
			String message = usuarioCreacion + " realizó un comentario en el ticket " + ticket.getId()
					+ ", comentario: "
					+ comentarioCreado.getDescripcion();
			String address = url + "dashboard/ticket/detalle/" + ticket.getId();
			String head = "deltanet - ticket " + ticket.getId() + " nuevo comentario";
			if (userCreator != null) {
				List<Object> emailsByPersonForUserCreator = this.emailService
						.findAllPer(userCreator.getPersona().getId());
				Iterator<Object> it = emailsByPersonForUserCreator != null ? emailsByPersonForUserCreator.iterator()
						: null;
				String getEmailForUserCreator = "";

				while (it != null && it.hasNext()) {
					Object[] row = (Object[]) it.next();
					getEmailForUserCreator = String.valueOf(row[4]);
					break;
				}

				sendEmailService.sendMailTicketMultiple(getEmailForUser, getEmailForUserCreator, head, "COMENTARIO",
						title, message, address);
			} else {
				sendEmailService.sendMailTicket(getEmailForUser, head, "COMENTARIO", title, message, address);
			}
			response.put("mensaje", "El comentario ha sido creado");
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", "El comentario ha sido creado, pero no se envio correo");
		}
		comentarioCreado.getTicket().getAreaDestino().setGerencia(null);
		comentarioCreado.getTicket().getAreaDestino().setPuestos(null);
		comentarioCreado.getTicket().getAreaOrigen().setGerencia(null);
		comentarioCreado.getTicket().getAreaOrigen().setPuestos(null);
		response.put("history", historial);
		response.put("comentario", comentarioCreado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/comentario/read/{id}")
	public ResponseEntity<?> ReadComentario(@PathVariable Long id) {
		Comentario comentario = null;
		Map<String, Object> response = new HashMap<>();

		try {
			comentario = comentarioService.findById(id);

			if (comentario == null) {
				response.put("mensaje",
						"El comentario ID: ".concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Archivo> archivos = archivoService.findByTablaAndTablaId("comentario", comentario.getId());

		comentario.getTicket().getAreaDestino().setGerencia(null);
		comentario.getTicket().getAreaDestino().setPuestos(null);
		comentario.getTicket().getAreaOrigen().setGerencia(null);
		comentario.getTicket().getAreaOrigen().setPuestos(null);
		response.put("comentario", comentario);
		response.put("archivos", archivos);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/comentario/read")
	public ResponseEntity<?> ReadAllComentario(@RequestParam("idTicket") Optional<Long> idTicket) {

		Map<String, Object> response = new HashMap<>();
		Map<Long, Object> comentArchivos = new HashMap<>();

		if (idTicket.isPresent()) {
			List<Comentario> comentarios = comentarioService.findAllByTicket(idTicket.get());

			for (Comentario comentario : comentarios) {
				List<Archivo> archivos = archivoService.findByTablaAndTablaId("comentario", comentario.getId());
				comentArchivos.put(comentario.getId(), archivos);
			}

			response.put("comentarios", comentarios);
			response.put("archivos", comentArchivos);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}

		List<Comentario> comentarios = comentarioService.findAll();

		return new ResponseEntity<List<Comentario>>(comentarios, HttpStatus.OK);

	}

	@PutMapping("/comentario/update/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> UpdateComentario(@PathVariable Long id, @RequestParam("descripcion") String descripcion,
			@RequestParam("visibilidad") char visibilidad, @RequestParam("usuario") String usuarioActualizacion,
			@RequestParam("idTicket") Long idTicket) {
		Comentario comentarioActual = comentarioService.findById(id);
		Map<String, Object> response = new HashMap<>();

		if (comentarioActual == null) {
			response.put("mensaje", "Error: no se puede editar, el comentario ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		Ticket ticket = ticketService.findById(idTicket);
		if (ticket == null) {
			response.put("mensaje",
					"El ticket ID: ".concat(idTicket.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		Historial historialTicket = new Historial();
		historialTicket.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("EDITAR_COMENTARIO"))));
		historialTicket.setTabla(tablas.get("TICKET"));
		String messageHistory = usuarioActualizacion + " editó un comentario del ticket ";
		historialTicket.setAccion(messageHistory);
		historialTicket.setUsuCreado(usuarioActualizacion);
		historialTicket.setFechaCreado(new Date());
		historialTicket.setTablaId(idTicket);

		Comentario comentarioBack = new Comentario();
		comentarioBack.setId(comentarioActual.getId());
		comentarioBack.setTicket(comentarioActual.getTicket());
		comentarioBack.setDescripcion(comentarioActual.getDescripcion());
		comentarioBack.setVisibilidad(comentarioActual.getVisibilidad());
		comentarioBack.setUsuCreado(comentarioActual.getUsuCreado());
		comentarioBack.setFechaCreado(comentarioActual.getFechaCreado());
		comentarioBack.setUsuEditado(comentarioActual.getUsuEditado());
		comentarioBack.setFechaEditado(comentarioActual.getFechaEditado());
		try {
			AutenticacionUsuario userCreator = null;
			AutenticacionUsuario user = null;
			comentarioActual.setDescripcion(descripcion);
			comentarioActual.setVisibilidad(visibilidad);
			comentarioActual.setFechaEditado(new Date());
			comentarioActual.setUsuEditado(usuarioActualizacion);
			comentarioService.save(comentarioActual);
			try {
				historialService.save(historialTicket);
			} catch (DataAccessException e) {
				comentarioService.save(comentarioBack);
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			userCreator = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
			user = ticket.getAutenticacionUsuario();

			List<Object> emailsByPersonForUser = this.emailService.findAllPer(user.getPersona().getId());
			Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
			String getEmailForUser = "";

			while (itForEmailUser != null && itForEmailUser.hasNext()) {
				Object[] row = (Object[]) itForEmailUser.next();
				getEmailForUser = String.valueOf(row[4]);
				break;
			}

			try {
				String title = "Estimado colaborador,";
				String message = usuarioActualizacion + " editó un comentario del ticket " + ticket.getId()
						+ ", comentario: " + comentarioActual.getDescripcion();
				String address = url + "dashboard/ticket/detalle/" + ticket.getId();
				String head = "deltanet - ticket " + ticket.getId() + " comentario editado";
				if (userCreator != null) {
					List<Object> emailsByPersonForUserCreator = this.emailService
							.findAllPer(userCreator.getPersona().getId());
					Iterator<Object> it = emailsByPersonForUserCreator != null ? emailsByPersonForUserCreator.iterator()
							: null;
					String getEmailForUserCreator = "";

					while (it != null && it.hasNext()) {
						Object[] row = (Object[]) it.next();
						getEmailForUserCreator = String.valueOf(row[4]);
						break;
					}
					// sendEmailService.sendMailTicketMultiple(getEmailForUser,
					// getEmailForUserCreator, head,
					// "COMENTARIO EDITADO", title, message, address);
				} else {
					// sendEmailService.sendMailTicket(getEmailForUser, head, "COMENTARIO EDITADO",
					// title, message,
					// address);
				}
				response.put("mensaje", "El comentario ha sido editado con éxito!");
			} catch (Exception e) {
				response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
				response.put("mensaje", "El comentario ha sido creado, pero no se envio correo");
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al editar el comentario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		comentarioActual.getTicket().getAreaDestino().setGerencia(null);
		comentarioActual.getTicket().getAreaDestino().setPuestos(null);
		comentarioActual.getTicket().getAreaOrigen().setGerencia(null);
		comentarioActual.getTicket().getAreaOrigen().setPuestos(null);
		response.put("comment", comentarioActual);
		response.put("history", historialTicket);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/comentario/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> DeleteComentario(@PathVariable Long id,
			@RequestParam("usuario") String usuarioActualizacion, @RequestParam("idTicket") Long idTicket) {
		Comentario comentarioActual = comentarioService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if (comentarioActual == null) {
			response.put("mensaje", "Error: no se puede editar, el comentario ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		Ticket ticket = ticketService.findById(idTicket);
		if (ticket == null) {
			response.put("mensaje",
					"El ticket ID: ".concat(idTicket.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		Historial historialTicket = new Historial();
		historialTicket
				.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ELIMINAR_COMENTARIO"))));
		historialTicket.setTabla(tablas.get("TICKET"));
		String messageHistory = usuarioActualizacion + " eliminó un comentario del ticket ";
		historialTicket.setAccion(messageHistory);
		historialTicket.setUsuCreado(usuarioActualizacion);
		historialTicket.setFechaCreado(new Date());
		historialTicket.setTablaId(idTicket);
		try {
			comentarioService.delete(id);
			historialService.save(historialTicket);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el comentario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		AutenticacionUsuario userCreator = null;
		AutenticacionUsuario user = null;
		userCreator = autenticacionUsuarioService.findByUsuario(ticket.getUsuarioCreador());
		user = ticket.getAutenticacionUsuario();

		List<Object> emailsByPersonForUser = this.emailService.findAllPer(user.getPersona().getId());
		Iterator<Object> itForEmailUser = emailsByPersonForUser != null ? emailsByPersonForUser.iterator() : null;
		String getEmailForUser = "";

		while (itForEmailUser != null && itForEmailUser.hasNext()) {
			Object[] row = (Object[]) itForEmailUser.next();
			getEmailForUser = String.valueOf(row[4]);
			break;
		}
		try {
			String title = "Estimado colaborador,";
			String message = usuarioActualizacion + " eliminó un comentario del ticket " + ticket.getId()
					+ ", comentario: " + comentarioActual.getDescripcion();
			String address = url + "dashboard/ticket/detalle/" + ticket.getId();
			String head = "deltanet - ticket " + ticket.getId() + " comentario eliminado";
			if (userCreator != null) {
				List<Object> emailsByPersonForUserCreator = this.emailService
						.findAllPer(userCreator.getPersona().getId());
				Iterator<Object> it = emailsByPersonForUserCreator != null ? emailsByPersonForUserCreator.iterator()
						: null;
				String getEmailForUserCreator = "";

				while (it != null && it.hasNext()) {
					Object[] row = (Object[]) it.next();
					getEmailForUserCreator = String.valueOf(row[4]);
					break;
				}
				sendEmailService.sendMailTicketMultiple(getEmailForUser, getEmailForUserCreator, head,
						"COMENTARIO ELIMINADO", title, message, address);
			} else {
				sendEmailService.sendMailTicket(getEmailForUser, head, "COMENTARIO ELIMINADO", title, message, address);
			}
			response.put("mensaje", "El comentario ha sido eliminado con éxito!");
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": ").concat(e.getMessage()));
			response.put("mensaje", "El comentario ha sido eliminado, pero no se envio correo");
		}
		response.put("history", historialTicket);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}