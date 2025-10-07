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

import com.delta.deltanet.models.entity.Empresa;
import com.delta.deltanet.models.service.IEmpresaService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/empresa")
public class EmpresaController {

	@Autowired
	private IEmpresaService empresaService;
  
	@PostMapping("/create")
	public ResponseEntity<?> CreateEmpresa(@RequestBody Empresa empresa){

		Map<String, Object> response = new HashMap<>();
		
		empresa.setUsuCreado(empresa.getUsuario_sistema());
		
		try {
			empresaService.save(empresa);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La empresa ha sido creada con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> UpdateEmpresa(@RequestBody Empresa empresa){
		
		Map<String, Object> response = new HashMap<>();
		
		empresa.setUsuEditado(empresa.getUsuario_sistema());
		empresa.setFechaEditado(new Date());
		
		try {
			empresaService.save(empresa);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La empresa ha sido actualizada con éxito!");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<?> ReadEmpresa(@PathVariable Long id) {
		Empresa empresa = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			empresa = empresaService.findById(id);
			
			if(empresa==null) {
				response.put("mensaje", "El empresa ID: ".concat(id.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Empresa>(empresa,HttpStatus.OK);
	}
	
	@GetMapping("/read")
	public ResponseEntity<?> ReadAllArea() {
		List<Empresa> empresas = empresaService.findAll();
		
		return new ResponseEntity<List<Empresa>>(empresas,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> DeleteArea(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			empresaService.delete(id);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el empresa en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La empresa ha sido eliminada con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}