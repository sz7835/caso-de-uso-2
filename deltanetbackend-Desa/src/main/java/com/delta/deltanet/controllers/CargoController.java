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

import com.delta.deltanet.models.entity.Cargo;
import com.delta.deltanet.models.service.ICargoService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/cargo")
public class CargoController {

	@Autowired
	private ICargoService cargoService;
  
	@PostMapping("/create")
	public ResponseEntity<?> CreateCargo(@RequestBody Cargo cargo){
		
		Map<String, Object> response = new HashMap<>();
		
		cargo.setUsuCreado(cargo.getUsuario_sistema());
		
		try {
			cargoService.save(cargo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cargo ha sido creado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> UpdateCargo(@RequestBody Cargo cargo){
		
		Map<String, Object> response = new HashMap<>();
		
		cargo.setUsuEditado(cargo.getUsuario_sistema());
		cargo.setFechaEditado(new Date());
		
		try {
			cargoService.save(cargo);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cargo ha sido actualizado con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<?> ReadCargo(@PathVariable Long id) {
		Cargo cargo = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cargo = cargoService.findById(id);
			
			if(cargo==null) {
				response.put("mensaje", "El cargo ID: ".concat(id.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Cargo>(cargo,HttpStatus.OK);
	}
	
	@GetMapping("/read")
	public ResponseEntity<?> ReadAllArea() {
		List<Cargo> cargos = cargoService.findAll();
		
		return new ResponseEntity<List<Cargo>>(cargos,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> DeleteArea(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			cargoService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cargo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cargo ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}