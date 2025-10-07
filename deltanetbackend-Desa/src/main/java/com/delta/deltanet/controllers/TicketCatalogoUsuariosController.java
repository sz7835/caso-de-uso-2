package com.delta.deltanet.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.delta.deltanet.models.entity.Areas;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.CatalogoServicio;
import com.delta.deltanet.models.entity.Gerencia;
import com.delta.deltanet.models.entity.OrgAreas;
import com.delta.deltanet.models.entity.lstRelacion2;
import com.delta.deltanet.models.service.AreasServiceImpl;
import com.delta.deltanet.models.service.AutenticacionUsuarioServiceImpl;
import com.delta.deltanet.models.service.GerenciaServiceImpl;
import com.delta.deltanet.models.service.ICatalogoServicioService;
import com.delta.deltanet.models.service.IOrgAreaService;
import com.delta.deltanet.models.service.IRelacionService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/catalogo")
public class TicketCatalogoUsuariosController {
	@Autowired
	private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;
	@Autowired
	private ICatalogoServicioService catalogoServicioService;
	@Autowired
	private GerenciaServiceImpl gerenciaService;
	@Autowired
	private AreasServiceImpl areasService;
	@Autowired
	private IOrgAreaService orgAreaService;
	@Autowired
	public IRelacionService relService;

	@GetMapping("/index")
	public ResponseEntity<?> index() {
		Map<String, Object> response = new HashMap<>();
		List<CatalogoServicio> catalogos = catalogoServicioService.findAll();
		Iterator<CatalogoServicio> it = catalogos.iterator();
		List<CatalogoServicio> lstResumen = new ArrayList<>();
		while (it.hasNext()) {
			CatalogoServicio row = it.next();
			CatalogoServicio lista = new CatalogoServicio();
			lista.setId(row.getId());
			lista.setNombre(row.getNombre());
			lista.setUsuCreado(row.getUsuCreado());
			lista.setFechaCreado(row.getFechaCreado());
			lista.setUsuEditado(row.getUsuEditado());
			lista.setFechaEditado(row.getFechaEditado());
			lista.setEstadoRegistro(row.getEstadoRegistro());
			lista.setArea(row.getArea());
			lista.getArea().setGerencia(row.getArea().getGerencia());
			lista.getArea().setPuestos(null);
			lista.getArea().getGerencia().setOrgAreas(null);
			lstResumen.add(lista);
		}
		response.put("catalogos", lstResumen);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/create")
	public ResponseEntity<?> create() {
		Map<String, Object> response = new HashMap<>();
		List<Gerencia> regGen = gerenciaService.findAll();
		Iterator<Gerencia> it = regGen.iterator();
		while (it.hasNext()) {
			Gerencia row = it.next();
			row.setOrgAreas(Collections.emptyList());
		}
		response.put("gerencias", regGen);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/areas/{id}")
	public ResponseEntity<?> lstAreas(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Areas> areas = areasService.getAreasGer(id);
			Iterator<Areas> it = areas.iterator();
			while (it.hasNext()) {
				Areas row = it.next();
				row.getGerencia().setOrgAreas(null);
			}
			response.put("areas", areas);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion de areas");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/store")
	public ResponseEntity<?> store(@RequestParam("area_id") Long area_id,
								   @RequestParam("nombre") String nombre,
								   @RequestParam("usu_creado") String usu_creado) {
			Map<String, Object> response = new HashMap<>();
			AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(usu_creado);
			if (user == null) {
				response.put("message", "Usuario no encontrado o habilitado");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			OrgAreas area = orgAreaService.findById(area_id);
			if (area == null) {
				response.put("message", "Area no encontrado o habilitado");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			// Validación de duplicados por nombre y area_id
			boolean existe = catalogoServicioService.existsNombreAreaActivo(nombre, area_id, null);
			if (existe) {
				response.put("message", "Ya existe un catálogo activo con el mismo nombre y área");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			try {
				CatalogoServicio catalogoCreado = new CatalogoServicio();
				catalogoCreado.setArea(area);
				catalogoCreado.setNombre(nombre);
				catalogoCreado.setUsuCreado(usu_creado);
				catalogoCreado.setFechaCreado(new Date());
				catalogoCreado.setEstadoRegistro("A");
				CatalogoServicio catalogo = catalogoServicioService.save(catalogoCreado);
				catalogo.getArea().setPuestos(null);
				catalogo.getArea().getGerencia().setOrgAreas(null);
				response.put("catalogo", catalogo);
				response.put("message", "Servicio creado satisfactoriamente.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.put("message", "Error al crear el catalogo.");
				response.put("Error", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
	}

	@GetMapping("/show/{id}")
	public ResponseEntity<?> ListByUsuario(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		CatalogoServicio catalogo = null;
		try {
			catalogo = catalogoServicioService.findById(id);
			catalogo.getArea().setPuestos(null);
			List<Gerencia> regGen = gerenciaService.findAll();
			Iterator<Gerencia> it = regGen.iterator();
			while (it.hasNext()) {
				Gerencia row = it.next();
				row.setOrgAreas(null);
			}
			List<Areas> areas = areasService.getAreasGer((int) (long) catalogo.getArea().getGerencia().getId());
			Iterator<Gerencia> it2 = regGen.iterator();
			while (it2.hasNext()) {
				Gerencia row = it2.next();
				row.setOrgAreas(null);
			}
			response.put("areas", areas);
			response.put("gerencias", regGen);
			response.put("catalogo", catalogo);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error al leer los catalogos.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestParam("area_id") Long area_id,
			@RequestParam("nombre") String nombre, @RequestParam("usu_creado") String usu_creado) {
			Map<String, Object> response = new HashMap<>();
			try {
				CatalogoServicio catalogo = catalogoServicioService.findById(id);
				if (catalogo == null) {
					response.put("message", "Catalogo no encontrado o habilitado");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
				AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(usu_creado);
				if (user == null) {
					response.put("message", "Usuario no encontrado o habilitado");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
				OrgAreas area = orgAreaService.findById(area_id);
				if (area == null) {
					response.put("message", "Area no encontrado o habilitado");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
				// Validación de duplicados por nombre y area_id, excluyendo el id actual
				boolean existe = catalogoServicioService.existsNombreAreaActivo(nombre, area_id, id);
				if (existe) {
					response.put("message", "Ya existe un servicio activo con el mismo nombre y área");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				catalogo.setArea(area);
				catalogo.setNombre(nombre);
				catalogo.setUsuEditado(usu_creado);
				catalogo.setFechaEditado(new Date());
				catalogoServicioService.save(catalogo);
				response.put("message", "Se actualizo el registro satisfactoriamente.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.put("message", "Error al actualizar relacion usuario - catalogos.");
				response.put("error", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteTicket(@PathVariable Long id, @RequestParam("usuario") String usuarioActualizacion) {
			Map<String, Object> response = new HashMap<>();
			try {
				CatalogoServicio reg = catalogoServicioService.findById(id);
				if (reg == null) {
					response.put("message", "El id [" + id + "] no se encuentra registrado.");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				// Solo validar duplicados si se va a activar
				if (reg.getEstadoRegistro().equals("I")) {
					boolean existe = catalogoServicioService.existsNombreAreaActivo(reg.getNombre(), reg.getArea().getId(), id);
					if (existe) {
						response.put("message", "Ya existe un servicio activo con el mismo nombre y área");
						return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
					}
				}
				String cambio = reg.getEstadoRegistro().equals("A") ? "desactivó" : "activó";
				reg.setEstadoRegistro(reg.getEstadoRegistro().equals("A") ? "I" : "A");
				reg.setUsuEditado(usuarioActualizacion);
				reg.setFechaEditado(new Date());
				catalogoServicioService.save(reg);
				response.put("message", "El registro se " + cambio + " satisfactoriamente.");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.put("message", "Error al actualizar el estado del registro.");
				response.put("Error", e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
	}

	@PostMapping("/services")
	public ResponseEntity<?> services(@RequestParam(name = "user_id") Long idUser) {
		Map<String, Object> response = new HashMap<>();
		try {
			AutenticacionUsuario user = autenticacionUsuarioService.buscaUserDelta(idUser);
			if (user == null) {
				response.put("mensaje", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			List<Object> listado = relService.busFiltrada2(user.getPersona().getId(), 1L);
			Iterator<Object> it = listado.iterator();
			lstRelacion2 relation = new lstRelacion2();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				relation.setIdRel((Long) row[0]);
				relation.setEstado((Long) row[1]);
				relation.setIdTipoRel((Long) row[2]);
				relation.setDescrip(String.valueOf(row[3]));
				relation.setFecIni((Date) row[4]);
				relation.setIdArea(String.valueOf(row[5]));
				break;
			}
			if (relation.getIdArea() != "") {
				List<CatalogoServicio> list = catalogoServicioService.findAllByArea(Long.valueOf(relation.getIdArea()));
				Iterator<CatalogoServicio> it2 = list.iterator();
				List<CatalogoServicio> lstResumen = new ArrayList<>();
				while (it2.hasNext()) {
					CatalogoServicio row = it2.next();
				/////row.setUsuarios(Collections.emptyList());
					lstResumen.add(row);
				}
				response.put("list", list);
			}
			response.put("user", user);
			response.put("relation", relation);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/services/store")
	public ResponseEntity<?> services_store(@RequestParam(name = "user_id") Long idUser) {
		Map<String, Object> response = new HashMap<>();
		try {
			AutenticacionUsuario user = autenticacionUsuarioService.buscaUserDelta(idUser);
			if (user == null) {
				response.put("mensaje", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			List<Object> listado = relService.busFiltrada2(user.getPersona().getId(), 1L);
			Iterator<Object> it = listado.iterator();
			lstRelacion2 relation = new lstRelacion2();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				relation.setIdRel((Long) row[0]);
				relation.setEstado((Long) row[1]);
				relation.setIdTipoRel((Long) row[2]);
				relation.setDescrip(String.valueOf(row[3]));
				relation.setFecIni((Date) row[4]);
				relation.setIdArea(String.valueOf(row[5]));
				break;
			}
			if (relation.getIdArea() != "") {
				List<CatalogoServicio> list = catalogoServicioService.findAllByArea(Long.valueOf(relation.getIdArea()));
				Iterator<CatalogoServicio> it2 = list.iterator();
				List<CatalogoServicio> lstResumen = new ArrayList<>();
				while (it2.hasNext()) {
					CatalogoServicio row = it2.next();
				/////row.setUsuarios(Collections.emptyList());
					lstResumen.add(row);
				}
				response.put("list", list);
			}
			response.put("user", user);
			response.put("relation", relation);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PostMapping("/services_store")
	public ResponseEntity<?> perfiles_store(@RequestParam(name = "id_usuario") Long idUser,
			@RequestParam(name = "perfiles") String[] perfiles) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<AutenticacionUsuario> usuario = autenticacionUsuarioService.findById(idUser);
			if (usuario.isEmpty()) {
				response.put("message", "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			catalogoServicioService.deleteService(idUser);
			if (perfiles.length > 0) {
				for (String item : perfiles) {
					Long idPerfil = Long.valueOf(item);
					catalogoServicioService.insert(idUser, idPerfil);
				}
			}
			response.put("message", "Se registraron correctamente los perfiles del usuario");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion perfiles_store");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
