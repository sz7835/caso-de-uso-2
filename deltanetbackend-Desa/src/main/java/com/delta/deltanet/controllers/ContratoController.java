package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.*;
import com.delta.deltanet.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/contrato")
public class ContratoController {

    @Autowired
    public IContrato2024Service contratoService;
    @Autowired
    public IRelacionService relacionService;
    @Autowired
    public ITipoContrato2024Service tipoContratoService;
    @Autowired
    public ITipoServicioService tipoServicioService;
    @Autowired
    public IFormaPagoService formaPagoService;
    @Autowired
    public IPersonaService perService;
    @Autowired
    IS3Service is3Service;
    @Autowired
    private IRepArchivoService repArchivoService;
    @Autowired
    private IContratoContactoService contratoContactoService;
    @Autowired
    private IRepArchivoFuncService archivoFuncService;
    @Autowired
    private IRepArchivoFormatoService archivoFormatoService;
    @Autowired
    private IRepArchivoEtiquetaService archivoEtiquetaService;
    @Autowired
    private ICronogramaModalidadService modalidadService;
    @Autowired
    private PerfilService profileService;
	@Autowired
	public ISisParamService parametroService;
    @Autowired
    private AdmFolderServiceImpl folderService;
    @Autowired
    private ModalidadService modalidadServ;

    @Autowired
    private ContratoService contratoServiceSecundary;
    @Autowired
    private IContratoOutsourcingService contratoOutsourcingService;
    @Autowired
    private INodoTipoService nodoTipoService;

    @Autowired
    public ITipoRelacionService tipRelService;
    
    @Autowired
    public IRelacionService relService;

    @Autowired
    public AreasServiceImpl areasService;
    
	@Value("${ruc}")
	private String RUC;

    @GetMapping("/busCron")
    public ResponseEntity<?> busCron(
			@RequestParam(required = false, name = "descrip") String descrip,
			@RequestParam(required = false, name = "periodoIni") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni,
			@RequestParam(required = false, name = "periodoFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
			@RequestParam(required = false, name = "estado") Long estado,
			@RequestParam(required = false, name = "numDias") Integer nDias,
			@RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
			@RequestParam(value = "column", required = false) Integer column,
			@RequestParam(value = "dir", required = false) String dir, Pageable pageable) {
		Map<String, Object> response = new HashMap<>();

		// String strEtiqueta = "CONTRATO_COM_ALERTA_VCMTO";
		// SisParam param = parametroService.buscaEtiqueta(strEtiqueta);
		// if (param == null) {
		// 	response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
		// 	return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		// }

		// try {
		// 	nDias = Integer.valueOf(param.getValor());
		// } catch (Exception x) {
		// 	response.put("mensaje", "el parametro: [" + strEtiqueta + "] no es un numero valido");
		// 	return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		// }

		if (fecIni != null || fecFin != null) {
			if (fecIni == null) {
				response.put("mensaje", "Se espera la fecha de inicio, para completar el rango de fechas de búsqueda.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (fecFin == null) {
				response.put("mensaje", "Se espera la fecha de fin, para completar el rango de fechas de búsqueda.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}

		if (swPag == 0) {
			List<lstContract> lstFinal = contratoContactoService.listadoFiltrado(descrip, fecIni, fecFin, estado, nDias);
			return ResponseEntity.ok(lstFinal);
		} else {
			Page<lstContract> lstFinal = contratoContactoService.listadoFiltrado(descrip, fecIni,fecFin, estado, nDias, column, dir, pageable);
			response.put("datos", lstFinal.getContent());
			response.put("totRegs", lstFinal.getTotalElements());
			response.put("totPags", lstFinal.getTotalPages());
			return ResponseEntity.ok(response);
		}
    }

    @GetMapping("/cronoModalidad/index")
    public ResponseEntity<?> modalidadIndex() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CronogramaModalidad> listado = modalidadService.findAll();
            response.put("message", "Se retorna listado de registros");
            response.put("modalidades", listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/cronoModalidad/show")
    public ResponseEntity<?> modalidadShow(@RequestParam(name = "idModalidad") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            CronogramaModalidad registro = modalidadService.findById(id);
            if (registro == null) {
                response.put("message", "La modalidad con id [" + id + "] no se encuentra registrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            response.put("message", "Se retorna el registro requerido");
            response.put("modalidad", registro);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/cronoModalidad/create")
    public ResponseEntity<?> modalidadCreate(@RequestParam(name = "descrip") String nombre,
                                             @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        String nom = nombre != null ? nombre.trim() : "";
        if (nom.isEmpty()) {
            response.put("message", "La descripción es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nom.matches("\\d+")) {
            response.put("message", "La descripción no puede estar compuesta únicamente por números");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            response.put("message", "La descripción solo puede contener letras, números y espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Verificar duplicados activos
        List<CronogramaModalidad> activos = modalidadService.findActive();
        boolean existe = activos.stream().anyMatch(m -> m.getNombre() != null && m.getNombre().trim().equalsIgnoreCase(nom));
        if (existe) {
            response.put("message", "Ya existe un registro activo con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            CronogramaModalidad reg = new CronogramaModalidad();
            reg.setNombre(nom);
            reg.setEstado(1);
            reg.setUsrCreate(usuario);
            reg.setFecCreate(new Date());
            modalidadService.save(reg);
            response.put("message", "El registro fue guardado exitósamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/cronoModalidad/update")
    public ResponseEntity<?> modalidadUpdate(@RequestParam(name = "idModalidad") Long id,
                                             @RequestParam(name = "descrip") String nombre,
                                             @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        String nom = nombre != null ? nombre.trim() : "";
        if (nom.isEmpty()) {
            response.put("message", "La descripción es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (nom.matches("\\d+")) {
            response.put("message", "La descripción no puede estar compuesta únicamente por números");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!nom.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            response.put("message", "La descripción solo puede contener letras, números y espacios.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        // Verificar duplicados activos excluyendo el actual
        List<CronogramaModalidad> activos = modalidadService.findActive();
        boolean existe = activos.stream().anyMatch(m -> m.getNombre() != null && m.getNombre().trim().equalsIgnoreCase(nom) && !m.getId().equals(id));
        if (existe) {
            response.put("message", "Ya existe un registro activo con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            CronogramaModalidad reg = modalidadService.findById(id);
            if (reg == null) {
                response.put("message", "La modalidad con id [" + id + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setNombre(nom);
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            modalidadService.save(reg);
            response.put("message", "El registro se actualizó exitósamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/cronoModalidad/chgEstado")
    public ResponseEntity<?> modalidadChgEstado(@RequestParam(name = "idModalidad") Long id,
            @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            CronogramaModalidad reg = modalidadService.findById(id);
            if (reg == null) {
                response.put("message", "La modalidad con id [" + id + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = reg.getEstado() == 1 ? "desactivó" : "activó";
            reg.setEstado(reg.getEstado() == 1 ? 0 : 1);
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            modalidadService.save(reg);
            response.put("message", "El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipoServicio/index")
    public ResponseEntity<?> tipoSrvIndex() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoServicio> listado = tipoServicioService.findAll();
            response.put("message", "Se retorna listado de registros");
            response.put("tipoServicio", listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipoServicio/create")
    public ResponseEntity<?> tipoSrvCreate() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TipoContrato> list = tipoContratoService.findAll();
            response.put("message", "Se retorna listado de registros");
            response.put("list", list);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/tipoServicio/show")
    public ResponseEntity<?> tipoSrvShow(@RequestParam(name = "idTipoServicio") Long idTipo) {
        Map<String, Object> response = new HashMap<>();
        try {
            TipoServicio registro = tipoServicioService.findById(idTipo);
            if (registro == null) {
                response.put("message", "El tipo de servicio con id [" + idTipo + "] no se encuentra registrado");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            List<TipoContrato> list = tipoContratoService.findAll();
            response.put("list", list);
            response.put("tipoServicio", registro);
            response.put("message", "Se retorna el registro requerido");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoServicio/create")
    public ResponseEntity<?> tipoSrvCreate(@RequestParam(name = "descrip") String descrip,
    		@RequestParam(name = "id_tipo") Long id_tipo,
            @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        TipoContrato item = tipoContratoService.findById(id_tipo);
        if (item == null ) {
            response.put("message", "El tipo de contrato es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            if (descrip == null || descrip.isEmpty()) {
                response.put("message", "El parámetro [descrip] debe tener valor.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            TipoServicio reg = new TipoServicio();
            reg.setDescrip(descrip);
            reg.setTipo(item);
            reg.setEstado(1);
            reg.setUsrCreate(usuario);
            reg.setFecCreate(new Date());
            tipoServicioService.save(reg);
            response.put("message", "El registro fue satisfactorio");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoServicio/update")
    public ResponseEntity<?> tipoSrvUpdate(@RequestParam(name = "idTipoServicio") Long idtipo,
    		@RequestParam(name = "id_tipo") Long id_tipo,
            @RequestParam(name = "descrip") String descrip,
            @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        TipoContrato item = tipoContratoService.findById(id_tipo);
        if (item == null ) {
            response.put("message", "El tipo de contrato es obligatorio");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            TipoServicio reg = tipoServicioService.findById(idtipo);
            if (reg == null) {
                response.put("message", "El tipo de servicio con id [" + idtipo + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setTipo(item);
            reg.setDescrip(descrip);
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            tipoServicioService.save(reg);
            response.put("message", "El registro se actualizó satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/tipoServicio/chgEstado")
    public ResponseEntity<?> tipoSrvChgEstado(@RequestParam(name = "idTipoServicio") Long idTipo,
            @RequestParam(name = "usuario") String usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            TipoServicio reg = tipoServicioService.findById(idTipo);
            if (reg == null) {
                response.put("message", "El tipo de servicio con id [" + idTipo + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = reg.getEstado() == 1 ? "desactivó" : "activó";
            reg.setEstado(reg.getEstado() == 1 ? 0 : 1);
            reg.setUsrUpdate(usuario);
            reg.setFecUpdate(new Date());
            tipoServicioService.save(reg);
            response.put("message", "El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/deleteContacto")
    public ResponseEntity<?> deleteContacto(@RequestParam(name = "idContrato") Long idContrato,
            @RequestParam(name = "idContacto") Long idContacto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ContratoContacto> cc = contratoContactoService.busca(idContrato, idContacto);
            if (cc.isEmpty()) {
                response.put("message", "El contacto [" + idContacto + "] del contrato [" + idContrato
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            contratoContactoService.delete(cc.get().getIdContratoContacto());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar el contacto [" + idContacto + "] del contrato [" + idContrato + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino el contacto [" + idContacto + "] del contrato [" + idContrato + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteAdjunto")
    public ResponseEntity<?> deleteAdjunto(@RequestParam(name = "idAdjunto") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivo> f = repArchivoService.getArchivo(id);
            if (f.isEmpty()) {
                response.put("message", "El id del adjunto [" + id + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            repArchivoService.delete(id);
        } catch (Exception e) {
            response.put("message", "Error al eliminar el adjunto [" + id + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        response.put("message", "Se elimino el adjunto [" + id + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> grabaContrato( @RequestParam(name = "idRelacion") Long idRelacion,
                                            @RequestParam(name = "idTipoContrato") Long idTipoContrato,
                                            @RequestParam(name = "idTipoServicio") Long idTipoServicio,
                                            @RequestParam(name = "idFormaPago") Long idFormaPago,
                                            @RequestParam(required = false, name = "descripcion") String descripcion,
                                            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
                                            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
                                            @RequestParam(required = false, name = "monto") Double monto,
                                            @RequestParam(required = false, name = "hes") Long hes,
                                            @RequestParam(required = false, name = "files") MultipartFile[] files,
                                            @RequestParam(required = false, name = "fncfiles") String[] funcs,
                                            @RequestParam(name = "createUser") String createUser,
                                            @RequestParam(required = false, name = "contactos") String[] contactos) {
        Map<String, Object> response = new HashMap<>();
        String funciones[][] = null;
        int pos = 0;
        if (files != null) {
            funciones = new String[funcs.length][2];
            for (MultipartFile f : files) {
                funciones[pos][0] = f.getOriginalFilename();
                funciones[pos][1] = funcs[pos];
                pos++;
            }
        }
        Relacion relacion = relacionService.buscaId(idRelacion);
        if (relacion == null) {
            response.put("message", "No existe la relación enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoRelacion tipoRelacion = relacion.getTipoRel();
        if (tipoRelacion.getIdTipoRel() != 1 && tipoRelacion.getIdTipoRel() != 2) {
            response.put("message", "La relación enviada debe ser de tipo cliente (1 o 2).");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoContrato tipoContrato = tipoContratoService.findById(idTipoContrato);
        if (tipoContrato == null) {
            response.put("message", "No existe el tipo de contrato enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoServicio tipoServicio = tipoServicioService.findById(idTipoServicio);
        if (tipoServicio == null) {
            response.put("message", "No existe el tipo de servicio enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        FormaPago formaPago = formaPagoService.findById(idFormaPago);
        if (formaPago == null) {
            response.put("message", "No existe la forma de pago enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Contrato2024 contrato = new Contrato2024();
        try {
            contrato.setRelacion(relacion);
            contrato.setTipoContrato(tipoContrato);
            NodoTipo tipoPersona = null;
            if (tipoRelacion.getIdTipoRel() == 1L || tipoRelacion.getIdTipoRel() == 2L) {
                tipoPersona = nodoTipoService.findById(3L);
            } else {
                tipoPersona = nodoTipoService.findById(2L);
            }
            contrato.setTipoPersona(tipoPersona);
            String strEtiqueta = "CONTRATO_COM_ALERTA_VCMTO";
            SisParam param = parametroService.buscaEtiqueta(strEtiqueta);
            if (param == null) {
                response.put("message", "no se encuentra el parámetro: [" + strEtiqueta + "]");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setVencimiento(Integer.valueOf(param.getValor()));
            contrato.setTipoServicio(tipoServicio);
            contrato.setFormaPago(formaPago);
            contrato.setDescripcion(descripcion);
            contrato.setFecInicio(fecIni);
            contrato.setFecFin(fecFin);
            contrato.setMonto(monto);
            contrato.setHes(hes);
            contrato.setEstado(3L); // 3:creado
            contrato.setCreateUser(createUser);
            contrato.setCreateDate(new Date());

            contratoService.Save(contrato);
        } catch (Exception e) {
            response.put("message", "Se produjo un error en la creación del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();

            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + contrato.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(), contrato.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = null;
                    for (String[] d : funciones) {
                        if (d[0].equals(archivo))
                            tipoArchivo = Long.valueOf(d[1]);
                    }

                    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                    String key = String.format("%s.%s", UUID.randomUUID(), extension);
                    Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
                    if (fext.isEmpty()) {
                        msgValidacion = "La extensión del archivo [" + extension + "] no se encuentra registrada.";
                    }
                    if (msgValidacion.isEmpty())
                        idFormato = fext.get().getIdArchivoFormato();

                    Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(tipoArchivo);
                    if (reg.isEmpty()) {
                        msgValidacion = "El tipo de archivo [" + tipoArchivo + "] enviado no esta registrado.";
                    }
                    ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                    if (ret.getStatusCodeValue() == 200) {
                        HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                        String url = dsa.get("url");
                        // filenames.add(file.getOriginalFilename() + " subido correctamente.");
                        RepArchivo adjunto = new RepArchivo();
                        adjunto.setIdFolder(folder.getId());
                        adjunto.setIdModuloFuncionalidad(tipoArchivo);
                        adjunto.setNombre(file.getOriginalFilename());
                        adjunto.setIdArchivoFormato(idFormato);
                        adjunto.setIdtabla(contrato.getId());
                        adjunto.setTabla("administracion_contratos");
                        adjunto.setEncriptado(key);
                        adjunto.setUrl(url);
                        adjunto.setUsrCreate(createUser);
                        adjunto.setFecCreate(new Date());

                        repArchivoService.save(adjunto);

                    } else {
                        filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                    }
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de contactos
        String msgContactos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (contactos != null) {
            try {
                List<String> lstContactos = new ArrayList<>();
                for (String cto : contactos) {
                    Optional<ContratoContacto> cc = contratoContactoService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoContacto regContacto = new ContratoContacto();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdContacto(Long.valueOf(cto));
                        contratoContactoService.save(regContacto);
                        lstContactos.add(cto);
                    }
                }
                response.put("contactos", lstContactos);
                if (!lstCtosError.isEmpty())
                    response.put("contactos no registrados", lstCtosError);
            } catch (Exception e) {
                msgContactos = "Error en el registro de contactos del contrato. " + e.getMessage();
            }
        }

        response.put("message", "Se creo el contrato satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContactos.isEmpty())
            response.put("message_contacts", msgContactos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/createEspecial")
    public ResponseEntity<?> createContractEspecial(@RequestParam(name = "idRelacion") Long idRelacion,
            @RequestParam(name = "idTipoContrato") Long idTipoContrato,
            @RequestParam(name = "idTipoServicio") Long idTipoServicio,
            @RequestParam(name = "idFormaPago") Long idFormaPago,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
            @RequestParam(required = false, name = "monto") Double monto,
            @RequestParam(required = false, name = "hes") Long hes,
            @RequestParam(name = "vencimiento") Integer vencimiento,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(required = false, name = "contactos") String[] contactos) {
        Map<String, Object> response = new HashMap<>();
        String funciones[][] = null;
        int pos = 0;
        if (files != null) {
            funciones = new String[funcs.length][2];
            for (MultipartFile f : files) {
                funciones[pos][0] = f.getOriginalFilename();
                funciones[pos][1] = funcs[pos];
                pos++;
            }
        }
        Relacion relacion = relacionService.buscaId(idRelacion);
        if (relacion == null) {
            response.put("mensaje", "No existe la relación enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoRelacion tipoRelacion = relacion.getTipoRel();
        if (tipoRelacion.getIdTipoRel() != 1 && tipoRelacion.getIdTipoRel() != 2 && tipoRelacion.getIdTipoRel() != 6) {
            response.put("mensaje", "La relación enviada tiene un tipo incorrecto.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoContrato tipoContrato = tipoContratoService.findById(idTipoContrato);
        if (tipoContrato == null) {
            response.put("mensaje", "No existe el tipo de contrato enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoServicio tipoServicio = tipoServicioService.findById(idTipoServicio);
        if (tipoServicio == null) {
            response.put("mensaje", "No existe el tipo de servicio enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        FormaPago formaPago = formaPagoService.findById(idFormaPago);
        if (formaPago == null) {
            response.put("mensaje", "No existe la forma de pago enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long type = 0L;
        if(tipoRelacion.getIdTipoRel() == 1L || tipoRelacion.getIdTipoRel() == 2L) {
	        type = 3L;
        }else {
	        type = 2L;
        }
        NodoTipo tipoPersona = nodoTipoService.findById(type);
        if (tipoPersona == null) {
            response.put("mensaje", "No existe la forma de pago enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Contrato2024 contrato = new Contrato2024();
        try {
            contrato.setRelacion(relacion);
            contrato.setTipoPersona(tipoPersona);
            contrato.setTipoContrato(tipoContrato);
            contrato.setTipoServicio(tipoServicio);
            contrato.setFormaPago(formaPago);
            contrato.setDescripcion(descripcion);
            contrato.setFecInicio(fecIni);
            contrato.setFecFin(fecFin);
            contrato.setMonto(monto);
            contrato.setHes(hes);
            contrato.setEstado(3L); // 3:creado
            contrato.setVencimiento(vencimiento);
            contrato.setCreateUser(createUser);
            contrato.setCreateDate(new Date());

            contratoService.Save(contrato);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la creación del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();

            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + contrato.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            contrato.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = null;
                    for (String[] d : funciones) {
                        if (d[0].equals(archivo))
                            tipoArchivo = Long.valueOf(d[1]);
                    }

                    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                    String key = String.format("%s.%s", UUID.randomUUID(), extension);
                    Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
                    if (fext.isEmpty()) {
                        msgValidacion = "La extensión del archivo [" + extension + "] no se encuentra registrada.";
                    }
                    if (msgValidacion.isEmpty())
                        idFormato = fext.get().getIdArchivoFormato();

                    Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(tipoArchivo);
                    if (reg.isEmpty()) {
                        msgValidacion = "El tipo de archivo [" + tipoArchivo + "] enviado no esta registrado.";
                    }
                    ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                    if (ret.getStatusCodeValue() == 200) {
                        HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                        String url = dsa.get("url");
                        // filenames.add(file.getOriginalFilename() + " subido correctamente.");
                        RepArchivo adjunto = new RepArchivo();
                        adjunto.setIdFolder(folder.getId());
                        adjunto.setIdModuloFuncionalidad(tipoArchivo);
                        adjunto.setNombre(file.getOriginalFilename());
                        adjunto.setIdArchivoFormato(idFormato);
                        adjunto.setIdtabla(contrato.getId());
                        adjunto.setTabla("administracion_contratos");
                        adjunto.setEncriptado(key);
                        adjunto.setUrl(url);
                        adjunto.setUsrCreate(createUser);
                        adjunto.setFecCreate(new Date());

                        repArchivoService.save(adjunto);

                    } else {
                        filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                    }
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de contactos
        String msgContactos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (contactos != null) {
            try {
                List<String> lstContactos = new ArrayList<>();
                for (String cto : contactos) {
                    Optional<ContratoContacto> cc = contratoContactoService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoContacto regContacto = new ContratoContacto();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdContacto(Long.valueOf(cto));
                        contratoContactoService.save(regContacto);
                        lstContactos.add(cto);
                    }
                }
                response.put("contactos", lstContactos);
                if (!lstCtosError.isEmpty())
                    response.put("contactos no registrados", lstCtosError);
            } catch (Exception e) {
                msgContactos = "Error en el registro de contactos del contrato. " + e.getMessage();
            }
        }

        response.put("message", "Se creo el contrato satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContactos.isEmpty())
            response.put("message_contacts", msgContactos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchProfile")
    public Map<String, Object> buscarPorNombreYEstado(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {
        List<Perfil> resultado = profileService.buscarPorNombreYEstado(nombre, estado);

        Map<String, Object> response = new HashMap<>();
        response.put("data", resultado);

        return response;
    }
    
    @PostMapping("/createOutsourcing")
    public ResponseEntity<?> createContractOutsourcing(@RequestParam(required = false, name = "idRelacion") Long idRelacion,
    		@RequestParam(name = "idPersona") Long idPersona,
    		@RequestParam(name = "idCliente") Long idCliente,
    		@RequestParam(name = "idModalidad") Long idModalidad,
    		@RequestParam(name = "puesto") String puesto,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
            @RequestParam(required = false, name = "monto") Double monto,
            @RequestParam(required = false, name = "hes") Long hes,
            @RequestParam(name = "vencimiento") Integer vencimiento,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(required = false, name = "contactos") String[] contactos) {
        Map<String, Object> response = new HashMap<>();
        String funciones[][] = null;
        int pos = 0;
        if (files != null) {
            funciones = new String[funcs.length][2];
            for (MultipartFile f : files) {
                funciones[pos][0] = f.getOriginalFilename();
                funciones[pos][1] = funcs[pos];
                pos++;
            }
        }
        Relacion relacion = relacionService.buscaId(idRelacion);
        if (relacion == null) {
            List<Object> pers = perService.findByPersonaJurD(2L, 4L, RUC);
            Object[] per = (Object[]) pers.get(0);
            Persona persona = perService.buscarId(idPersona);
            Persona prove = perService.buscarId((Long) per[0]);
            if(persona == null){
                response.put("message","No se encuentra la persona enviada. ID: ".concat(idPersona.toString()));
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(prove == null){
                response.put("message","No se encuentra el proveedor");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            relacion = new Relacion();
            Relacion registro2 = new Relacion();
            TipoRelacion tipRel = tipRelService.buscaId(4L);
            TipoRelacion inver = tipRelService.buscaId(tipRel.getRelacionback());
            Long obtenerTipo = tipRel.getOrigen();
            Long obtenerTipo2 = inver.getOrigen();
            relacion.setTipoRel(tipRel);
            OrgAreas areas = persona.getArea();
            Optional<Areas> ad = areasService.getById(areas.getId().intValue());
            Areas area = ad.get();
            if (area != null) {
            	relacion.setArea(area);
            	registro2.setArea(area);
            }
            relacion.setPersonaD(prove);
            relacion.setPersona(persona);
            relacion.setFecIni(fecIni);
            relacion.setFecFin(fecFin);
            relacion.setEstado(1L);
            relacion.setCreateUser(createUser);
            relacion.setCreateDate(new Date());
            relService.save(relacion);
            registro2.setTipoRel(inver);
            registro2.setIdReverse(relacion.getIdRel());
            registro2.setPersona(prove);
            registro2.setPersonaD(persona);
            registro2.setFecIni(fecIni);
            registro2.setFecFin(fecFin);
            registro2.setEstado(1L);
            registro2.setCreateUser(createUser);
            registro2.setCreateDate(new Date());
            NodoTipo nodoTipo = nodoTipoService.findById(obtenerTipo);
            NodoTipo nodoTipo2 = nodoTipoService.findById(obtenerTipo2);
            relacion.setNodoTipo(nodoTipo);
            registro2.setNodoTipo(nodoTipo2);
            relService.save(registro2);
            relacion.setIdReverse(registro2.getIdRel());
            relacion = relService.save(relacion);
        }else {
            Relacion relacionRev = relacionService.buscaId(relacion.getIdReverse());
        	relacion.setFecFin(fecFin);
        	relacionRev.setFecFin(fecFin);
        	this.relService.save(relacion);
        	this.relService.save(relacionRev);
        }
        System.out.println(relacion.getIdRel());
        ContratoOutsourcing exists = contratoOutsourcingService.findRelation(relacion.getIdRel());
        if (exists != null) {
			response.put("message", "Ya existe un contrato activo y no se puede crear otro.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
        
        TipoRelacion tipoRelacion = relacion.getTipoRel();
        if (tipoRelacion.getIdTipoRel() != 4) {
            response.put("message", "La relacion enviada tiene un tipo incorrecto.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        TipoContrato tipoContrato = tipoContratoService.findById(2L);
        if (tipoContrato == null) {
            response.put("message", "No existe el tipo de contrato enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Modalidad modalidad = modalidadServ.findById(idModalidad);
        if (modalidad == null) {
            response.put("message", "No existe el tipo de modalidad enviada.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        NodoTipo tipoPersona = nodoTipoService.findById(1L);
        if (tipoPersona == null) {
            response.put("message", "No existe el tipo de persona enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ContratoOutsourcing contrato = new ContratoOutsourcing();
        try {
            contrato.setRelacion(relacion);
            contrato.setTipoContrato(tipoContrato);
            contrato.setTipoPersona(tipoPersona);
            contrato.setDescripcion(descripcion);
            contrato.setFecInicio(fecIni);
            contrato.setFecFin(fecFin);
            contrato.setMonto(monto);
            contrato.setHes(0L);
            contrato.setIdCliente(idCliente);//buscar y actualizar cuando este linkeado en tablas
            contrato.setModalidad(modalidad);
            contrato.setPuesto(puesto);
            contrato.setVencimiento(vencimiento);
            contrato.setEstado(1L);
            contrato.setCreateUser(createUser);
            contrato.setCreateDate(new Date());

            contratoOutsourcingService.save(contrato);
        } catch (Exception e) {
            response.put("message", "Se produjo un error en la creación del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();

            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + contrato.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            contrato.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = null;
                    for (String[] d : funciones) {
                        if (d[0].equals(archivo))
                            tipoArchivo = Long.valueOf(d[1]);
                    }

                    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                    String key = String.format("%s.%s", UUID.randomUUID(), extension);
                    Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
                    if (fext.isEmpty()) {
                        msgValidacion = "La extension del archivo [" + extension + "] no se encuentra registrada.";
                    }
                    if (msgValidacion.isEmpty())
                        idFormato = fext.get().getIdArchivoFormato();

                    Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(tipoArchivo);
                    if (reg.isEmpty()) {
                        msgValidacion = "El tipo de archivo [" + tipoArchivo + "] enviado no esta registrado.";
                    }
                    ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                    if (ret.getStatusCodeValue() == 200) {
                        HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                        String url = dsa.get("url");
                        // filenames.add(file.getOriginalFilename() + " subido correctamente.");
                        RepArchivo adjunto = new RepArchivo();
                        adjunto.setIdFolder(folder.getId());
                        adjunto.setIdModuloFuncionalidad(tipoArchivo);
                        adjunto.setNombre(file.getOriginalFilename());
                        adjunto.setIdArchivoFormato(idFormato);
                        adjunto.setIdtabla(contrato.getId());
                        adjunto.setTabla("administracion_contratos");
                        adjunto.setEncriptado(key);
                        adjunto.setUrl(url);
                        adjunto.setUsrCreate(createUser);
                        adjunto.setFecCreate(new Date());

                        repArchivoService.save(adjunto);

                    } else {
                        filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                    }
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de contactos
        String msgContactos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (contactos != null) {
            try {
                List<String> lstContactos = new ArrayList<>();
                for (String cto : contactos) {
                    Optional<ContratoContacto> cc = contratoContactoService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoContacto regContacto = new ContratoContacto();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdContacto(Long.valueOf(cto));
                        contratoContactoService.save(regContacto);
                        lstContactos.add(cto);
                    }
                }
                response.put("contactos", lstContactos);
                if (!lstCtosError.isEmpty())
                    response.put("contactos no registrados", lstCtosError);
            } catch (Exception e) {
                msgContactos = "Error en el registro de contactos del contrato. " + e.getMessage();
            }
        }

        response.put("message", "Se creo el contrato satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContactos.isEmpty())
            response.put("message_contacts", msgContactos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> verReg(@RequestParam(name = "idContrato") Long idContrato) {
        Map<String, Object> response = new HashMap<>();
        Contrato2024 contrato = contratoService.findById(idContrato);
        if (contrato == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        xContrato registro = new xContrato();
        xRelacion regRel = new xRelacion();
        xPersona regPer = new xPersona();

        if (contrato.getRelacion() != null && contrato.getRelacion().getPersona() != null) {
            regPer.setIdPer(contrato.getRelacion().getPersona().getId());

            if (contrato.getRelacion().getPersona().getTipoPer() != null) {
                regPer.setIdTipoPer(contrato.getRelacion().getPersona().getTipoPer().getIdTipoPer());
            }

            if (contrato.getRelacion().getPersona().getTipoDoc() != null) {
                regPer.setIdTipoDoc(contrato.getRelacion().getPersona().getTipoDoc().getIdTipDoc());
            }

            regPer.setDocumento(contrato.getRelacion().getPersona().getDocumento());

            if (contrato.getRelacion().getPersona().getAtencion() != null) {
                regPer.setAtencion(contrato.getRelacion().getPersona().getAtencion().getAbrev());
            }

            regPer.setEstado(contrato.getRelacion().getPersona().getEstado());
            regPer.setVisible(contrato.getRelacion().getPersona().getVisible());

            if (contrato.getRelacion().getPersona().getPerNat() != null) {
                regPer.setIdPerNat(contrato.getRelacion().getPersona().getPerNat().getIdPerNat());
                regPer.setNombre(contrato.getRelacion().getPersona().getPerNat().getNombre() + " "
                        + contrato.getRelacion().getPersona().getPerNat().getApePaterno() + " "
                        + contrato.getRelacion().getPersona().getPerNat().getApeMaterno());
            }

            if (contrato.getRelacion().getPersona().getPerJur() != null) {
                regPer.setIdPerJur(contrato.getRelacion().getPersona().getPerJur().getIdPerJur());
                regPer.setRazSoc(contrato.getRelacion().getPersona().getPerJur().getRazonSocial());
            }
            regPer.setIdTitular(contrato.getRelacion().getPersona().getIdTitular());
        }

        if (contrato.getRelacion() != null) {
            regRel.setIdRel(contrato.getRelacion().getIdRel());
            regRel.setIdTiporel(contrato.getRelacion().getIdTipoRel());
            regRel.setPersona(regPer);
            regRel.setIdArea(contrato.getRelacion().getIdArea());
            regRel.setArea(contrato.getRelacion().getArea());
            regRel.setEstado(contrato.getRelacion().getEstado());
            regRel.setFecIni(contrato.getRelacion().getFecIni());
            regRel.setFecFin(contrato.getRelacion().getFecFin());
        }

        registro.setId(contrato.getId());
        registro.setRelacion(regRel);
        registro.setIdTipoContrato(contrato.getTipoContrato().getId());
        if(contrato.getTipoServicio() != null) {
            registro.setIdtipoServicio(contrato.getTipoServicio().getId());
        }
        if(contrato.getFormaPago() != null) {
            registro.setIdFormaPago(contrato.getFormaPago().getId());
        }
        registro.setDescripcion(contrato.getDescripcion());
        registro.setFecInicio(contrato.getFecInicio());
        registro.setFecFin(contrato.getFecFin());
        registro.setMonto(contrato.getMonto());
        registro.setHes(contrato.getHes());
        registro.setEstado(contrato.getEstado());
        registro.setVencimiento(contrato.getVencimiento());

        response.put("contrato", registro);

        List<TipoContrato> lstTipoCto = tipoContratoService.findAll();
        List<TipoServicio> lstTipoSrv = tipoServicioService.findAll();
        List<FormaPago> lstFormaPgo = formaPagoService.findAll();
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_contratos]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(etiqueta.get().getFolder().getId());

        response.put("TipoContrato", lstTipoCto);
        response.put("TipoServicio", lstTipoSrv);
        response.put("FormaPago", lstFormaPgo);
        response.put("TipoArchivo", lstTipoArchivo);

        List<RepArchivo> listaObj = repArchivoService.getRepArchivos(contrato.getId(), "administracion_contratos");
        List<xAdjunto> lstAdjuntos = new ArrayList<>();
        for (RepArchivo archi : listaObj) {
            Optional<RepArchivoFuncionalidad> f = archivoFuncService.busca(archi.getIdModuloFuncionalidad());
            if (f.isPresent()) {
                xAdjunto reg = new xAdjunto();
                reg.setIdArchivo(archi.getIdArchivo());
                reg.setIdFolder(archi.getIdFolder());
                reg.setIdModuloFuncionalidad(archi.getIdModuloFuncionalidad());
                reg.setDescFuncionalidad(f.get().getNombre());
                reg.setNombre(archi.getNombre());
                reg.setIdArchivoFormato(archi.getIdArchivoFormato());
                reg.setIdtabla(archi.getIdtabla());
                reg.setTabla(archi.getTabla());
                reg.setEncriptado(archi.getEncriptado());
                reg.setUrl(archi.getUrl());
                reg.setUsrCreate(archi.getUsrCreate());
                reg.setFecCreate(archi.getFecCreate());
                reg.setUsrUpdate(archi.getUsrUpdate());
                reg.setFecUpdate(archi.getFecUpdate());
                lstAdjuntos.add(reg);
            }
        }
        response.put("Adjuntos", lstAdjuntos);
        // Agregar contactos del contrato
        List<ContratoContacto> contactos = contratoContactoService.getContactos(contrato.getId());
        List<xContacto> lstContactos = new ArrayList<>();

        // Obtener idPer de la persona de la relación del contrato
        Long idPer = null;
        if (contrato.getRelacion() != null && contrato.getRelacion().getPersona() != null) {
            idPer = contrato.getRelacion().getPersona().getId();
        }

        // Buscar motivos de contactos relacionados
        List<Relacion> listado = null;
        listado = relacionService.searchRelCto(idPer);

        // Crear un mapa para asociar idPersona con su motivo correspondiente
        Map<Long, String> motivosPorPersona = new HashMap<>();
        for(Relacion rel : listado) {
            if(rel.getPersona() != null && rel.getTipoRel() != null) {
                Long idPerMot = rel.getIdPersona();
                String desMot = rel.getTipoRel().getDescrip();
                motivosPorPersona.put(idPerMot, desMot);
            }
        }
        for (ContratoContacto c : contactos) {
            xContacto x = new xContacto();
            x.setIdContratoContacto(c.getIdContratoContacto());
            x.setIdContrato(c.getIdContrato());
            x.setIdContacto(c.getIdContacto());
            x.setEstado(c.getEstado() != null ? c.getEstado().longValue() : null);
            Long idPersonaContacto = null;
            if (c.getContactPerson() != null) {
                Persona persona = c.getContactPerson();
                idPersonaContacto = persona.getId();
                x.setIdPersona(idPersonaContacto);
                x.setTipoDoc(persona.getTipoDoc() != null ? persona.getTipoDoc().getNombre() : null);
                x.setNroDoc(persona.getDocumento());
                if (persona.getPerNat() != null) {
                    x.setNombre(persona.getPerNat().getNombre() + " " + persona.getPerNat().getApePaterno() + " " + persona.getPerNat().getApeMaterno());
                } else if (persona.getPerJur() != null) {
                    x.setNombre(persona.getPerJur().getRazonSocial());
                }
            } else if (c.getIdContacto() != null) {
                Persona persona = perService.buscarId(c.getIdContacto());
                if (persona != null) {
                    idPersonaContacto = persona.getId();
                    x.setIdPersona(idPersonaContacto);
                    x.setTipoDoc(persona.getTipoDoc() != null ? persona.getTipoDoc().getNombre() : null);
                    x.setNroDoc(persona.getDocumento());
                    if (persona.getPerNat() != null) {
                        x.setNombre(persona.getPerNat().getNombre() + " " + persona.getPerNat().getApePaterno() + " " + persona.getPerNat().getApeMaterno());
                    } else if (persona.getPerJur() != null) {
                        x.setNombre(persona.getPerJur().getRazonSocial());
                    }
                }
            }
            String motivoAsignado = null;
            if (idPersonaContacto != null && motivosPorPersona.containsKey(idPersonaContacto)) {
                motivoAsignado = motivosPorPersona.get(idPersonaContacto);
            }
            x.setMotivo(motivoAsignado);
            lstContactos.add(x);
        }
        response.put("Contactos", lstContactos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/showOutsourcing")
    public ResponseEntity<?> getContratoOutsourcing(@RequestParam(name = "idContrato") Long idContrato) {
        Map<String, Object> response = new HashMap<>();
        ContratoOutsourcing contrato = contratoOutsourcingService.findById(idContrato);
        if (contrato == null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        xContrato registro = new xContrato();
        xRelacion regRel = new xRelacion();
        xPersona regPer = new xPersona();

        regPer.setIdPer(contrato.getRelacion().getPersona().getId());
        regPer.setIdTipoPer(contrato.getRelacion().getPersona().getTipoPer().getIdTipoPer());
        regPer.setIdTipoDoc(contrato.getRelacion().getPersona().getTipoDoc().getIdTipDoc());
        regPer.setDocumento(contrato.getRelacion().getPersona().getDocumento());
        regPer.setAtencion(contrato.getRelacion().getPersona().getAtencion().getAbrev());
        regPer.setEstado(contrato.getRelacion().getPersona().getEstado());
        regPer.setVisible(contrato.getRelacion().getPersona().getVisible());
        if (contrato.getRelacion().getPersona().getPerNat() != null) {
            regPer.setIdPerNat(contrato.getRelacion().getPersona().getPerNat().getIdPerNat());
            regPer.setNombre(contrato.getRelacion().getPersona().getPerNat().getNombre() + " "
                    + contrato.getRelacion().getPersona().getPerNat().getApePaterno() + " "
                    + contrato.getRelacion().getPersona().getPerNat().getApeMaterno());
        }
        if (contrato.getRelacion().getPersona().getPerJur() != null) {
            regPer.setIdPerJur(contrato.getRelacion().getPersona().getPerJur().getIdPerJur());
            regPer.setRazSoc(contrato.getRelacion().getPersona().getPerJur().getRazonSocial());
        }
        regPer.setIdTitular(contrato.getRelacion().getPersona().getIdTitular());

        regRel.setIdRel(contrato.getRelacion().getIdRel());
        regRel.setIdTiporel(contrato.getRelacion().getIdTipoRel());
        regRel.setPersona(regPer);
        regRel.setIdArea(contrato.getRelacion().getIdArea());
        regRel.setArea(contrato.getRelacion().getArea());
        regRel.setEstado(contrato.getRelacion().getEstado());
        regRel.setFecIni(contrato.getRelacion().getFecIni());
        regRel.setFecFin(contrato.getRelacion().getFecFin());

        registro.setId(contrato.getId());
        registro.setRelacion(regRel);
        registro.setIdTipoContrato(contrato.getTipoContrato().getId());
        registro.setDescripcion(contrato.getDescripcion());
        registro.setFecInicio(contrato.getFecInicio());
        registro.setFecFin(contrato.getFecFin());
        registro.setMonto(contrato.getMonto());
        registro.setIdCliente(contrato.getIdCliente());
        registro.setVencimiento(contrato.getVencimiento());
        registro.setIdModalidad(contrato.getModalidad() != null ? contrato.getModalidad().getId() : null);
        registro.setPuesto(contrato.getPuesto());
        registro.setEstado(contrato.getEstado());

        response.put("contrato", registro);

        List<RepArchivo> listaObj = repArchivoService.getRepArchivos(contrato.getId(), "administracion_contratos");
        List<xAdjunto> lstAdjuntos = new ArrayList<>();
        for (RepArchivo archi : listaObj) {
            Optional<RepArchivoFuncionalidad> f = archivoFuncService.busca(archi.getIdModuloFuncionalidad() );
            if (f.isPresent()) {
                xAdjunto reg = new xAdjunto();
                reg.setIdArchivo(archi.getIdArchivo());
                reg.setIdFolder(archi.getIdFolder());
                reg.setIdModuloFuncionalidad(archi.getIdModuloFuncionalidad());
                reg.setDescFuncionalidad(f.get().getNombre());
                reg.setNombre(archi.getNombre());
                reg.setIdArchivoFormato(archi.getIdArchivoFormato());
                reg.setIdtabla(archi.getIdtabla());
                reg.setTabla(archi.getTabla());
                reg.setEncriptado(archi.getEncriptado());
                reg.setUrl(archi.getUrl());
                reg.setUsrCreate(archi.getUsrCreate());
                reg.setFecCreate(archi.getFecCreate());
                reg.setUsrUpdate(archi.getUsrUpdate());
                reg.setFecUpdate(archi.getFecUpdate());
                lstAdjuntos.add(reg);
            }
        }
        response.put("Adjuntos", lstAdjuntos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updEstado")
    public ResponseEntity<?> updateEstado(@RequestParam(name = "idContrato") Long idContrato,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(name = "estado") Long estado) {
        Map<String, Object> response = new HashMap<>();
        Long actEstado;
        Long newEstado = 0L;
        Contrato2024 contrato = contratoService.findById(idContrato);
        if (contrato == null) {
            response.put("mensaje", "El id del contrato enviado no fue encontrado.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        actEstado = contrato.getEstado();
        if (actEstado == 3) {
            if (estado == 1 || estado == 2) {
                newEstado = estado;
            } else {
                response.put("mensaje", "El contrato tiene estado 3, se espera un nuevo estado 1 ó 2");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (actEstado == 1) {
            if (estado == 2 || estado == 4) {
                newEstado = estado;
            } else {
                response.put("mensaje", "El contrato tiene estado 1, se espera un nuevo estado 2 ó 4");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (actEstado == 2) {
            if (estado == 1 || estado == 4) {
                newEstado = estado;
            } else {
                response.put("mensaje", "El contrato tiene estado 2, se espera un nuevo estado 1 ó 4");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (actEstado == 4) {
            response.put("mensaje", "El contrato tiene estado 4, no se puede cambiar el estado.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        contrato.setEstado(newEstado);
        contrato.setUpdateUser(createUser);
        contrato.setUpdateDate(new Date());
        try {
            contratoService.Save(contrato);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la actualización del estado del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Se actualizó correctamente el estado del contrato.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update2")
    public ResponseEntity<?> update(@RequestParam(name = "idContrato") Long idContrato,
            @RequestParam(required = false, name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
            @RequestParam(required = false, name = "estado") Long estado,
            @RequestParam(required = true, name = "usuario") String usuario) {
    	Map<String, Object> response = new HashMap<>();
        boolean datos = false;
        Contrato2024 contrato = contratoService.findById(idContrato);
        if (contrato == null) {
            response.put("mensaje", "El id del contrato enviado no fue encontrado.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (fecFin != null) {
        	contrato.setFecFin(fecFin);
            datos = true;
        }
        if (estado != null) {
        	contrato.setEstado(estado);
            datos = true;
        }
        if(datos == true) {
            try {
                contrato.setUpdateUser(usuario);
                contrato.setUpdateDate(new Date());
                contratoService.Save(contrato);
            } catch (Exception e) {
                response.put("mensaje", "Se produjo un error en la actualización del contrato.");
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("mensaje", "Se actualizó correctamente el contrato.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put("mensaje", "No hay campos a actualizar");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam(name = "idContrato") Long idContrato,
            @RequestParam(required = false, name = "idRelacion") Long idRelacion,
            @RequestParam(required = false, name = "idTipoContrato") Long idTipoContrato,
            @RequestParam(required = false, name = "idTipoServicio") Long idTipoServicio,
            @RequestParam(required = false, name = "idFormaPago") Long idFormaPago,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
            @RequestParam(required = false, name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
            @RequestParam(required = false, name = "monto") Double monto,
            @RequestParam(required = false, name = "hes") Long hes,
            @RequestParam(required = false, name = "vencimiento") Integer vencimiento,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(required = false, name = "contactos") String[] contactos) {
        Map<String, Object> response = new HashMap<>();
        String funciones[][] = null;
        int pos = 0;
        if (files != null) {
            funciones = new String[funcs.length][2];
            for (MultipartFile f : files) {
                funciones[pos][0] = f.getOriginalFilename();
                funciones[pos][1] = funcs[pos];
                pos++;
            }
        }
        boolean datos = false;

        Contrato2024 contrato = contratoService.findById(idContrato);
        if (contrato == null) {
            response.put("mensaje", "El id del contrato enviado no fue encontrado.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (idRelacion != null) {
            Relacion relacion = relacionService.buscaId(idRelacion);
            if (relacion == null) {
                response.put("mensaje", "No existe la relación enviada.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setRelacion(relacion);
            datos = true;
        }

        if (idTipoContrato != null) {
            TipoContrato tipoContrato = tipoContratoService.findById(idTipoContrato);
            if (tipoContrato == null) {
                response.put("mensaje", "No existe el tipo de contrato enviado.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setTipoContrato(tipoContrato);
            datos = true;
        }

        if (idTipoServicio != null) {
            TipoServicio tipoServicio = tipoServicioService.findById(idTipoServicio);
            if (tipoServicio == null) {
                response.put("mensaje", "No existe el tipo de servicio enviado.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setTipoServicio(tipoServicio);
            datos = true;
        }

        if (idFormaPago != null) {
            FormaPago formaPago = formaPagoService.findById(idFormaPago);
            if (formaPago == null) {
                response.put("mensaje", "No existe la forma de pago enviada.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setFormaPago(formaPago);
            datos = true;
        }

        if (descripcion != null) {
            contrato.setDescripcion(descripcion);
            datos = true;
        }
        if (fecIni != null) {
            contrato.setFecInicio(fecIni);
            datos = true;
        }
        if (fecFin != null) {
            contrato.setFecFin(fecFin);
            datos = true;
        }
        if (monto != null) {
            contrato.setMonto(monto);
            datos = true;
        }
        if (hes != null) {
            contrato.setHes(hes);
            datos = true;
        }
        if (vencimiento != null && vencimiento > 0) {
            contrato.setVencimiento(vencimiento);
            datos = true;
        }


        if (!datos) {
            response.put("mensaje", "No se tiene datos para actualizar el contrato.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        contrato.setUpdateUser(createUser);
        contrato.setUpdateDate(new Date());

        try {
            contratoService.Save(contrato);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la actualización del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());

                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + contrato.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            contrato.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = null;
                    for (String[] d : funciones) {
                        if (d[0].equals(archivo))
                            tipoArchivo = Long.valueOf(d[1]);
                    }

                    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                    String key = String.format("%s.%s", UUID.randomUUID(), extension);
                    Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
                    if (fext.isEmpty())
                        msgValidacion = "La extension del archivo [" + extension + "] no se encuentra registrada.";
                    if (msgValidacion.isEmpty())
                        idFormato = fext.get().getIdArchivoFormato();

                    Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(tipoArchivo);
                    if (reg.isEmpty())
                        msgValidacion = "El tipo de archivo [" + tipoArchivo + "] enviado no esta registrado.";
                    if (msgValidacion.isEmpty()) {
                        ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                        if (ret.getStatusCodeValue() == 200) {
                            HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                            String url = dsa.get("url");
                            // filenames.add(file.getOriginalFilename() + " subido correctamente.");
                            RepArchivo adjunto = new RepArchivo();
                            adjunto.setIdFolder(folder.getId());
                            adjunto.setIdModuloFuncionalidad(tipoArchivo);
                            adjunto.setNombre(file.getOriginalFilename());
                            adjunto.setIdArchivoFormato(idFormato);
                            adjunto.setIdtabla(contrato.getId());
                            adjunto.setTabla("administracion_contratos");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate(createUser);
                            adjunto.setFecCreate(new Date());

                            repArchivoService.save(adjunto);

                        } else {
                            filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                        }
                    } else {
                        filenames.add(file.getOriginalFilename() + " Error: " + msgValidacion);
                    }
                }
                response.put("message_files", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de contactos
        String msgContactos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (contactos != null) {
            try {
                List<String> lstContactos = new ArrayList<>();
                for (String cto : contactos) {
                    Optional<ContratoContacto> cc = contratoContactoService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoContacto regContacto = new ContratoContacto();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdContacto(Long.valueOf(cto));
                        regContacto.setEstado(1); // Estado activo por defecto
                        regContacto.setCreateUser(createUser);
                        regContacto.setCreateDate(new Date());
                        contratoContactoService.save(regContacto);
                        lstContactos.add(cto);
                    }
                }
                response.put("contactos", lstContactos);
                if (!lstCtosError.isEmpty())
                    response.put("contactos no registrados", lstCtosError);
            } catch (Exception e) {
                msgContactos = "Error en el registro de contactos del contrato. " + e.getMessage();
            }
        }

        response.put("mensaje", "Se actualizó correctamente el contrato.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContactos.isEmpty())
            response.put("message_contacts", msgContactos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updateOutsourcing")
    public ResponseEntity<?> update(@RequestParam(required = false, name = "idContrato") Long idContrato,
    		@RequestParam(required = false, name = "idCliente") Long idCliente,
            @RequestParam(required = false, name = "idModalidad") Long idModalidad,
            @RequestParam(required = false, name = "puesto") String puesto,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "fecInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecIni,
            @RequestParam(required = false, name = "fecFin") @DateTimeFormat(pattern = "dd/MM/yyyy") Date fecFin,
            @RequestParam(required = false, name = "monto") Double monto,
            @RequestParam(required = false, name = "vencimiento") Integer vencimiento,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(required = false, name = "fncfiles") String[] funcs
            /*@RequestParam(required = false, name = "contactos") String[] contactos*/) {
        Map<String, Object> response = new HashMap<>();
        String funciones[][] = null;
        int pos = 0;
        if (files != null) {
            funciones = new String[funcs.length][2];
            for (MultipartFile f : files) {
                funciones[pos][0] = f.getOriginalFilename();
                funciones[pos][1] = funcs[pos];
                pos++;
            }
        }
        boolean datos = false;

        ContratoOutsourcing contrato = contratoOutsourcingService.findById(idContrato);
        if (contrato == null) {
            response.put("mensaje", "El id del contrato enviado no fue encontrado.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (idCliente != null) {
            contrato.setIdCliente(idCliente);
            datos = true;
        }

        if (idModalidad != null) {
            Modalidad modalidad = modalidadServ.findById(idModalidad);
            if (modalidad == null) {
                response.put("mensaje", "No existe el tipo de modalidad enviado.");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            contrato.setModalidad(modalidad);
            datos = true;
        }

        if (puesto != null) {
            contrato.setPuesto(puesto);
            datos = true;
        }

        if (descripcion != null) {
            contrato.setDescripcion(descripcion);
            datos = true;
        }
        if (fecIni != null) {
            contrato.setFecInicio(fecIni);
            datos = true;
        }
        if (fecFin != null) {
            contrato.setFecFin(fecFin);
            Relacion relacion = contrato.getRelacion();
            Relacion relacionRev = this.relacionService.buscaId(relacion.getIdReverse());
            if(relacionRev != null){
	            relacionRev.setFecFin(fecFin);
	            this.relacionService.save(relacionRev);
            }
            relacion.setFecFin(fecFin);
            this.relacionService.save(relacion);
            datos = true;
        }
        if (monto != null) {
            contrato.setMonto(monto);
            datos = true;
        }
        if (vencimiento != null && vencimiento > 0) {
            contrato.setVencimiento(vencimiento);
            datos = true;
        }

        if (!datos) {
            response.put("mensaje", "No se tiene datos para actualizar el contrato.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        contrato.setUpdateUser(createUser);
        contrato.setUpdateDate(new Date());

        try {
        	contratoOutsourcingService.save(contrato);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la actualización del contrato.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());

                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + contrato.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            contrato.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = null;
                    for (String[] d : funciones) {
                        if (d[0].equals(archivo))
                            tipoArchivo = Long.valueOf(d[1]);
                    }

                    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                    String key = String.format("%s.%s", UUID.randomUUID(), extension);
                    Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
                    if (fext.isEmpty())
                        msgValidacion = "La extension del archivo [" + extension + "] no se encuentra registrada.";
                    if (msgValidacion.isEmpty())
                        idFormato = fext.get().getIdArchivoFormato();

                    Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(tipoArchivo);
                    if (reg.isEmpty())
                        msgValidacion = "El tipo de archivo [" + tipoArchivo + "] enviado no esta registrado.";
                    if (msgValidacion.isEmpty()) {
                        ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                        if (ret.getStatusCodeValue() == 200) {
                            HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                            String url = dsa.get("url");
                            // filenames.add(file.getOriginalFilename() + " subido correctamente.");
                            RepArchivo adjunto = new RepArchivo();
                            adjunto.setIdFolder(folder.getId());
                            adjunto.setIdModuloFuncionalidad(tipoArchivo);
                            adjunto.setNombre(file.getOriginalFilename());
                            adjunto.setIdArchivoFormato(idFormato);
                            adjunto.setIdtabla(contrato.getId());
                            adjunto.setTabla("administracion_contratos");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate(createUser);
                            adjunto.setFecCreate(new Date());

                            repArchivoService.save(adjunto);

                        } else {
                            filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                        }
                    } else {
                        filenames.add(file.getOriginalFilename() + " Error: " + msgValidacion);
                    }
                }
                response.put("message_files", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/index")
    public ResponseEntity<?> index(@RequestParam(required = false, name = "idRelacion") Long idRelacion,
            @RequestParam(required = false, name = "idTipoContrato") Long idTipoContrato,
            @RequestParam(required = false, name = "idTipoServicio") Long idTipoServicio,
            @RequestParam(required = false, name = "idFormaPago") Long idFormaPago,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "fecIniRng1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIniRng1,
            @RequestParam(required = false, name = "fecIniRng2") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIniRng2,
            @RequestParam(required = false, name = "fecFinRng1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFinRng1,
            @RequestParam(required = false, name = "fecFinRng2") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFinRng2,
            @RequestParam(required = false, name = "estado") Long estado,
            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
            @RequestParam(value = "column", required = false) Integer column,
            @RequestParam(value = "dir", required = false) String dir,
            Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        if (swPag == 0) {
            List<Object> listado = contratoService.listadoFiltrado(idRelacion, idTipoContrato, idTipoServicio,
                    idFormaPago, descripcion, fecIniRng1, fecIniRng2, fecFinRng1, fecFinRng2, estado);
            return ResponseEntity.ok(listado);
        } else {
            Page<Object> listado = contratoService.listadoFiltrado(idRelacion, idTipoContrato, idTipoServicio,
                    idFormaPago, descripcion, fecIniRng1, fecIniRng2, fecFinRng1, fecFinRng2, estado, column, dir,
                    pageable);
            response.put("datos", listado.getContent());
            response.put("totRegs", listado.getTotalElements());
            response.put("totPags", listado.getTotalPages());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/searchTemp")
    public ResponseEntity<?> buscarContratos(
            @RequestParam(required = false, name = "idRelacion") Long idRelacion,
            @RequestParam(required = false, name = "idTipoContrato") Long idTipoContrato,
            @RequestParam(required = false, name = "idTipoServicio") Long idTipoServicio,
            @RequestParam(required = false, name = "idFormaPago") Long idFormaPago,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "fecIniRng1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIniRng1,
            @RequestParam(required = false, name = "fecIniRng2") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIniRng2,
            @RequestParam(required = false, name = "fecFinRng1") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFinRng1,
            @RequestParam(required = false, name = "fecFinRng2") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFinRng2,
            @RequestParam(required = false, name = "estado") Long estado,
            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
            @RequestParam(value = "column", required = false) Integer column,
            @RequestParam(value = "dir", required = false) String dir,
            Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Page<Contrato> listado = contratoServiceSecundary.searchContratos(
                idRelacion, idTipoContrato, idTipoServicio, idFormaPago, descripcion,
                fecIniRng1, fecIniRng2, fecFinRng1, fecFinRng2,
                estado, swPag, column, dir, pageable);
        response.put("datos", listado.getContent());
        response.put("totRegs", listado.getTotalElements());
        response.put("totPags", listado.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaTablasContrato")
    public ResponseEntity<Map<String, Object>> obtenerLista() {
        Map<String, Object> response = new HashMap<>();

        List<RelacionDTO> lstRelaciones = relacionService.findRelacionesForContrato();
        List<TipoContrato> lstTipoCto = tipoContratoService.findAll();
        List<TipoServicio> lstTipoSrv = tipoServicioService.findAll();
        List<FormaPago> lstFormaPgo = formaPagoService.findAll();

        response.put("TipoContrato", lstTipoCto);
        response.put("TipoServicio", lstTipoSrv);
        response.put("FormaPago", lstFormaPgo);
        response.put("Relaciones", lstRelaciones);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaTablasContratoOutsourcing")
    public ResponseEntity<Map<String, Object>> obtenerListaOutsourcing() {
        Map<String, Object> response = new HashMap<>();

        List<RelacionDTO> lstRelacionesPersonas = relacionService.findRelacionesForContratoOutsourcing();
        List<RelacionDTO> lstRelacionesClientes = relacionService.findRelacionesClientesForContratoOutsourcing();
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(3L);
        List<ModalidadDto> lstModalidades = modalidadServ.findByEstado(1).stream()
        		.map(modalidad -> modalidad.toDto())
        		.collect(Collectors.toList());
        TipoContrato lstTipoCto = tipoContratoService.findById(2L);
        response.put("tipoContrato", lstTipoCto);
        response.put("RelacionesPer", lstRelacionesPersonas);
        response.put("RelacionesCli", lstRelacionesClientes);
        response.put("Modalidades", lstModalidades);
        response.put("TipoArchivos", lstTipoArchivo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchTrabajador")
    public ResponseEntity<?> searchTrabajadorOutcourcing(@RequestParam(required = false, name = "nombre") String nombre,
    		@RequestParam(required = false, name = "documento") String documento){
    	Map<String, Object> response = new HashMap<>();
    	
    	List<RelacionTrabajadorDatosDTO> lista = relacionService.searchTrabajadorOutsourcing(nombre, documento);
    	
    	response.put("Trabajadores", lista);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listaTablasTest")
    public ResponseEntity<?> lstTablasTest(@RequestParam(name = "idPersona") Long idPersona) {
        Map<String, Object> response = new HashMap<>();
        List<TipoServicio> lstTipoSrv = tipoServicioService.findAll();
        List<FormaPago> lstFormaPgo = formaPagoService.findAll();
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_contratos]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(etiqueta.get().getFolder().getId());
        TipoContrato lstTipoCto = null;
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
            registro.setTipPer(persona.tipoPer.idTipoPer);
            registro.setTipDoc(persona.tipoDoc.idTipDoc);
            registro.setNroDoc(persona.documento);
            if (persona.tipoPer.idTipoPer == 1) {
                lstTipoCto = tipoContratoService.findById(2L);
                registro.setTratamiento(persona.atencion.getId());
                registro.setNombre(persona.perNat.apePaterno
                        .concat(" ")
                        .concat(persona.perNat.apeMaterno)
                        .concat(", ")
                        .concat(persona.perNat.nombre));
                registro.setFecIni(dateFormat.format(persona.perNat.fecNacim));
                registro.setSexo(persona.perNat.sex);
                registro.setEstadoCivil(persona.perNat.estCivil);
            } else {
                lstTipoCto = tipoContratoService.findById(1L);
                registro.setNombre(persona.perJur.razonSocial);
                registro.setFecIni(dateFormat.format(persona.perJur.fecIniOper));
                registro.setRazcom(persona.perJur.razonComercial);
            }
            // Buscar y formatear el nombre del titular si aplicable
            if (persona.idTitular != null) {
                Persona titular = perService.buscarId(persona.idTitular.longValue());
                registro.setIdTitular(persona.idTitular);
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
            /*
            List<Object> personas = contactoService.findbyContactosN(persona.id);
            List<lstPersona1> lstResumen2 = new ArrayList<>();
            if (!relaciones.isEmpty()) {
                for (Relacion relacion : relaciones) {
                    Persona personaRelacionada = relacion.getPersonaD(); // Nodo destino (persona relacionada)
                    if (personaRelacionada != null && personaRelacionada.getPerNat() != null) {
                        lstPersona1 lista = new lstPersona1();
                        lista.setId(personaRelacionada.getId());
                        lista.setNombre(personaRelacionada.getPerNat().getNombre() + " " +
                                        personaRelacionada.getPerNat().getApePaterno() + " " +
                                        personaRelacionada.getPerNat().getApeMaterno());
                        lstResumen2.add(lista);
                    }
                }
            }

            registro.setPersonasNaturales(lstResumen2);
            */

            registro.setEstado(persona.estado);
            registro.setFecCrea(persona.creDate);

            response.put("persona", registro);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la funcion listatablas al obtener data de la persona");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("TipoContrato", lstTipoCto);
        response.put("TipoServicio", lstTipoSrv);
        response.put("FormaPago", lstFormaPgo);
        response.put("TipoArchivo", lstTipoArchivo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/listaTablas")
    public ResponseEntity<?> lstTablas(@RequestParam(name = "idPersona") Long idPersona) {
        Map<String, Object> response = new HashMap<>();
        List<TipoContrato> lstTipoCto = tipoContratoService.findAll();
        List<TipoServicio> lstTipoSrv = tipoServicioService.findAll();
        List<FormaPago> lstFormaPgo = formaPagoService.findAll();
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_contratos");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_contratos]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(etiqueta.get().getFolder().getId());

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
            registro.setTipPer(persona.tipoPer.idTipoPer);
            registro.setTipDoc(persona.tipoDoc.idTipDoc);
            registro.setNroDoc(persona.documento);
            if (persona.tipoPer.idTipoPer == 1) {
                registro.setTratamiento(persona.atencion.getId());
                registro.setNombre(persona.perNat.apePaterno
                        .concat(" ")
                        .concat(persona.perNat.apeMaterno)
                        .concat(", ")
                        .concat(persona.perNat.nombre));
                registro.setFecIni(dateFormat.format(persona.perNat.fecNacim));
                registro.setSexo(persona.perNat.sex);
                registro.setEstadoCivil(persona.perNat.estCivil);
            } else {
                registro.setNombre(persona.perJur.razonSocial);
                registro.setFecIni(dateFormat.format(persona.perJur.fecIniOper));
                registro.setRazcom(persona.perJur.razonComercial);
            }
            // Buscar y formatear el nombre del titular si aplicable
            if (persona.idTitular != null) {
                Persona titular = perService.buscarId(persona.idTitular.longValue());
                registro.setIdTitular(persona.idTitular);
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

            List<Relacion> relaciones = relacionService.findByNodoOrigen(persona.id);
            List<lstPersona1> lstResumen2 = new ArrayList<>();
            if (!relaciones.isEmpty()) {
                for (Relacion relacion : relaciones) {
                    Persona personaRelacionada = relacion.getPersonaD(); // Nodo destino (persona relacionada)
                    if (personaRelacionada != null && personaRelacionada.getPerNat() != null) {
                        lstPersona1 lista = new lstPersona1();
                        lista.setId(personaRelacionada.getId());
                        lista.setNombre(personaRelacionada.getPerNat().getNombre() + " " +
                                        personaRelacionada.getPerNat().getApePaterno() + " " +
                                        personaRelacionada.getPerNat().getApeMaterno());
                        lstResumen2.add(lista);
                    }
                }
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

            response.put("persona", registro);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la funcion listatablas al obtener data de la persona");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("TipoContrato", lstTipoCto);
        response.put("TipoServicio", lstTipoSrv);
        response.put("FormaPago", lstFormaPgo);
        response.put("TipoArchivo", lstTipoArchivo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

class xPersona {
    private Long idPer;
    private Long idTipoPer;
    private Long idTipoDoc;
    private String documento;
    private String atencion;
    private Integer estado;
    private Integer visible;
    private Long idPerNat;
    private String nombre;
    private Long idPerJur;
    private String razSoc;
    private Long idTitular;

    public Long getIdPer() {
        return idPer;
    }

    public void setIdPer(Long idPer) {
        this.idPer = idPer;
    }

    public Long getIdTipoPer() {
        return idTipoPer;
    }

    public void setIdTipoPer(Long idTipoPer) {
        this.idTipoPer = idTipoPer;
    }

    public Long getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(Long idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
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

    public Long getIdPerNat() {
        return idPerNat;
    }

    public void setIdPerNat(Long idPerNat) {
        this.idPerNat = idPerNat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdPerJur() {
        return idPerJur;
    }

    public void setIdPerJur(Long idPerJur) {
        this.idPerJur = idPerJur;
    }

    public String getRazSoc() {
        return razSoc;
    }

    public void setRazSoc(String razSoc) {
        this.razSoc = razSoc;
    }

    public Long getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(Long idTitular) {
        this.idTitular = idTitular;
    }
}

class xRelacion {
    private Long idRel;
    private Long idTiporel;
    private xPersona persona;
    private Long idArea;
    private Areas area;
    private Long estado;
    private Date fecIni;
    private Date fecFin;

    public Long getIdRel() {
        return idRel;
    }

    public void setIdRel(Long idRel) {
        this.idRel = idRel;
    }

    public Long getIdTiporel() {
        return idTiporel;
    }

    public void setIdTiporel(Long idTiporel) {
        this.idTiporel = idTiporel;
    }

    public xPersona getPersona() {
        return persona;
    }

    public void setPersona(xPersona persona) {
        this.persona = persona;
    }

    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }
}

class xContrato {
    private Long id;
    private xRelacion relacion;
    private Long idTipoContrato;
    private Long idtipoServicio;
    private Long idFormaPago;
    private String descripcion;
    private Date fecInicio;
    private Date fecFin;
    private Double monto;
    private Long hes;
    private Long idCliente;
    private Long idModalidad;
    private String puesto;
    private Integer vencimiento;
    private Long estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public xRelacion getRelacion() {
        return relacion;
    }

    public void setRelacion(xRelacion relacion) {
        this.relacion = relacion;
    }

    public Long getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Long idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    public Long getIdtipoServicio() {
        return idtipoServicio;
    }

    public void setIdtipoServicio(Long idtipoServicio) {
        this.idtipoServicio = idtipoServicio;
    }

    public Long getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Long idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Long getHes() {
        return hes;
    }

    public void setHes(Long hes) {
        this.hes = hes;
    }

    public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdModalidad() {
		return idModalidad;
	}

	public void setIdModalidad(Long idModalidad) {
		this.idModalidad = idModalidad;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

	public Integer getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Integer vencimiento) {
		this.vencimiento = vencimiento;
	}
}

class xContacto {
    private Long idContratoContacto;
    private Long idContrato;
    private Long idContacto;
    private Long idPersona;
    private String tipoDoc;
    private String nroDoc;
    private String nombre;
    private String motivo;
    private Long estado;

    public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Long getIdContratoContacto() {
        return idContratoContacto;
    }

    public void setIdContratoContacto(Long idContratoContacto) {
        this.idContratoContacto = idContratoContacto;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }
}

class xAdjunto {
    private Long idArchivo;
    private Long idFolder;
    private Long idModuloFuncionalidad;
    private String descFuncionalidad;
    private String nombre;
    private Long idArchivoFormato;
    private Long idtabla;
    private String tabla;
    private String encriptado;
    private String url;
    private String usrCreate;
    private Date fecCreate;
    private String usrUpdate;
    private Date fecUpdate;

    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Long getIdModulo() {
        return idFolder;
    }

    public void setIdFolder(Long idFolder) {
        this.idFolder = idFolder;
    }

    public Long getIdModuloFuncionalidad() {
        return idModuloFuncionalidad;
    }

    public void setIdModuloFuncionalidad(Long idModuloFuncionalidad) {
        this.idModuloFuncionalidad = idModuloFuncionalidad;
    }

    public String getDescFuncionalidad() {
        return descFuncionalidad;
    }

    public void setDescFuncionalidad(String descFuncionalidad) {
        this.descFuncionalidad = descFuncionalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdArchivoFormato() {
        return idArchivoFormato;
    }

    public void setIdArchivoFormato(Long idArchivoFormato) {
        this.idArchivoFormato = idArchivoFormato;
    }

    public Long getIdtabla() {
        return idtabla;
    }

    public void setIdtabla(Long idtabla) {
        this.idtabla = idtabla;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getEncriptado() {
        return encriptado;
    }

    public void setEncriptado(String encriptado) {
        this.encriptado = encriptado;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsrCreate() {
        return usrCreate;
    }

    public void setUsrCreate(String usrCreate) {
        this.usrCreate = usrCreate;
    }

    public Date getFecCreate() {
        return fecCreate;
    }

    public void setFecCreate(Date fecCreate) {
        this.fecCreate = fecCreate;
    }

    public String getUsrUpdate() {
        return usrUpdate;
    }

    public void setUsrUpdate(String usrUpdate) {
        this.usrUpdate = usrUpdate;
    }

    public Date getFecUpdate() {
        return fecUpdate;
    }

    public void setFecUpdate(Date fecUpdate) {
        this.fecUpdate = fecUpdate;
    }
}