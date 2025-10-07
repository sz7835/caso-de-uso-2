package com.delta.deltanet.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.Cargo;
import com.delta.deltanet.models.entity.Empresa;
import com.delta.deltanet.models.entity.PersonalExterno;
import com.delta.deltanet.models.entity.PersonalInterno;
import com.delta.deltanet.models.entity.TipoDoc;
import com.delta.deltanet.models.entity.TipoPersonal;
import com.delta.deltanet.models.service.ICargoService;
import com.delta.deltanet.models.service.IEmpresaService;
import com.delta.deltanet.models.service.IPersonalExternoService;
import com.delta.deltanet.models.service.IPersonalInternoService;
import com.delta.deltanet.models.service.ITipoDocService;
import com.delta.deltanet.models.service.ITipoPersonalService;
import com.delta.deltanet.models.service.SendEmailService;

@SuppressWarnings("all")
@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/personal")
public class PersonalController {

	@Autowired
	private IEmpresaService empresaService;
	@Autowired
	private ICargoService cargoService;
	@Autowired
	private ITipoDocService tipoDocService;
	@Autowired
	private IPersonalInternoService personalInternoService;
	@Autowired
	private IPersonalExternoService personalExternoService;
	@Autowired
	private ITipoPersonalService tipoPersonalService;
	@Autowired
    private SendEmailService sendEmailService;
	
	
	@GetMapping("/info")
	public ResponseEntity<?> GetInfo(){
		Map<String, Object> response = new HashMap<>();
		
		List<TipoPersonal> tiposPersonal = tipoPersonalService.findAll();
		List<TipoDoc> tiposDoc = tipoDocService.findAll();
		List<Empresa> empresas = empresaService.findAll();
		List<Cargo> cargos = cargoService.findAll();
		
		
		response.put("types", tiposPersonal);
		response.put("documents", tiposDoc);
		response.put("companies", empresas);
		response.put("positions", cargos);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> CreatePersonal(@RequestParam("usuario") String usuario,
											@RequestParam("document") String document,
											@RequestParam("nombres") String nombres,
											@RequestParam("apaterno") String apaterno,
											@RequestParam("amaterno") String amaterno,
											@RequestParam("email") String email,
											@RequestParam("type_doc") Long tipoDocId,
											@RequestParam("type_id") Long tipoPersonal,
											@RequestParam("company_id") Long empresaId,
											@RequestParam("position_id") Optional<Long> cargoId,
											@RequestParam("date") String fecha,
											@RequestParam("usuario_sistema") String usuarioSistema){
		
		Map<String, Object> response = new HashMap<>();
		
		if(cargoId.isPresent()) {
			try {
				Empresa empresa = empresaService.findById(empresaId);
				TipoDoc tipoDoc = tipoDocService.findById(tipoDocId);
				Cargo cargo = cargoService.findById(cargoId.get());
				
				PersonalExterno personal = new PersonalExterno();
				personal.setEmpresa(empresa);
				personal.setNombres(nombres);
				personal.setApePaterno(apaterno);
				personal.setApeMaterno(amaterno);
				personal.setTipoDoc(tipoDoc);
				personal.setNroDoc(document);
				personal.setCargo(cargo);
				personal.setUsuario(usuario);
				personal.setEstado("E");
				personal.setUsuCreado(usuarioSistema);
				personal.setFechaCreado(new Date());
				
				personalExternoService.save(personal);
				
				response.put("mensaje", "Personal externo guardado con éxito");
				response.put("personal", personal);
			} catch (Exception e) {
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			try {
				Empresa empresa = empresaService.findById(empresaId);
				TipoDoc tipoDoc = tipoDocService.findById(tipoDocId);
				
				PersonalInterno personal = new PersonalInterno();
				personal.setEmpresa(empresa);
				personal.setNombres(nombres);
				personal.setApePaterno(apaterno);
				personal.setApeMaterno(amaterno);
				personal.setFecNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
				personal.setTipoDoc(tipoDoc);
				personal.setNroDoc(document);
				personal.setCorreoPer(email);
				personal.setUsuario(usuario);
				personal.setEstado("E");
				personal.setUsuCreado(usuarioSistema);
				personal.setFechaCreado(new Date());
				
				personalInternoService.save(personal);
				
				response.put("mensaje", "Personal interno guardado con éxito");
				response.put("personal", personal);
			} catch (Exception e) {
				response.put("mensaje", "Error al realizar el insert en la base de datos");
				response.put("error", e.getMessage());
				return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@PutMapping("/activar")
	public ResponseEntity<?> ActivatePersonal(@RequestParam("usuario") String usuario,
											@RequestParam("tipo") String tipo,
											@RequestParam("titulo") String titulo,
											@RequestParam("mensaje") String mensaje,
											@RequestParam("servicio") String servicio,
											@RequestParam("correo") String correo,
											@RequestParam("usuario_sistema") String usuarioSistema){
		Map<String, Object> response = new HashMap<>();
		
		try {
			if(tipo.equals("1")) {
				PersonalInterno personalInt = personalInternoService.findByUsuario(usuario);
				if(personalInt==null) {
					response.put("mensaje", "No existe el personal interno en la base de datos");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}
				personalInt.setEstado("A");
				personalInt.setUsuEditado(usuarioSistema);
				personalInt.setFechaEditado(new Date());
				personalInternoService.save(personalInt);
			}else if(tipo.equals("2")) {
				PersonalExterno personalExt = personalExternoService.findByUsuario(usuario);
				if(personalExt==null) {
					response.put("mensaje", "No existe el personal externo en la base de datos");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}
				personalExt.setEstado("A");
				personalExt.setUsuEditado(usuarioSistema);
				personalExt.setFechaEditado(new Date());
				personalExternoService.save(personalExt);
			}else {
				response.put("mensaje", "No existe el tipo de personal");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			
			//SEND EMAIL
			sendEmailService.sendMail(correo, titulo, mensaje, servicio);
			
			response.put("mensaje", "La activacion se realizo con exito");
		} catch (Exception e) {
			response.put("mensaje", "Error al activar el personal");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/sendMail")
	public ResponseEntity<?> Send(@RequestParam("correo") String correo,
								  @RequestParam("titulo") String titulo,
								  @RequestParam("mensaje") String mensaje) {
		Map<String, Object> response = new HashMap<>();
	
        
        try {
			sendEmailService.sendMail(correo, titulo, mensaje, null);
			response.put("draw", "Enviado");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (MessagingException e) {
			response.put("draw", "Error al enviar");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
}
