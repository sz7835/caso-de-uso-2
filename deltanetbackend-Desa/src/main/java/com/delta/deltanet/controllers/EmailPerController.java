package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.IEmailService;
import com.delta.deltanet.models.service.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SuppressWarnings("all")
@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/correoPer")
public class EmailPerController {
    @Autowired
    private IEmailService emailService;

    @Autowired
    private IPersonaService perService;

    @PersistenceContext
	private EntityManager entityManager;

    @GetMapping("/show")
    public ResponseEntity<?> index(Long idCorreo){
        Map<String, Object> response = new HashMap<>();
        try {
            EMail mail = emailService.findById(idCorreo);
            if (mail==null){
                response.put("mensaje", "El correo ID: ".concat(idCorreo.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(mail.getPersona().getArea() != null) {
                mail.getPersona().getArea().setPuestos(null);
                mail.getPersona().getArea().getGerencia().setOrgAreas(null);
            }
            if(mail.getPersona().getPuesto() != null) {
                mail.getPersona().getPuesto().getArea().setPuestos(null);
            }
            response.put("correo", mail);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la busqueda en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateCorreo(@RequestParam(name="idPersona") Long idPersona,
                                          @RequestParam(name="tipo") Long tipo,
                                          @RequestParam(name="correo") String correo,
                                          @RequestParam(name="usuCreado") String usuCreado){
        Map<String, Object> response = new HashMap<>();
        try {
            EMail registro = new EMail();
            Persona persona = perService.buscarId(idPersona);
            if (persona==null){
                response.put("mensaje", "El ID Persona: ".concat(idPersona.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Verificar si el correo ya existe
			List<EMail> existingCorreos = emailService.findByCorreo(correo);
			if (!existingCorreos.isEmpty()) {
				response.put("message", "El correo ya existe en la base de datos");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
            registro.setPersona(persona);
            registro.setTipo(tipo);
            registro.setCorreo(correo);
            registro.setEstado(1);
            registro.setUsuCreado(usuCreado);
            registro.setFechaCreado(new Date());

            emailService.save(registro);

            response.put("message", "Correo guardado con éxito");

        } catch (Exception e){
            response.put("message", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<?> ReadAllEmails(@RequestParam(name="idPersona") Long idPer,
                                           @RequestParam(name="tipo") Long tipo,
                                           @RequestParam(required = false,name="email") String email,
                                           @RequestParam(name="estado") Integer estado){

        List<EMail> correos = null;
        List<Object> listado = null;

        if(tipo==9 && email==null && estado==9){
            listado = emailService.findAllPer(idPer);
        }
        if(tipo!=9 && email==null && estado==9){
            listado = emailService.findByCorreosPer(tipo, idPer);
        }
        if(tipo!=9 && email!=null && estado==9){
            listado = emailService.findByCorreosPer(tipo, email, idPer);
        }
        if(tipo!=9 && email==null && estado!=9){
            listado = emailService.findByCorreosPer(tipo, estado, idPer);
        }
        if(tipo!=9 && email!=null && estado!=9){
            listado = emailService.findByCorreosPer(tipo, email, estado, idPer);
        }
        if(tipo==9 && email!=null && estado==9){
            listado = emailService.findByCorreosPer(email, idPer);
        }
        if(tipo==9 && email!=null && estado!=9){
            listado = emailService.findByCorreosPer(email, estado, idPer);
        }
        if(tipo==9 && email==null && estado!=9){
            listado = emailService.findByCorreosPer(estado, idPer);
        }

        Persona registro;
        Iterator<Object> it = listado != null ? listado.iterator() : null;
        List<lstEmail1> lstResumen = new ArrayList<>();

        while (it != null && it.hasNext()){
            Object[] row=(Object[]) it.next();
            registro = perService.buscarId((Long) row[1]);
            lstEmail1 lista = new lstEmail1();
            lista.setIdEMail((Long) row[0]);
            lista.setIdPersona(registro.getId());
            //lista.setIdContacto((Long) row[2]);
            lista.setTipo((Long) row[2]);
            lista.setCorreo(String.valueOf(row[3]));
            lista.setEstado((Integer) row[4]);
            lista.setUsuCreado(String.valueOf(row[5]));
            lista.setFechaCreado((Date) row[6]);
            lista.setUsuUpdate(String.valueOf(row[7]));
            lista.setFechaUpdate((Date) row[8]);

            lstResumen.add(lista);
        }

        return new ResponseEntity<>(lstResumen, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateCorreo(@RequestParam(name = "idEMail") Long idEMail,
                                        @RequestParam(name = "correo") String correo,
                                        @RequestParam(name = "tipo") Long tipo,
                                        @RequestParam(name = "updUsuario") String updUser){
        Map<String, Object> response = new HashMap<>();
        try {
            EMail mail = emailService.findById(idEMail);
            if (mail==null){
                response.put("mensaje", "El correo ID: ".concat(idEMail.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Verificar si ya existe otro correo con el mismo valor
			String sql = "SELECT * FROM per_persona_correo WHERE id != :idEMail AND email = :correo";
			Query query = entityManager.createNativeQuery(sql, EMail.class);
			query.setParameter("idEMail", idEMail);
			query.setParameter("correo", correo);
			List<EMail> existingCorreos = query.getResultList();

			if (!existingCorreos.isEmpty()) {
				response.put("mensaje", "El correo ya existe en la base de datos.");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

            mail.setTipo(tipo);
            mail.setCorreo(correo);
            mail.setUsuUpdate(updUser);
            mail.setFechaUpdate(new Date());
            emailService.save(mail);
            response.put("mensaje", "Correo actualizado con éxito");
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la actualización en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> UpdEstado(@RequestParam(name = "idEMail") Long idEMail,
            @RequestParam(name = "updUsuario") String updUser){
        Map<String, Object> response = new HashMap<>();
        String message = "Correo desactivado con éxito";
        try {
            EMail mail = emailService.findById(idEMail);
            if (mail==null){
                response.put("mensaje", "El correo ID: ".concat(idEMail.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (mail.getEstado()==0){
                mail.setEstado(1);
                message = "Correo activado con éxito";
            }else if(mail.getEstado()==1){
                mail.setEstado(0);
            }

            mail.setUsuUpdate(updUser);
            mail.setFechaUpdate(new Date());
            emailService.save(mail);

            response.put("mensaje", message);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la actualizacion en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
