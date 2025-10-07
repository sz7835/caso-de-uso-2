package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Parametro;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.Relacion;
import com.delta.deltanet.models.entity.Telefono;
import com.delta.deltanet.models.entity.TelefonoTipo;
import com.delta.deltanet.models.entity.TipoDocumento;
import com.delta.deltanet.models.entity.TipoRelacion;
import com.delta.deltanet.models.entity.lstDireccion1;
import com.delta.deltanet.models.entity.lstEmail1;
import com.delta.deltanet.models.entity.lstPersona1;
import com.delta.deltanet.models.entity.lstPersona3;
import com.delta.deltanet.models.entity.lstTelefono1;
import com.delta.deltanet.models.service.IPersonaService;
import com.delta.deltanet.models.service.IRelacionService;
import com.delta.deltanet.models.service.ITelefonoService;
import com.delta.deltanet.models.service.ITipoTelefonoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/telefonoCto")
public class TelefonoCtoController {
    @Autowired
    private ITelefonoService telefonoService;
    @Autowired
    public IPersonaService perService;
    @Autowired
    private ITipoTelefonoService tipoTelefonoService;
    @Autowired
    public IRelacionService relService;

    // @GetMapping("/index")
    // public ResponseEntity<?> ReadAllPhones(@RequestParam(name="idContacto") Long idCto,
    //                                        @RequestParam(name="tipo") Long tipo,
    //                                        @RequestParam(required = false, name="nro") String nro,
    //                                        @RequestParam(name="estado") Integer estado){
    //     Map<String, Object> response = new HashMap<>();
    //     List<Telefono> telefonos = null;
    //     List<Object> listado = null;
    //     if (nro != null && estado != 9 && tipo != 9){
    //         listado = telefonoService.findByTelefonos(idCto, nro, estado, tipo);
    //     }
    //     if (nro == null && estado == 9 && tipo == 9){
    //         listado = telefonoService.findByTelefonos(idCto);
    //     }
    //     if (nro != null && estado == 9 && tipo == 9){
    //         listado = telefonoService.findByTelefonos(idCto,nro);
    //     }
    //     if (nro != null && estado != 9 && tipo == 9){
    //         listado = telefonoService.findByTelefonos(idCto,nro,estado);
    //     }
    //     if (nro != null && estado == 9 && tipo != 9){
    //         listado = telefonoService.findByTelefonos(idCto,nro,tipo);
    //     }
    //     if (nro == null && estado != 9 && tipo == 9){
    //         listado = telefonoService.findByTelefonos(idCto,estado);
    //     }
    //     if (nro == null && estado != 9 && tipo != 9){
    //         listado = telefonoService.findByTelefonos(idCto,estado,tipo);
    //     }
    //     if (nro == null && estado == 9 && tipo != 9){
    //         listado = telefonoService.findByTelefonos(idCto,tipo);
    //     }
    //     Iterator<Object> it = listado.iterator();
    //     List<lstTelefono1> lstResumen = new ArrayList<>();
    //     while (it.hasNext()){
    //         Object[] row=(Object[]) it.next();
    //         //registro = perService.buscarId((Long) row[1]);
    //         lstTelefono1 lista = new lstTelefono1();
    //         lista.setIdTelefono((Long) row[0]);
    //         //lista.setIdPersona(registro.getId());
    //         lista.setTipo((Long) row[3]);
    //         lista.setNumero(String.valueOf(row[4]));
    //         lista.setEstado((Integer) row[5]);
    //         lista.setUsuCreado(String.valueOf(row[6]));
    //         lista.setFechaCreado((Date) row[7]);
    //         lista.setUsuUpdate(String.valueOf(row[8]));
    //         lista.setFechaUpdate((Date) row[9]);
    //         lstResumen.add(lista);
    //     }
    //     return new ResponseEntity<>(lstResumen, HttpStatus.OK);
    // }

    @GetMapping("/index")
    public ResponseEntity<?> ReadAllPhones(@RequestParam(name="idContacto") Long idCto,
                                           @RequestParam(name="tipo") Long tipo,
                                           @RequestParam(required = false, name="nro") String nro,
                                           @RequestParam(name="estado") Integer estado){
        Map<String, Object> response = new HashMap<>();
        List<Telefono> telefonos = null;
        List<Object> listado = null;
        Iterator<Object> it = listado.iterator();
        List<lstTelefono1> lstResumen = new ArrayList<>();
        while (it.hasNext()){
            Object[] row=(Object[]) it.next();
            //registro = perService.buscarId((Long) row[1]);
            lstTelefono1 lista = new lstTelefono1();
            lista.setIdTelefono((Long) row[0]);
            //lista.setIdPersona(registro.getId());
            //lista.setIdContacto((Long) row[2]);
            lista.setTipo((Long) row[3]);
            lista.setNumero(String.valueOf(row[4]));
            lista.setEstado((Integer) row[5]);
            lista.setUsuCreado(String.valueOf(row[6]));
            lista.setFechaCreado((Date) row[7]);
            lista.setUsuUpdate(String.valueOf(row[8]));
            lista.setFechaUpdate((Date) row[9]);
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
                response.put("mensaje", "El télefono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("telefono",phone);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la busqueda en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @GetMapping("/showCto")
    public ResponseEntity<?> show(@RequestParam(name = "idPersona") Long idPersona,
    		@RequestParam(name = "idContactoDe") Long idContactoDe) {
        Map<String, Object> response = new HashMap<>();

        try {
        	
            Persona persona = perService.buscarId(idPersona);
            Persona personaDe = perService.buscarId(idContactoDe);
            if (persona == null || personaDe == null) {
                response.put("mensaje", "La persona ID: ".concat((persona == null?idPersona.toString():idContactoDe.toString())
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            
            List<Relacion> listado = null;
            Long idTipoNodoDestino = personaDe.getIdNodoTipo();
            List<Integer> tipoRelTipos = new ArrayList<Integer>();
        	tipoRelTipos.add(3);
        	if(personaDe.getPerJur() != null) {
        		tipoRelTipos.add(1);
        	}
            listado = relService.buscarRelacionContactos(idContactoDe, idTipoNodoDestino, null, null, tipoRelTipos, null, null, null);

            boolean isContacto = listado.stream().anyMatch(r -> idPersona.equals(r.getIdPersona()));
            
            if(!isContacto) {
            	response.put("mensaje", "No tiene permiso para acceder a los teléfonos de esta persona, ya que no está registrada como su contacto.");
            	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            lstPersona3 registro = new lstPersona3();
            registro.setId(persona.id);
            registro.setIdNodoTipo(persona.getIdNodoTipo());
            registro.setTipPer(persona.tipoPer.idTipoPer);
            registro.setTipDoc(persona.tipoDoc.idTipDoc);
            registro.setNroDoc(persona.documento);
            registro.setNroDoc(persona.documento);
            registro.setEstado(persona.estado);
            registro.setFecCrea(persona.creDate);

            List<lstTelefono1> listaTlf1 = new ArrayList<>();
            for (int i = 0; i < persona.telefonos.size(); i++) {
                lstTelefono1 phone = new lstTelefono1();
                // Telefono elemento = persona.telefonos.get(i);
                // Long idphone = persona.telefonos.get(i).getIdTelefono();
                phone.setIdTelefono(persona.telefonos.get(i).getIdTelefono());
                if (persona.telefonos.get(i).getPersona() != null) {
                    phone.setIdPersona(persona.telefonos.get(i).getPersona().id);
                }

                phone.setTipo(persona.telefonos.get(i).getTipo());
                phone.setNumero(persona.telefonos.get(i).getNumero());
                phone.setEstado(persona.telefonos.get(i).getEstado());
                phone.setUsuCreado(persona.telefonos.get(i).getUsuCreado());
                phone.setFechaCreado(persona.telefonos.get(i).getFechaCreado());
                phone.setUsuUpdate(persona.telefonos.get(i).getUsuUpdate());
                phone.setFechaUpdate(persona.telefonos.get(i).getFechaUpdate());

                listaTlf1.add(phone);
            }
            registro.setTelefonos(listaTlf1);
            
            if (persona.tipoPer.getIdTipoPer() == 1L) {
            	registro.setNombre(persona.perNat.getNombre());
            	registro.setApellidoP(persona.perNat.getApePaterno());
            	registro.setApellidoM(persona.perNat.getApeMaterno());
            	registro.setSexo(persona.perNat.getSex());
                registro.setEstadoCivil(persona.perNat.getEstCivil());
            }
            if (persona.tipoPer.getIdTipoPer() == 2L) {
            	registro.setNombre(persona.perJur.getRazonSocial());
            	registro.setRazcom(persona.perJur.getRazonComercial());
            }
            
            response.put("persona", registro);
            List<TelefonoTipo> tiposTelefono = tipoTelefonoService.findAll();
            response.put("tipoTelefono", tiposTelefono);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la funcion show telefonos contacto");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateTelefono(@RequestParam(name = "idContacto") Long idCto,
                                            @RequestParam(name = "tipo") Long tipo,
                                            @RequestParam(name = "numero") String nro,
                                            @RequestParam(name = "usuario") String usuario){
        Map<String, Object> response = new HashMap<>();
        try{
        	/*
            Contacto contacto = ctoService.buscaId(idCto);
            if (contacto==null){
                response.put("mensaje","El ID contacto proporcionado no se encuentra registrado en personas. ID: ".concat(idCto.toString()));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }*/
            Telefono phone = new Telefono();
            phone.setTipo(tipo);
            phone.setNumero(nro);
            phone.setEstado(1);
            phone.setUsuCreado(usuario);
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
                response.put("mensaje", "El teléfono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
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
        try{
            Telefono phone = telefonoService.findById(idTelefono);
            if (phone == null){
                response.put("mensaje", "El teléfono ID: ".concat(idTelefono.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (phone.getEstado() == 0){
                phone.setEstado(1);
            } else if (phone.getEstado() == 1) {
                phone.setEstado(0);
            }
            phone.setUsuUpdate(usuUpdate);
            phone.setFechaUpdate(new Date());
            telefonoService.save(phone);
            response.put("mensaje", "Teléfono actualizado con éxito");
        }catch (Exception e){
            response.put("mensaje", "Error al realizar la actualización en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
