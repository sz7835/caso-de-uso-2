package com.delta.deltanet.controllers;

import com.delta.deltanet.controllers.exception.RelationCreationException;
import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/persona")
public class PersonaController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IPersonaService2 personaService;

    @Autowired
    public IPersonaService perService;

    @Autowired
    public ITipoPersonaService tipPerService;

    @Autowired
    public ITipoDocumentoService tipDocumService;

    @Autowired
    public IPersonaNaturalService perNaturalService;

    @Autowired
    public IPersonaJuridicaService perJuridicaService;

    @Autowired
    public ITipoRelacionService tipRelacionService;

    @Autowired
    public ISexoService sexoService;

    @Autowired
    public IGerenciaService gerenciaService;

    @Autowired
    public ITipoEstadoCivilService estadoCivilService;

    @Autowired
    public IParametroService parametroService;

    @Autowired
    public RelacionController relacionController;

    @Autowired
    public TelefonoPerController telefonoController;

    @Autowired
    public EmailPerController correoController;

    @Autowired
    private IAutenticacionUsuarioService autenticacionController;

    @Autowired
    private IAuthPerfilUserService authPerfilUserService;

    @Autowired
    private IAuthPerfilService authPerfilService;

    @Autowired
    private IAuthFuncService authFuncService;

    @Autowired
    private IAtencionService atencionService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IOrgAreaService orgAreaService;

    @Autowired
    private AutenticacionUsuarioServiceImpl autenticacionUsuarioService;

    @Autowired
    private JefeAreaServiceImpl jefeAreaService;

    @Autowired
    private ISisParamService sisparamService;

    @Autowired
    private INodoTipoService nodoTipoService;

    @Autowired
    private IPuestoService puestoService;

    @Autowired
    private ITipoTelefonoService tipoTelefonoService;
    
    @Autowired
    private ITipoDireccionService tipoDireccionService;
    
    @Autowired
    private ITipoCorreoService tipoCorreoService;

    @GetMapping("/create")
    public ResponseEntity<?> lstParamsa() {
        Map<String, Object> response = new HashMap<>();

        List<TipoPersona> tiposPersona = tipPerService.findAll();

        List<TipoDocumento> tiposDocums = tipDocumService.findAll();

        List<Sexo> sexos = sexoService.findAll();

        List<OrgAreas> areas = orgAreaService.findAll();

        Iterator<OrgAreas> iterator = areas.iterator();
		while(iterator.hasNext()){
			OrgAreas row = iterator.next();
			row.getGerencia().setOrgAreas(null);
			List<Puestos> puestos = row.getPuestos();
	        Iterator<Puestos> iterator2 = puestos.iterator();
			while(iterator2.hasNext()){
				Puestos row2 = iterator2.next();
				row2.setArea(null);
			}
		}

        SisParam param = sisparamService.findById(11L);

        // Obtiene la lista de tipos de relaciones como objetos array
        List<TipoRelacion> tipoRelacionesArray = tipRelacionService.Lista();

        // Estado Civil
        List<TipoEstadoCivil> estadosCiviles = estadoCivilService.findAll();

        // tipos de telefono y correo
        List<Parametro> filteredPerParams = parametroService.findByParams(2L);

        List<Object> persons = perService.findPersonaRelacion(4L);

        List<Atencion> atenciones = atencionService.findAll();

        List<TipoRelacion> tipoRel = tipRelacionService.findAll();

        response.put("param", param);
        response.put("persons", persons);
        response.put("areas", areas);
        response.put("tipoPersona", tiposPersona);
        response.put("tipoDocumento", tiposDocums);
        response.put("sexos", sexos);
        response.put("relaciones", tipoRelacionesArray);
        response.put("estadoCivil", estadosCiviles);
        response.put("tipoTelefono", filteredPerParams);
        response.put("tipoCorreo", filteredPerParams);
        response.put("tipoRel", tipoRel);
        response.put("atenciones", atenciones);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> grabaPer(@RequestParam(name = "idTipoPer") Long idTipoPer, // 1=Persona Natural, 2=Persona
                                                                                        // Juridica
            @RequestParam(name = "idTipoDoc") Long idTipoDoc,
            @RequestParam(name = "documento") String documento,
            @RequestParam(required = false, name = "tratamiento") Integer tratamiento,
            @RequestParam(required = false, name = "idTitular") Long idTitular,
            // Datos de Persona Jurídica
            @RequestParam(required = false, name = "razonSoc") String razonSoc,
            @RequestParam(required = false, name = "razonCom") String razonCom,
            @RequestParam(required = false, name = "tipoCli") String tipoCli,
            @RequestParam(required = false, name = "fechaIni") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fechaIni,
            // Datos de Persona Natural
            @RequestParam(required = false, name = "sexo") Long idSexo,
            @RequestParam(required = false, name = "estadoCiv") Long estadoCiv,
            @RequestParam(required = false, name = "nombre") String nombre,
            @RequestParam(required = false, name = "apePat") String apePat,
            @RequestParam(required = false, name = "apeMat") String apeMat,
            @RequestParam(required = false, name = "fecNac") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecNac,
            // Datos de Persona Natural - Teléfono
            @RequestParam(required = false, name = "idTipoTel") Long idTipoTel,
            @RequestParam(required = false, name = "telefono") String telefono,
            // Datos de Persona Natural - Correo
            @RequestParam(required = false, name = "idTipoEmail") Long idTipoEmail,
            @RequestParam(required = false, name = "correo") String correo,
            @RequestParam(required = false, name = "type") String type,
            // Datos de Persona - Creación
            @RequestParam(required = false, name = "idArea") Long idArea,
            @RequestParam(required = false, name = "idPuesto") Long idPuesto,
            @RequestParam(name = "createUser") String createUser) throws Exception {

        // Verificación de tipo de persona y tipo de documento para Persona Jurídica
        if (idTipoPer == 2) {
            if (idTipoDoc != 4) {
                return ResponseEntity.unprocessableEntity().body(Map.of("mensaje",
                        "Para Persona Jurídica el tipo de documento debe ser RUC (tipo documento 4)."));
            }
            // Verificar el idTitular solo si idTipoPer es 2
            if (idTitular != null) {
                List<Object> personaNaturales = perService.findByPersonaNat();
                boolean titularExists = personaNaturales.stream()
                        .map(obj -> ((Object[]) obj)[0])
                        .anyMatch(id -> id.equals(Long.valueOf(idTitular)));

                if (!titularExists) {
                    return ResponseEntity.unprocessableEntity().body(
                            Map.of("mensaje", "El idTitular proporcionado no existe entre las personas naturales."));
                }
            }
        }
        // Verifica el valor de tipo Cliente (C,P)
        if (tipoCli == null && idTipoPer == 2) {
            tipoCli = "C";
        }
        if (tipoCli != null) {
            if ((!tipoCli.equals("C")) && (!tipoCli.equals("P"))) {
                return ResponseEntity.unprocessableEntity()
                        .body(Map.of("mensaje", "El tipo de cliente solo permite los valores de C o P"));
            }
        }
        // Búsqueda y validaciones iniciales
        TipoPersona tipoPersona = tipPerService.buscaTipoPer(idTipoPer);
        TipoDocumento tipoDocumento = tipDocumService.buscaTipDoc(idTipoDoc);
        Integer longitud = tipDocumService.buscarLongitud(idTipoDoc);
        if (tipoPersona == null || tipoDocumento == null) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("mensaje", "Tipo de persona o documento no encontrado en la base de datos."));
        }

        // Validaciones de existencia
        if (idTipoPer == 1) {
            if (!documento.equals("00000000") && !perService.findByPersonaNat(1L, idTipoDoc, documento).isEmpty()) {
                return ResponseEntity.unprocessableEntity().body(
                        Map.of("mensaje", "Ya existe una persona con ese tipo de documento y número de documento"));
            }
        } else if (idTipoPer == 2) {
            if (!documento.equals("00000000") && !perService.findByPersonaJurD(2L, idTipoDoc, documento).isEmpty()) {
                return ResponseEntity.unprocessableEntity().body(
                        Map.of("mensaje", "Ya existe una persona con ese tipo de documento y número de documento"));
            }
        }

        // Validación de longitud del documento
        if (documento.length() != longitud) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La longitud del documento no coincide con la esperada. Debe ser de " + longitud + " caracteres."));
        }

        // Validación de la edad si esta presente
        if (fecNac != null) {
            Date fechaLimite = new Date(1123154400000L); // Ajustar según sea necesario
            if (fecNac.after(fechaLimite)) {
                return ResponseEntity.unprocessableEntity()
                        .body(Map.of("mensaje", "La persona debe ser mayor de edad."));
            }
        }

        // Validación de datos obligatorios
        if (idTipoPer == 1) {
            if (idSexo == null || estadoCiv == null || nombre == null || apePat == null || apeMat == null
                    || fecNac == null || idTipoTel == null || telefono == null
                    || idTipoEmail == null || correo == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Faltan datos obligatorios para Persona Natural."));
            }
        } else if (idTipoPer == 2) {
            if (razonSoc == null || razonCom == null || fechaIni == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Faltan datos obligatorios para Persona Jurídica."));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Tipo de persona no válido."));
        }
        
        String typeNodo = "";
        if("PN".equals(type)) typeNodo = "G1PER";
        if("PNNP".equals(type)) typeNodo = "G2NEG";
        if("PJ".equals(type)) typeNodo = "G3EMP";
        
        try {
            // Si es persona natural
        	NodoTipo nodo = this.nodoTipoService.searchNodo(typeNodo);
            if (nodo == null) {
                    return ResponseEntity.unprocessableEntity().body(Map.of("mensaje", "El tipo de nodo no existe"));
            }
            if (idTipoPer == 1) {
                // Creación de Persona Natural
                PersonaNatural perNat = new PersonaNatural();
                perNat.setNombre(nombre);
                perNat.setApePaterno(apePat);
                perNat.setApeMaterno(apeMat);
                perNat.setFecNacim(fecNac);
                perNat.setSex(sexoService.findById(idSexo));
                perNat.setEstCivil(estadoCivilService.findById(estadoCiv));
                perNat = perNaturalService.save(perNat);

                // Creación de Persona
                Persona regPer = new Persona();
                regPer.setTipoPer(tipoPersona);
                regPer.setTipoDoc(tipoDocumento);
                regPer.setDocumento(documento);
                regPer.setIdNodoTipo(nodo.getId());
                if (idPuesto != null) {
                	Puestos puesto = puestoService.findById(idPuesto);
                    regPer.setPuesto(puesto);
                    regPer.setArea(puesto.getArea());
                }
                Atencion trata = new Atencion();
                Optional<Atencion> regTra = atencionService.findById(tratamiento);
                if (regTra.isPresent()) {
                    trata = regTra.get();
                }
                // regPer.setTratamiento(tratamiento != null ? tratamiento : null);
                regPer.setAtencion(trata);
                regPer.setEstado(1);
                regPer.setVisible(1);
                regPer.setPerNat(perNat);
                regPer.setCreUser(createUser);
                regPer.setCreDate(new Date());
                regPer = perService.save(regPer);
                Long idPersona = regPer.getId();

                // Crea el teléfono
                ResponseEntity<?> telefonoResp = telefonoController.CreateTelefono(idPersona, idTipoTel, telefono,
                        createUser);
                if (!telefonoResp.getStatusCode().equals(HttpStatus.OK)) {
                    throw new RelationCreationException(
                            "Error en creación de teléfono: " + telefonoResp.getBody().toString());
                }
                // Crea el correo
                ResponseEntity<?> correoResp = correoController.CreateCorreo(idPersona, idTipoEmail, correo,
                        createUser);
                if (!correoResp.getStatusCode().equals(HttpStatus.OK)) {
                    throw new RelationCreationException(
                            "Error en creación de correo: " + correoResp.getBody().toString());
                }
            }

            // Si es persona juridica
            if (idTipoPer == 2) {
                // Creación de Persona Jurídica
                PersonaJuridica perJur = new PersonaJuridica();
                perJur.setRazonSocial(razonSoc);
                perJur.setRazonComercial(razonCom);
                perJur.setFecIniOper(fechaIni);
                perJur.setEstado(1L);
                perJur.setTipo("C");
                perJur = perJuridicaService.save(perJur);

                Atencion trata = new Atencion();
                Optional<Atencion> regTra = atencionService.findById(4);
                if (regTra.isPresent()) {
                    trata = regTra.get();
                }

                // Creación de Persona
                Persona regPer = new Persona();
                if (idPuesto != null) {
                	Puestos puesto = puestoService.findById(idPuesto);
                    regPer.setPuesto(puesto);
                    regPer.setArea(puesto.getArea());
                }
                regPer.setTipoPer(tipoPersona);
                regPer.setTipoDoc(tipoDocumento);
                regPer.setDocumento(documento);
                regPer.setIdNodoTipo(nodo.getId());
                // regPer.setTratamiento(tratamiento != null ? tratamiento : null);
                regPer.setAtencion(trata);
                regPer.setEstado(1);
                regPer.setVisible(1);
                regPer.setPerJur(perJur);
                regPer.setIdTitular(idTitular);
                regPer.setCreUser(createUser);
                regPer.setCreDate(new Date());
                regPer = perService.save(regPer);
                Long idPersona = regPer.getId();

                // Crea el teléfono
                ResponseEntity<?> telefonoResp = telefonoController.CreateTelefono(idPersona, idTipoTel, telefono,
                        createUser);
                if (!telefonoResp.getStatusCode().equals(HttpStatus.OK)) {
                    throw new RelationCreationException(
                            "Error en creación de teléfono: " + telefonoResp.getBody().toString());
                }
                // Crea el correo
                ResponseEntity<?> correoResp = correoController.CreateCorreo(idPersona, idTipoEmail, correo,
                        createUser);
                if (!correoResp.getStatusCode().equals(HttpStatus.OK)) {
                    throw new RelationCreationException(
                            "Error en creación de correo: " + correoResp.getBody().toString());
                }
            }
            return ResponseEntity.ok(Map.of("mensaje", "Registro completo con éxito."));

        } catch (Exception e) {
            throw e;
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchPersonas(@RequestParam(name="nombre", required = false) String nombre,
    		@RequestParam(name="nroDoc", required = false) String nroDoc,
    		@RequestParam(name="idPer", required = false) Long idPer){
		Map<String, Object> response = new HashMap<>();
		
		if(idPer != null) {
			Persona persona = perService.buscarId(idPer);
			PersonaSearchDTO personaDto = persona.toPersonaSearchDto();
			response.put("persona", personaDto);
		}else {
			List<PersonaSearchDTO> personas = perService.searchByNombreOrDoc(nombre, nroDoc);
			response.put("personas", personas);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PostMapping("/save")
    // public ResponseEntity<?> grabaPer(@RequestParam(name="idTipoPer") Long
    // idTipoPer,
    // @RequestParam(name="idTipoDoc") Long idTipoDoc,
    // @RequestParam(name="documento") String documento,
    // @RequestParam(required = false,name="tratamiento") Integer tratamiento,
    // @RequestParam(required = false,name="nombre") String nombre,
    // @RequestParam(required = false,name="apPaterno") String apePat,
    // @RequestParam(required = false,name="apMaterno") String apeMat,
    // @RequestParam(required = false,name="fecnac") @DateTimeFormat(pattern =
    // "dd/MM/yyyy") Date fecNac,
    // @RequestParam(name="idSexo") Long idSexo,
    // @RequestParam(required = false,name="razonSoc") String razonSoc,
    // @RequestParam(required = false,name="razonCom") String razonCom,
    // @RequestParam(required = false,name="fechaIni") @DateTimeFormat(pattern =
    // "dd/MM/yyyy") Date fechaIni,
    // @RequestParam(name="creaUser") String creaUser){

    // Map<String, Object> response = new HashMap<>();
    // PersonaNatural perNat = new PersonaNatural();
    // PersonaJuridica perJur = new PersonaJuridica();
    // TipoPersona tipoPersona=tipPerService.buscaTipoPer(idTipoPer);
    // if (tipoPersona==null){
    // response.put("mensaje", "El tipo persona ID: ".concat(idTipoPer.toString()
    // .concat(" no existe en la base de datos")));
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    // List<Object> list = null;
    // if(idTipoPer == 1){
    // if(!documento.equals("00000000")){
    // list = perService.findByPersonaNat(idTipoPer, idTipoDoc, documento);
    // }else {
    // list = Collections.emptyList();
    // }
    // }else {
    // list = perService.findByPersonaJur(idTipoPer, idTipoDoc, documento);
    // }
    // if (list.size() > 0){
    // response.put("mensaje", "Ya existe una persona con ese tipo de documento y
    // numero de documento");
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    // TipoDocumento tipoDocumento = tipDocumService.buscaTipDoc(idTipoDoc);
    // if (tipoDocumento==null){
    // response.put("mensaje", "El tipo documento ID: ".concat(idTipoDoc.toString()
    // .concat(" no existe en la base de datos")));
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    // //Si es persona natural
    // if(idTipoPer == 1){
    // try {
    // perNat.setNombre(nombre);
    // perNat.setApePaterno(apePat);
    // perNat.setApeMaterno(apeMat);

    // Date fechaLimite = new Date(1123154400000L);
    // if (fecNac.before(fechaLimite) || fecNac.equals(fechaLimite)) {
    // perNat.setFecNacim(fecNac);
    // } else {
    // response.put("mensaje", "No se puede crear a la persona ya que no es mayor de
    // 18 años.");
    // response.put("error", "Error de validación.");
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    // Sexo sex = sexoService.findById(idSexo);
    // perNat.setSex(sex);
    // perNaturalService.save(perNat);
    // } catch (Exception e){
    // response.put("mensaje", "Error al crear registro en
    // per_datos_persona_natural. Verifique los datos del registro");
    // response.put("error", e.getMessage());
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    // }

    // //Si es persona juridica
    // if(idTipoPer == 2){
    // try {
    // perJur.setRazonSocial(razonSoc);
    // perJur.setRazonComercial(razonCom);
    // perJur.setFecIniOper(fechaIni);

    // perJuridicaService.save(perJur);
    // } catch (Exception e){
    // response.put("mensaje", "Error al crear registro en
    // per_datos_persona_juridica. Verifique los datos del registro.");
    // response.put("error", e.getMessage());
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    // }

    // try {
    // Persona regPer = new Persona();
    // regPer.setTipoPer(tipoPersona);
    // regPer.setTipoDoc(tipoDocumento);
    // regPer.setDocumento(documento);

    // if (tratamiento != null) {
    // regPer.setTratamiento(tratamiento);
    // }
    // else {
    // regPer.setTratamiento(null);
    // }

    // regPer.setEstado(1);
    // if(idTipoPer == 1){
    // regPer.setPerNat(perNat);
    // }
    // if(idTipoPer == 2){
    // regPer.setPerJur(perJur);
    // }
    // regPer.setCreUser(creaUser);
    // regPer.setCreDate(new Date());

    // perService.save(regPer);

    // response.put("mensaje", "Persona guardada con éxito");
    // response.put("persona", regPer);
    // } catch (Exception e){
    // response.put("mensaje", "Error al realizar la insercion en la base de
    // datos");
    // response.put("error", e.getMessage());
    // return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/index")
    public ResponseEntity<?> ListaPersonal(@RequestParam(name = "idTipoPer") Long idTipoPer,
            @RequestParam(name = "idTipoDoc") Long idTipoDoc,
            @RequestParam(required = false, name = "idTipoRel") Long idTipoRel,
            @RequestParam(required = false, name = "nrodoc") String nroDoc,
            @RequestParam(required = false, name = "nombres") String nombres,
            @RequestParam(required = false, name = "apepat") String apepat,
            @RequestParam(required = false, name = "apemat") String apemat,
            @RequestParam(name = "estado") int estado,
            @RequestParam(value = "contract", required = false, defaultValue = "0") Integer contract,
            @RequestParam(value = "paginado", required = true, defaultValue = "0") Integer swPag,
            @RequestParam(required = false, name = "column") Integer column,
            @RequestParam(required = false, name = "dir") String dir,
            Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        String[] colPN = { "boton", "tipodoc", "nrodoc", "nombres", "apellidos", "atencion", "fecnac", "correo",
                "telefono" };

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        // CriteriaQuery<Persona> cq = cb.createQuery(Persona.class).distinct(true); //
        // Asegurar resultados únicos
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);
        Root<Persona> personaRoot = cq.from(Persona.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), idTipoPer));

        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.LEFT);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        relacionJoin.on(relacionPredicate);

        // Filtrado común
        if (idTipoRel != null && idTipoRel != 0) {
            predicates.add(cb.equal(relacionJoin.get("tipoRel"), idTipoRel));
        }

        if (contract != 0) {
            Join<Relacion, Contrato> contratoJoin = relacionJoin.join("contratos");
            predicates.add(cb.equal(contratoJoin.get("idTipoContrato"), 2L));
            Date fechaActual = java.sql.Date.valueOf(LocalDate.now());
            predicates.add(cb.greaterThanOrEqualTo(contratoJoin.get("fecFin"), fechaActual));
        }

        if (idTipoDoc != 0) {
            predicates.add(cb.equal(personaRoot.get("tipoDoc").get("idTipDoc"), idTipoDoc));
        }

        if (nroDoc != null && !nroDoc.isEmpty()) {
            predicates.add(cb.like(personaRoot.get("documento"), "%" + nroDoc + "%"));
        }

        if (estado != 0) {
            predicates.add(cb.equal(personaRoot.get("estado"), estado));
        }

        // Agregar condicionales para Tipo de Persona
        Join<Persona, PersonaNatural> perNatJoin = null;
        Join<Persona, PersonaJuridica> perJurJoin;
        if (idTipoPer == 1) { // Persona Natural
            perNatJoin = personaRoot.join("perNat", JoinType.LEFT);
            if (nombres != null && !nombres.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombres + "%"));
            }
            if (apepat != null && !apepat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apepat + "%"));
            }
            if (apemat != null && !apemat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apemat + "%"));
            }
            // if (descrip != null && !descrip.isEmpty()){
            // Predicate nombrePredicate = cb.like(perNatJoin.get("nombre"), "%" + descrip +
            // "%");
            // Predicate apePatPredicate = cb.like(perNatJoin.get("apePaterno"), "%" +
            // descrip + "%");
            // Predicate apeMatPredicate = cb.like(perNatJoin.get("apeMaterno"), "%" +
            // descrip + "%");
            // predicates.add(cb.or(nombrePredicate, apePatPredicate, apeMatPredicate));
            // }
        } else if (idTipoPer == 2) { // Persona Jurídica
            perJurJoin = personaRoot.join("perJur", JoinType.LEFT);
            if (nombres != null && !nombres.isEmpty()) {
                Predicate razonSocialPredicate = cb.like(perJurJoin.get("razonSocial"), "%" + nombres + "%");
                predicates.add(razonSocialPredicate);
            }
            if (apepat != null && !apepat.isEmpty()) {
                Predicate razonComercialPredicate = cb.like(perJurJoin.get("razonComercial"), "%" + apepat + "%");
                predicates.add(razonComercialPredicate);
            }
        }

        Join<Persona, Atencion> atencionJoin = personaRoot.join("atencion", JoinType.LEFT);
        Join<Persona, OrgAreas> areaJoin = personaRoot.join("area", JoinType.LEFT);
        Join<Persona, Telefono> telefonoJoin = personaRoot.join("telefonos", JoinType.LEFT);
        Join<Persona, EMail> emailJoin = personaRoot.join("correos", JoinType.LEFT);
        Join<Persona, Direccion> direccionJoin = personaRoot.join("direcciones", JoinType.LEFT);

        // cq.multiselect(personaRoot.get("id"),personaRoot.get("tipoDoc").get("nombre"),personaRoot.get("documento"));

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(personaRoot.get("id"));

        List<Persona> personas;
        Long count = 0L;

        if (idTipoPer == 1) {
            if (column == null || dir == null) {
                cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")));
            } else {
                if (dir.equals("asc")) {
                    if (column == 1)
                        cq.orderBy(cb.asc(personaRoot.get("tipoDoc").get("nombre")));
                    if (column == 2)
                        cq.orderBy(cb.asc(personaRoot.get("documento")));
                    if (column == 3)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")),
                                cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                    if (column == 4)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("nombre")));
                    if (column == 5)
                        cq.orderBy(cb.asc(personaRoot.get("atencion").get("descrip")));
                    if (column == 6)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("fecNacim")));
                }
                if (dir.equals("desc")) {
                    if (column == 1)
                        cq.orderBy(cb.desc(personaRoot.get("tipoDoc").get("nombre")));
                    if (column == 2)
                        cq.orderBy(cb.desc(personaRoot.get("documento")));
                    if (column == 3)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("apePaterno")),
                                cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                    if (column == 4)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("nombre")));
                    if (column == 5)
                        cq.orderBy(cb.desc(personaRoot.get("atencion").get("descrip")));
                    if (column == 6)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("fecNacim")));
                }
            }
        }
        if (idTipoPer == 2) {
            if (column == null || dir == null) {
                cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
            } else {
                if (dir.equals("asc")) {
                    if (column == 1)
                        cq.orderBy(cb.asc(personaRoot.get("documento")));
                    if (column == 2)
                        cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
                    if (column == 3)
                        cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonComercial")));
                    if (column == 4)
                        cq.orderBy(cb.asc(personaRoot.get("perJur").get("fecIniOper")));
                }
                if (dir.equals("desc")) {
                    if (column == 1)
                        cq.orderBy(cb.desc(personaRoot.get("documento")));
                    if (column == 2)
                        cq.orderBy(cb.desc(personaRoot.get("perJur").get("razonSocial")));
                    if (column == 3)
                        cq.orderBy(cb.desc(personaRoot.get("perJur").get("razonComercial")));
                    if (column == 4)
                        cq.orderBy(cb.desc(personaRoot.get("perJur").get("fecIniOper")));
                }
            }
        }
        if (swPag == 0) {
            personas = entityManager.createQuery(cq).getResultList();
        } else {
            // colPN =
            // {"boton","tipodoc","documento","nombres","apellidos","atencion","fecnac","correo","telefono"};
            // colPJ =
            // {"boton","documento","razonSocial","razonComercial","fecIni","correos","telefono","Relaciones"};

            personas = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize()).getResultList();

            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Persona> personaRootCount = countQuery.from(Persona.class);

            predicates = new ArrayList<>();
            predicates.add(cb.equal(personaRootCount.get("visible"), 1));
            predicates.add(cb.equal(personaRootCount.get("tipoPer").get("idTipoPer"), idTipoPer));

            relacionJoin = personaRootCount.join("relaciones", JoinType.LEFT);
            relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
            relacionJoin.on(relacionPredicate);
            if (idTipoRel != null && idTipoRel != 0) {
                predicates.add(cb.equal(relacionJoin.get("tipoRel"), idTipoRel));
            }

            if (idTipoDoc != 0)
                predicates.add(cb.equal(personaRootCount.get("tipoDoc").get("idTipDoc"), idTipoDoc));

            if (contract != 0) {
                Join<Relacion, Contrato> contratoJoin = relacionJoin.join("contratos");
                predicates.add(cb.equal(contratoJoin.get("idTipoContrato"), 2L));
                Date fechaActual = java.sql.Date.valueOf(LocalDate.now());
                predicates.add(cb.greaterThanOrEqualTo(contratoJoin.get("fecFin"), fechaActual));
            }
            
            if (nroDoc != null && !nroDoc.isEmpty())
                predicates.add(cb.like(personaRootCount.get("documento"), "%" + nroDoc + "%"));

            if (estado != 0)
                predicates.add(cb.equal(personaRootCount.get("estado"), estado));

            if (idTipoPer == 1) {
                perNatJoin = personaRootCount.join("perNat", JoinType.LEFT);
                if (nombres != null && !nombres.isEmpty()) {
                    predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombres + "%"));
                }
                if (apepat != null && !apepat.isEmpty()) {
                    predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apepat + "%"));
                }
                if (apemat != null && !apemat.isEmpty()) {
                    predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apemat + "%"));
                }
            } else {
                perJurJoin = personaRootCount.join("perJur", JoinType.LEFT);
                if (nombres != null && !nombres.isEmpty()) {
                    Predicate razonSocialPredicate = cb.like(perJurJoin.get("razonSocial"), "%" + nombres + "%");
                    predicates.add(razonSocialPredicate);
                }
                if (apepat != null && !apepat.isEmpty()) {
                    Predicate razonComercialPredicate = cb.like(perJurJoin.get("razonComercial"), "%" + apepat + "%");
                    predicates.add(razonComercialPredicate);
                }
            }
            areaJoin = personaRootCount.join("area", JoinType.LEFT);
            atencionJoin = personaRootCount.join("atencion", JoinType.LEFT);
            telefonoJoin = personaRootCount.join("telefonos", JoinType.LEFT);
            emailJoin = personaRootCount.join("correos", JoinType.LEFT);
            direccionJoin = personaRootCount.join("direcciones", JoinType.LEFT);

            countQuery.select(cb.count(personaRootCount)).where(cb.and(predicates.toArray(new Predicate[0])))
                    .groupBy(personaRootCount.get("id"));
            List<Long> lista = entityManager.createQuery(countQuery).getResultList();
            count = (long) lista.size();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Object> respuesta = new ArrayList<>();
        if (idTipoPer == 1) {
            List<lstPersona4> listaPersonaNatural = personas.stream()
                    .map(persona -> mapToPersonaNatural(persona, dateFormat, idTipoRel))
                    .collect(Collectors.toList());
            respuesta.addAll(listaPersonaNatural);
        } else if (idTipoPer == 2) {
            List<lstPersonaJuridica> listaPersonaJuridica = personas.stream()
                    .map(persona -> mapToPersonaJuridica(persona, dateFormat)).collect(Collectors.toList());
            respuesta.addAll(listaPersonaJuridica);
        }

        if (swPag == 0) {
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            Page<Object> result1 = new PageImpl<>(respuesta, pageable, count);
            response.put("datos", result1.getContent());
            response.put("totRegs", result1.getTotalElements());
            response.put("totPags", result1.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private lstPersona4 mapToPersonaNatural(Persona persona, SimpleDateFormat dateFormat, Long idTipoRel) {
        lstPersona4 perNatDto = new lstPersona4();
        perNatDto.setId(persona.getId());
        if (persona.getTipoPer().getNombre() == null)
            perNatDto.setTipPer("---");
        else
            perNatDto.setTipPer(persona.getTipoPer().getNombre());
        if (persona.getTipoDoc().getNombre() == null)
            perNatDto.setTipDoc("---");
        else
            perNatDto.setTipDoc(persona.getTipoDoc().getNombre());
        if (persona.getDocumento() == null)
            perNatDto.setNroDoc("---");
        else
            perNatDto.setNroDoc(persona.getDocumento());
        if (persona.getAtencion().getAbrev() == null)
            perNatDto.setAtencion("---");
        else
            perNatDto.setAtencion(persona.getAtencion().getAbrev());
        if (persona.getPerNat().getNombre() == null)
            perNatDto.setNombre("---");
        else
            perNatDto.setNombre(persona.getPerNat().getNombre());
        if (persona.getPerNat().getApePaterno() == null)
            perNatDto.setApePat("---");
        else
            perNatDto.setApePat(persona.getPerNat().getApePaterno());
        if (persona.getPerNat().getApeMaterno() == null)
            perNatDto.setApeMat("---");
        else
            perNatDto.setApeMat(persona.getPerNat().getApeMaterno());
        if (persona.getPerNat().getFecNacim() == null)
            perNatDto.setFecNac("---");
        else
            perNatDto.setFecNac(dateFormat.format(persona.getPerNat().getFecNacim()));
        if (persona.getArea() == null) {
            perNatDto.setDesArea("---");
            perNatDto.setIdArea(null);
        }else {
            perNatDto.setDesArea(persona.getArea().getNombre());
            perNatDto.setIdArea(persona.getArea().getId());
        }
        if (persona.getPuesto() == null) {
            perNatDto.setDesPuesto("---");
            perNatDto.setIdPuesto(null);
        }else {
            perNatDto.setDesPuesto(persona.getPuesto().getNombre());
            perNatDto.setIdPuesto(persona.getPuesto().getId());
        }
        // List<String> relationsFiltrados;
        List<relacDTO> relationsFiltrados;
        if (idTipoRel != null && idTipoRel != 0) {
            /*
             * relationsFiltrados = persona.getRelaciones().stream()
             * .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(idTipoRel) &&
             * relacion.getEstado() == 1)
             * .sorted(Comparator.comparing(relacion ->
             * relacion.getTipoRel().getIdTipoRel()))
             * .map(relacion -> relacion.getTipoRel().getDescrip())
             * .collect(Collectors.toList());
             */
            relationsFiltrados = persona.getRelaciones().stream()
                    .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(idTipoRel) && relacion.getEstado() == 1)
                    .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                    .map(relacion -> {
                    	Long days = null;
                    	if(relacion.getContratos().size() > 0) {
                    		LocalDate hoy = LocalDate.now();
                    		LocalDate fechaFin = relacion.getContratos().get(0).getFecFin()
                    		        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    		days = ChronoUnit.DAYS.between(fechaFin, hoy);
                    	}
                    	return new relacDTO(relacion.getTipoRel().getIdTipoRel(),
                                relacion.getTipoRel().getDescrip(),
                                relacion.getFecIni(), relacion.getFecFin(), days);
                    })
                    .collect(Collectors.toList());
        } else {
            /*
             * relationsFiltrados = persona.getRelaciones().stream()
             * .filter(relacion -> relacion.getEstado() == 1)
             * .sorted(Comparator.comparing(relacion ->
             * relacion.getTipoRel().getIdTipoRel()))
             * .map(relacion -> relacion.getTipoRel().getDescrip())
             * .collect(Collectors.toList());
             */
            relationsFiltrados = persona.getRelaciones().stream()
                    .filter(relacion -> relacion.getEstado() == 1)
                    .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                    .map(relacion -> {
                    	Long days = null;
                    	if(relacion.getContratos().size() > 0) {
                    		LocalDate hoy = LocalDate.now();
                    		LocalDate fechaFin = relacion.getContratos().get(0).getFecFin()
                    		        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    		days = ChronoUnit.DAYS.between(fechaFin, hoy);
                    	}
                    	return new relacDTO(relacion.getTipoRel().getIdTipoRel(),relacion.getTipoRel().getDescrip(),
                                relacion.getFecIni(), relacion.getFecFin(), days);
                    })
                    .collect(Collectors.toList());
        }
        perNatDto.setTipoRel(relationsFiltrados);
        List<String> telefonosFiltrados;
        telefonosFiltrados = persona.getTelefonos().stream()
                .filter(telefono -> telefono.getEstado() == 1 && telefono.getTipo() == 3) // Filtrar por estado
                .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                .map(Telefono::getNumero)
                .collect(Collectors.toList());
        if (telefonosFiltrados != null && !telefonosFiltrados.isEmpty()) {
            perNatDto.setFonoPrin(telefonosFiltrados.get(0));
        } else
            perNatDto.setFonoPrin("---");
        telefonosFiltrados = persona.getTelefonos().stream()
                .filter(telefono -> telefono.getEstado() == 1 && telefono.getTipo() == 2) // Filtrar por estado
                .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                .map(Telefono::getNumero)
                .collect(Collectors.toList());
        if (telefonosFiltrados != null && !telefonosFiltrados.isEmpty()) {
            perNatDto.setFonoPers(telefonosFiltrados.get(0));
        } else
            perNatDto.setFonoPers("---");

        List<String> emailsFiltrados;
        emailsFiltrados = persona.getCorreos().stream()
                .filter(email -> email.getEstado() == 1 && email.getTipo() == 1) // Filtrar por estado
                .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                .map(EMail::getCorreo)
                .collect(Collectors.toList());
        if (emailsFiltrados != null && !emailsFiltrados.isEmpty()) {
            perNatDto.setEmailPrin(emailsFiltrados.get(0));
        } else
            perNatDto.setEmailPrin("---");
        emailsFiltrados = persona.getCorreos().stream()
                .filter(email -> email.getEstado() == 1 && email.getTipo() == 2) // Filtrar por estado
                .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                .map(EMail::getCorreo)
                .collect(Collectors.toList());
        if (emailsFiltrados != null && !emailsFiltrados.isEmpty()) {
            perNatDto.setEmailPers(emailsFiltrados.get(0));
        } else
            perNatDto.setEmailPers("---");

        List<Direccion> direccionesFiltradasPorTipoDireccion;
        direccionesFiltradasPorTipoDireccion = persona.getDirecciones().stream()
                .filter(direccion -> direccion.getEstado() == 1) // Filtrar por estado
                .sorted(Comparator.comparing(Direccion::getTipo)) // Ordenar por tipo
                .collect(Collectors.toList());
        List<String> direccionesFinales;
        direccionesFinales = direccionesFiltradasPorTipoDireccion.stream()
                .map(Direccion::getDireccion)
                .collect(Collectors.toList());
        perNatDto.setDirecciones(direccionesFinales);

        String valEst = String.valueOf(persona.getEstado());
        perNatDto.setEstado(valEst.isEmpty() ? "---" : valEst);
        return perNatDto;
    }

    private lstPersonaJuridica mapToPersonaJuridica(Persona persona, SimpleDateFormat dateFormat) {
        lstPersonaJuridica perJurDto = new lstPersonaJuridica();
        perJurDto.setId(persona.getId());
        if (persona.getTipoPer().getNombre() == null)
            perJurDto.setTipPer("---");
        else
            perJurDto.setTipPer(persona.getTipoPer().getNombre());
        if (persona.getTipoDoc().getNombre() == null)
            perJurDto.setTipDoc("---");
        else
            perJurDto.setTipDoc(persona.getTipoDoc().getNombre());
        if (persona.getDocumento() == null)
            perJurDto.setNroDoc("---");
        else
            perJurDto.setNroDoc(persona.getDocumento());
        if (persona.getAtencion().getAbrev() == null)
            perJurDto.setTratamiento("---");
        else
            perJurDto.setTratamiento(persona.getAtencion().getAbrev());
        if (persona.getPerJur().getRazonSocial() == null)
            perJurDto.setRazSocial("---");
        else
            perJurDto.setRazSocial(persona.getPerJur().getRazonSocial());
        if (persona.getPerJur().getRazonComercial() == null)
            perJurDto.setRazComercial("---");
        else
            perJurDto.setRazComercial(persona.getPerJur().getRazonComercial());
        if (persona.getPerJur().getFecIniOper() == null)
            perJurDto.setFechaInicio("---");
        else
            perJurDto.setFechaInicio(dateFormat.format(persona.getPerJur().getFecIniOper()));
        if (persona.getIdTitular() == null)
            perJurDto.setIdTitular("---");
        else
            perJurDto.setIdTitular(persona.getIdTitular().toString());
        if (persona != null && persona.getIdTitular() != null) {
            Persona titular = perService.buscarId(persona.getIdTitular().longValue());
            if (titular != null && titular.getPerNat() != null) {
                String nombreTitular = titular.getPerNat().getApePaterno() + " " +
                        titular.getPerNat().getApeMaterno() + ", " +
                        titular.getPerNat().getNombre();
                perJurDto.setNombreTitular(nombreTitular);
            } else {
                perJurDto.setNombreTitular("---");
            }
        } else {
            perJurDto.setNombreTitular("---");
        }

        List<lstEmail1> correos = persona.getCorreos().stream()
                .filter(email -> email.getEstado() == 1) // Filtrar por estado
                .map(email -> createEmailDto(email))
                .collect(Collectors.toList());
        perJurDto.setCorreos(correos);

        List<lstTelefono1> telefonos = persona.getTelefonos().stream()
                .filter(telefono -> telefono.getEstado() == 1) // Filtrar por estado
                .map(telefono -> createTelefonoDto(telefono))
                .collect(Collectors.toList());
        perJurDto.setTelefonos(telefonos);

        List<Relacion> relaciones = persona.getRelaciones().stream()
                .filter(relacion -> relacion.getEstado() == 1)
                .filter(relacion -> relacion.getTipoRel() != null && (relacion.getTipoRel().getTipo() == 1 || relacion.getTipoRel().getTipo() == 2))
                .collect(Collectors.toList());
        perJurDto.setRelaciones(relaciones);

        if (Objects.equals(persona.getEstado(), null))
            perJurDto.setEstado("---");
        else
            perJurDto.setEstado(String.valueOf(persona.getEstado()));
        return perJurDto;
    }

    private lstEmail1 createEmailDto(EMail email) {
        lstEmail1 emailDto = new lstEmail1();
        emailDto.setIdEMail(email.getIdEMail());
        emailDto.setIdPersona(email.getPersona().getId());
        emailDto.setTipo(email.getTipo());
        emailDto.setCorreo(email.getCorreo());
        emailDto.setEstado(email.getEstado());
        emailDto.setUsuCreado(email.getUsuCreado());
        emailDto.setFechaCreado(email.getFechaCreado());
        emailDto.setUsuUpdate(email.getUsuUpdate());
        emailDto.setFechaUpdate(email.getFechaUpdate());
        return emailDto;
    }

    private lstTelefono1 createTelefonoDto(Telefono telefono) {
        lstTelefono1 telefonoDto = new lstTelefono1();
        telefonoDto.setIdTelefono(telefono.getIdTelefono());
        telefonoDto.setIdPersona(telefono.getPersona().getId());
        telefonoDto.setTipo(telefono.getTipo());
        telefonoDto.setNumero(telefono.getNumero());
        telefonoDto.setEstado(telefono.getEstado());
        telefonoDto.setUsuCreado(telefono.getUsuCreado());
        telefonoDto.setFechaCreado(telefono.getFechaCreado());
        telefonoDto.setUsuUpdate(telefono.getUsuUpdate());
        telefonoDto.setFechaUpdate(telefono.getFechaUpdate());
        return telefonoDto;
    }

    @PostMapping("/update")
    public ResponseEntity<?> UpdatePer(@RequestParam(name = "idPer") Long idPer,
            @RequestParam(name = "tipPer") Long tipPer, // 1=Persona Natural, 2=Persona Juridica
            @RequestParam(name = "idTipoDoc") Long tipDoc,
            @RequestParam(name = "documento") String nroDoc,
            @RequestParam(required = false, name = "tratamiento") Integer tratamiento,
            // Datos de Persona Natural
            @RequestParam(required = false, name = "nombre") String nombre,
            @RequestParam(required = false, name = "apePat") String apPat,
            @RequestParam(required = false, name = "apeMat") String apMat,
            @RequestParam(required = false, name = "sexo") Long idSexo,
            @RequestParam(required = false, name = "estadoCiv") Long estadoCiv,
            @RequestParam(required = false, name = "fecNac") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecNac,
            // Datos de Persona Jurídica
            @RequestParam(required = false, name = "razonSoc") String razonSoc,
            @RequestParam(required = false, name = "razonCom") String razonCom,
            @RequestParam(required = false, name = "idTitular") Long idTitular,
            @RequestParam(required = false, name = "fechaIni") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
            // Datos de Persona
            @RequestParam(required = false, name = "idArea") Long idArea,
            @RequestParam(required = false, name = "idPuesto") Long idPuesto,
            @RequestParam(name = "updUser") String updUser) {

        Map<String, Object> response = new HashMap<>();
        Persona registro = perService.buscarId(idPer);
        if (registro == null) {
            response.put("mensaje", "El persona ID: ".concat(idPer.toString()
                    .concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        TipoDocumento tipoDocumento = tipDocumService.buscaTipDoc(tipDoc);
        if (tipoDocumento == null) {
            response.put("mensaje", "El tipo documento ID: ".concat(tipDoc.toString()
                    .concat(" no existe en la base de datos")));
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Validación de datos obligatorios
        if (tipPer == 1) {
            if (nombre == null || apPat == null || apMat == null || idSexo == null || estadoCiv == null
                    || fecNac == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Faltan datos obligatorios para Persona Natural."));
            }
        } else if (tipPer == 2) {
            if (razonSoc == null || razonCom == null || fecIni == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Faltan datos obligatorios para Persona Jurídica."));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Tipo de persona no válido."));
        }

        Integer longitud = tipDocumService.buscarLongitud(tipDoc);
        // Validación de longitud del documento
        if (nroDoc.length() != longitud) {
            return ResponseEntity.badRequest().body(Map.of("mensaje",
                    "La longitud del documento no coincide con la esperada. Debe ser de " + longitud + " caracteres."));
        }

        if (tipPer == 2) {
            if (tipDoc != 4) {
                return ResponseEntity.unprocessableEntity().body(Map.of("mensaje",
                        "Para Persona Jurídica el tipo de documento debe ser RUC (tipo documento 4)."));
            }
            // Verificar el idTitular solo si idTipoPer es 2
            if (idTitular != null) {
                List<Object> personaNaturales = perService.findByPersonaNat();
                boolean titularExists = personaNaturales.stream()
                        .map(obj -> ((Object[]) obj)[0])
                        .anyMatch(id -> id.equals(Long.valueOf(idTitular)));

                if (!titularExists) {
                    return ResponseEntity.unprocessableEntity().body(
                            Map.of("mensaje", "El idTitular proporcionado no existe entre las personas naturales."));
                }
            }
        }
        try {
            registro.setTipoDoc(tipoDocumento);
            registro.setDocumento(nroDoc);
            registro.setUpdUser(updUser);
            registro.setUpdDate(new Date());
            if(idTitular != null && idTitular > 0 ) {
            	registro.setIdTitular(idTitular);
            }else {
            	registro.setIdTitular(null);
            }
            Atencion trata = null;
            Optional<Atencion> regTra = atencionService.findById(tratamiento);
            if (regTra.isPresent()) {
                trata = regTra.get();
            }
            // regPer.setTratamiento(tratamiento != null ? tratamiento : null);
            registro.setAtencion(trata);
            if (tipPer == 1) { // Persona Natural
                if (idPuesto != null && idPuesto > 0) {
                	Puestos puesto = puestoService.findById(idPuesto);
                	registro.setPuesto(puesto);
                	registro.setArea(puesto.getArea());
                }else {
                	registro.setPuesto(null);
                	registro.setArea(null);
                }
                PersonaNatural pNat = registro.getPerNat();
                pNat.setNombre(nombre);
                pNat.setApePaterno(apPat);
                pNat.setApeMaterno(apMat);
                pNat.setFecNacim(fecNac);
                Sexo sex = sexoService.findById(idSexo);
                pNat.setSex(sex);
                TipoEstadoCivil estCivil = estadoCivilService.findById(estadoCiv);
                pNat.setEstCivil(estCivil);
                registro.setPerNat(pNat);
            } else if (tipPer == 2) { // Persona Jurídica{
            	registro.setPuesto(null);
            	registro.setArea(null);
                PersonaJuridica pJur = registro.getPerJur();
                pJur.setRazonSocial(razonSoc);
                pJur.setRazonComercial(razonCom);
                pJur.setFecIniOper(fecIni);
                registro.setPerJur(pJur);
            }
            perService.save(registro);
            response.put("mensaje", "Persona actualizada con éxito");
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar la actualizacion de persona");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PostMapping("/update")
    // public ResponseEntity<?> UpdatePer(@RequestParam(name="idPer") Long idPer,
    // @RequestParam(name="tipPer") Long tipPer,
    // @RequestParam(name="tipDoc") Long tipDoc,
    // @RequestParam(name="tratam") int tratam,
    // @RequestParam(name="nroDoc") String nroDoc,
    // @RequestParam(required = false,name="nombre") String nombre,
    // @RequestParam(required = false,name="apPat") String apPat,
    // @RequestParam(required = false,name="apMat") String apMat,
    // @RequestParam(required = false,name="fecNac") @DateTimeFormat(pattern =
    // "dd/MM/yyyy") Date fecNac,
    // @RequestParam(name="idSexo") Long idSexo,
    // @RequestParam(required = false,name="razonSoc") String razonSoc,
    // @RequestParam(required = false,name="razonCom") String razonCom,
    // @RequestParam(required = false,name="fechaIni") @DateTimeFormat(pattern =
    // "dd/MM/yyyy") Date fecIni,
    // @RequestParam(name="updUser") String updUser){

    // Map<String, Object> response = new HashMap<>();
    // Persona registro = perService.buscarId(idPer);
    // if(registro == null){
    // response.put("mensaje", "El persona ID: ".concat(idPer.toString()
    // .concat(" no existe en la base de datos")));
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    // TipoDocumento tipoDocumento = tipDocumService.buscaTipDoc(tipDoc);
    // if (tipoDocumento==null){
    // response.put("mensaje", "El tipo documento ID: ".concat(tipDoc.toString()
    // .concat(" no existe en la base de datos")));
    // return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    // try {
    // if (tipPer == 1){ //Persona natural
    // registro.setTipoDoc(tipoDocumento);
    // registro.setTratamiento(tratam);
    // registro.setDocumento(nroDoc);

    // PersonaNatural pNat = registro.getPerNat();
    // pNat.setNombre(nombre);
    // pNat.setApePaterno(apPat);
    // pNat.setApeMaterno(apMat);
    // pNat.setFecNacim(fecNac);
    // Sexo sex = sexoService.findById(idSexo);
    // pNat.setSex(sex);

    // registro.setPerNat(pNat);
    // registro.setUpdUser(updUser);
    // registro.setUpdDate(new Date());

    // perService.save(registro);

    // response.put("mensaje", "Persona actualizada con éxito");
    // response.put("persona", registro);

    // }
    // else if(tipPer == 2){ //Persona judicial
    // registro.setTipoDoc(tipoDocumento);
    // registro.setTratamiento(tratam);
    // registro.setDocumento(nroDoc);

    // PersonaJuridica pJur = registro.getPerJur();
    // pJur.setRazonSocial(razonSoc);
    // pJur.setRazonComercial(razonCom);
    // pJur.setFecIniOper(fecIni);

    // registro.setUpdUser(updUser);
    // registro.setUpdDate(new Date());

    // perService.save(registro);

    // registro.setPerJur(pJur);
    // response.put("mensaje", "Persona actualizada con éxito");
    // response.put("persona", registro);
    // }
    // } catch (Exception e){
    // response.put("mensaje", "Error al realizar la actualizacion de persona");
    // response.put("error", e.getMessage());
    // return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping("/show")
    public ResponseEntity<?> show(@RequestParam(name = "idPersona") Long idPersona) {
        Map<String, Object> response = new HashMap<>();

        try {
            Persona persona = perService.buscarId(idPersona);
            if (persona == null) {
                response.put("mensaje", "La persona ID: ".concat(idPersona.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            lstPersona3 registro = new lstPersona3();
            registro.setId(persona.id);
            registro.setTratamiento(persona.getAtencion().getId());
            registro.setIdNodoTipo(persona.getIdNodoTipo());
            registro.setTipPer(persona.tipoPer.idTipoPer);
            registro.setTipDoc(persona.tipoDoc.idTipDoc);
            registro.setNroDoc(persona.documento);

            if(persona.area != null) {
                registro.setIdArea(persona.area.getId());
                persona.area.getGerencia().setOrgAreas(null);
                persona.area.setPuestos(null);
                registro.setArea(persona.area);
            }
            if(persona.puesto != null) {
                registro.setIdPuesto(persona.puesto.getId());
            }
            registro.setPuesto(persona.puesto);
            registro.setNroDoc(persona.documento);
            // Buscar y formatear el nombre del titular si aplicable
            if (persona.getIdTitular() != null) {
                Persona titular = perService.buscarId(persona.getIdTitular());
                registro.setIdTitular(persona.getIdTitular());
                if (titular != null && titular.perNat != null) {
                    String nombreTitular = titular.perNat.apePaterno
                            .concat(" ")
                            .concat(titular.perNat.apeMaterno)
                            .concat(", ")
                            .concat(titular.perNat.nombre);
                    registro.setNombreTitular(nombreTitular);
                } else {
                    registro.setNombreTitular("---");
                }
            } else {
                registro.setNombreTitular("---");
            }
            List<Object> personas = perService.findByPersonaNat();
            Iterator<Object> it = personas.iterator();
            List<lstPersona1> lstResumen2 = new ArrayList<>();
            while (it.hasNext()) {
                Object[] row = (Object[]) it.next();
                lstPersona1 lista = new lstPersona1();
                lista.setId((Long) row[0]);
                lista.setNombre(String.valueOf(row[1]));

                lstResumen2.add(lista);
            }
            registro.setPersonasNaturales(lstResumen2);

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

            List<lstEmail1> listaEml1 = new ArrayList<>();
            for (int i = 0; i < persona.correos.size(); i++) {
                lstEmail1 email = new lstEmail1();
                email.setIdEMail(persona.correos.get(i).getIdEMail());
                if (persona.correos.get(i).getPersona() != null) {
                    email.setIdPersona(persona.correos.get(i).getPersona().id);
                }
                email.setTipo(persona.correos.get(i).getTipo());
                email.setCorreo(persona.correos.get(i).getCorreo());
                email.setEstado(persona.correos.get(i).getEstado());
                email.setUsuCreado(persona.correos.get(i).getUsuCreado());
                email.setFechaCreado(persona.correos.get(i).getFechaCreado());
                email.setUsuUpdate(persona.correos.get(i).getUsuUpdate());
                email.setFechaUpdate(persona.correos.get(i).getFechaUpdate());

                listaEml1.add(email);
            }
            registro.setCorreos(listaEml1);

            List<lstDireccion1> listaDir1 = new ArrayList<>();
            for (int i = 0; i < persona.direcciones.size(); i++) {
                lstDireccion1 direc = new lstDireccion1();
                direc.setId(persona.direcciones.get(i).getId());
                if (persona.direcciones.get(i).getPersona() != null) {
                    direc.setIdPersona(persona.direcciones.get(i).getPersona().id);
                }
                direc.setTipo(persona.direcciones.get(i).getTipo());
                if (persona.direcciones.get(i).getDistrito() != null) {
                    direc.setIdDistrito(persona.direcciones.get(i).getDistrito().id);
                }
                direc.setDireccion(persona.direcciones.get(i).getDireccion());
                direc.setEstado(persona.direcciones.get(i).getEstado());
                direc.setCreateUser(persona.direcciones.get(i).getCreateUser());
                direc.setCreateDate(persona.direcciones.get(i).getCreateDate());
                direc.setUpdateUser(persona.direcciones.get(i).getUpdateUser());
                direc.setUpdateDate(persona.direcciones.get(i).getUpdateDate());

                listaDir1.add(direc);
            }
            registro.setDirecciones(listaDir1);

            List<Integer> tipos = new ArrayList<Integer>();
            tipos.add(3);

            if (persona.tipoPer.getIdTipoPer() == 1L) {
                registro.setNombre(persona.perNat.getNombre());
                registro.setApellidoP(persona.perNat.getApePaterno());
                registro.setApellidoM(persona.perNat.getApeMaterno());
                registro.setSexo(persona.perNat.getSex());
                registro.setEstadoCivil(persona.perNat.getEstCivil());
                registro.setFecIni(persona.perNat.getFecNacim().toString());
            }
            if (persona.tipoPer.getIdTipoPer() == 2L) {
            	registro.setNombre(persona.perJur.getRazonSocial());
            	registro.setRazcom(persona.perJur.getRazonComercial());
                registro.setFecIni(persona.perJur.getFecIniOper().toString());
                tipos.add(2);
            }

            List<CorreoTipo> correosTipo = new ArrayList<>();
			try {
				String correoSql = "SELECT * FROM per_persona_correo_tipo";
				Query correoQuery = entityManager.createNativeQuery(correoSql, CorreoTipo.class);
				correosTipo = correoQuery.getResultList();
			} catch (Exception ex) {
				response.put("error", "Error al ejecutar la consulta directa: " + ex.getMessage());
			}
			response.put("tipoCorreo", correosTipo);
            List<DireccionTipo> direccionesTipo = new ArrayList<>();
			try {
				String direccionSql = "SELECT * FROM per_persona_direccion_tipo";
				Query direccionQuery = entityManager.createNativeQuery(direccionSql, DireccionTipo.class);
				direccionesTipo = direccionQuery.getResultList();
			} catch (Exception ex) {
				response.put("error", "Error al ejecutar la consulta directa: " + ex.getMessage());
			}
            response.put("tipoDireccion", direccionesTipo);
            List<TipoRelacion> tiposRelacion = tipRelacionService.findByTipo(tipos, persona.getIdNodoTipo());
            response.put("TipoRelacion", tiposRelacion);
            List<TipoDocumento> tipoDocumentos = tipDocumService.findAll();
            response.put("TipoDocumento", Objects.requireNonNullElse(tipoDocumentos, "-- no hay registros --"));
            List<Parametro> filteredPerParams = parametroService.findByParams(2L);
            response.put("tipo", filteredPerParams);
            List<TelefonoTipo> tiposTelefono = tipoTelefonoService.findAll();
            response.put("tipoTelefono", tiposTelefono);
            response.put("persona", registro);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la funcion show");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/showActions")
	public ResponseEntity<?> showActions(@RequestParam(name = "idPersona") Long idPersona,
										@RequestParam(name = "idContacto") Long idContacto) {
		Map<String, Object> response = new HashMap<>();
		try {
            // Obtener la persona y su tipo
            Persona typePer = perService.buscarId(idPersona);
            if (typePer == null) {
                response.put("message", "La persona ID: ".concat(idPersona.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            Long tipoPersona = typePer.getTipoPer().getIdTipoPer();
            int gemTipo = (tipoPersona == 1) ? 3 : 1;

			// Validar si idContacto es un contacto válido para idPersona
			String sql = "SELECT g.id_nodo_origen FROM grafo_enlace AS g " +
						"INNER JOIN grafo_enlace_motivo AS gem ON gem.id = g.id_enlace_motivo " +
						"WHERE g.id_nodo_destino = :idPersona AND gem.tipo = :gemTipo AND g.estado = 1";
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("idPersona", idPersona);
            query.setParameter("gemTipo", gemTipo);
			List<Object> idNodoOrigenList = query.getResultList();

			// Convertir los resultados a una lista de Long
			List<Long> idNodoOrigenLongList = new ArrayList<>();
			for (Object id : idNodoOrigenList) {
				idNodoOrigenLongList.add(((Number) id).longValue());
			}

			// Verificar si idContacto está en la lista de id_nodo_origen
			if (!idNodoOrigenLongList.contains(idContacto)) {
				response.put("message", "No tiene permiso para acceder a la información de esta persona, ya que no está registrada como su contacto.");
				return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
			}

			// Validar si idContacto es una persona existente
			Persona persona = perService.buscarId(idContacto);
			if (persona == null) {
				response.put("message", "La persona ID: ".concat(idContacto.toString()
						.concat(" no existe en la base de datos")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			lstPersona3 registro = new lstPersona3();
			registro.setId(persona.id);
			registro.setTratamiento(persona.getAtencion().getId());
			registro.setIdNodoTipo(persona.getIdNodoTipo());
			registro.setTipPer(persona.tipoPer.idTipoPer);
			registro.setTipDoc(persona.tipoDoc.idTipDoc);
			registro.setNroDoc(persona.documento);
			registro.setPuesto(persona.puesto);
			registro.setNroDoc(persona.documento);
			registro.setEstado(persona.estado);
			registro.setFecCrea(persona.creDate);
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

            List<CorreoTipo> tipoCorreo = new ArrayList<>();
            List<CorreoTipo> tipoCorreoForm = new ArrayList<>();
            try {
                // Todos los tipos de correo
                String sqlTodos = "SELECT * FROM per_persona_correo_tipo";
                Query queryTodos = entityManager.createNativeQuery(sqlTodos, CorreoTipo.class);
                tipoCorreo = queryTodos.getResultList();
                // Tipos de correo filtrados por tipoPersona
                String sqlFiltrado = "SELECT * FROM per_persona_correo_tipo WHERE id_tipo_persona = :tipoPersona";
                Query queryFiltrado = entityManager.createNativeQuery(sqlFiltrado, CorreoTipo.class);
                queryFiltrado.setParameter("tipoPersona", tipoPersona);
                tipoCorreoForm = queryFiltrado.getResultList();
            } catch (Exception ex) {
                response.put("error", "Error al ejecutar la consulta directa: " + ex.getMessage());
            }
            response.put("tipoCorreo", tipoCorreo);
            response.put("tipoCorreoForm", tipoCorreoForm);

            List<DireccionTipo> direccionesTipo = new ArrayList<>();
			try {
				String direccionSql = "SELECT * FROM per_persona_direccion_tipo WHERE id_tipo_persona = " + persona.tipoPer.getIdTipoPer();
				Query direccionQuery = entityManager.createNativeQuery(direccionSql, DireccionTipo.class);
				direccionesTipo = direccionQuery.getResultList();
			} catch (Exception ex) {
				response.put("error", "Error al ejecutar la consulta directa: " + ex.getMessage());
			}
            response.put("tipoDireccion", direccionesTipo);
			response.put("persona", registro);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la función show");
			response.put("debug", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

    @PostMapping("/delete")
    public ResponseEntity<?> updEstado(@RequestParam(name = "idPersona") Long idPersona,
            @RequestParam(name = "updUsuario") String updUser) {
        Map<String, Object> response = new HashMap<>();
        String message = "Registro desactivado con éxito";

        try {
            Persona persona = perService.buscarId(idPersona);
            if (persona == null) {
                response.put("mensaje", "Registro ID: ".concat(idPersona.toString()
                        .concat(" no existe en la base de datos")));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (persona.getEstado() == 0) {
                persona.setEstado(1);
                message = "Registro activado con éxito";
            } else if (persona.getEstado() == 1) {
                persona.setEstado(0);
            }

            persona.setUpdUser(updUser);
            persona.setUpdDate(new Date());

            perService.save(persona);

            response.put("mensaje", message);
            response.put("persona", persona);
        } catch (Exception e) {
            response.put("mensaje", "Error al realizar cambio de estado del registro");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<?> listaMaestra(@RequestParam(required = false, name = "tipoPer") Long tipoPer) {
        Map<String, Object> response = new HashMap<>();
        Long idTipoPer = tipoPer;
        if (tipoPer == null)
            idTipoPer = 1L;

        List<TipoPersona> tipoPersonas = tipPerService.findAll();
        response.put("TipoPersona", Objects.requireNonNullElse(tipoPersonas, "-- no hay registros --"));

        List<TipoRelacion> tipoRelaciones = tipRelacionService.findAll();
        Iterator<TipoRelacion> it = tipoRelaciones.iterator();
        List<lstRelacion1> lstResumen = new ArrayList<>();

        while (it.hasNext()) {
        	TipoRelacion row = it.next();
        }
        response.put("TipoRelacion", tipoRelaciones);

        List<TipoDocumento> tipoDocumentos = tipDocumService.findAll();
        response.put("TipoDocumento", Objects.requireNonNullElse(tipoDocumentos, "-- no hay registros --"));

        List<lstPersona1> lstResumen2 = new ArrayList<>();
        if (idTipoPer == 1) {
            List<Object> personas = perService.findByPersonaNat();
            Iterator<Object> it2 = personas.iterator();
            while (it2.hasNext()) {
                Object[] row = (Object[]) it2.next();
                lstPersona1 lista = new lstPersona1();
                lista.setId((Long) row[0]);
                lista.setNombre(String.valueOf(row[1]));

                lstResumen2.add(lista);
            }
        }
        if (idTipoPer == 2) {
            List<Object> personas = perService.finByPersonaJur();
            Iterator<Object> it2 = personas.iterator();
            while (it2.hasNext()) {
                Object[] row = (Object[]) it2.next();
                lstPersona1 lista = new lstPersona1();
                lista.setId((Long) row[0]);
                lista.setNombre(String.valueOf(row[1]));

                lstResumen2.add(lista);
            }
        }

        List<Parametro> filteredPerParams = parametroService.findByParams(2L);
        response.put("tipo", filteredPerParams);
        response.put("Contacto", lstResumen2);

        List<DireccionTipo> direccionesTipo = tipoDireccionService.findAllByIdTipoPersona(idTipoPer);
        response.put("tipoDireccion", direccionesTipo);
        
        List<TelefonoTipo> tiposTelefono = tipoTelefonoService.findAllByIdTipoPersona(idTipoPer);
        response.put("tipoTelefono", tiposTelefono);
        
        List<CorreoTipo> tiposEmail = tipoCorreoService.findAllByIdTipoPersona(idTipoPer);
        response.put("tipoEmail", tiposEmail);

        //List<MotivoContacto> motivos = motCtoService.findAll();
        //response.put("Motivo", Objects.requireNonNullElse(motivos, "-- no hay registros --"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/listaClientes")
    public ResponseEntity<?> listadoClientes() {
        Map<String, Object> response = new HashMap<>();
        List<Object> clientes = personaService.listadoClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/listaClientesPorContrato")
    public ResponseEntity<?> listadoClientesPorContrato() {
        Map<String, Object> response = new HashMap<>();
        List<Object> clientes = personaService.listadoClientesPorContrato();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/listaOutsourcing")
    public ResponseEntity<?> listadoOutsourcing(@RequestParam(required = false, name = "idcli") Long idCliente,
            @RequestParam(required = false, name = "tipoDoc") Long tipdoc,
            @RequestParam(required = false, name = "nroDoc") String nrodoc,
            @RequestParam(required = false, name = "nombre") String nombre,
            @RequestParam(required = false, name = "apePat") String apePaterno,
            @RequestParam(required = false, name = "apeMat") String apeMaterno,
            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
            @RequestParam(value = "column", required = false) Integer column,
            @RequestParam(value = "dir", required = false) String dir,
            Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        if (swPag == 0) {
            List<Object> recursos = personaService.listadoOutsourcing(idCliente, tipdoc, nrodoc, nombre, apePaterno,
                    apeMaterno);
            return ResponseEntity.ok(recursos);
        } else {
        	Page<Object> recursos = personaService.listadoOutsourcing(idCliente, tipdoc, nrodoc, nombre, apePaterno,
                    apeMaterno, column, dir, pageable);
            response.put("datos", recursos.getContent());
            response.put("totRegs", recursos.getTotalElements());
            response.put("totPags", recursos.getTotalPages());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filtrarPorTipoPersonaYUbigeo(
            @RequestParam("tipoPer") Long tipoPer,
            @RequestParam(value = "tipoRel", required = false) Long tipoRel,
            @RequestParam(value = "tipoDoc", required = false) Long tipoDoc,
            @RequestParam(value = "nroDoc", required = false) String nroDoc,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apePat", required = false) String apePat,
            @RequestParam(value = "apeMat", required = false) String apeMat,
            @RequestParam(value = "idCto", required = false) Long idCto,
            @RequestParam(value = "motivo", required = false) Integer motivo,
            @RequestParam(value = "tipoTel", required = false) Integer tipoTel,
            @RequestParam(value = "tipoCorreo", required = false) Integer tipoCorreo,
            @RequestParam(value = "tipoDir", required = false) Integer tipoDir,
            @RequestParam(value = "dpto", required = false) String departamento,
            @RequestParam(value = "prov", required = false) String provincia,
            @RequestParam(value = "dist", required = false) String distrito,
            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
            @RequestParam(value = "column", required = false) Integer column,
            @RequestParam(value = "dir", required = false) String dir,
            Pageable pageable) {

        Map<String, Object> response = new HashMap<>();
        if (swPag == 0) {
            List<Object> personas = personaService.filtradoAvanzado(tipoPer, tipoRel, tipoDoc,
                    nroDoc, nombre, apePat, apeMat, idCto, motivo, tipoTel, tipoCorreo, tipoDir,
                    departamento, provincia, distrito);
            return ResponseEntity.ok(personas);
        } else {
            Page<Object> personas = personaService.filtradoAvanzado(tipoPer, tipoRel, tipoDoc,
                    nroDoc, nombre, apePat, apeMat, idCto, motivo, tipoTel, tipoCorreo, tipoDir,
                    departamento, provincia, distrito, column, dir, pageable);
            response.put("datos", personas.getContent());
            response.put("totRegs", personas.getTotalElements());
            response.put("totPags", personas.getTotalPages());
            return ResponseEntity.ok(response);
        }

    }

    @PostMapping("/busqueda")
    public ResponseEntity<?> filtrarNatural(
            @RequestParam("tipoPer") Long tipoPer,
            @RequestParam(value = "tipoRel", required = false) Long tipoRel,
            @RequestParam(value = "tratamiento", required = false) Long tratamiento,
            @RequestParam(value = "tipoDoc", required = false) Long tipoDoc,
            @RequestParam(value = "nroDoc", required = false) String nroDoc,
            @RequestParam(value = "nombre", required = false, defaultValue = "") String nombre,
            @RequestParam(value = "apePat", required = false) String apePat,
            @RequestParam(value = "apeMat", required = false) String apeMat,
            @RequestParam(value = "estado", required = false) Long estado,
            @RequestParam(value = "tipoTel", required = false) Integer tipoTel,
            @RequestParam(value = "tipoCorreo", required = false) Integer tipoCorreo,
            @RequestParam(value = "tipoDir", required = false) Integer tipoDir,
            @RequestParam(value = "dpto", required = false) String departamento,
            @RequestParam(value = "prov", required = false) String provincia,
            @RequestParam(value = "dist", required = false) String distrito,
            @RequestParam(value = "idCto", required = false) Long idCto,
            @RequestParam(value = "motivo", required = false) Integer motivo,
            @RequestParam(value = "column", required = false) Integer column,
            @RequestParam(value = "dir", required = false) String dir,
            @RequestParam(value = "orderby", required = false) String[] orders,
            @RequestParam(value = "paginado", required = true, defaultValue = "0") Integer swPag,
            Pageable pageable) {

        Map<String, Object> response = new HashMap<>();
        List<Object> respuesta = new ArrayList<>();

        if (tipoPer == 1) {
            if (swPag == 0) {

                List<lstPersona5> personas = personaService.filtradoNatural(tipoPer, tipoRel, tipoDoc,
                        nroDoc, nombre, apePat, apeMat, idCto, motivo);
                respuesta.addAll(personas);
                response.put("datos", respuesta);
            } else {

                Page<lstPersona5> personas = personaService.filtradoNatural(tipoPer, tipoRel, tipoDoc,
                        nroDoc, nombre, apePat, apeMat, idCto, motivo, column, dir, pageable);
                respuesta.addAll(personas.getContent());
                response.put("datos", respuesta);
                response.put("totRegs", personas.getTotalElements());
                response.put("totPags", personas.getTotalPages());
            }
        } else if (tipoPer == 2) {
            if (motivo == null || motivo == 0) {
                response.put("validacion", "Para personas juridicas es obligatorio el campo MOTIVO");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else {
                if (swPag == 0) {

                    List<lstPersona6> empresas = personaService.filtradoJuridico(tipoRel, tratamiento, nroDoc,
                            nombre, estado, tipoTel, tipoCorreo, tipoDir, departamento, provincia, distrito, motivo);
                    response.put("datos", empresas);
                } else {

                    Page<lstPersona6> empresas = personaService.filtradoJuridico(tipoRel, tratamiento, nroDoc,
                            nombre, estado, tipoTel, tipoCorreo, tipoDir, departamento, provincia, distrito,
                            motivo, column, dir, orders, pageable);
                    respuesta.addAll(empresas.getContent());
                    response.put("datos", respuesta);
                    response.put("totRegs", empresas.getTotalElements());
                    response.put("totPags", empresas.getTotalPages());
                }
            }
        } else {
            response.put("mensaje", "Tipo de persona no valido. (1: Natural, 2:Juridica)");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
        // return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/infoLogin")
    public ResponseEntity<?> infoLogin(@RequestParam(name = "username") String username,
            @RequestParam(name = "application") Integer aplicacion) {
        Map<String, Object> response = new HashMap<>();
        List<Object> listado = null;
        List<Object> funcionalidades = null;
        List<Object> detalles = null;
        List<Object> detServ = null;

        listado = autenticacionController.buscaUsuarioAutenticado(username);
        AutenticacionUsuario user = autenticacionUsuarioService.findByUsuario(username);

        List<lstAuth1> lstResumen = new ArrayList<>();
        List<lstAuth1Func> lstFuncPadres = new ArrayList<>();
        List<lstAuth1FuncDet> lstFuncHijos = new ArrayList<>();
        List<lstAuth1FuncSrv> lstHijosSrv = new ArrayList<>();
        List<Integer> ListaIdPerfiles = new ArrayList<Integer>();
        List<lstAuth1Perfil> lstRegPerfiles = new ArrayList<>();

        Iterator<Object> it = listado.iterator();
        Persona regPer = new Persona();
        lstAuth1Perfil regPerfil = new lstAuth1Perfil();
        AutorizacionPerfil detPerfil = new AutorizacionPerfil();
        Optional<AutorizacionPerfilUsuario> perfil1;
        List<Object> perfiles;
        Long idUsuario;
        while (it.hasNext()) {
            Object[] row = (Object[]) it.next();
            lstAuth1 registro = new lstAuth1();
            idUsuario = (Long) row[0];
            registro.setId((Long) row[0]);
            registro.setUsuario(String.valueOf(row[1]));
            registro.setTipoUsuario((int) row[2]);
            registro.setTipoUsuarioDelta((int) row[3]);
            registro.setCodEstUsuario((int) row[4]);
            registro.setNomCompleto(String.valueOf(row[5]));

            perfiles = authPerfilUserService.findPerfiles(idUsuario);
            ListaIdPerfiles = new ArrayList<Integer>();
            Iterator<Object> itP = perfiles.iterator();
            while (itP.hasNext()) {
                Object[] row9 = (Object[]) itP.next();
                regPerfil = new lstAuth1Perfil();
                int valor = (Integer) row9[0];
                ListaIdPerfiles.add(valor);

                detPerfil = authPerfilService.findById(valor);
                regPerfil.setIdPerfil(valor);
                if (detPerfil != null) {
                    regPerfil.setDescripcion(detPerfil.getDescripcion());
                    regPerfil.setNombre(detPerfil.getNombre());
                }

                lstRegPerfiles.add(regPerfil);
            }
            funcionalidades = authFuncService.ListaFuncionalidades(ListaIdPerfiles, aplicacion);
            Iterator<Object> it1 = funcionalidades.iterator();
            while (it1.hasNext()) {
                lstAuth1Func lstAuth1Func; // Declare the variable lstAuth1Func
                Object[] row1 = (Object[]) it1.next();
                lstAuth1Func regFnc = new lstAuth1Func();
                Integer idFunc = Integer.valueOf(String.valueOf(row1[0]));

                regFnc.setIdFunc(idFunc);
                regFnc.setNombre(String.valueOf(row1[1]));
                regFnc.setDescripcion(String.valueOf(row1[2]));
                regFnc.setRuta(String.valueOf(row1[3]));
                regFnc.setIcon(String.valueOf(row1[4]));
                regFnc.setFlgMenu(String.valueOf(row1[5]));
                regFnc.setFlgControl(String.valueOf(row1[6]));
                regFnc.setRoute(String.valueOf(row1[8]));

                int idPerfil = (Integer) row1[7];

                lstFuncHijos = new ArrayList<>();
                Long idF = Long.valueOf(String.valueOf(idFunc));
                detalles = authFuncService.ListaFuncionalidades(ListaIdPerfiles, aplicacion, idF);
                Iterator<Object> it2 = detalles.iterator();
                while (it2.hasNext()) {
                    Object[] row2 = (Object[]) it2.next();
                    lstAuth1FuncDet regFncDet = new lstAuth1FuncDet();
                    Integer idFunc2 = Integer.valueOf(String.valueOf(row2[0]));
                    regFncDet.setIdFunc(idFunc2);
                    regFncDet.setNombre(String.valueOf(row2[1]));
                    regFncDet.setDescripcion(String.valueOf(row2[2]));
                    regFncDet.setRuta(String.valueOf(row2[3]));
                    regFncDet.setIcon(String.valueOf(row2[4]));
                    regFncDet.setFlgMenu(String.valueOf(row2[5]));
                    regFncDet.setFlgControl(String.valueOf(row2[6]));
                    regFncDet.setRoute(String.valueOf(row2[8]));

                    Long idFuncion = (Long) row2[0];

                    lstHijosSrv = new ArrayList<>();
                    detServ = authFuncService.ListaFuncionalidades(ListaIdPerfiles, aplicacion, idFuncion);
                    Iterator<Object> it3 = detServ.iterator();
                    while (it3.hasNext()) {
                        Object[] row3 = (Object[]) it3.next();
                        lstAuth1FuncSrv regFncSrv = new lstAuth1FuncSrv();
                        regFncSrv.setIdFunc((Integer) row3[0]);
                        regFncSrv.setNombre(String.valueOf(row3[1]));
                        regFncSrv.setDescripcion(String.valueOf(row3[2]));
                        regFncSrv.setRuta(String.valueOf(row3[3]));
                        regFncSrv.setIcon(String.valueOf(row3[4]));
                        regFncSrv.setFlgMenu(String.valueOf(row3[5]));
                        regFncSrv.setFlgControl(String.valueOf(row3[6]));
                        regFncSrv.setRoute(String.valueOf(row3[8]));

                        lstHijosSrv.add(regFncSrv);
                    }
                    regFncDet.setServicios(lstHijosSrv);
                    lstFuncHijos.add(regFncDet);
                }
                regFnc.setDetalle(lstFuncHijos);

                lstFuncPadres.add(regFnc);
            }

            registro.setPerfil(lstRegPerfiles);
            registro.setFuncionalidades(lstFuncPadres);

            lstResumen.add(registro);
        }

        response.put("datos", lstResumen);
        // JEFE DE AREA
        List<JefeArea> jefaturas = jefeAreaService.buscaJefes(user.getPersona().getId(), 1);
        List<usuarioJefe> jefes = new ArrayList<>();
        for (JefeArea item : jefaturas) {
            usuarioJefe jefe = new usuarioJefe();
            jefe.setId(item.getId());
            jefe.setId_area(item.getIdArea());
            jefe.setId_usuario(item.getIdUsuario());
            jefe.setCreate_date(item.getCreateDate());
            jefe.setCreate_user(item.getCreateUser());
            jefe.setEstado(item.getEstado());
            jefe.setUpdate_user(item.getUpdateUser());
            jefe.setUpdate_date(item.getUpdateDate());

            if (item.getArea() != null) {
                usuarioAreaDTO v = new usuarioAreaDTO();
                Integer idArea = item.getArea().getId();
                v.setId(idArea);
                v.setId_gerencia(item.getArea().getIdGerencia());
                v.setNombre(item.getArea().getNombre());
                v.setEstado(item.getArea().getEstado());
                jefe.setArea(v);
            }

            jefes.add(jefe);
        }
        response.put("jefes", jefes);
        // PERSONA
        Persona p = user.getPersona();
        usuarioPersona2 per = new usuarioPersona2();
        per.setId(p.getId());
        per.setId_tipo_persona(p.getTipoPer().getIdTipoPer());
        per.setId_tipo_documento(p.getTipoDoc().getIdTipDoc());
        per.setDocumento(p.getDocumento());
        per.setTratamiento(p.getAtencion().getId());
        per.setId_datos_persona_natural(p.getPerNat().getIdPerNat());
        per.setEstado(p.getEstado());
        per.setVisible(p.getVisible());
        per.setCreate_user(p.getCreUser());
        per.setCreate_date(p.getCreDate());
        per.setUpdate_user(p.getUpdUser());
        per.setUpdate_date(p.getUpdDate());

        List<usuarioRelac> relaciones = new ArrayList<>();
        List<Relacion> lstRelac = p.getRelaciones();
        for (Relacion r : lstRelac) {
            usuarioRelac rc = new usuarioRelac();
            rc.setId(r.getIdRel());
            rc.setId_tipo_relacion(r.getTipoRel().getIdTipoRel());
            rc.setId_persona(r.getPersona().getId());
            rc.setId_area(r.getIdArea());
            rc.setEstado(r.getEstado());
            rc.setCreate_user(r.getCreateUser());
            rc.setCreate_date(r.getCreateDate());
            rc.setUpdate_user(r.getUpdateUser());
            rc.setUpdate_date(r.getUpdateDate());

            if (r.getArea() != null) {
                usuarioAreaDTO a = new usuarioAreaDTO();
                Areas datAreas = r.getArea();
                a.setId(datAreas.getId());
                a.setId_gerencia(datAreas.getIdGerencia());
                a.setNombre(datAreas.getNombre());
                a.setEstado(datAreas.getEstado());
                rc.setArea(a);
            }
            relaciones.add(rc);
        }
        per.setRelaciones(relaciones);
        response.put("person", per);
        // USUARIO
        usuarioDetalleDTO usuario = new usuarioDetalleDTO();
        usuario.setId(user.getId());
        usuario.setUsuario(user.getUsuario());
        usuario.setId_persona(user.getPersona().getId());
        usuario.setSeC_PASSWORD(user.getSecPassword());
        usuario.setTipO_USUARIO(user.getTipoUsuario());
        usuario.setTipO_USUARIO_DELTA(user.getTipoUsuarioDelta());
        usuario.setNuM_INTENTOS(user.getNumIntentos());
        usuario.setFeC_INGRESO(user.getFecIngreso());
        usuario.setUsR_INGRESO(user.getUsrIngreso());
        usuario.setCoD_EST_USUARIO(user.getCodEstUsuario());
        usuario.setUsR_ULT_MOD(user.getUsrUltMod());
        usuario.setFeC_ULT_MOD(user.getFecUltMod());
        usuario.setAutorizado(user.getAutorizado());
        usuario.setEnroladO_SERVICE(user.getEnroladoService());
        response.put("user", usuario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

class usuarioPersona2 {
    private Long id;
    private Long id_tipo_persona;
    private Long id_tipo_documento;
    private String documento;
    private Integer tratamiento;
    private Long id_datos_persona_natural;
    private Long id_datos_persona_juridica;
    private Integer estado;
    private Integer visible;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private usuarioTipoDoc tipoDocumento;
    private usuarioPerNat datosNatural;
    private usuarioPerNat datosJuridica;
    private List<usuarioRelac> relaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tipo_persona() {
        return id_tipo_persona;
    }

    public void setId_tipo_persona(Long id_tipo_persona) {
        this.id_tipo_persona = id_tipo_persona;
    }

    public Long getId_tipo_documento() {
        return id_tipo_documento;
    }

    public void setId_tipo_documento(Long id_tipo_documento) {
        this.id_tipo_documento = id_tipo_documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Integer tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Long getId_datos_persona_natural() {
        return id_datos_persona_natural;
    }

    public void setId_datos_persona_natural(Long id_datos_persona_natural) {
        this.id_datos_persona_natural = id_datos_persona_natural;
    }

    public Long getId_datos_persona_juridica() {
        return id_datos_persona_juridica;
    }

    public void setId_datos_persona_juridica(Long id_datos_persona_juridica) {
        this.id_datos_persona_juridica = id_datos_persona_juridica;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public usuarioTipoDoc getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(usuarioTipoDoc tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public usuarioPerNat getDatosNatural() {
        return datosNatural;
    }

    public void setDatosNatural(usuarioPerNat datosNatural) {
        this.datosNatural = datosNatural;
    }

    public usuarioPerNat getDatosJuridica() {
        return datosJuridica;
    }

    public void setDatosJuridica(usuarioPerNat datosJuridica) {
        this.datosJuridica = datosJuridica;
    }

    public List<usuarioRelac> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<usuarioRelac> relaciones) {
        this.relaciones = relaciones;
    }
}

class relacDTO {
    private Long idTipoRel;
    private String descrip;
    private Date fecIni;
    private Date fecFin;
    private Long vencimiento;

    public relacDTO(long idTipoRel, String descrip, Date fecIni, Date fecFin, Long vencimiento) {
        super();
        this.idTipoRel = idTipoRel;
        this.descrip = descrip;
        this.fecIni = fecIni;
        this.fecFin = fecFin;
        this.vencimiento = vencimiento;
    }

    public Long getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Long vencimiento) {
		this.vencimiento = vencimiento;
	}

    public Date getFecFin() {
		return fecFin;
	}

	public void setFecFin(Date fecFin) {
		this.fecFin = fecFin;
	}

	public Long getIdTipoRel() {
        return idTipoRel;
    }

    public void setIdTipoRel(Long idTipoRel) {
        this.idTipoRel = idTipoRel;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    @Override
    public String toString() {
        return "relacDTO{" +
                "idTipoRel=" + idTipoRel +
                ", descrip='" + descrip + '\'' +
                ", fecIni=" + fecIni +
                ", fecFin=" + fecFin +
                '}';
    }

}