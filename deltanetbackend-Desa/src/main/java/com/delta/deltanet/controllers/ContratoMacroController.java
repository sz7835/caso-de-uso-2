package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmContratoMacro;
import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.ContratoPerfiles;
import com.delta.deltanet.models.entity.Perfil;
import com.delta.deltanet.models.entity.RepArchivo;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.service.AdmContratoMacroService;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.ComPropuestaService;
import com.delta.deltanet.models.service.ContratoPerfilService;
import com.delta.deltanet.models.service.IRepArchivoEtiquetaService;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import com.delta.deltanet.models.service.IRepArchivoFuncService;
import com.delta.deltanet.models.service.IRepArchivoService;
import com.delta.deltanet.models.service.PerfilService;
import com.delta.deltanet.models.service.TarifarioMonedaService;
import com.delta.deltanet.services.IS3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/contratoMacro")
public class ContratoMacroController {

    @Autowired
    private AdmContratoMacroService contratoMacroService;
    @Autowired
    private TarifarioMonedaService tarifarioMonedaService;
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
    private ContratoPerfilService contratoPerfilService;
    @Autowired
    private ComPropuestaService propuestaService;
    @Autowired
    private IRepArchivoFormatoService formatoService;
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private AdmFolderServiceImpl folderService;

    @PostMapping("/create")
    public ResponseEntity<?> crearContratoMacro(
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam BigDecimal monto,
            @RequestParam(name = "idFormaPago") Long monedaId,
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "createUser") String usuario,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(required = false, name = "perfiles") String[] perfiles) {
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
        TarifarioMoneda moneda = tarifarioMonedaService.buscaId(monedaId);
        if (moneda == null) {
            response.put("mensaje", "No existe el tipo de moneda enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        AdmContratoMacro contrato = contratoMacroService.crearContratoMacro(
                fechaInicio, fechaFin, monto, monedaId, descripcion, usuario);

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                // Obtener configuración del bucket y etiquetas
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("mantenimiento_contrato_macro")
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
                    if (ret.getStatusCodeValue() != 200) {
                        response.put("mensaje", "Error al subir archivo: " + file.getOriginalFilename());
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
                        adjunto.setIdtabla(contrato.getId().longValue());
                        adjunto.setTabla("mantenimiento_contrato_macro");
                        adjunto.setEncriptado(key);
                        adjunto.setUrl(url);
                        adjunto.setUsrCreate(usuario);
                        adjunto.setFecCreate(new Date());

                        repArchivoService.save(adjunto);
                    } else
                        filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
                }

                response.put("adjuntos", filenames);
            } catch (Exception e) {
                msgAdjuntos = "Error al subir adjuntos. " + e.getMessage();
            }
        }

        // Procesamiento de contactos
        String msgPerfiles = "";
        List<String> lstCtosError = new ArrayList<>();
        if (perfiles != null) {
            try {
                List<String> lstPerfiles = new ArrayList<>();
                for (String cto : perfiles) {
                    Optional<ContratoPerfiles> cc = contratoPerfilService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoPerfiles regContacto = new ContratoPerfiles();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdPerfil(Long.valueOf(cto));
                        contratoPerfilService.save(regContacto);
                        lstPerfiles.add(cto);
                    }
                }
                response.put("perfiles", lstPerfiles);
                if (!lstCtosError.isEmpty())
                    response.put("Perfiles no registrados", lstCtosError);
            } catch (Exception e) {
                msgPerfiles = "Error en el registro de perfiles del contrato. " + e.getMessage();
            }
        }

        response.put("message", "Se creo el contrato macro satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgPerfiles.isEmpty())
            response.put("message_contacts", msgPerfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> mostrarContratoMacro(@RequestParam(name = "idContratoMacro") Long id) {
        try {
            AdmContratoMacro contrato = contratoMacroService.obtenerContratoMacro(id);
            Map<String, Object> response = new HashMap<>();

            if (contrato == null) {
                response.put("message", "Contrato no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Obtener archivos adjuntos relacionados con la propuesta
            List<RepArchivo> listaArchivos = repArchivoService.getRepArchivos(id);
            List<xAdjunto> lstAdjuntos = new ArrayList<>();
            for (RepArchivo archivo : listaArchivos) {
                // Filtrar por la tabla "mantenimiento_propuestas"
                if ("mantenimiento_contrato_macro".equals(archivo.getTabla())) {
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

            List<ContratoPerfiles> lista = contratoPerfilService.getPerfiles(id);
            List<xPerfil> lstPerfiles = new ArrayList<>();
            for (ContratoPerfiles cto : lista) {
                Perfil dato = perfilService.buscaId(cto.getIdPerfil());
                xPerfil reg = new xPerfil();
                reg.setIdContratoPerfil(cto.getIdContratoPerfil());
                reg.setIdContrato(cto.getIdContrato());
                reg.setIdPerfil(cto.getIdPerfil());

                reg.setNombre(dato.getNombre());
                reg.setEstado(Long.valueOf(dato.getEstado()));
                lstPerfiles.add(reg);
            }

            List<TarifarioMoneda> monedas = propuestaService.obtenerTiposMoneda();
            List<RepArchivoFormato> listado = formatoService.findAll();

            response.put("listado", listado);
            response.put("tipoMoneda", monedas);
            response.put("perfiles", lstPerfiles);
            response.put("data", contrato);
            response.put("Adjuntos", lstAdjuntos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> actualizarContratoMacro(
            @RequestParam(name = "idContratoMacro") Long id,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false) BigDecimal monto,
            @RequestParam(name = "idFormaPago", required = false) Long monedaId,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(required = false, name = "perfiles") String[] perfiles,
            @RequestParam(name = "updateUser") String usuario) {
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
        AdmContratoMacro contrato = contratoMacroService.actualizarContratoMacro(
                id, fechaInicio, fechaFin, monto, monedaId, descripcion, usuario);
        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("mantenimiento_contrato_macro")
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
                            adjunto.setTabla("mantenimiento_contrato_macro");
                            adjunto.setEncriptado(key);
                            adjunto.setUrl(url);
                            adjunto.setUsrCreate(usuario);
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
        String msgPerfiles = "";
        List<String> lstCtosError = new ArrayList<>();
        if (perfiles != null) {
            try {
                List<String> lstPerfiles = new ArrayList<>();
                for (String cto : perfiles) {
                    Optional<ContratoPerfiles> cc = contratoPerfilService.busca(contrato.getId(), Long.valueOf(cto));
                    if (cc.isPresent()) {
                        lstCtosError.add("El contacto [" + cto + "] del contrato [" + contrato.getId()
                                + "] ya se encuentra registrado.");
                    } else {
                        ContratoPerfiles regContacto = new ContratoPerfiles();
                        regContacto.setIdContrato(contrato.getId());
                        regContacto.setIdPerfil(Long.valueOf(cto));
                        contratoPerfilService.save(regContacto);
                        lstPerfiles.add(cto);
                    }
                }
                response.put("perfiles", lstPerfiles);
                if (!lstCtosError.isEmpty())
                    response.put("Perfiles no registrados", lstCtosError);
            } catch (Exception e) {
                msgPerfiles = "Error en el registro de perfiles del contrato. " + e.getMessage();
            }
        }

        response.put("message", "Se actualizó correctamente el contrato macro.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        if (!msgPerfiles.isEmpty())
            response.put("message_perfiles", msgPerfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cambiarEstado")
    public ResponseEntity<?> cambiarEstadoContratoMacro(
            @RequestParam Long id,
            @RequestParam String usuario) {
        try {
            String mensaje = contratoMacroService.cambiarEstadoContratoMacro(id, usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("message", mensaje);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> buscarContratosMacro(
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false, defaultValue = "0") Long moneda) {
        try {
            List<AdmContratoMacro> contratos = contratoMacroService.buscarContratosMacro(fechaInicio, fechaFin, moneda);
            return new ResponseEntity<>(contratos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> obtenerCatalogo1() {
        Map<String, Object> catalogo = new HashMap<>();

        List<TarifarioMoneda> monedas = propuestaService.obtenerTiposMoneda();
        List<RepArchivoFormato> listado = formatoService.findAll();

        catalogo.put("listado", listado);
        catalogo.put("tipoMoneda", monedas);

        return ResponseEntity.ok(catalogo);
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

    @PostMapping("/deletePerfil")
    public ResponseEntity<?> deletePerfil(@RequestParam(name = "idContrato") Long idContrato,
            @RequestParam(name = "idPerfil") Long idPerfil) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ContratoPerfiles> cc = contratoPerfilService.busca(idContrato, idPerfil);
            if (cc.isEmpty()) {
                response.put("message", "El contacto [" + idPerfil + "] del contrato [" + idContrato
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            contratoPerfilService.delete(cc.get().getIdContratoPerfil());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar el contacto [" + idPerfil + "] del contrato [" + idContrato + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino el contacto [" + idPerfil + "] del contrato [" + idContrato + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

class xPerfil {
    private Long idContratoPerfil;
    private Long idContrato;
    private Long idPerfil;
    private String nombre;
    private Long estado;

    public Long getIdContratoPerfil() {
        return idContratoPerfil;
    }

    public void setIdContratoPerfil(Long idContratoPerfil) {
        this.idContratoPerfil = idContratoPerfil;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }
}
