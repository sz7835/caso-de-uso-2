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

import com.delta.deltanet.models.entity.TipoDoc;
import com.delta.deltanet.models.service.ITipoDocService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/tipoDoc")
public class TipoDocController {

	@Autowired
	private ITipoDocService tipoDocService;
  
	@PostMapping("/create")
	public ResponseEntity<?> CreateTipoDoc(@RequestBody TipoDoc tipoDoc){
		
		Map<String, Object> response = new HashMap<>();
		
		tipoDoc.setUsuCreado(tipoDoc.getUsuario_sistema());
		
		try {
			tipoDocService.save(tipoDoc);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El tipoDoc ha sido creado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> UpdateTipoDoc(@RequestBody TipoDoc tipoDoc){
		
		Map<String, Object> response = new HashMap<>();
		
		tipoDoc.setUsuEditado(tipoDoc.getUsuario_sistema());
		tipoDoc.setFechaEditado(new Date());
		
		try {
			tipoDocService.save(tipoDoc);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El tipoDoc ha sido actualizado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<?> ReadTipoDoc(@PathVariable Long id) {
		TipoDoc tipoDoc = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			tipoDoc = tipoDocService.findById(id);
			
			if(tipoDoc==null) {
				response.put("mensaje", "El tipoDoc ID: ".concat(id.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<TipoDoc>(tipoDoc,HttpStatus.OK);
	}
	
	@GetMapping("/read")
	public ResponseEntity<?> ReadAllArea() {
		List<TipoDoc> tipoDocs = tipoDocService.findAll();
		
		return new ResponseEntity<List<TipoDoc>>(tipoDocs,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> DeleteArea(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			tipoDocService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el tipoDoc en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El tipoDoc ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}