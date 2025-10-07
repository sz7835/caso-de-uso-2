package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.IDepartamentoService;
import com.delta.deltanet.models.service.IDireccionService;
import com.delta.deltanet.models.service.IDistritoService;
import com.delta.deltanet.models.service.IPersonaService;
import com.delta.deltanet.models.service.IProvinciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/dirCto")
public class DirecCtoController {

	@Autowired
    private IProvinciaService provinciaService;
    @Autowired
    private IDistritoService distritoService;
    @Autowired
    private IDepartamentoService departamentoService;
    @Autowired
    private IDireccionService direccionService;
    @Autowired
    private IPersonaService perService;

    @GetMapping("/create")
    public ResponseEntity<?> listaLocs(){
        Map<String, Object> response = new HashMap<>();

        List<Departamento> departamentos = departamentoService.findByAll();
        response.put("departamentos", departamentos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> grabaDir(@RequestParam("idContacto") Long idContacto,
                                      @RequestParam("tipo") Long tipo,
                                      @RequestParam("direccion") String direccion,
                                      @RequestParam("idDistrito") Long idDistrito,
                                      @RequestParam("creaUser") String creaUser){
        Map<String, Object> response = new HashMap<>();

        try {
            Distrito distrito = distritoService.finddByIdDist(idDistrito);
            if (distrito == null) {
				response.put("mensaje",
						"El distrito ID: ".concat(idDistrito.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

            Persona contacto = perService.buscarId(idContacto);
            if (contacto==null){
                response.put("mensaje","El ID de contacto no se encuentra registrado en persona. ID: ".concat(idContacto.toString()));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Validar si la dirección ya existe para la persona
			List<Direccion> direccionesExistentes = direccionService.findByPersonaAndDireccion(idContacto, direccion);
			if (!direccionesExistentes.isEmpty()) {
				response.put("mensaje", "La dirección ya existe para esta persona");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

            Direccion direcc = new Direccion();
            direcc.setPersona(contacto);
            direcc.setTipo(tipo);
            direcc.setDireccion(direccion);
            direcc.setDistrito(distrito);
            direcc.setEstado(1);
            direcc.setCreateUser(creaUser);
            direcc.setCreateDate(new Date());

            direccionService.save(direcc);

            response.put("mensaje", "Dirección guardada con éxito");
            response.put("direccion", direcc);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la inserción en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<?> ListaDirecciones(@RequestParam(name = "idContacto") Long idPer,
                                            @RequestParam(required = false, name = "tipo") Long tipo,
                                            @RequestParam(required = false, name = "direccion") String direccion,
                                            @RequestParam(name = "estado") Integer estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<lstDireccion1> lstResumen = direccionService.buscarDirecciones(idPer, tipo, direccion, estado);
            return new ResponseEntity<>(lstResumen, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar el listado de direcciones");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> muestraDir(@RequestParam(name="idDireccion") Long idDireccion){
        Map<String, Object> response = new HashMap<>();
        try {
            Direccion direccion = direccionService.buscaDir(idDireccion);
            if (direccion == null){
                response.put("mensaje", "La dirección ID: ".concat(idDireccion.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            List<Departamento> departamentos = departamentoService.findByAll();
            List<Object> provincias = provinciaService.lstPorDpto(direccion.getDistrito().getProvincia().getDepartamento().getId());
            List<Object> distritos = distritoService.lstPorProv(direccion.getDistrito().getProvincia().getId());
            response.put("departamentos", departamentos);
            response.put("distritos", distritos);
            response.put("provincias", provincias);
            response.put("direccion", direccion);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la búsqueda de direcciones");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdateDir(@RequestParam(name="idDireccion") Long idDire,
                                       @RequestParam(required = false,name="tipo") Long tipo,
                                       @RequestParam(required = false,name="direccion") String Dire,
                                       @RequestParam(required = false,name="idDistrito") Long idDist,
                                       @RequestParam(name="updUsuario") String updUser){
        Map<String, Object> response = new HashMap<>();
        try {
            Direccion registro = direccionService.buscaDir(idDire);
            if (registro==null){
                response.put("mensaje", "La direccion ID: ".concat(idDire.toString().concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }

            // Validar si la dirección ya existe para la persona (ignorando la que se está actualizando)
            List<Direccion> direccionesExistentes = direccionService.findByPersonaAndDireccion(registro.getPersona().getId(), Dire);
            List<Direccion> direccionesFiltradas = direccionesExistentes.stream()
                    .filter(direccion -> !direccion.getId().equals(idDire))
                    .collect(Collectors.toList());
            if (!direccionesFiltradas.isEmpty()) {
                response.put("mensaje", "La dirección ya existe para esta persona");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            if (idDist != null){
                Distrito distrito = distritoService.finddByIdDist(idDist);
                if (distrito==null){
                    response.put("mensaje", "El distrito ID: ".concat(idDist.toString().concat(" no existe en la base de datos")));
                    return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
                }
                registro.setDistrito(distrito);
            }
            if (tipo != null){
                registro.setTipo(tipo);
            }
            if (Dire != null){
                registro.setDireccion(Dire);
            }
            registro.setUpdateUser(updUser);
            registro.setUpdateDate(new Date());
            direccionService.save(registro);
            response.put("mensaje", "Dirección actualizada con éxito");
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la actualización de direcciones");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> updEstado(@RequestParam(name = "id") Long id,
			@RequestParam(name = "updateUser") String updateUser){
        Map<String, Object> response = new HashMap<>();
        String message = "Direccion desactivada con éxito";
        try {
            Direccion direccion = direccionService.buscaDir(id);
            if (direccion==null){
                response.put("mensaje", "La direccion ID: ".concat(id.toString().concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (direccion.getEstado()==0){
                direccion.setEstado(1);
                message = "Correo activado con éxito";
            }else if(direccion.getEstado()==1){
                direccion.setEstado(0);
            }
            direccion.setUpdateUser(updateUser);
            direccion.setUpdateDate(new Date());
            direccionService.save(direccion);
            response.put("mensaje", message);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la actualización en la base de datos");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
