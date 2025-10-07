package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.Contrato2024;
import com.delta.deltanet.models.entity.Cronograma;
import com.delta.deltanet.models.entity.LiquidacionCronograma;
import com.delta.deltanet.models.entity.LiquidacionFactura;
import com.delta.deltanet.models.entity.LiquidacionPago;
import com.delta.deltanet.models.entity.LiquidacionResponseDTO;
import com.delta.deltanet.models.entity.LiquidacionTipoOperacion;
import com.delta.deltanet.models.entity.Liquidaciones;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.RepArchivo;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import com.delta.deltanet.models.entity.SbsBanca;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.IContrato2024Service;
import com.delta.deltanet.models.service.ICronogramaService;
import com.delta.deltanet.models.service.IRepArchivoEtiquetaService;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import com.delta.deltanet.models.service.IRepArchivoFuncService;
import com.delta.deltanet.models.service.IRepArchivoService;
import com.delta.deltanet.models.service.LiquidacionFacturaService;
import com.delta.deltanet.models.service.LiquidacionPagoServiceImpl;
import com.delta.deltanet.models.service.LiquidacionTipoOperacionServiceImpl;
import com.delta.deltanet.models.service.LiquidacionesService;
import com.delta.deltanet.models.service.SbsBancaServiceImpl;
import com.delta.deltanet.services.IS3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/liquidaciones")
public class LiquidacionesController {

    @Autowired
    private LiquidacionesService liquidacionesService;
    @Autowired
    private LiquidacionFacturaService liquidacionFacturaService;

    @Autowired
    private ICronogramaService cronogramaService;

    @Autowired
    private IRepArchivoService repArchivoService;

    @Autowired
    private IRepArchivoFuncService archivoFuncService;

    @Autowired
    private IRepArchivoEtiquetaService archivoEtiquetaService;

    @Autowired
    IS3Service is3Service;

    @Autowired
    private SbsBancaServiceImpl bancaService;

    @Autowired
    private LiquidacionTipoOperacionServiceImpl liquidacionTipoOperacionService;
    
    @Autowired
    private LiquidacionPagoServiceImpl liquidacionPagoServiceImpl;

    @Autowired
    private IRepArchivoFormatoService archivoFormatoService;

    @Autowired
    public IContrato2024Service contratoService;
    @Autowired
    private AdmFolderServiceImpl folderService;
    
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarLiquidaciones(@RequestParam(required = false, name = "idCli") Integer idCliente,
            @RequestParam(required = false, name = "fechaIni") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false, name = "fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(required = false, name = "estado") Integer estado,
            @RequestParam(required = false, name = "paginado", defaultValue = "0") Integer swPag, Pageable pageable) {

        try {
            Map<String, Object> response = new HashMap<>();

            if (swPag == 0) {
                List<LiquidacionResponseDTO> liquidaciones = liquidacionesService
                        .buscarLiquidacionesConFiltros(idCliente, fechaInicio, fechaFin, estado);
                response.put("data", liquidaciones);
            } else {
                Page<LiquidacionResponseDTO> paginaLiquidaciones = liquidacionesService
                        .buscarLiquidacionesConFiltrosPaginado(idCliente, fechaInicio, fechaFin, estado, pageable);
                response.put("data", paginaLiquidaciones.getContent());
                response.put("totRegs", paginaLiquidaciones.getTotalElements());
                response.put("totPags", paginaLiquidaciones.getTotalPages());
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> create() {
        Map<String, Object> response = new HashMap<>();
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_contratos]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long idSubModulo = etiqueta.get().getFolder().getId();
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(idSubModulo);
        response.put("TipoArchivo", lstTipoArchivo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/anular")
    public ResponseEntity<?> anularLiquidacion(@RequestParam(name = "id") Long idLiq,
            @RequestParam(name = "usuario") String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            Liquidaciones liq = liquidacionesService.findLiquidacionById(idLiq);
            if (liq == null) {
                response.put("message", "El id [" + idLiq + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = liq.getEstado() == 1 ? "anuló" : "activó";
            liq.setEstado(liq.getEstado() == 1 ? 2 : 1);
            liq.setUpdateUser(username);
            liq.setUpdateDate(new Date());
            liquidacionesService.save(liq);
            response.put("message", "La liquidación se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al anular la liquidación.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/liquidar")
    public ResponseEntity<?> liquidarLiquidacion(@RequestParam(name = "id") Long idLiq,
            @RequestParam(name = "usuario") String username) {
        Map<String, Object> response = new HashMap<>();
        try {
            Liquidaciones liq = liquidacionesService.findLiquidacionById(idLiq);
            if (liq == null) {
                response.put("message", "El id [" + idLiq + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (liq.getEstado() == 4) {
                response.put("message", "La liquidación ya se encuentra en estado 'LIQUIDADA'.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (liq.getEstado() != 1) {
                response.put("message", "Solo se puede liquidar una liquidación en estado activo.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            liq.setEstado(4);
            liq.setUpdateUser(username);
            liq.setUpdateDate(new Date());
            liquidacionesService.save(liq);
            response.put("message", "Se liquidó satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al liquidar la liquidación.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/cronos")
    public ResponseEntity<?> cronos(@RequestParam(name = "id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
        	Contrato2024 contract = contratoService.findById(id);
            List<Cronograma> list = cronogramaService.BusquedaCronogramas(id);
            List<RepArchivo> listaObj = repArchivoService.getRepArchivos(id, "administracion_contratos");
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
            response.put("contract", contract);
            response.put("Adjuntos", lstAdjuntos);
            response.put("cronos", list);
            response.put("message", "Se liquidó satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al liquidar la liquidación.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/listClientes")
    public List<Persona> getPersonasJuridicas(@RequestParam(required = false, name = "razonSocial") String razonSocial,
            @RequestParam(required = false, name = "documento") String documento) {
        return liquidacionesService.findPersonasJuridicasByTipoPersona(2L, razonSocial, documento);
    }

    @GetMapping("/listContatos")
    public List<Contrato> filterContratos(@RequestParam(value = "idTipoContrato", required = false) Long idTipoContrato,
            @RequestParam(value = "idTipoServicio", required = false) Long idTipoServicio,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "personaId", required = false) Long personaId) {
        return liquidacionesService.getContratosByFilters(idTipoContrato, idTipoServicio, descripcion, personaId);
    }

    @PostMapping("/save")
    public ResponseEntity<?> guardarLiquidacion(@RequestParam(name = "idCliente", required = true) int idCliente,
            @RequestParam(name = "idContrato", required = true) int idContrato,
            @RequestParam(name = "fechaRegistro", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro,
            @RequestParam(name = "monto", required = true) BigDecimal monto,
            @RequestParam(name = "observaciones", required = false) String observaciones,
            @RequestParam(name = "usuario", required = true) String usuario,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(required = false, name = "cronogramas") String[] cronogramas) {
        Map<String, Object> response = new HashMap<>();

        // try {
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

        // Verificar si el cliente existe y es persona jurídica
        Persona cliente = liquidacionesService.findPersonasJuridicasByTipoPersona(2L, null, null).stream()
                .filter(p -> p.getId() == idCliente).findFirst().orElse(null);
        if (cliente == null) {
            response.put("mensaje", "El cliente seleccionado no es una persona jurídica o no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Verificar si el contrato existe
        Contrato contrato = liquidacionesService.getContratosByFilters(null, null, null, null).stream()
                .filter(c -> c.getId() == idContrato).findFirst().orElse(null);
        if (contrato == null) {
            response.put("mensaje", "El contrato no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar que la fecha de registro no sea futura
        if (fechaRegistro.after(new Date())) {
            response.put("mensaje", "La fecha de registro no puede ser una fecha futura.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar que las observaciones no superen los 300 caracteres
        if (observaciones != null && observaciones.length() > 300) {
            response.put("mensaje", "Las observaciones no pueden superar los 300 caracteres.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Crear la nueva liquidación
        Liquidaciones nuevaLiquidacion = new Liquidaciones();
        try {
            nuevaLiquidacion.setIdCliente(idCliente);
            nuevaLiquidacion.setIdContrato(idContrato);
            nuevaLiquidacion.setFechaRegistro(fechaRegistro);
            nuevaLiquidacion.setMonto(monto);
            if(observaciones != null && observaciones != "") {
            	nuevaLiquidacion.setObservaciones(observaciones);
            }else {
            	nuevaLiquidacion.setObservaciones("");
            }
            nuevaLiquidacion.setEstado(1);
            nuevaLiquidacion.setCreateUser(usuario);
            nuevaLiquidacion.setCreateDate(new Date());
            // Guardar la liquidación
            liquidacionesService.save(nuevaLiquidacion);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la creación de la liquidación.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();

            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + nuevaLiquidacion.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            nuevaLiquidacion.getId().toString());
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
                            adjunto.setIdtabla(nuevaLiquidacion.getId());
                            adjunto.setTabla("administracion_liquidation");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate("admin");
                            adjunto.setFecCreate(new Date());

                            repArchivoService.save(adjunto);

                        } else {
                            filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                        }
                    }
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de cronograma
        String msgContratos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (nuevaLiquidacion != null) {
            try {
                List<String> lstContratos = new ArrayList<>();
                for (String cto : cronogramas) {
                    Optional<LiquidacionCronograma> lc = liquidacionesService.buscaLiqCro(nuevaLiquidacion.getId(),
                            Long.valueOf(cto));
                    if (lc.isPresent()) {
                        lstCtosError.add("El cronograma [" + cto + "] de la liquidación [" + nuevaLiquidacion.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        LiquidacionCronograma regCro = new LiquidacionCronograma();
                        regCro.setIdCronograma(Long.valueOf(cto));
                        regCro.setIdLiquidacion(nuevaLiquidacion.getId());
                        liquidacionesService.saveLiqCrono(regCro);
                        lstContratos.add(cto);
                    }
                }
                response.put("contratos", lstContratos);
                if (!lstCtosError.isEmpty())
                    response.put("contratos no registrados", lstCtosError);
            } catch (Exception e) {
                msgContratos = "Error en el registro de contrato de la liquidación. " + e.getMessage();
            }
        }
        response.put("message", "Se creo el cronograma satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContratos.isEmpty())
            response.put("message_contacts", msgContratos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getServiciosYContratos() {
        Map<String, Object> response = liquidacionesService.obtenerTipos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/obtener-datos-cronograma")
    public ResponseEntity<Map<String, Object>> obtenerDatosCronograma(
            @RequestParam(value = "idContrato", required = true) Long idContrato) {
        Map<String, Object> datos = liquidacionesService.obtenerDatosCronograma(idContrato);
        return ResponseEntity.ok(datos);
    }

	@GetMapping("/show/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Liquidaciones liquidation = null;
		Map<String, Object> response = new HashMap<>();
		try {
			liquidation = liquidacionesService.findLiquidacionById(id);
	        if (liquidation == null) {
	            response.put("message", "Liquidacion no encontrada");
	            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
	        }
			List<LiquidacionCronograma> cronos = liquidacionesService.ListCronos(id);
	        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation");
	        if (etiqueta.isEmpty()) {
	            response.put("message", "No se encuentra la etiqueta [administracion_liquidation]");
	            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
	        }
            List<RepArchivo> listaObj = repArchivoService.getRepArchivos((long)liquidation.getId(), "administracion_liquidation");
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
            response.put("AdjuntosLiquidation", lstAdjuntos);

            listaObj = repArchivoService.getRepArchivos((long)liquidation.getIdContrato(), "administracion_contratos");
            lstAdjuntos = new ArrayList<>();
            for (RepArchivo archi : listaObj) {
                Optional<RepArchivoFuncionalidad> f = archivoFuncService.busca(archi.getIdModuloFuncionalidad());
                if (f.isPresent()) {
                    xAdjunto reg = new xAdjunto();
                    reg.setIdArchivo(archi.getIdArchivo());
                    reg.setIdFolder(archi.getIdArchivo());
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
            response.put("AdjuntosContrato", lstAdjuntos);
	        Long idSubModulo = etiqueta.get().getFolder().getId();
	        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(idSubModulo);
	        response.put("TipoArchivo", lstTipoArchivo);
            response.put("liquidation", liquidation);
            response.put("ListCronos", cronos);
            return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateLiquidacion(@PathVariable Long id,
    		@RequestParam(name = "idCliente", required = true) int idCliente,
            @RequestParam(name = "idContrato", required = true) int idContrato,
            @RequestParam(name = "fechaRegistro", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro,
            @RequestParam(name = "monto", required = true) BigDecimal monto,
            @RequestParam(name = "observaciones", required = false) String observaciones,
            @RequestParam(name = "usuario", required = true) String usuario,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(required = false, name = "cronogramas") String[] cronogramas) {
        Map<String, Object> response = new HashMap<>();

        // Verificar si el cliente existe y es persona jurídica
        Liquidaciones liquidation = liquidacionesService.findLiquidacionById(id);
        if (liquidation == null) {
            response.put("mensaje", "La liquidacion no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (liquidation.getEstado() != 1) {
            response.put("mensaje", "La liquidacion no esta activo.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // try {
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

        // Verificar si el cliente existe y es persona jurídica
        Persona cliente = liquidacionesService.findPersonasJuridicasByTipoPersona(2L, null, null).stream()
                .filter(p -> p.getId() == idCliente).findFirst().orElse(null);
        if (cliente == null) {
            response.put("mensaje", "El cliente seleccionado no es una persona jurídica o no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Verificar si el contrato existe
        Contrato contrato = liquidacionesService.getContratosByFilters(null, null, null, null).stream()
                .filter(c -> c.getId() == idContrato).findFirst().orElse(null);
        if (contrato == null) {
            response.put("mensaje", "El contrato no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar que la fecha de registro no sea futura
        if (fechaRegistro.after(new Date())) {
            response.put("mensaje", "La fecha de registro no puede ser una fecha futura.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar que las observaciones no superen los 300 caracteres
        if (observaciones != null && observaciones.length() > 300) {
            response.put("mensaje", "Las observaciones no pueden superar los 300 caracteres.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Crear la nueva liquidación
        try {
        	liquidation.setIdCliente(idCliente);
        	liquidation.setIdContrato(idContrato);
        	liquidation.setFechaRegistro(fechaRegistro);
        	liquidation.setMonto(monto);
            if(observaciones != null && observaciones != "") {
            	liquidation.setObservaciones(observaciones);
            }else {
            	liquidation.setObservaciones("");
            }
            liquidation.setUpdateUser(usuario);
            liquidation.setUpdateDate(new Date());
            // Guardar la liquidación
            liquidacionesService.save(liquidation);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la creación de la liquidación.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();

            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + liquidation.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                    		liquidation.getId().toString());
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
                            adjunto.setIdtabla(liquidation.getId());
                            adjunto.setTabla("administracion_liquidation");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate("admin");
                            adjunto.setFecCreate(new Date());

                            repArchivoService.save(adjunto);

                        } else
                            filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                    } else {
                        filenames.add(file.getOriginalFilename() + " Error: " + msgValidacion);
                    }
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }
        // Procesamiento de cronograma
        String msgContratos = "";
        List<String> lstCtosError = new ArrayList<>();
        if (liquidation != null) {
            try {
            	liquidacionesService.deleteCronoLiqs(id);
                List<String> lstContratos = new ArrayList<>();
                for (String cto : cronogramas) {
                    Optional<LiquidacionCronograma> lc = liquidacionesService.buscaLiqCro(liquidation.getId(),
                            Long.valueOf(cto));
                    if (lc.isPresent()) {
                        lstCtosError.add("El cronograma [" + cto + "] de la liquidación [" + liquidation.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        LiquidacionCronograma regCro = new LiquidacionCronograma();
                        regCro.setIdCronograma(Long.valueOf(cto));
                        regCro.setIdLiquidacion(liquidation.getId());
                        liquidacionesService.saveLiqCrono(regCro);
                        lstContratos.add(cto);
                    }
                }
                response.put("cronogramas", lstContratos);
                if (!lstCtosError.isEmpty())
                    response.put("cronogramas no registrados", lstCtosError);
            } catch (Exception e) {
                msgContratos = "Error en el registro de contrato de la liquidación. " + e.getMessage();
            }
        }
        response.put("message", "Se actualizo la liquidación satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgContratos.isEmpty())
            response.put("message_contacts", msgContratos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteCrono")
    public ResponseEntity<?> deleteCrono(@RequestParam(name="idContrato") Long idContrato,
            @RequestParam(name = "idLiquidacion") Long idLiquidacion){
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<LiquidacionCronograma> cc = liquidacionesService.buscaLiqCro(idContrato, idLiquidacion);
			if(cc.isEmpty()){
				response.put("message","El contacto [" + idLiquidacion + "] del contrato [" + idContrato + "] no se encuentra registrado.");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			liquidacionesService.deleteCronoLiq(cc.get().getId());
		}catch (Exception e){
			response.put("message","Error al eliminar el contacto [" + idLiquidacion + "] del contrato [" + idContrato + "]");
			response.put("error",e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		response.put("message","Se elimino el contacto [" + idLiquidacion + "] del contrato [" + idContrato + "] satisfactoriamente.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

    @PostMapping("/deleteAdjunto")
    public ResponseEntity<?> deleteAdjunto(@RequestParam(name="idAdjunto") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivo> f = repArchivoService.getArchivo(id);
            if (f.isEmpty()){
                response.put("message","El id del adjunto [" + id + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            repArchivoService.delete(id);
        } catch (Exception e){
            response.put("message","Error al eliminar el adjunto [" + id + "]");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        response.put("message","Se elimino el adjunto [" + id + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/factura/create/{id}")
    public ResponseEntity<?> bill_create(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        // Verificar si el cliente existe y es persona jurídica
        Liquidaciones liquidation = liquidacionesService.findLiquidacionById(id);
        if (liquidation == null) {
            response.put("message", "La liquidacion no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (liquidation.getEstado() != 4L) {
            response.put("message", "La liquidacion no esta liquidada.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation_bill");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_liquidation_bill]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Long idSubModulo = etiqueta.get().getFolder().getId();
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(idSubModulo);
        response.put("archivos", lstTipoArchivo);
        response.put("liquidation", liquidation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/factura/create/{id}")
    public ResponseEntity<?> bill_store(@PathVariable Long id,
    		@RequestParam(name = "serie", required = true) String serie,
            @RequestParam(name = "numero", required = true) String numero,
            @RequestParam(name = "monto", required = true) BigDecimal monto,
            @RequestParam(name = "fecha", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam(name = "file", required = true) MultipartFile file,
            @RequestParam(name = "type", required = true) Long type,
            @RequestParam(name = "usuario", required = true) String usuario) {
        Map<String, Object> response = new HashMap<>();
        // Verificar si el cliente existe y es persona jurídica
        Liquidaciones liquidation = liquidacionesService.findLiquidacionById(id);
        if (liquidation == null) {
            response.put("message", "La liquidacion no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (liquidation.getEstado() != 4) {
            response.put("message", "La liquidacion no esta liquidada.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            LiquidacionFactura fact = new LiquidacionFactura();
            fact.setLiquidacion(liquidation);
            fact.setSerie(serie);
            fact.setNumero(numero);
            fact.setFecha(fecha);
            fact.setMonto(monto);
            fact.setEstado(1);
            fact.setCreateUser(usuario);
            fact.setCreateDate(new Date());
            liquidation.setEstado(5);
            liquidation = liquidacionesService.save(liquidation);
            LiquidacionFactura factura = liquidacionFacturaService.save(fact);
            RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation_bill")
                    .orElse(null);
            AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
            String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                    + fact.getId().toString();
            ResponseEntity<?> sw = is3Service.readFolder(pathFile);
            if (sw.getStatusCodeValue() != 200) {
                is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                		fact.getId().toString());
            }
            String msgValidacion = "";
            Long idFormato = null;
            String filenames = "";

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String key = String.format("%s.%s", UUID.randomUUID(), extension);
            Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
            if (fext.isEmpty()) {
                msgValidacion = "La extension del archivo [" + extension + "] no se encuentra registrada.";
            }
            if (msgValidacion.isEmpty())
                idFormato = fext.get().getIdArchivoFormato();

            Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(type);
            if (reg.isEmpty()) {
                msgValidacion = "El tipo de archivo [" + type + "] enviado no esta registrado.";
            }
            if (msgValidacion.isEmpty()) {

                ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                if (ret.getStatusCodeValue() == 200) {
					HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                    String url = dsa.get("url");
                    RepArchivo adjunto = new RepArchivo();
                    adjunto.setIdFolder(folder.getId());
                    adjunto.setIdModuloFuncionalidad(type);
                    adjunto.setNombre(file.getOriginalFilename());
                    adjunto.setIdArchivoFormato(idFormato);
                    adjunto.setIdtabla(fact.getId());
                    adjunto.setTabla("administracion_liquidation_bill");
                    adjunto.setEncriptado(key);
                    adjunto.setUrl(url);
                    adjunto.setUsrCreate(usuario);
                    adjunto.setFecCreate(new Date());
                    repArchivoService.save(adjunto);
                } else
                	filenames = (file.getOriginalFilename() + " presento error al subirlo.");
                response.put("factura", factura);
            } else {
            	filenames = (file.getOriginalFilename() + " Error: " + msgValidacion);
            }
            response.put("adjuntos", filenames);
            response.put("factura", factura);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al generar la facturación");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/payment/create/{id}")
    public ResponseEntity<?> payment_create(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        // Verificar si el cliente existe y es persona jurídica
        Liquidaciones liquidation = liquidacionesService.findLiquidacionById(id);
        if (liquidation == null) {
            response.put("message", "La liquidacion no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (liquidation.getEstado() != 5L) {
            response.put("message", "La liquidacion no esta facturada.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<RepArchivoEtiqueta> etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation_payment");
        if (etiqueta.isEmpty()) {
            response.put("message", "No se encuentra la etiqueta [administracion_liquidation_payment]");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<SbsBanca> registros = this.bancaService.getBancas();
        List<LiquidacionTipoOperacion> types = this.liquidacionTipoOperacionService.findAll();
        Long idSubModulo = etiqueta.get().getFolder().getId();
        List<RepArchivoFuncionalidad> lstTipoArchivo = archivoFuncService.getRepFuncionalidades(idSubModulo);
        response.put("archivos", lstTipoArchivo);
        response.put("liquidation", liquidation);
        response.put("banks", registros);
        response.put("types", types);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/payment/create/{id}")
    public ResponseEntity<?> payment_store(@PathVariable Long id,
            @RequestParam(name = "id_banco", required = true) Long id_banco,
            @RequestParam(name = "id_tipo_operacion", required = true) Long id_tipo_operacion,
    		@RequestParam(name = "nro_operacion", required = true) String nro_operacion,
            @RequestParam(name = "monto", required = true) BigDecimal monto,
            @RequestParam(name = "fecha_pago", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_pago,
            @RequestParam(name = "file", required = true) MultipartFile file,
            @RequestParam(name = "type", required = true) Long type,
            @RequestParam(name = "usuario", required = true) String usuario) {
        Map<String, Object> response = new HashMap<>();
        // Verificar si el cliente existe y es persona jurídica
        Liquidaciones liquidation = liquidacionesService.findLiquidacionById(id);
        if (liquidation == null) {
            response.put("message", "La liquidacion no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (liquidation.getEstado() != 5) {
            response.put("message", "La liquidacion no esta liquidada.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<SbsBanca> bank = this.bancaService.getById((int)(long)id_banco);
        if (bank.isEmpty()) {
            response.put("message", "El banco no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        LiquidacionTipoOperacion typeO = this.liquidacionTipoOperacionService.getById(id_tipo_operacion);
        if (type == null) {
            response.put("message", "El banco no existe.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
        	LiquidacionPago fact = new LiquidacionPago();
            fact.setLiquidacion(liquidation);
            fact.setBank(bank.get());
            fact.setType(typeO);
            fact.setNroOperacion(nro_operacion);
            fact.setMonto(monto);
            fact.setFechaPago(fecha_pago);
            fact.setEstado(1);
            fact.setCreateUser(usuario);
            fact.setCreateDate(new Date());
            liquidation.setEstado(6);
            liquidation = liquidacionesService.save(liquidation);
            LiquidacionPago factura = liquidacionPagoServiceImpl.save(fact);
            RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("administracion_liquidation_payment")
                    .orElse(null);
            AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
            String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                    + fact.getId().toString();
            ResponseEntity<?> sw = is3Service.readFolder(pathFile);
            if (sw.getStatusCodeValue() != 200) {
                is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                		fact.getId().toString());
            }
            String msgValidacion = "";
            Long idFormato = null;
            String filenames = "";

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String key = String.format("%s.%s", UUID.randomUUID(), extension);
            Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
            if (fext.isEmpty()) {
                msgValidacion = "La extension del archivo [" + extension + "] no se encuentra registrada.";
            }
            if (msgValidacion.isEmpty())
                idFormato = fext.get().getIdArchivoFormato();

            Optional<RepArchivoFuncionalidad> reg = archivoFuncService.busca(type);
            if (reg.isEmpty()) {
                msgValidacion = "El tipo de archivo [" + type + "] enviado no esta registrado.";
            }
            if (msgValidacion.isEmpty()) {

                ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);

                if (ret.getStatusCodeValue() == 200) {
					HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                    String url = dsa.get("url");
                    RepArchivo adjunto = new RepArchivo();
                    adjunto.setIdFolder(folder.getId());
                    adjunto.setIdModuloFuncionalidad(type);
                    adjunto.setNombre(file.getOriginalFilename());
                    adjunto.setIdArchivoFormato(idFormato);
                    adjunto.setIdtabla(fact.getId());
                    adjunto.setTabla("administracion_liquidation_payment");
                    adjunto.setEncriptado(key);
                    adjunto.setUrl(url);
                    adjunto.setUsrCreate(usuario);
                    adjunto.setFecCreate(new Date());
                    repArchivoService.save(adjunto);
                } else
                	filenames = (file.getOriginalFilename() + " presento error al subirlo.");
                response.put("factura", factura);
            } else {
            	filenames = (file.getOriginalFilename() + " Error: " + msgValidacion);
            }
            response.put("adjuntos", filenames);
            response.put("factura", factura);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al generar la facturación");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
