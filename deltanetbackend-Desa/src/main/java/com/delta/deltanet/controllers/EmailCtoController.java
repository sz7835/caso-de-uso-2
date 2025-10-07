package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.CorreoTipo;
import com.delta.deltanet.models.entity.EMail;
import com.delta.deltanet.models.entity.Parametro;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.TipoDocumento;
import com.delta.deltanet.models.entity.lstDireccion1;
import com.delta.deltanet.models.entity.lstEmail1;
import com.delta.deltanet.models.entity.lstPersona1;
import com.delta.deltanet.models.entity.lstPersona3;
import com.delta.deltanet.models.entity.lstTelefono1;
import com.delta.deltanet.models.service.IEmailService;
import com.delta.deltanet.models.service.IParametroService;
import com.delta.deltanet.models.service.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/correoCto")
public class EmailCtoController {
	@Autowired
	private IEmailService emailService;
	@Autowired
	private IPersonaService perService;
	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/show")
	public ResponseEntity<?> showEmail(	@RequestParam(name = "idPersona") Long idPersona,
										@RequestParam(name = "idContacto") Long idContacto) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Validar si idContacto es un contacto válido para idPersona
			String sql = "SELECT g.id_nodo_origen FROM grafo_enlace AS g " +
						"INNER JOIN grafo_enlace_motivo AS gem ON gem.id = g.id_enlace_motivo " +
						"WHERE g.id_nodo_destino = :idPersona AND gem.tipo = 3 AND g.estado = 1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("idPersona", idPersona);
			List<Object> idNodoOrigenList = query.getResultList();

			// Convertir los resultados a una lista de Long
			List<Long> idNodoOrigenLongList = new ArrayList<>();
			for (Object id : idNodoOrigenList) {
				idNodoOrigenLongList.add(((Number) id).longValue());
			}

			// Verificar si idContacto está en la lista de id_nodo_origen
			if (!idNodoOrigenLongList.contains(idContacto)) {
				response.put("message", "No tiene permiso para acceder a los correos de esta persona, ya que no está registrada como su contacto.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}

			// Validar si idContacto es una persona existente
			Persona persona = perService.buscarId(idContacto);
			if (persona == null) {
				response.put("message", "La persona ID: ".concat(idContacto.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			lstPersona3 registro = new lstPersona3();
			registro.setId(persona.id);
			registro.setTratamiento(persona.getAtencion().getId());
			registro.setIdNodoTipo(persona.getIdNodoTipo());
			registro.setTipPer(persona.tipoPer.idTipoPer);
			registro.setTipDoc(persona.tipoDoc.idTipDoc);
			registro.setNroDoc(persona.documento);
			registro.setPuesto(persona.puesto);
			registro.setNroDoc(persona.documento);
			registro.setEstado(persona.estado);
			registro.setFecCrea(persona.creDate);
			if (persona.tipoPer.getIdTipoPer() == 1L) {
				registro.setNombre(persona.perNat.getNombre());
				registro.setApellidoP(persona.perNat.getApePaterno());
				registro.setApellidoM(persona.perNat.getApeMaterno());
				registro.setSexo(persona.perNat.getSex());
				registro.setEstadoCivil(persona.perNat.getEstCivil());
			}
			if (persona.tipoPer.getIdTipoPer() == 2L) {
				registro.setNombre(persona.perJur.getRazonSocial());
				registro.setRazcom(persona.perJur.getRazonComercial());
			}
			List<CorreoTipo> correosTipo = new ArrayList<>();
			try {
				String correoSql = "SELECT * FROM per_persona_correo_tipo WHERE id_tipo_persona = " + persona.tipoPer.getIdTipoPer();
				Query correoQuery = entityManager.createNativeQuery(correoSql, CorreoTipo.class);
				correosTipo = correoQuery.getResultList();
			} catch (Exception ex) {
				response.put("error", "Error al ejecutar la consulta directa: " + ex.getMessage());
			}
			response.put("tipo", correosTipo);
			response.put("persona", registro);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la función show");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createEmail(@RequestParam(name = "idContacto") Long idCto,
										@RequestParam(name = "tipo") Long tipo,
										@RequestParam(name = "correo") String email,
										@RequestParam(name = "usuario") String usuario) {
		Map<String, Object> response = new HashMap<>();
		try {
			EMail correo = new EMail();
			Persona persona = perService.buscarId(idCto);
			if (persona == null) {
				response.put("message", "La persona ID: "
						.concat(idCto.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			// Verificar si el correo ya existe
			List<EMail> existingCorreos = emailService.findByCorreo(email);
			if (!existingCorreos.isEmpty()) {
				response.put("message", "El correo ya existe en la base de datos");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
			correo.setPersona(persona);
			correo.setTipo(tipo);
			correo.setCorreo(email);
			correo.setEstado(1);
			correo.setUsuCreado(usuario);
			correo.setFechaCreado(new Date());
			emailService.save(correo);
			response.put("message", "Correo guardado con éxito");
		} catch (Exception e) {
			response.put("message", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/update")
	public ResponseEntity<?> updateEmail(@RequestParam(name = "idEMail") Long idEMail,
										@RequestParam(name = "correo") String correo,
										@RequestParam(name = "tipo") Long tipo,
										@RequestParam(name = "usuUpdate") String updUser) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Buscar el correo actual por ID
			EMail mail = emailService.findById(idEMail);
			if (mail == null) {
				response.put("message", "El correo ID: "
						.concat(idEMail.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			// Verificar si ya existe otro correo con el mismo valor
			String sql = "SELECT * FROM per_persona_correo WHERE id != :idEMail AND email = :correo";
			Query query = entityManager.createNativeQuery(sql, EMail.class);
			query.setParameter("idEMail", idEMail);
			query.setParameter("correo", correo);
			List<EMail> existingCorreos = query.getResultList();

			if (!existingCorreos.isEmpty()) {
				response.put("message", "El correo ya existe en la base de datos.");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			// Actualizar los datos del correo
			mail.setTipo(tipo);
			mail.setCorreo(correo);
			mail.setUsuUpdate(updUser);
			mail.setFechaUpdate(new Date());
			emailService.save(mail);

			response.put("message", "Correo actualizado con éxito");
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualización en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<?> updateStatus(	@RequestParam(name = "idEMail") Long idEMail,
											@RequestParam(name = "updUsuario") String updUser) {
		Map<String, Object> response = new HashMap<>();
		String message = "Correo desactivado con éxito";
		try {
			EMail mail = emailService.findById(idEMail);
			if (mail == null) {
				response.put("message",
						"El correo ID: ".concat(idEMail.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (mail.getEstado() == 0) {
				mail.setEstado(1);
				message = "Correo activado con éxito";
			} else if (mail.getEstado() == 1) {
				mail.setEstado(0);
			}
			mail.setUsuUpdate(updUser);
			mail.setFechaUpdate(new Date());
			emailService.save(mail);
			response.put("message", message);
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualización en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
