package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.delta.deltanet.models.entity.Historial;
import com.delta.deltanet.models.entity.Prioridad;
import com.delta.deltanet.models.service.IHistorialService;
import com.delta.deltanet.models.service.IPrioridadService;
import com.delta.deltanet.models.service.ITipoAccionService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/ticket")
public class TicketPrioridadController {

	@Autowired
	private IPrioridadService prioridadService;
	@Autowired
	private ITipoAccionService tipoAccionService;
	@Autowired
	private IHistorialService historialService;
  
	//VariableEntorno
	@Value("#{${tablas}}")
	private Map<String,String> tablas;
	@Value("#{${acciones}}")
	private Map<String,String> acciones;
	
	//PRIORIDAD
	@PostMapping("/prioridad/create")
	public ResponseEntity<?> CreatePrioridad(@RequestParam("nombrePrioridad") String nombrePrioridad, @RequestParam("usuario") String usuarioCreacion){		
		Prioridad prioridad = new Prioridad();
		prioridad.setNombre(nombrePrioridad);
		prioridad.setUsuCreado(usuarioCreacion);
		prioridad.setFechaCreado(new Date());
		
		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(acciones.get("CREARID"))));
		historial.setTabla(tablas.get("PRIORIDAD"));
		historial.setAccion(acciones.get("CREAR"));
		historial.setUsuCreado(usuarioCreacion);
		historial.setFechaCreado(new Date());
		
		Prioridad prioridadCreada = new Prioridad();
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			prioridadCreada = prioridadService.save(prioridad);
			
			try {
				
				historial.setTablaId(prioridadCreada.getId());
				historialService.save(historial);
			} catch (DataAccessException e) {
				prioridadService.delete(prioridadCreada.getId());
				
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			} 
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La prioridad ha sido creada con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/prioridad/read/{id}")
	public ResponseEntity<?> ReadPrioridad(@PathVariable Long id) {
		Prioridad prioridad= null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			prioridad = prioridadService.findById(id);
			
			if(prioridad==null) {
				response.put("mensaje", "La prioridad ID: ".concat(id.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			if(prioridad.getEstadoRegistro()=='B') {
				response.put("mensaje", "La prioridad ID: ".concat(id.toString()
						.concat(" ha sido eliminada")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Prioridad>(prioridad,HttpStatus.OK);
	}
	
	@GetMapping("/prioridad/read")
	public ResponseEntity<?> ReadAllPrioridad() {
		List<Prioridad> prioridades = prioridadService.findAll();
		
		return new ResponseEntity<List<Prioridad>>(prioridades,HttpStatus.OK);
	}
	
	@PutMapping("/prioridad/update/{id}")
	public ResponseEntity<?> UpdatePrioridad(@PathVariable Long id, @RequestParam("nombrePrioridad") String nombrePrioridad, @RequestParam("usuario") String usuarioActualizacion) {
		Prioridad prioridadActual = prioridadService.findById(id);
		
		Map<String,Object> response = new HashMap<>();
		
		if(prioridadActual==null) {
			response.put("mensaje", "Error: no se puede editar, la prioridad ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(acciones.get("EDITARID"))));
		historial.setTabla(tablas.get("PRIORIDAD"));
		historial.setTablaId(id);
		historial.setAccion(acciones.get("EDITAR"));
		historial.setUsuCreado(usuarioActualizacion);
		historial.setFechaCreado(new Date());
		
		Prioridad prioridadBack = new Prioridad();
		prioridadBack.setId(prioridadActual.getId());
		prioridadBack.setNombre(prioridadActual.getNombre());
		prioridadBack.setUsuCreado(prioridadActual.getUsuCreado());
		prioridadBack.setFechaCreado(prioridadActual.getFechaCreado());
		prioridadBack.setUsuEditado(prioridadActual.getUsuEditado());
		prioridadBack.setFechaEditado(prioridadActual.getFechaEditado());
		prioridadBack.setEstadoRegistro(prioridadActual.getEstadoRegistro());
		
		try {
			prioridadActual.setNombre(nombrePrioridad);
			prioridadActual.setFechaEditado(new Date());
			prioridadActual.setUsuEditado(usuarioActualizacion);
			
			prioridadService.save(prioridadActual);
			
			try {
				historialService.save(historial);
			} catch (DataAccessException e) {
				prioridadService.save(prioridadBack);
				
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la prioridad en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La prioridad ha sido actualizada con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/prioridad/delete/{id}")
	public ResponseEntity<?> DeletePrioridad(@PathVariable Long id, @RequestParam("usuario") String usuarioActualizacion) {
		Prioridad prioridadActual = prioridadService.findById(id);
		Map<String, Object> response = new HashMap<>();
		
		if(prioridadActual==null) {
			response.put("mensaje", "Error: no se puede eliminar, la prioridad ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		Historial historial = new Historial();
		historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(acciones.get("ELIMINARID"))));
		historial.setTabla(tablas.get("PRIORIDAD"));
		historial.setTablaId(id);
		historial.setAccion(acciones.get("ELIMINAR"));
		historial.setUsuCreado(usuarioActualizacion);
		historial.setFechaCreado(new Date());
		
		Prioridad prioridadBack = new Prioridad();
		prioridadBack.setId(prioridadActual.getId());
		prioridadBack.setNombre(prioridadActual.getNombre());
		prioridadBack.setUsuCreado(prioridadActual.getUsuCreado());
		prioridadBack.setFechaCreado(prioridadActual.getFechaCreado());
		prioridadBack.setUsuEditado(prioridadActual.getUsuEditado());
		prioridadBack.setFechaEditado(prioridadActual.getFechaEditado());
		prioridadBack.setEstadoRegistro(prioridadActual.getEstadoRegistro());
		
		prioridadActual.setEstadoRegistro('B');
		prioridadActual.setFechaEditado(new Date());
		prioridadActual.setUsuEditado(usuarioActualizacion);
		
		try {
			prioridadService.save(prioridadActual);
			
			try {
				historialService.save(historial);
			} catch (DataAccessException e) {
				prioridadService.save(prioridadBack);
				
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la prioridad en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La prioridad ha sido eliminada con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}

	
	// --- SERVICIOS ESPECIALES PRIORIDAD ---
    @GetMapping("/prioridad/especial/index")
    public ResponseEntity<?> indexEspecialPrioridad(@RequestParam(required = false) Long id,
                                                   @RequestParam(required = false) Character estado) {
        Map<String, Object> response = new HashMap<>();
        List<Prioridad> registros = new java.util.ArrayList<>();
        if (id != null) {
            Prioridad reg = prioridadService.findById(id);
            if (reg == null) {
                response.put("error", true);
                response.put("message", "No existe el registro proporcionado");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (reg.getEstadoRegistro() != 'A') {
                response.put("error", true);
                response.put("message", "Acceso denegado, no se puede acceder a un registro desactivado");
                response.put("data", java.util.Collections.singletonList(reg));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if (estado == null || estado == '0' || reg.getEstadoRegistro() == estado) {
                registros.add(reg);
            }
        } else if (estado != null) {
            for (Prioridad reg : prioridadService.findAll()) {
                if (reg.getEstadoRegistro() == estado) {
                    registros.add(reg);
                }
            }
        } else {
            registros = prioridadService.findAll();
        }
        response.put("data", registros);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/prioridad/especial/create")
    public ResponseEntity<?> createEspecialPrioridad(@RequestParam String nombre,
                                                     @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
		nombre = nombre == null ? "" : nombre.trim();
		// Validaciones de nombre
		if (nombre.isEmpty()) {
			response.put("error", true);
			response.put("message", "El campo 'nombre' es obligatorio");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
			response.put("error", true);
			response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (nombre.matches("^\\d+$")) {
			response.put("error", true);
			response.put("message", "El campo 'nombre' no puede ser completamente numérico");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// Validar duplicado solo en activos usando servicio
		if (prioridadService.existsNombreActivo(nombre, null)) {
			response.put("error", true);
			response.put("message", "Ya existe un registro con ese nombre");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Prioridad nuevo = new Prioridad();
		nuevo.setNombre(nombre);
		nuevo.setUsuCreado(usuario);
		nuevo.setFechaCreado(new java.util.Date());
		nuevo.setEstadoRegistro('A');
		prioridadService.save(nuevo);
		response.put("message", "Registro creado correctamente");
		response.put("registro", nuevo);
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/prioridad/especial/update")
    public ResponseEntity<?> updateEspecialPrioridad(@RequestParam Long id,
                                                     @RequestParam(required = false) String nombre,
                                                     @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
        Prioridad reg = prioridadService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
		if (nombre != null) {
			nombre = nombre.trim();
			if (nombre.isEmpty()) {
				response.put("error", true);
				response.put("message", "El campo 'nombre' es obligatorio");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
				response.put("error", true);
				response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (nombre.matches("^\\d+$")) {
				response.put("error", true);
				response.put("message", "El campo 'nombre' no puede ser completamente numérico");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (prioridadService.existsNombreActivo(nombre, id)) {
				response.put("error", true);
				response.put("message", "Ya existe otro registro con ese nombre");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			reg.setNombre(nombre);
		}
        reg.setUsuEditado(usuario);
        reg.setFechaEditado(new java.util.Date());
        prioridadService.save(reg);
        response.put("message", "Registro actualizado correctamente");
        response.put("registro", reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/prioridad/especial/changeStatus")
    public ResponseEntity<?> changeStatusEspecialPrioridad(@RequestParam Long id,
                                                           @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
        Prioridad reg = prioridadService.findById(id);
        if (reg == null) {
            response.put("error", true);
            response.put("message", "No existe el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
		String mensaje;
		switch (reg.getEstadoRegistro()) {
			case 'A':
				reg.setEstadoRegistro('I');
				reg.setUsuEditado(usuario);
				reg.setFechaEditado(new java.util.Date());
				prioridadService.save(reg);
				mensaje = "Registro desactivado correctamente";
				break;
			case 'I':
				// Validar que no exista otro registro activo con el mismo nombre y distinto id
				if (prioridadService.existsNombreActivo(reg.getNombre(), id)) {
					response.put("error", true);
					response.put("message", "Ya existe otro registro con ese nombre");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				reg.setEstadoRegistro('A');
				reg.setUsuEditado(usuario);
				reg.setFechaEditado(new java.util.Date());
				prioridadService.save(reg);
				mensaje = "Registro activado correctamente";
				break;
			default:
				reg.setEstadoRegistro('I');
				reg.setUsuEditado(usuario);
				reg.setFechaEditado(new java.util.Date());
				prioridadService.save(reg);
				mensaje = "Registro desactivado correctamente";
				break;
		}
		response.put("message", mensaje);
		response.put("registro", reg);
		return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
