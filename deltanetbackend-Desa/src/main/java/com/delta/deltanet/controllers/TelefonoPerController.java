package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.IPersonaService;
import com.delta.deltanet.models.service.ITelefonoService;
import com.delta.deltanet.models.service.ITipoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SuppressWarnings("all")
@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/telefonoPer")
public class TelefonoPerController {
    @Autowired
    private ITelefonoService telefonoService;

    @Autowired
    private IPersonaService perService;

    @GetMapping("/index")
    public ResponseEntity<?> ReadAllPhones(@RequestParam(name="idPersona") Long idPer,
                                           @RequestParam(required = false, name="tipo") Long tipo,
                                           @RequestParam(required = false, name="nro") String nro,
                                           @RequestParam(name="estado") Integer estado){
        List<Object> listado = null;

        if (tipo != null && nro != null && estado != 9){
            listado = telefonoService.findByTelefonosPer(tipo, nro, estado, idPer);
        }

        if (tipo != null && nro == null && estado == 9){
            listado = telefonoService.findByTelefonosPer(tipo, idPer);
        }

        if (tipo != null && nro != null && estado == 9){
            listado = telefonoService.findByTelefonosPer(tipo,nro,idPer);
        }

        if (tipo != null && nro == null && estado != 9){
            listado = telefonoService.findByTelefonosPer(tipo,estado,idPer);
        }

        if (tipo == null && nro != null && estado == 9){
            listado = telefonoService.findByTelefonosPer(nro,idPer);
        }

        if (tipo == null && nro != null && estado != 9){
            listado = telefonoService.findByTelefonosPer(nro,estado,idPer);
        }

        if (tipo == null && nro == null && estado != 9){
            listado = telefonoService.findByTelefonosPer(estado,idPer);
        }

        if (tipo == null && nro == null && estado == 9){
            listado = telefonoService.findAllPer(idPer);
        }

        Persona registro;
        Iterator<Object> it = listado != null ? listado.iterator() : null;
        List<lstTelefono1> lstResumen = new ArrayList<>();

        while (it != null && it.hasNext()){
            Object[] row=(Object[]) it.next();
            registro = perService.buscarId((Long) row[1]);
            lstTelefono1 lista = new lstTelefono1();
            lista.setIdTelefono((Long) row[0]);
            lista.setIdPersona(registro.getId());
            lista.setTipo((Long) row[2]);
            lista.setNumero(String.valueOf(row[3]));
            lista.setEstado((Integer) row[4]);
            lista.setUsuCreado(String.valueOf(row[5]));
            lista.setFechaCreado((Date) row[6]);
            lista.setUsuUpdate(String.valueOf(row[7]));
            lista.setFechaUpdate((Date) row[8]);

            lstResumen.add(lista);
        }

        return new ResponseEntity<>(lstResumen, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> muestraReg(Long idTelefono){
        Map<String, Object> response = new HashMap<>();

        try {
            Telefono phone = telefonoService.findById(idTelefono);
            if (phone == null){
                response.put("mensaje", "El telefono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(phone.getPersona().getArea() != null) {
	            phone.getPersona().getArea().setPuestos(null);
	            phone.getPersona().getArea().getGerencia().setOrgAreas(null);
            }
            phone.getPersona().setPuesto(null);
            response.put("telefono",phone);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la busqueda en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateTelefono(@RequestParam(name = "idPersona") Long idPer,
                                            @RequestParam(name = "tipo") Long tipo,
                                            @RequestParam(name = "numero") String nro,
                                            @RequestParam(name = "usuario") String user){
        Map<String, Object> response = new HashMap<>();

        try{
        	if (telefonoService.existsByNumero(nro)) {
        		response.put("mensaje", "El telefono ya existe en la base de datos.");
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        	}
        	
            Persona persona = perService.buscarId(idPer);
            if (persona == null){
                response.put("mensaje", "El ID persona no se encuentra en la base de datos. ID: ".concat(idPer.toString()));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Telefono phone = new Telefono();
            phone.setPersona(persona);
            phone.setTipo(tipo);
            phone.setNumero(nro);
            phone.setEstado(1);
            phone.setUsuCreado(user);
            phone.setFechaCreado(new Date());

            telefonoService.save(phone);

            response.put("mensaje", "Teléfono guardado con éxito");
        }catch (Exception e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateTelefono(@RequestParam(name = "idTelefono") Long idTelefono,
    		@RequestParam(name = "tipo") Long tipo,
    		@RequestParam(name = "numero") String numero,
            @RequestParam(name = "usuUpdate") String usuUpdate){
        Map<String, Object> response = new HashMap<>();
        try{
            Telefono phone = telefonoService.findById(idTelefono);
            if (phone == null){
                response.put("mensaje", "El telefono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!tipo.equals(phone.getTipo())) {
            	if (telefonoService.existsByNumeroAndPersona_IdNot(numero, phone.getPersona().getId())) {
            		response.put("mensaje", "El telefono ya existe en la base de datos.");
                    return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            	}
            }
            if (!numero.equals(phone.getNumero())) {
            	if (telefonoService.existsByNumero(numero)) {
            		response.put("mensaje", "El telefono ya existe en la base de datos.");
                    return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            	}
            }
            phone.setTipo(tipo);
            phone.setNumero(numero);
            phone.setUsuUpdate(usuUpdate);
            phone.setFechaUpdate(new Date());
            telefonoService.save(phone);
            response.put("mensaje", "Teléfono actualizado con éxito");
        }catch (Exception e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> UpdEstado(@RequestParam(name = "idTelefono") Long idTelefono,
						                @RequestParam(name = "usuUpdate") String usuUpdate){
        Map<String, Object> response = new HashMap<>();
        String message = "Teléfono desactivado con éxito";
        try{
            Telefono phone = telefonoService.findById(idTelefono);
            if (phone == null){
                response.put("mensaje", "El telefono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (phone.getEstado() == 0){
                message = "Teléfono activado con éxito";
                phone.setEstado(1);
            } else if (phone.getEstado() == 1) {
                phone.setEstado(0);
            }
            phone.setUsuUpdate(usuUpdate);
            phone.setFechaUpdate(new Date());
            telefonoService.save(phone);
            response.put("mensaje", message);
        }catch (Exception e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
