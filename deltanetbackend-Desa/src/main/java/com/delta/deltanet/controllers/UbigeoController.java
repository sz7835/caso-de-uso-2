package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.Ubigeo;
import com.delta.deltanet.models.service.IUbigeoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/ubigeo")
public class UbigeoController {

	@Autowired
	private IUbigeoService ubigeoService;
  
	@PostMapping("/create")
	public ResponseEntity<?> CreateUbigeo(@RequestBody Ubigeo ubigeo){
		
		Map<String, Object> response = new HashMap<>();
		
		ubigeo.setUsuCreado(ubigeo.getUsuario_sistema());
		
		try {
			ubigeoService.save(ubigeo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El ubigeo ha sido creado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> UpdateUbigeo(@RequestBody Ubigeo ubigeo){
		
		Map<String, Object> response = new HashMap<>();
		
		ubigeo.setUsuEditado(ubigeo.getUsuario_sistema());
		ubigeo.setFechaEditado(new Date());
		
		try {
			ubigeoService.save(ubigeo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El ubigeo ha sido actualizado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<?> ReadUbigeo(@PathVariable String id) {
		Ubigeo ubigeo = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			ubigeo = ubigeoService.findById(id);
			
			if(ubigeo==null) {
				response.put("mensaje", "El ubigeo ID: ".concat(id.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Ubigeo>(ubigeo,HttpStatus.OK);
	}
	
	@GetMapping("/read")
	public ResponseEntity<?> ReadAllArea() {
		List<Ubigeo> ubigeos = ubigeoService.findAll();
		
		return new ResponseEntity<List<Ubigeo>>(ubigeos,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> DeleteArea(@PathVariable("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			ubigeoService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el ubigeo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El ubigeo ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}