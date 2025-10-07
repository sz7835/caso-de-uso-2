package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SuppressWarnings("all")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/relacion")
public class RelacionController {

    @Autowired
    private SisParamServiceImpl sisparamService;
    
    @Autowired
    public IRelacionService relService;

    @Autowired
    public ITipoRelacionService tipRelService;
    
    @Autowired
    public ITipoContrato2024Service tipoContratoService;

    @Autowired
    public IPersonaService perService;

    @Autowired
    public IAreaService areaService;

    @Autowired
    public AreasServiceImpl areasService;

    @Autowired
    public IGerenciaService gerenciaService;
    
    @Autowired
    public INodoTipoService nodoTipoService;
    
    @Autowired
    private IContrato2024Service contratoService;
    
	@Value("${ruc}")
	private String RUC;

    @GetMapping("/search")
    public ResponseEntity<?> listado(@RequestParam(name="idPer") Long idPer){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> listado = relService.listaRelaciones(idPer);
            if (listado == null) {
                response.put("mensaje", "No se encuentran relaciones para el idPersona: "
                        .concat(idPer.toString()));
            } else {
                List<lstRelacion2> lstResumen = new ArrayList<>();
                Iterator<Object> it = listado.iterator();
                Long nroContratos;
                while (it.hasNext()){
                    Object[] row = (Object[]) it.next();

                    nroContratos = relService.nroContratos((Long) row[0]);

                    lstRelacion2 lista = new lstRelacion2();
                    lista.setIdRel((Long) row[0]);
                    lista.setEstado((Long) row[1]);
                    lista.setIdTipoRel((Long)row[2]);
                    lista.setDescrip(String.valueOf(row[3]));

                    lista.setCantidad(nroContratos);
                    lista.setFecIni((Date) row[4]);


                    lstResumen.add(lista);
                }
                response.put("relaciones",lstResumen);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la función listado");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> listaItems(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> lstTipos = tipRelService.listaRelaciones();
            if (lstTipos == null){
                response.put("mensaje", "No se encuentran items en Tipo_Relacion");
            } else {
                List<lstRelacion1> lstResumen = new ArrayList<>();
                Iterator<Object> it = lstTipos.iterator();
                while (it.hasNext()){
                    Object[] row = (Object[]) it.next();
                    lstRelacion1 lista = new lstRelacion1();
                    lista.setIdRel((Long) row[0]);
                    lista.setDescrip(String.valueOf(row[1]));

                    lstResumen.add(lista);
                }
                response.put("TipoRelacion",lstResumen);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la función listaItems");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> creaRelacion(@RequestParam(name="idPer") Long idPer,
            							  @RequestParam(name="nodoDestino") Long nodoDestino,
                                          @RequestParam(name="idTipRel") Long idTipoRel,
                                          @RequestParam(name="idArea", required = false) Integer idArea,
                                        //   @RequestParam(name="idTipoContrato", required = false) Long idTipoContrato,
                                          @RequestParam(name="fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
                                          @RequestParam(name="fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
                                          @RequestParam(name="creaUsr") String creaUsr){
        Map<String, Object> response = new HashMap<>();
        Relacion existente = relService.findByOrigenDestinoTipoEstado(idPer, nodoDestino, idTipoRel, 1L);
        if (existente != null) {
            response.put("mensaje", "Ya existe una relación activa con estos datos.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        TipoRelacion tipRel = tipRelService.buscaId(idTipoRel);
        TipoRelacion inver = tipRelService.buscaId(tipRel.getRelacionback());
        if(tipRel == null){
            response.put("mensaje","No se encuentra el tipo de relación enviado. ID: ".concat(idTipoRel.toString()));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Long obtenerTipo = tipRel.getOrigen();
        Long obtenerTipo2 = inver.getOrigen();

        Persona persona = perService.buscarId(idPer);
        if(persona == null){
            response.put("mensaje","No se encuentra la persona enviada. ID: ".concat(idPer.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Persona prove = perService.buscarId(nodoDestino);
        try {
            Relacion registro = new Relacion();
            Relacion registro2 = new Relacion();
            registro.setTipoRel(tipRel);
            if (idArea != null) {
                Optional<Areas> ad = areasService.getById(idArea);
                if(ad==null){
                    response.put("mensaje","No se encuentra el area enviado. ID: ".concat(idArea.toString()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                Areas area = ad.get();
                registro.setArea(area);
                registro2.setArea(area);
            }
            registro.setPersonaD(prove);
            registro.setPersona(persona);
            registro.setFecIni(fecIni);
            registro.setFecFin(fecFin);
            registro.setEstado(1L);
            registro.setCreateUser(creaUsr);
            registro.setCreateDate(new Date());
            relService.save(registro);
            registro2.setTipoRel(inver);
            registro2.setIdReverse(registro.getIdRel());
            registro2.setPersona(prove);
            registro2.setPersonaD(persona);
            registro2.setFecIni(fecIni);
            registro2.setFecFin(fecFin);
            registro2.setEstado(1L);
            registro2.setCreateUser(creaUsr);
            registro2.setCreateDate(new Date());
            // if (idTipoContrato != null) {
    	    // 	TipoContrato tipoC = tipoContratoService.findById(idTipoContrato);
    	    //     if(tipoC==null){
    	    //         response.put("mensaje","No se encuentra el tipo de contrato enviado. ID: ".concat(idArea.toString()));
    	    //         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	    //     }
    	    //     //registro.setTipoContrato(tipoC);
            //     //registro2.setTipoContrato(tipoC);
            // }else {
            NodoTipo nodoTipo = nodoTipoService.findById(obtenerTipo);
            NodoTipo nodoTipo2 = nodoTipoService.findById(obtenerTipo2);
            registro.setNodoTipo(nodoTipo);
            registro2.setNodoTipo(nodoTipo2);
            // }
            relService.save(registro2);
            registro.setIdReverse(registro2.getIdRel());
            relService.save(registro);
            response.put("mensaje","La relación se creó satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje",e.getMessage());
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @PostMapping("/saveCto")
    public ResponseEntity<?> creaRelacionCto(@RequestParam(name="idPer") Long idPer,
            							  @RequestParam(name="idCto") Long idCto,
                                          @RequestParam(name="idTipoRel") Long idTipoRel,
                                          @RequestParam(name="idArea", required = false) Integer idArea,
                                          @RequestParam(name="idPuesto", required = false) Integer idPuesto,
                                          @RequestParam(name="fecInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
                                          @RequestParam(name="fecFin", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
                                          @RequestParam(name = "creaUsr") String creaUsr){
        Map<String, Object> response = new HashMap<>();
        
        if(idPer.equals(idCto)) {
        	response.put("mensaje", "El contacto no puede ser la misma persona.");
        	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        Persona persona = perService.buscarId(idPer);
        Persona contacto = perService.buscarId(idCto);
        
        if(persona == null || contacto == null) {
        	response.put("mensaje", (persona==null?"Persona ID: ".concat(idPer.toString()):"Contacto ID: ".concat(idCto.toString())).concat(" no existen en la base de datos."));
        	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        TipoRelacion tipoRel = tipRelService.buscaId(idTipoRel);
        TipoRelacion inverRel = tipRelService.buscaId(tipoRel.getRelacionback());
        if(tipoRel == null){
            response.put("mensaje","No se encuentra el tipo de relacion enviado. ID: ".concat(idTipoRel.toString()));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            Relacion registro = new Relacion();
            Relacion registro2 = new Relacion();
            registro.setTipoRel(tipoRel);
            if (idArea != null) {
                Optional<Areas> ad = areasService.getById(idArea);
                if(ad==null){
                    response.put("mensaje","No se encuentra el area enviado. ID: ".concat(idArea.toString()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                Areas area = ad.get();
                registro.setArea(area);
                registro2.setArea(area);
            }
            if(fecFin != null) {
            	registro.setFecFin(fecFin);
            	registro2.setFecFin(fecFin);
            }
            
            
            registro.setPersonaD(persona);
            registro.setPersona(contacto);
            registro.setNodoTipo(nodoTipoService.findById(persona.getIdNodoTipo()));
            registro.setFecIni(fecIni!=null ? fecIni : new Date());
            registro.setEstado(1L);
            registro.setCreateUser(creaUsr);
            registro.setCreateDate(new Date());
            relService.save(registro);
            
            registro2.setTipoRel(inverRel);
            registro2.setIdReverse(registro.getIdRel());
            registro2.setPersona(persona);
            registro2.setPersonaD(contacto);
            registro2.setNodoTipo(nodoTipoService.findById(contacto.getIdNodoTipo()));
            registro2.setFecIni(fecIni!=null ? fecIni : new Date());
            registro2.setEstado(1L);
            registro2.setCreateUser(creaUsr);
            registro2.setCreateDate(new Date());
            relService.save(registro2);
            
            registro.setIdReverse(registro2.getIdRel());
            relService.save(registro);
            
            response.put("mensaje","La relación se creó satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje",e.getMessage());
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/updateCto")
    public ResponseEntity<?> creaRelacionCto(@RequestParam(name="idRel") Long idRel,
                                            @RequestParam(name="idCto") Long idCto,
                                            @RequestParam(name="idTipoRel") Long idTipoRel,
                                            @RequestParam(name = "updateUsr") String updateUsr){
        Map<String, Object> response = new HashMap<>();

        Relacion relacion = relService.buscaId(idRel);
        if(relacion == null) {
            response.put("mensaje", "La relación no existe");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Relacion inverRel = relService.buscaId(relacion.getIdReverse());

        Persona contacto = perService.buscarId(idCto);
        if(contacto == null) {
        	response.put("mensaje", "Contacto ID: ".concat(idCto.toString()).concat(" no existen en la base de datos."));
        	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        TipoRelacion tipoRel = tipRelService.buscaId(idTipoRel);
        TipoRelacion inverTipoRel = tipRelService.buscaId(tipoRel.getRelacionback());
        if(tipoRel == null){
            response.put("mensaje","No se encuentra el tipo de relación enviado. ID: ".concat(idTipoRel.toString()));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
        	if(!idCto.equals(relacion.getIdPersona())) {
        		relacion.setPersona(contacto);
        		relacion.setUpdateDate(new Date());
        		relacion.setUpdateUser(updateUsr);
        		
        		inverRel.setPersonaD(contacto);
        		inverRel.setUpdateDate(new Date());
        		inverRel.setUpdateUser(updateUsr);
        		inverRel.setNodoTipo(nodoTipoService.findById(contacto.getIdNodoTipo()));
        	}
        	
        	if(!idTipoRel.equals(relacion.getTipoRel().getIdTipoRel())){
        		relacion.setTipoRel(tipoRel);
        		inverRel.setTipoRel(inverTipoRel);
        	}            
            
            relService.saveAndFlush(relacion);
            relService.saveAndFlush(inverRel);
            
            response.put("mensaje","La relación se actualizo satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje",e.getMessage());
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updRelacion(@RequestParam(name = "idRelacion") Long idRel,
    									 @RequestParam(name="nodoDestino") Long nodoDestino,
							             @RequestParam(name="idTipRel") Long idTipoRel,
							             @RequestParam(name="idArea", required = false) Integer idArea,
							             @RequestParam(name="idTipoContrato", required = false) Long idTipoContrato,
							             @RequestParam(name="fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
							             @RequestParam(name="fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
                                         @RequestParam(name = "updUser") String updUser){
        Map<String, Object> response = new HashMap<>();
        Relacion relacion = relService.buscaId(idRel);
        if(relacion==null){
            response.put("mensaje","No se encuentra la relación enviada. ID: ".concat(idRel.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Relacion relacionRev = null;
        if(relacion.getIdReverse() != null) {
        	relacionRev = relService.buscaId(relacion.getIdReverse());
        }
        TipoRelacion tipRel = tipRelService.buscaId(idTipoRel);
        if(tipRel == null){
            response.put("mensaje","No se encuentra el tipo de relación enviado. ID: ".concat(idTipoRel.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoRelacion inver = tipRelService.buscaId(tipRel.getRelacionback());
        Persona prove = perService.buscarId(nodoDestino);
        try {
            relacion.setTipoRel(tipRel);
        	if(relacionRev != null) {
        		relacionRev.setTipoRel(tipRel);
        	}
            if (idArea != null) {
                Optional<Areas> ad = areasService.getById(idArea);
                if(ad==null){
                    response.put("mensaje","No se encuentra el area enviado. ID: ".concat(idArea.toString()));
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                Areas area = ad.get();
            	relacion.setArea(area);
            	if(relacionRev != null) {
            		relacionRev.setArea(area);
            	}
            }else {
            	relacion.setIdArea(null);
            	if(relacionRev != null) {
            		relacionRev.setArea(null);
            	}
            }
            relacion.setPersonaD(prove);
            relacion.setFecIni(fecIni);
            relacion.setFecFin(fecFin);
            relacion.setUpdateUser(updUser);
            relacion.setUpdateDate(new Date());
            relService.save(relacion);
            if(relacionRev != null) {
            	relacionRev.setPersona(prove);
            	relacionRev.setFecIni(fecIni);
            	relacionRev.setFecFin(fecFin);
            	relacionRev.setUpdateUser(updUser);
            	relacionRev.setUpdateDate(new Date());
            }else {
                relacionRev = new Relacion();
                relacionRev.setTipoRel(inver);
                relacionRev.setIdReverse(relacion.getIdRel());
                relacionRev.setPersona(prove);
                relacionRev.setPersonaD(relacion.getPersona());
                relacionRev.setFecIni(fecIni);
                relacionRev.setFecFin(fecFin);
                relacionRev.setEstado(1L);
                relacionRev.setCreateUser(updUser);
                relacionRev.setCreateDate(new Date());
            	relacionRev.setUpdateUser(updUser);
            	relacionRev.setUpdateDate(new Date());
            }
            if (idTipoContrato != null) {
    	    	TipoContrato tipoC = tipoContratoService.findById(idTipoContrato);
    	        if(tipoC==null){
    	            response.put("mensaje","No se encuentra el tipo de contrato enviado. ID: ".concat(idArea.toString()));
    	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    	        }
                //relacion.setTipoContrato(tipoC);
            	//relacionRev.setTipoContrato(tipoC);
            }else {
                relacion.setNodoTipo(null);
            	relacionRev.setNodoTipo(null);
            }
            Contrato2024 contrato = this.contratoService.findByRelacion(relacion.getIdRel());
            if(contrato != null) {
	            contrato.setFecFin(fecFin);
	            contrato = this.contratoService.Save(contrato);
            }
            relService.save(relacionRev);
            relacion.setIdReverse(relacionRev.getIdRel());
            relService.save(relacion);
            response.put("mensaje","La relacion se actualizo satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la funcion updRelacion");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping("/index")
    public ResponseEntity<?> busqueda(@RequestParam(name="idPer") Long idPer,
                                      @RequestParam(required = false,name = "idTipRel") Long tipoRel,
                                      @RequestParam(required = false,name = "fecIni") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
                                      @RequestParam(required = false,name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
                                      @RequestParam(required = false,name = "estado") Long estado){

        Map<String, Object> response = new HashMap<>();
        if (fecIni != null && fecFin == null){
            response.put("mensaje","El rango de fechas esta incompleto. Debe ingresar las 2 fechas.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (fecIni == null && fecFin != null){
            response.put("mensaje","El rango de fechas esta incompleto. Debe ingresar las 2 fechas.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Persona persona = perService.buscarId(idPer);
        if(persona == null){
            response.put("mensaje","No se encuentra el ID persona enviado. ID: ".concat(idPer.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            List<Object> listado = null;
            if ((tipoRel == null || tipoRel == 0) && fecIni == null && fecFin == null && estado == 9) {
                listado = relService.busFiltrada(idPer);
            }
            if (tipoRel != null && tipoRel != 0 && fecIni == null && fecFin == null && estado == 9) {
                listado = relService.busFiltrada(idPer, tipoRel);
            }
            if (tipoRel != null && tipoRel != 0 && fecIni != null && fecFin != null && estado == 9) {
                listado = relService.busFiltrada(idPer, tipoRel, fecIni, fecFin);
            }
            if (tipoRel != null && tipoRel != 0 && fecIni != null && fecFin != null && estado != 9) {
                listado = relService.busFiltrada(idPer, tipoRel, fecIni, fecFin, estado);
            }
            if (tipoRel != null && tipoRel != 0 && fecIni == null && fecFin == null && estado != 9) {
                listado = relService.busFiltrada(idPer, tipoRel, estado);
            }
            if ((tipoRel == null || tipoRel == 0) && fecIni != null && fecFin != null && estado == 9) {
                listado = relService.busFiltrada(idPer, tipoRel, fecIni, fecFin);
            }
            if ((tipoRel == null || tipoRel == 0) && fecIni != null && fecFin != null && estado != 9) {
                listado = relService.busFiltrada(idPer, tipoRel, fecIni, fecFin, estado);
            }
            if ((tipoRel == null || tipoRel == 0) && fecIni == null && fecFin == null && estado != 9) {
                listado = relService.busFiltrada4(idPer, estado);
            }
            // --- FILTRO SEGÚN TIPO DE PERSONA Y tipoRel == 9 ---
            if (tipoRel != null && tipoRel == 0 && listado != null) {
                Long tipoPersona = persona.getTipoPer().getIdTipoPer();
                final List<Long> idsPermitidos;
                if (!tipoPersona.equals(4L)) {
                    idsPermitidos = tipRelService.findIdsByNodoTipoOrigenAndTipoIn(1L, Arrays.asList(1,2));
                } else {
                    String documento = persona.getDocumento();
                    if (documento != null && documento.startsWith("10")) {
                        idsPermitidos = tipRelService.findIdsByNodoTipoOrigenAndTipoIn(2L, Arrays.asList(1,2));
                    } else {
                        idsPermitidos = tipRelService.findIdsByNodoTipoOrigenAndTipoIn(3L, Arrays.asList(1,2));
                    }
                }
                // Filtrar listado por los idsPermitidos
                listado.removeIf(row -> !idsPermitidos.contains((Long)((Object[])row)[2]));
            }
            // --- FIN FILTRO ---
            if (listado == null) {
                response.put("mensaje", "No se encuentran relaciones para el idPesona: "
                        .concat(idPer.toString()));
            } else {
                List<lstRelacion2> lstResumen = new ArrayList<>();
                Iterator<Object> it = listado.iterator();
                Long nroContratos;
                Long idArea;
                Long orgArea;
                String nomArea;
                String nomGerencia;
                while (it.hasNext()){
                    Object[] row = (Object[]) it.next();

                    nroContratos = relService.nroContratos((Long) row[0]);
                    idArea = relService.idArea((Long) row[0]);
                    nomArea = (idArea != null) ? relService.findAreaNameById(idArea) : "---";
                    nomGerencia = null;

                    Optional<Long> optionalOrgArea = relService.findGerenciaIdByAreaId(idArea);
                    orgArea = optionalOrgArea.orElse(null);
                    nomGerencia = (orgArea != null) ? relService.findGerenciaNameById(orgArea) : "---";

                    lstRelacion2 lista = new lstRelacion2();
                    lista.setIdRel((Long) row[0]);
                    lista.setEstado((Long) row[1]);
                    lista.setIdTipoRel((Long) row[2]);
                    lista.setDescrip(String.valueOf(row[3]));
                    lista.setCantidad(nroContratos);
                    lista.setFecIni((Date) row[4]);
                    lista.setFecFin((Date) row[5]);
                    lista.setIdPer((Long) row[6]);
                    lista.setIdNodoDestino((Long) row[7]);
                    //lista.setTipo((Long) row[8]);
                    lista.setIdTipoContrato((Long) row[8]);
                    Persona personO = perService.buscarId(lista.getIdPer());
                    Persona personD = perService.buscarId(lista.getIdNodoDestino());
                    if(personD != null) {
                        if(personD.getArea() != null) {
                            personD.getArea().setGerencia(null);
                            if(personO.getArea() != null) {
                                personO.getArea().setPuestos(null);
                            }
                        }
                    }
                    if(personO.getArea() != null) {
                        personO.getArea().setGerencia(null);
                        personO.getArea().setPuestos(null);
                    }
                    lista.setPersonaO(personO);
                    lista.setPersonaD(personD);
                    lista.setIdArea(idArea != null ? String.valueOf(idArea) : "---");
                    lista.setIdGerencia(orgArea != null ? String.valueOf(orgArea) : "---");
                    lista.setNomArea(nomArea != null ? nomArea : "---");
                    lista.setNomGerencia(nomGerencia != null ? nomGerencia : "---");

                    lstResumen.add(lista);
                }
                response.put("relaciones",lstResumen);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la función listado");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/index/contactos")
    public ResponseEntity<?> busqueda(@RequestParam(name="idPer") Long idPer,
    		@RequestParam(required = false,name = "idMotivo") Long idMotivo,
    		@RequestParam(required = false,name = "nombre") String nombre,
    		@RequestParam(required = false,name = "tipoDoc") Long tipoDoc,
    		@RequestParam(required = false,name = "nroDoc") String nroDoc,
    		@RequestParam(required = false,name = "estado") Long estado){
        Map<String, Object> response = new HashMap<>();

        Persona persona = perService.buscarId(idPer);
        if(persona == null){
            response.put("mensaje","No se encuentra el ID persona enviado. ID: ".concat(idPer.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            List<Relacion> listado = null;
            Long idTipoNodoDestino = persona.getIdNodoTipo();
            List<Integer> tipoRelTipos = new ArrayList<Integer>();
        	tipoRelTipos.add(3);
        	if(persona.getPerJur() != null) {
        		tipoRelTipos.add(1);
        	}
            listado = relService.buscarRelacionContactos(idPer, idTipoNodoDestino, nombre, idMotivo, tipoRelTipos, tipoDoc, nroDoc, estado);
            
            if (listado == null) {
                response.put("mensaje", "No se encuentran relaciones para el idPesona: "
                        .concat(idPer.toString()));
            } else {
                List<RelacionContactosDTO> lstContactos = new ArrayList<>();
                for(Relacion rel : listado) {
                	if(rel.getPersona() != null) {
                		RelacionContactosDTO contacto = new RelacionContactosDTO();
            			contacto.setIdRel(rel.getIdRel());
            			contacto.setIdPer(rel.getIdPersona());
            			contacto.setIdTipoPer(rel.getPersona().getTipoPer().getIdTipoPer());
            			contacto.setTipoDoc(rel.getPersona().getTipoDoc().getNombre());
            			contacto.setNroDoc(rel.getPersona().getDocumento());
            			contacto.setMotivo(rel.getTipoRel().getDescrip());
            			contacto.setEstado(rel.getEstado());
                		if(rel.getPersona().getTipoPer().getIdTipoPer() == 1){
                			contacto.setNombre(rel.getPersona().getPerNat().getNombre() + " "
                            + rel.getPersona().getPerNat().getApePaterno() + " "
                            + rel.getPersona().getPerNat().getApeMaterno());
                		}else {
                			contacto.setNombre(rel.getPersona().getPerJur().getRazonSocial());
                		}
                		lstContactos.add(contacto);
                	}
                }
                response.put("contactos",lstContactos);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la función listado");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/index/search")
    public ResponseEntity<?> busqueda(@RequestParam(name="idRel") Long idRel,
    								@RequestParam(name="idPer") Long idPer){
        Map<String, Object> response = new HashMap<>();
        
        Persona persona = perService.buscarId(idPer);
        if(persona == null){
            response.put("mensaje","No se encuentra el ID persona enviado. ID: ".concat(idPer.toString()));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            List<Relacion> listado = null;
            Long idTipoNodoDestino = persona.getIdNodoTipo();
            List<Integer> tipoRelTipos = new ArrayList<Integer>();
        	tipoRelTipos.add(3);
        	if(persona.getPerJur() != null) {
        		tipoRelTipos.add(1);
        	}
            listado = relService.buscarRelacionContactos(idPer, idTipoNodoDestino, null, null, tipoRelTipos, null, null, null);
            
            Relacion relacion = relService.buscaId(idRel);
            if(relacion == null){
                response.put("mensaje","No se encuentra el ID relación enviado. ID: ".concat(idRel.toString()));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            
            if (listado == null) {
                response.put("mensaje", "No se encuentran relaciones para el idPesona: "
                        .concat(idPer.toString()));
            } else {
            	boolean isRelacionPresent = listado.stream().anyMatch(r -> idRel.equals(r.getIdRel()));
            	
            	if(!isRelacionPresent) {
            		response.put("mensaje", "No existe esta relación para este contacto");
            		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            	}

            	RelacionContactosDTO relacionDto = new RelacionContactosDTO();
            	
            	if(relacion.getPersona() != null) {
            		relacionDto.setIdRel(relacion.getIdRel());
            		relacionDto.setIdPer(relacion.getIdPersona());
            		relacionDto.setIdTipoPer(relacion.getPersona().getTipoPer().getIdTipoPer());
            		relacionDto.setTipoDoc(relacion.getPersona().getTipoDoc().getNombre());
            		relacionDto.setNroDoc(relacion.getPersona().getDocumento());
            		relacionDto.setMotivo(relacion.getTipoRel().getDescrip());
            		relacionDto.setIdMotivo(relacion.getTipoRel().getIdTipoRel());
            		relacionDto.setEstado(relacion.getEstado());
            		if(relacion.getPersona().getTipoPer().getIdTipoPer() == 1){
            			relacionDto.setNombre(relacion.getPersona().getPerNat().getNombre() + " "
                        + relacion.getPersona().getPerNat().getApePaterno() + " "
                        + relacion.getPersona().getPerNat().getApeMaterno());
            		}else {
            			relacionDto.setNombre(relacion.getPersona().getPerJur().getRazonSocial());
            		}
                	
                }
                response.put("relacion", relacionDto);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la funcion listado");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    @PostMapping("/delete")
    public ResponseEntity<?> updEstado(@RequestParam(name = "idRelacion")Long idRelacion,
                                       @RequestParam(name = "updUsuario")String usuario){
        Map<String, Object> response = new HashMap<>();
        String message = "Relacion desactivada con éxito";

        try {
            Relacion registro = new Relacion();
            registro=relService.buscaId(idRelacion);
            if (registro==null){
                response.put("mensaje","No se encuentra el ID relacion a actualizar. ID: ".concat(idRelacion.toString()));
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (registro.getEstado()==0L){
                registro.setEstado(1L);
                registro.setFecFin(null);
                message="Relacion activada con éxito";
            }else if(registro.getEstado()==1L){
                registro.setEstado(0L);
                registro.setFecFin(new Date());
            }
            registro.setUpdateUser(usuario);
            registro.setUpdateDate(new Date());
            relService.save(registro);
            if(registro.getIdReverse() != null) {
            	Relacion reverse = relService.buscaId(registro.getIdReverse());
                if (registro==null){
                    response.put("mensaje","No se encuentra el ID relacion a actualizar. ID: ".concat(idRelacion.toString()));
                    return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
                }

                if (reverse.getEstado()==0L){
                	reverse.setEstado(1L);
                	reverse.setFecFin(null);
                }else if(reverse.getEstado()==1L){
                	reverse.setEstado(0L);
                	reverse.setFecFin(new Date());
                }
                reverse.setUpdateUser(usuario);
                reverse.setUpdateDate(new Date());
                relService.save(reverse);
            }
            response.put("mensaje",message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje","Error inesperado en la funcion updEstado");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/listaOutsourcing")
    public ResponseEntity<?> listaOutsourcing(){
        Map<String, Object> response = new HashMap<>();
        List<Object> lstConsultores = relService.listaOutsourcing();
        response.put("Consultores",lstConsultores);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> infoCese(){
        Map<String, Object> response = new HashMap<>();
        List<Object> pers = perService.findByPersonaJurD(2L, 4L, RUC);
        Object per = pers.get(0);
        response.put("person",per);
        List<Gerencia> gerencias = gerenciaService.findAllWithAreas();
        Iterator<Gerencia> iterator = gerencias.iterator();
		while(iterator.hasNext()){
			Gerencia row = iterator.next();
			List<OrgAreas> areas = row.getOrgAreas();
	        Iterator<OrgAreas> iterator2 = areas.iterator();
			while(iterator2.hasNext()){
				OrgAreas row2 = iterator2.next();
				row2.setGerencia(null);
				row2.setPuestos(null);
			}
		}
        response.put("gerencias",gerencias);
        List<TipoContrato> tipos = tipoContratoService.findAll();
        response.put("tipos", tipos);
        SisParam param = sisparamService.findById(11L);
        response.put("param",param);
        List<TipoRelacion> lstTipos = tipRelService.Lista();
        if (lstTipos == null){
            response.put("relaciones", null);
        } else {
            response.put("TipoRelacion",lstTipos);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/indexEspecial")
    public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Long id,
                                           @RequestParam(required = false) Integer estado) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> relacionesConDetalle = new ArrayList<>();
        List<TipoRelacion> relaciones = new ArrayList<>();
        if (id != null) {
            TipoRelacion rel = tipRelService.buscaId(id);
            if (rel != null) {
                if (rel.getEstado() == 0) {
                    response.put("error", true);
                    response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
                    response.put("data", Collections.singletonList(rel));
                    List<NodoTipo> nodos = nodoTipoService.findAll();
                    response.put("nodotipos", nodos);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                if (estado == null || estado == 0 || rel.getEstado() == estado) {
                    relaciones.add(rel);
                }
            } else {
                response.put("error", true);
                response.put("data", new ArrayList<>());
                response.put("message", "No existe un motivo enlace con el registro proporcionado");
                List<NodoTipo> nodos = nodoTipoService.findAll();
                response.put("nodotipos", nodos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else if (estado != null) {
            for (TipoRelacion rel : tipRelService.Lista()) {
                if (rel.getEstado() == estado) {
                    relaciones.add(rel);
                }
            }
        } else {
            relaciones = tipRelService.Lista();
        }
        // Construir la lista con detallerelacionback
        for (TipoRelacion rel : relaciones) {
            Map<String, Object> relMap = new HashMap<>();
            relMap.put("idTipoRel", rel.getIdTipoRel());
            relMap.put("origen", rel.getOrigen());
            relMap.put("destino", rel.getDestino());
            relMap.put("tipo", rel.getTipo());
            relMap.put("descrip", rel.getDescrip());
            relMap.put("estado", rel.getEstado());
            relMap.put("nodoOrigen", rel.getNodoOrigen());
            relMap.put("nodoDestino", rel.getNodoDestino());
            relMap.put("relacionback", rel.getRelacionback());
            relMap.put("create_user", rel.getCreateUser());
            relMap.put("create_date", rel.getCreateDate());
            relMap.put("update_user", rel.getUpdateUser());
            relMap.put("update_date", rel.getUpdateDate());
            if (rel.getRelacionback() != null) {
                TipoRelacion back = tipRelService.buscaId(rel.getRelacionback());
                if (back != null) {
                    String descripRel = back.getDescrip();
                    String abrevOrigen = back.getNodoOrigen() != null ? back.getNodoOrigen().getAbrev() : "";
                    String abrevDestino = back.getNodoDestino() != null ? back.getNodoDestino().getAbrev() : "";
                    String detalle = descripRel + "(" + abrevOrigen + " - " + abrevDestino + ")";
                    relMap.put("detalleRel", detalle);
                }
            }
            relacionesConDetalle.add(relMap);
        }
        response.put("data", relacionesConDetalle);
        List<TipoRelacion> relacionesSinRelacionBack = new ArrayList<>();
        for (TipoRelacion rel : relaciones) {
            if (rel.getRelacionback() == null && rel.getEstado() == 1) {
                relacionesSinRelacionBack.add(rel);
            }
        }
        response.put("relaciones", relacionesSinRelacionBack);
        List<NodoTipo> nodos = nodoTipoService.findAll();
        response.put("nodotipos", nodos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/createEspecial")
    public ResponseEntity<?> createEspecial(@RequestParam Long tipoOrigen,
                                            @RequestParam Long tipoDestino,
                                            @RequestParam Integer tipo,
                                            @RequestParam String nombre,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        // Validar tipo
        if (tipo == null || (tipo != 1 && tipo != 2 && tipo != 3)) {
            response.put("error", true);
            response.put("message", "El campo 'tipo' solo puede ser 1, 2 o 3");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar nombre: no caracteres especiales ni solo números
        if (nombre == null || nombre.trim().isEmpty()) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Caracteres especiales permitidos: letras, números, espacios, guion y guion bajo
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // No permitir solo números
        if (nombre.matches("^\\d+$")) {
            response.put("error", true);
            response.put("message", "El campo 'nombre' no puede ser completamente numérico");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar NodoTipo origen
        NodoTipo nodoOrigen = nodoTipoService.findById(tipoOrigen);
        if (nodoOrigen == null || nodoOrigen.getEstado() != 1) {
            response.put("error", true);
            response.put("message", "El tipoOrigen no existe o no está activo");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar NodoTipo destino
        NodoTipo nodoDestino = nodoTipoService.findById(tipoDestino);
        if (nodoDestino == null || nodoDestino.getEstado() != 1) {
            response.put("error", true);
            response.put("message", "El tipoDestino no existe o no está activo");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista el mismo nombre con los mismos tipos
        List<TipoRelacion> existentes = tipRelService.findByDescripAndOrigenAndDestinoAndTipo(nombre, tipoOrigen, tipoDestino, tipo);
        if (existentes != null && !existentes.isEmpty()) {
            response.put("error", true);
            response.put("message", "Ya existe un motivo enlace con el mismo nombre y los mismos tipos (origen, destino, tipo)");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Crear TipoRelacion
        TipoRelacion nueva = new TipoRelacion();
        nueva.setOrigen(tipoOrigen);
        nueva.setDestino(tipoDestino);
        nueva.setTipo(tipo);
        nueva.setDescrip(nombre);
        nueva.setEstado(1);
        nueva.setNodoOrigen(nodoOrigen);
        nueva.setNodoDestino(nodoDestino);
        nueva.setCreateUser(username);
        nueva.setCreateDate(new Date());
        tipRelService.save(nueva);
        response.put("message", "Motivo enlace creado correctamente");
        response.put("TipoRelacion", nueva);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateEspecial")
    public ResponseEntity<?> updateEspecial(@RequestParam Long id,
                                            @RequestParam(required = false) Long tipoOrigen,
                                            @RequestParam(required = false) Long tipoDestino,
                                            @RequestParam(required = false) Integer tipo,
                                            @RequestParam(required = false) String nombre,
                                            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        TipoRelacion rel = tipRelService.buscaId(id);
        if (rel == null) {
            response.put("error", true);
            response.put("message", "No existe el motivo enlace con el registro proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long origenFinal = tipoOrigen != null ? tipoOrigen : rel.getOrigen();
        Long destinoFinal = tipoDestino != null ? tipoDestino : rel.getDestino();
        Integer tipoFinal = tipo != null ? tipo : rel.getTipo();
        String nombreFinal = nombre != null ? nombre : rel.getDescrip();
        // Validar nombre si se va a actualizar
        if (nombre != null) {
            if (nombre.trim().isEmpty()) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' es obligatorio");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no debe contener caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (nombre.matches("^\\d+$")) {
                response.put("error", true);
                response.put("message", "El campo 'nombre' no puede ser completamente numérico");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        // Validar que no exista el mismo nombre con los mismos tipos (omitiendo el actual)
        List<TipoRelacion> existentes = tipRelService.findByDescripAndOrigenAndDestinoAndTipo(nombreFinal, origenFinal, destinoFinal, tipoFinal);
        boolean existe = existentes.stream().anyMatch(t -> !t.getIdTipoRel().equals(id));
        if (existe) {
            response.put("error", true);
            response.put("message", "Ya existe un motivo enlace con el mismo nombre y los mismos tipos (origen, destino, tipo)");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (tipoOrigen != null) {
            NodoTipo nodoOrigen = nodoTipoService.findById(tipoOrigen);
            if (nodoOrigen == null || nodoOrigen.getEstado() != 1) {
                response.put("error", true);
                response.put("message", "El tipoOrigen no existe o no está activo");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            rel.setOrigen(tipoOrigen);
            rel.setNodoOrigen(nodoOrigen);
        }
        if (tipoDestino != null) {
            NodoTipo nodoDestino = nodoTipoService.findById(tipoDestino);
            if (nodoDestino == null || nodoDestino.getEstado() != 1) {
                response.put("error", true);
                response.put("message", "El tipoDestino no existe o no está activo");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            rel.setDestino(tipoDestino);
            rel.setNodoDestino(nodoDestino);
        }
        if (tipo != null) {
            if (tipo != 1 && tipo != 2 && tipo != 3) {
                response.put("error", true);
                response.put("message", "El campo 'tipo' solo puede ser 1, 2 o 3");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            rel.setTipo(tipo);
        }
        if (nombre != null) {
            rel.setDescrip(nombre);
        }
        rel.setUpdateUser(username);
        rel.setUpdateDate(new Date());
        tipRelService.save(rel);
        response.put("message", "Motivo enlace actualizado correctamente");
        response.put("TipoRelacion", rel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeStatusEspecial")
    public ResponseEntity<?> changeStatusEspecial(@RequestParam Long id, @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        TipoRelacion motivo = tipRelService.buscaId(id);
        if (motivo == null) {
            response.put("error", true);
            response.put("message", "No existe motivo enlace con el id proporcionado");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Validar que no exista el mismo nombre con los mismos tipos si se va a activar
        if (motivo.getEstado() == 0) {
            List<TipoRelacion> existentes = tipRelService.findByDescripAndOrigenAndDestinoAndTipo(motivo.getDescrip(), motivo.getOrigen(), motivo.getDestino(), motivo.getTipo());
            boolean existe = existentes.stream().anyMatch(t -> !t.getIdTipoRel().equals(id));
            if (existe) {
                response.put("error", true);
                response.put("message", "Ya existe un motivo enlace con el mismo nombre y los mismos tipos (origen, destino, tipo)");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        boolean tieneRelacionBack = motivo.getRelacionback() != null;
        String mensaje;
        if (motivo.getEstado() == 1) {
            motivo.setEstado(0);
            motivo.setUpdateUser(username);
            motivo.setUpdateDate(new Date());
            tipRelService.save(motivo);
            if (tieneRelacionBack) {
                TipoRelacion back = tipRelService.buscaId(motivo.getRelacionback());
                if (back != null && back.getEstado() == 1) {
                    back.setEstado(0);
                    back.setUpdateUser(username);
                    back.setUpdateDate(new Date());
                    tipRelService.save(back);
                    mensaje = "Motivo enlace y su relación de regreso desactivados con éxito";
                } else {
                    mensaje = "Motivo enlace desactivado con éxito";
                }
            } else {
                mensaje = "Motivo enlace desactivado con éxito";
            }
        } else {
            motivo.setEstado(1);
            motivo.setUpdateUser(username);
            motivo.setUpdateDate(new Date());
            tipRelService.save(motivo);
            if (tieneRelacionBack) {
                TipoRelacion back = tipRelService.buscaId(motivo.getRelacionback());
                if (back != null && back.getEstado() == 0) {
                    back.setEstado(1);
                    back.setUpdateUser(username);
                    back.setUpdateDate(new Date());
                    tipRelService.save(back);
                    mensaje = "Motivo enlace y su relación de regreso activados con éxito";
                } else {
                    mensaje = "Motivo enlace activado con éxito";
                }
            } else {
                mensaje = "Motivo enlace activado con éxito";
            }
        }
        response.put("message", mensaje);
        response.put("TipoRelacion", motivo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/crearRelacion")
    public ResponseEntity<?> crearRelacionTipoRegreso(@RequestParam Long idRel,
                                                      @RequestParam(required = false) Long idRegreso,
                                                      @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        TipoRelacion rel = tipRelService.buscaId(idRel);
        if (rel == null) {
            response.put("error", true);
            response.put("message", "No existe el motivo enlace con idRel proporcionado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (idRegreso != null) {
            // Crear relación de regreso
            TipoRelacion regreso = tipRelService.buscaId(idRegreso);
            if (regreso == null) {
                response.put("error", true);
                response.put("message", "No existe el motivo enlace con idRegreso proporcionado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (rel.getRelacionback() != null) {
                response.put("error", true);
                response.put("message", "El motivo enlace idRel ya tiene una relación de regreso asignada");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if (regreso.getRelacionback() != null) {
                response.put("error", true);
                response.put("message", "El motivo enlace idRegreso ya tiene una relación de regreso asignada");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            rel.setRelacionback(idRegreso);
            rel.setUpdateUser(username);
            rel.setUpdateDate(new Date());
            regreso.setRelacionback(idRel);
            regreso.setUpdateUser(username);
            regreso.setUpdateDate(new Date());
            tipRelService.save(rel);
            tipRelService.save(regreso);
            response.put("message", "Relación de regreso creada exitosamente");
            response.put("TipoRelacion", rel);
            response.put("TipoRelacionRegreso", regreso);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Eliminar relación de regreso existente
            if (rel.getRelacionback() == null) {
                response.put("error", true);
                response.put("message", "El motivo enlace idRel no tiene una relación de regreso para eliminar");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            TipoRelacion regreso = tipRelService.buscaId(rel.getRelacionback());
            if (regreso != null) {
                regreso.setRelacionback(null);
                regreso.setUpdateUser(username);
                regreso.setUpdateDate(new Date());
                tipRelService.save(regreso);
            }
            rel.setRelacionback(null);
            rel.setUpdateUser(username);
            rel.setUpdateDate(new Date());
            tipRelService.save(rel);
            response.put("message", "Relación de regreso eliminada exitosamente");
            response.put("TipoRelacion", rel);
            if (regreso != null) {
                response.put("TipoRelacionRegreso", regreso);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
