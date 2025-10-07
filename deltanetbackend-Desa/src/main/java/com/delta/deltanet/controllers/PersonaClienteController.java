package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/persona-cliente")
public class PersonaClienteController {
	
    @Autowired
    private PersonaClienteServiceImpl personaClienteService;
    
    @Autowired
    public IPersonaService perService;

    
	@GetMapping("/index")
	public ResponseEntity<?> index(@RequestParam(required = false, name="estado") Long estado,
			@RequestParam(name="persona_id") Long idPersona){
		Map<String, Object> response = new HashMap<>();
	      try {
	    	  List<PersonaCliente> clients = null;
	    	  if(estado == null) {
		          clients = personaClienteService.findAllP(idPersona);
	    	  }else {
		          clients = personaClienteService.findAllPS(idPersona, estado);
	    	  }
	          for(PersonaCliente client : clients) {
	        	  client.getPerson().getArea().getGerencia().setOrgAreas(null);
	        	  client.getPerson().getArea().setPuestos(null);
	          }
	          response.put("clients", clients);
	          return new ResponseEntity<>(response, HttpStatus.OK);
	      } catch (Exception e) {
	          response.put("message", "Error al listar los clientes");
	          response.put("error", e.getMessage());
	          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	}

    @PostMapping("/save")
    public ResponseEntity<?> creaRelacion(@RequestParam(name="idPersona") Long idPersona,
            							  @RequestParam(name="idCliente") Long idCliente,
                                          @RequestParam(name = "creaUsr") String creaUsr){
        Map<String, Object> response = new HashMap<>();
        Persona persona = perService.buscarId(idPersona);
        if(persona == null){
            response.put("message","No se encuentra la persona enviada. ID: ".concat(idPersona.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Persona cliente = perService.buscarId(idCliente);
        if(cliente == null){
            response.put("message","No se encuentra el cliente enviada. ID: ".concat(idCliente.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        PersonaCliente item = personaClienteService.findPersonaAndClient(idPersona, idCliente);
        if(item != null){
            response.put("message","Ya existe una relacion con el cliente activo");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            PersonaCliente registro = new PersonaCliente();
            registro.setClient(cliente);
            registro.setPerson(persona);
            registro.setEstado(1L);
            registro.setCreateUsr(creaUsr);
            registro.setCreateFec(new Date());
            registro = personaClienteService.save(registro);
            registro.getPerson().getArea().getGerencia().setOrgAreas(null);
            registro.getPerson().getArea().setPuestos(null);
            response.put("registro", registro);
            response.put("message","La relacion se creo satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message",e.getMessage());
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam(name="idPersonaCliente") Long idPersonaCliente,
    								@RequestParam(name="idPersona") Long idPersona,
									@RequestParam(name="idCliente") Long idCliente,
						            @RequestParam(name = "updUser") String updUser) {
        Map<String, Object> response = new HashMap<>();
        PersonaCliente personaCliente = personaClienteService.findById(idPersonaCliente);
        if(personaCliente == null){
            response.put("message","No se encuentra la relacion enviada. ID: ".concat(idPersonaCliente.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Persona persona = perService.buscarId(idPersona);
        if(persona == null){
            response.put("message","No se encuentra la persona enviada. ID: ".concat(idPersona.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Persona cliente = perService.buscarId(idCliente);
        if(cliente == null){
            response.put("message","No se encuentra el cliente enviada. ID: ".concat(idCliente.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
        	personaCliente.setClient(cliente);
        	personaCliente.setPerson(persona);
        	personaCliente.setUpdteUsr(updUser);
        	personaCliente.setUpdateFec(new Date());
        	personaCliente = personaClienteService.save(personaCliente);
        	personaCliente.getPerson().getArea().getGerencia().setOrgAreas(null);
        	personaCliente.getPerson().getArea().setPuestos(null);
            response.put("message", "Actualización existosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la funcion show");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "updUser") String updUser){
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        PersonaCliente personCliente = personaClienteService.findById(id);
        if(personCliente == null){
            response.put("message", "El id de jefe de area [" + id + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (personCliente.getEstado() == 0L) {
            PersonaCliente item = personaClienteService.findPersonaAndClient(personCliente.getIdPersona(), personCliente.getIdCliente());
            if(item != null){
                response.put("message","Ya existe una relacion con el cliente activo por lo cual no puedes activar esta relacion");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        	personCliente.setEstado(1L);
            accion = "habilitado";
        } else {
        	personCliente.setEstado(0L);
            accion = "deshabilitado";
        }
        try {
        	personCliente.setUpdteUsr(updUser);
        	personCliente.setUpdateFec(new Date());
        	personaClienteService.save(personCliente);
        	personCliente.getPerson().getArea().getGerencia().setOrgAreas(null);
        	personCliente.getPerson().getArea().setPuestos(null);
            response.put("personCliente",personCliente);
            response.put("message","Registro " + accion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message", "Error inesperado en la funcion delete");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}