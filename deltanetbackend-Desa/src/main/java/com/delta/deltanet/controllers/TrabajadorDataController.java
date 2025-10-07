package com.delta.deltanet.controllers;

import com.delta.deltanet.models.dao.AdmDatosTrabajadorDao;
import com.delta.deltanet.models.dao.IDiscapacidadDao;
import com.delta.deltanet.models.dao.IEnfermedadDao;
import com.delta.deltanet.models.dao.IPersonaNaturalDao;
import com.delta.deltanet.models.dao.ISbsAfpDao;
import com.delta.deltanet.models.dao.ISbsBancaDao;
import com.delta.deltanet.models.dao.ISexoDao;
import com.delta.deltanet.models.dao.ITipoDiscapacidadDao;
import com.delta.deltanet.models.dao.ITipoEnfermedadDao;
import com.delta.deltanet.models.dao.ITipoEstadoCivilDao;
import com.delta.deltanet.models.dao.ITrabFamiliaDao;
import com.delta.deltanet.models.dao.ITrabajadorSaludDao;
import com.delta.deltanet.models.dao.ITrabajadorSueldoDao;
import com.delta.deltanet.models.entity.AdmDatosTrabajador;
import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.Discapacidad;
import com.delta.deltanet.models.entity.Enfermedad;
import com.delta.deltanet.models.entity.PersonaNatural;
import com.delta.deltanet.models.entity.RepArchivo;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import com.delta.deltanet.models.entity.SbsAfp;
import com.delta.deltanet.models.entity.SbsBanca;
import com.delta.deltanet.models.entity.Sexo;
import com.delta.deltanet.models.entity.TipoDiscapacidad;
import com.delta.deltanet.models.entity.TipoEnfermedad;
import com.delta.deltanet.models.entity.TipoEstadoCivil;
import com.delta.deltanet.models.entity.TrabajadorFamilia;
import com.delta.deltanet.models.entity.TrabajadorSalud;
import com.delta.deltanet.models.entity.TrabajadorSueldo;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.IRepArchivoEtiquetaService;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import com.delta.deltanet.models.service.IRepArchivoFuncService;
import com.delta.deltanet.models.service.IRepArchivoService;
import com.delta.deltanet.services.IS3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/trabajadores")
public class TrabajadorDataController {

    @Autowired
    IS3Service is3Service;
    @Autowired
    private IRepArchivoService repArchivoService;
    @Autowired
    private IRepArchivoFormatoService archivoFormatoService;
    @Autowired
    private IRepArchivoEtiquetaService archivoEtiquetaService;
    @Autowired
    private IRepArchivoFuncService archivoFuncService;
    @Autowired
    private IRepArchivoFormatoService formatoService;
    @Autowired
    private ISexoDao sexoService;
    @Autowired
    private ITipoEstadoCivilDao tipoEstadoCivilService;
    @Autowired
    private ITipoEnfermedadDao tipoEnfermedadService;
    @Autowired
    private IEnfermedadDao enfermedadService;
    @Autowired
    private ITipoDiscapacidadDao tipoDiscapacidadService;
    @Autowired
    private IDiscapacidadDao discapacidadService;
    @Autowired
    private AdmDatosTrabajadorDao trabajadorService;
    @Autowired
    private ITrabFamiliaDao admTrabajadorFamiliaDao;
    @Autowired
    private IPersonaNaturalDao perDatosPersonaNaturalDao;
    @Autowired
    private ITrabajadorSaludDao admTrabajadorSaludDao;
    @Autowired
    private IPersonaNaturalDao personaNaturalService;
    @Autowired
    private ITipoEstadoCivilDao estadoCivilService;
    @Autowired
    private ISbsAfpDao sbsAfpService;
    @Autowired
    private ISbsBancaDao sbsBancaService;
    @Autowired
    private ITrabajadorSueldoDao trabajadorSueldoService;
    @Autowired
    private AdmFolderServiceImpl folderService;

    @PostMapping("/create")
    public ResponseEntity<?> crearTrabajador(
            @RequestParam(name = "personaId") Long personaId,
            @RequestParam(name = "conQuienVive", required = false) String viveConQuien,
            @RequestParam(name = "tieneHijosMayores", defaultValue = "false") Boolean tieneHijos,
            @RequestParam(name = "cantidadHijos", defaultValue = "0") Integer cantidadHijos,

            @RequestParam(name = "contactos", required = false) String contactos,

            @RequestParam(name = "padeceEnfermedad", defaultValue = "false") Boolean padeceEnfermedad,
            @RequestParam(name = "idEnfermedad", required = false) Integer idEnfermedad,
            @RequestParam(name = "enfermedad", required = false) Integer enfermedad,
            @RequestParam(name = "tratamiento", required = false) String tratamiento,

            @RequestParam(name = "tieneDiscapacidad", defaultValue = "false") Boolean tieneDiscapacidad,
            @RequestParam(name = "idDiscapacidad", required = false) Integer idDiscapacidad,
            @RequestParam(name = "discapacidad", required = false) Integer discapacidad,
            @RequestParam(name = "ajustesRazonables", required = false) String ajustesRazonables,

            @RequestParam(name = "afiliadoONPAFP", defaultValue = "0") String afiliacion,
            @RequestParam(name = "indicarCual", required = false) String indicarAfiliacion,
            @RequestParam(name = "bancoSueldo", required = false) String banco,
            @RequestParam(name = "numeroCuenta", required = false) String nroCuenta,
            @RequestParam(name = "cci", required = false) String nroCuentaInterb,

            @RequestParam(name = "estadoCivil", required = false) Long idEstadoCivil,
            @RequestParam(name = "peso", required = false) Double peso,
            @RequestParam(name = "estatura", required = false) Integer estatura,

            @RequestParam(name = "username") String usuario,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs) {
        Map<String, Object> response = new HashMap<>();

        Map<String, Long> fileToFunctionMap = new HashMap<>();

        // Match files with their function IDs
        if (files != null && funcs != null && files.length == funcs.length) {
            for (int i = 0; i < files.length; i++) {
                fileToFunctionMap.put(files[i].getOriginalFilename(), Long.valueOf(funcs[i]));
            }
        }

        AdmDatosTrabajador trabajador = trabajadorService.findByPersonaId(personaId);
        if (trabajador == null) {
            trabajador = new AdmDatosTrabajador();
            trabajador.setPersonaId(personaId);
            trabajador.setUsrCreate(usuario);
            trabajador.setFecCreate(new Date());
        } else {
            trabajador.setUsrUpdate(usuario);
            trabajador.setFecUpdate(new Date());
        }
        trabajador.setViveConQuien(viveConQuien);
        trabajador.setTieneHijos(tieneHijos);
        trabajador.setCantidadHijos(cantidadHijos);
        trabajador.setPadeceEnfermedad(padeceEnfermedad);
        trabajador.setTieneDiscapacidad(tieneDiscapacidad);
        // Guardar en la base de datos
        trabajadorService.save(trabajador);

        // Buscar PersonaNatural por ID
        PersonaNatural persona = personaNaturalService.findByPersonaNaturalId(personaId);
        if (persona == null) {
            response.put("mensaje", "No se encontró la persona con el ID especificado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Actualizar los campos si se proporcionan
        if (idEstadoCivil != null) {
            Optional<TipoEstadoCivil> estadoCivil = estadoCivilService.findById(idEstadoCivil);
            if (estadoCivil == null) {
                response.put("mensaje", "No se encontró el estado civil con el ID especificado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            persona.setEstCivil(estadoCivil.orElse(null));
        }

        if (peso != null) {
            persona.setPeso(peso);
        }

        if (estatura != null) {
            persona.setEstatura(estatura);
        }
        // Guardar
        personaNaturalService.save(persona);

        // Buscar si existe el trabajador
        List<TrabajadorSalud> trabajadorSaludList = admTrabajadorSaludDao.findByPersonaNaturalId(personaId);
        TrabajadorSalud trabajadorSalud = trabajadorSaludList.isEmpty() ? null : trabajadorSaludList.get(0);
        boolean isNew = trabajadorSalud == null;

        if (isNew) {
            trabajadorSalud = new TrabajadorSalud();
            trabajadorSalud.setCreateUser(usuario);
            trabajadorSalud.setCreateDate(new Date());
        } else {
            trabajadorSalud.setUpdateUser(usuario);
            trabajadorSalud.setUpdateDate(new Date());
        }

        // Datos básicos
        trabajadorSalud.setIdPerNat(personaId);
        trabajadorSalud.setEstado(1);

        // Manejo de enfermedad
        if (padeceEnfermedad) {
            trabajadorSalud.setFlagEnfer(idEnfermedad);
            trabajadorSalud.setIdEnfer(enfermedad);
            trabajadorSalud.setTratamEnfer(tratamiento);
        } else {
            trabajadorSalud.setFlagEnfer(0);
            trabajadorSalud.setIdEnfer(null);
            trabajadorSalud.setTratamEnfer(null);
        }

        // Manejo de discapacidad
        if (tieneDiscapacidad) {
            trabajadorSalud.setFlagDiscap(idDiscapacidad);
            trabajadorSalud.setIdDiscap(discapacidad);
            trabajadorSalud.setAjusteDiscap(ajustesRazonables);
        } else {
            trabajadorSalud.setFlagDiscap(0);
            trabajadorSalud.setIdDiscap(null);
            trabajadorSalud.setAjusteDiscap(null);
        }

        // Guardar el trabajador
        trabajadorSalud = admTrabajadorSaludDao.save(trabajadorSalud);

        TrabajadorSueldo trabajadorSueldo = trabajadorSueldoService.buscaByPerNat(personaId);
        if (trabajadorSueldo == null) {
            trabajadorSueldo = new TrabajadorSueldo();
            trabajadorSueldo.setIdPerNat(personaId);
            trabajadorSueldo.setCreateUser(usuario);
            trabajadorSueldo.setCreateDate(new Date());
        } else {
            trabajadorSueldo.setUpdateUser(usuario);
            trabajadorSueldo.setUpdateDate(new Date());
        }
        trabajadorSueldo.setEstado(1);
        trabajadorSueldo.setIdAFP(Integer.parseInt(afiliacion));
        trabajadorSueldo.setIdBanca(Integer.parseInt(indicarAfiliacion));
        trabajadorSueldo.setNroAfp(banco);
        trabajadorSueldo.setNroCuenta(nroCuenta);
        trabajadorSueldo.setNroCCI(nroCuentaInterb);
        trabajadorSueldoService.save(trabajadorSueldo);

        if (contactos != null && !contactos.trim().isEmpty()) {
            // Dividir la cadena en números individuales
            String[] numerosStr = contactos.trim().split("\\s+");

            // Verificar que la cantidad de números es par
            if (numerosStr.length % 2 != 0) {
                response.put("error", "La cantidad de números debe ser par (ID contacto y convivencia)");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Procesar los números de dos en dos
            for (int i = 0; i < numerosStr.length; i += 2) {
                try {
                    Long contactoId = Long.parseLong(numerosStr[i]);
                    Integer convivencia = Integer.parseInt(numerosStr[i + 1]);

                    List<TrabajadorFamilia> familias = admTrabajadorFamiliaDao.findByPersonaId(personaId);
                    if (familias.isEmpty()) {
                        TrabajadorFamilia familia = new TrabajadorFamilia();
                        familia.setIdPerNat(personaId);
                        familia.setConvivencia(convivencia);
                        familia.setCreateUser(usuario);
                        familia.setCreateDate(new Date());
                        admTrabajadorFamiliaDao.save(familia);
                    } else {
                        for (TrabajadorFamilia familia : familias) {
                            familia.setConvivencia(convivencia);
                            familia.setUpdateUser(usuario);
                            familia.setUpdateDate(new Date());
                            admTrabajadorFamiliaDao.save(familia);
                        }
                    }
                } catch (NumberFormatException e) {
                    response.put("error", "Formato de número inválido: " + numerosStr[i] + " o " + numerosStr[i + 1]);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("trabajadores_archivos_adjuntos")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());

                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + trabajador.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            trabajador.getId().toString());
                }

                String msgValidacion = "";
                Long idFormato = null;

                for (MultipartFile file : files) {
                    String archivo = file.getOriginalFilename();
                    Long tipoArchivo = fileToFunctionMap.get(archivo);

                    if (tipoArchivo == null) {
                        filenames.add(archivo + " Error: No matching function ID found");
                        continue;
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
                        if (ret.getStatusCodeValue() != 200) {
                            response.put("mensaje", "Error al subir archivo: " +
                                    file.getOriginalFilename());
                            response.put("detalle", ret.getBody());
                            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                        }

                        HashMap<String, String> dsa = (HashMap<String, String>) ret.getBody();
                        String url = dsa.get("url");

                        if (url != null && !url.isEmpty()) {
                            RepArchivo adjunto = new RepArchivo();
                            adjunto.setIdFolder(folder.getId());
                            adjunto.setIdModuloFuncionalidad(tipoArchivo);
                            adjunto.setNombre(file.getOriginalFilename());
                            adjunto.setIdArchivoFormato(idFormato);
                            adjunto.setIdtabla(personaId);
                            adjunto.setTabla("trabajadores_archivos_adjuntos");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate(usuario);
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

        response.put("message", "Se registro correctamente los datos del trabajador.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> obtenerInformacion() {
        Map<String, Object> catalogo = new HashMap<>();

        RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("trabajadores_archivos_adjuntos")
                .orElse(null);
        List<RepArchivoFuncionalidad> reg = archivoFuncService.getRepFuncionalidades(etiqueta.getFolder().getId());
        List<RepArchivoFormato> listado = formatoService.findAll();
        List<Sexo> sexos = sexoService.findAll();
        List<TipoEstadoCivil> estadoCivil = tipoEstadoCivilService.findAll();

        List<TipoEnfermedad> tipoEnfermedad = tipoEnfermedadService.findAll();
        List<Enfermedad> enfermedad = enfermedadService.findAll();
        Map<Integer, TipoEnfermedad> tipoEnfermedadMap = tipoEnfermedad.stream()
                .collect(Collectors.toMap(TipoEnfermedad::getId, tipo -> tipo));

        List<Map<String, Object>> enfermedadesAgrupadas = enfermedad.stream()
                .collect(Collectors.groupingBy(Enfermedad::getCodTipo))
                .entrySet().stream()
                .map(entry -> {
                    Integer tipoId = entry.getKey();
                    List<Enfermedad> enfermedades = entry.getValue();
                    TipoEnfermedad tipo = tipoEnfermedadMap.get(tipoId);
                    Map<String, Object> agrupacion = new HashMap<>();
                    agrupacion.put("tipoEnfermedad", tipo);
                    agrupacion.put("enfermedades", enfermedades);
                    return agrupacion;
                })
                .collect(Collectors.toList());

        catalogo.put("listEnfermedades", enfermedadesAgrupadas);

        // Agrupando las discapacidades por tipo
        List<TipoDiscapacidad> tipoDiscapacidad = tipoDiscapacidadService.findAll();
        List<Discapacidad> discapacidad = discapacidadService.findAll();
        Map<Integer, TipoDiscapacidad> tipoDiscapacidadMap = tipoDiscapacidad.stream()
                .collect(Collectors.toMap(TipoDiscapacidad::getId, tipo -> tipo));

        List<Map<String, Object>> discapacidadesAgrupadas = discapacidad.stream()
                .collect(Collectors.groupingBy(Discapacidad::getCodTipo))
                .entrySet().stream()
                .map(entry -> {
                    Integer tipoId = entry.getKey();
                    List<Discapacidad> discapacidades = entry.getValue();
                    TipoDiscapacidad tipo = tipoDiscapacidadMap.get(tipoId);
                    Map<String, Object> agrupacion = new HashMap<>();
                    agrupacion.put("tipoDiscapacidad", tipo);
                    agrupacion.put("discapacidades", discapacidades);
                    return agrupacion;
                })
                .collect(Collectors.toList());

        catalogo.put("listDiscapacidades", discapacidadesAgrupadas);

        List<SbsBanca> banco = sbsBancaService.findAll();
        List<SbsAfp> afp = sbsAfpService.findAll();
        catalogo.put("submodulo", reg);
        catalogo.put("formatos", listado);
        catalogo.put("sexos", sexos);
        catalogo.put("estadoCivil", estadoCivil);
        catalogo.put("bancos", banco);
        catalogo.put("afp", afp);

        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/info")
    public ResponseEntity<?> obtenerInformacionTrabajador(@RequestParam Long personaId) {
        Map<String, Object> response = new HashMap<>();

        List<TrabajadorFamilia> familia = admTrabajadorFamiliaDao.findByPersonaNaturalId(personaId);
        List<TrabajadorSalud> salud = admTrabajadorSaludDao.findByPersonaNaturalId(personaId);
        AdmDatosTrabajador datosTrabajador = trabajadorService.findByPersonaId(personaId);
        PersonaNatural personaNatural = perDatosPersonaNaturalDao.findByPersonaNaturalId(personaId);
        TrabajadorSueldo trabajadorSueldo = trabajadorSueldoService.buscaByPerNat(personaId);

        List<RepArchivo> listaArchivos = repArchivoService.getRepArchivos(personaId);
        List<xAdjunto> lstAdjuntos = new ArrayList<>();
        for (RepArchivo archivo : listaArchivos) {
            // Filtrar por la tabla "mantenimiento_propuestas"
            if ("trabajadores_archivos_adjuntos".equals(archivo.getTabla())) {
                Optional<RepArchivoFuncionalidad> funcionalidadOpt = archivoFuncService
                        .busca(archivo.getIdModuloFuncionalidad());
                if (funcionalidadOpt.isPresent()) {
                    xAdjunto adjunto = new xAdjunto();
                    adjunto.setIdArchivo(archivo.getIdArchivo());
                    adjunto.setIdFolder(archivo.getIdFolder());
                    adjunto.setIdModuloFuncionalidad(archivo.getIdModuloFuncionalidad());
                    adjunto.setDescFuncionalidad(funcionalidadOpt.get().getNombre());
                    adjunto.setNombre(archivo.getNombre());
                    adjunto.setIdArchivoFormato(archivo.getIdArchivoFormato());
                    adjunto.setIdtabla(archivo.getIdtabla());
                    adjunto.setTabla(archivo.getTabla());
                    adjunto.setEncriptado(archivo.getEncriptado());
                    adjunto.setUrl(archivo.getUrl());
                    adjunto.setUsrCreate(archivo.getUsrCreate());
                    adjunto.setFecCreate(archivo.getFecCreate());
                    adjunto.setUsrUpdate(archivo.getUsrUpdate());
                    adjunto.setFecUpdate(archivo.getFecUpdate());
                    lstAdjuntos.add(adjunto);
                }
            }
        }

        response.put("Adjuntos", lstAdjuntos);
        response.put("familia", familia);
        response.put("salud", salud);
        response.put("datosTrabajador", datosTrabajador);
        response.put("personaNatural", personaNatural);
        response.put("trabajadorSueldo", trabajadorSueldo);

        return ResponseEntity.ok(response);

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

        response.put("message", "Se elimino el adjunto satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}