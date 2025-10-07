package com.delta.deltanet.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.delta.deltanet.models.dao.ComProductoServicioDao;
import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.BusquedaPropuestaDTO;
import com.delta.deltanet.models.entity.ComProductoServicio;
import com.delta.deltanet.models.entity.ComPropuesta;
import com.delta.deltanet.models.entity.ComPropuestaEstado;
import com.delta.deltanet.models.entity.ComPropuestaMotivoRechazo;
import com.delta.deltanet.models.entity.RepArchivo;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.ComPropuestaService;
import com.delta.deltanet.models.service.IRepArchivoEtiquetaService;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import com.delta.deltanet.models.service.IRepArchivoFuncService;
import com.delta.deltanet.models.service.IRepArchivoService;
import com.delta.deltanet.models.service.TarifarioMonedaService;
import com.delta.deltanet.services.IS3Service;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/proposal")
public class ComPropuestaController {

    @Autowired
    private ComPropuestaService propuestaService;
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
    private TarifarioMonedaService tarifarioMonedaService;
    @Autowired
    private ComProductoServicioDao productoServicioDao;
    @Autowired
    private IRepArchivoFormatoService formatoService;
    @Autowired
    private AdmFolderServiceImpl folderService;

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> obtenerCatalogo1() {
        Map<String, Object> catalogo = new HashMap<>();

        List<TarifarioMoneda> monedas = propuestaService.obtenerTiposMoneda();
        List<ComPropuestaEstado> estados = propuestaService.obtenerEstadosPropuesta();
        List<ComProductoServicio> productos = propuestaService.obtenerTiposProductos();

        catalogo.put("tipoMoneda", monedas);
        catalogo.put("estadoPropuesta", estados);
        catalogo.put("tipoProdServ", productos);

        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/indexCreate")
    public ResponseEntity<Map<String, Object>> obtenerCatalogo2() {
        Map<String, Object> catalogo = new HashMap<>();

        List<TarifarioMoneda> monedas = propuestaService.obtenerTiposMoneda();
        List<ComProductoServicio> productos = propuestaService.obtenerTiposProductos();
        List<RepArchivoFormato> listado = formatoService.findAll();

        catalogo.put("listado", listado);
        catalogo.put("tipoMoneda", monedas);
        catalogo.put("tipoProdServ", productos);

        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> buscarPropuestas(
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(name = "moneda", required = false, defaultValue = "0") Integer monedaId,
            @RequestParam(name = "estado", required = false, defaultValue = "0") Integer estadoId) {

        Map<String, Object> response = new HashMap<>();

        try {
            BusquedaPropuestaDTO filtro = new BusquedaPropuestaDTO(fechaInicio, fechaFin, monedaId, estadoId);
            List<ComPropuesta> propuestas = propuestaService.buscarPropuestas(filtro);
            response.put("data", propuestas);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Error interno al buscar propuestas: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> savePropose(
            @RequestParam(name = "fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(name = "productoServicio") Integer prodServ,
            @RequestParam(name = "monto") Double monto,
            @RequestParam(name = "moneda") Long monedaId,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(name = "createUser") String usuario) {
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
        ComProductoServicio productoServicio = productoServicioDao.findById(prodServ).orElse(null);
        if (productoServicio == null) {
            response.put("mensaje", "No existe el producto/servicio enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Validar TarifarioMoneda
        TarifarioMoneda moneda = tarifarioMonedaService.buscaId(monedaId);
        if (moneda == null) {
            response.put("mensaje", "No existe el tipo de moneda enviado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ComPropuesta propuesta = new ComPropuesta();
        try {
            propuesta.setFecha(fecha);
            propuesta.setProdServ(prodServ);
            propuesta.setMoneda(monedaId);
            propuesta.setMonto(BigDecimal.valueOf(monto));
            propuesta.setDescripcion(descripcion);
            propuesta.setEstado(1);
            propuesta.setCreateUser(usuario);
            propuesta.setCreateDate(LocalDateTime.now());

            // Guardar la propuesta
            propuestaService.save(propuesta);

        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la creación de la propuesta.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                // Obtener configuración del bucket y etiquetas
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("mantenimiento_propuestas")
                        .orElse(null);

                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());

                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + propuesta.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                	is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder(),
                            propuesta.getId().toString());
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
                        adjunto.setIdtabla(propuesta.getId().longValue());
                        adjunto.setTabla("mantenimiento_propuestas");
                        adjunto.setEncriptado(key);
                        adjunto.setUrl(url);
                        adjunto.setUsrCreate(usuario);
                        adjunto.setFecCreate(new Date());

                        repArchivoService.save(adjunto);
                    } else {
                        response.put("mensaje",
                                "No se pudo generar una URL válida para el archivo: " + file.getOriginalFilename());
                        response.put("detalle", "Posible error en la subida a S3.");
                        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                }
                response.put("adjuntos", filenames);
            } catch (Exception e) {
                response.put("mensaje", "Error al procesar los archivos.");
                response.put("error", e.getMessage());
            }
        }

        response.put("message", "Se creo el contrato satisfactoriamente.");
        if (!msgAdjuntos.isEmpty())
            response.put("message_files", msgAdjuntos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> mostrarPropuesta(@RequestParam(name = "idPropuesta") Integer idPropuesta) {
        Map<String, Object> response = new HashMap<>();
        List<RepArchivoFormato> listado = formatoService.findAll();

        // Buscar la propuesta por ID
        Optional<ComPropuesta> propuestaOpt = propuestaService.findById(idPropuesta);
        if (propuestaOpt.isEmpty()) {
            response.put("message", "Propuesta no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        ComPropuesta propuesta = propuestaOpt.get();

        Optional<ComPropuestaMotivoRechazo> mtvRechazo = propuestaService.buscarPorIdRechazo(idPropuesta.longValue());

        // Obtener TarifarioMoneda
        List<TarifarioMoneda> lstMonedas = propuestaService.obtenerTiposMoneda();

        // Obtener ComProductoServicio
        List<ComProductoServicio> lstProductos = propuestaService.obtenerTiposProductos();

        // Obtener archivos adjuntos relacionados con la propuesta
        List<RepArchivo> listaArchivos = repArchivoService.getRepArchivos(idPropuesta.longValue());
        List<xAdjunto> lstAdjuntos = new ArrayList<>();
        for (RepArchivo archivo : listaArchivos) {
            // Filtrar por la tabla "mantenimiento_propuestas"
            if ("mantenimiento_propuestas".equals(archivo.getTabla())) {
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

        // Agregar información al response
        response.put("motivoRechazo", mtvRechazo);
        response.put("listado", listado);
        response.put("propuesta", propuesta);
        response.put("TarifarioMoneda", lstMonedas);
        response.put("TiposProductos", lstProductos);
        response.put("Adjuntos", lstAdjuntos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePropose(
            @RequestParam(name = "idPropuesta") Integer idPropuesta,
            @RequestParam(required = false, name = "fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
            @RequestParam(required = false, name = "productoServicio") Integer prodServ,
            @RequestParam(required = false, name = "monto") Double monto,
            @RequestParam(required = false, name = "moneda") Long monedaId,
            @RequestParam(required = false, name = "descripcion") String descripcion,
            @RequestParam(required = false, name = "estado") Integer estado,
            @RequestParam(required = false, name = "files") MultipartFile[] files,
            @RequestParam(required = false, name = "fncfiles") String[] funcs,
            @RequestParam(name = "updateUser") String usuario) {
        Map<String, Object> response = new HashMap<>();
        boolean datosActualizados = false;

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
        // Buscar la propuesta existente
        Optional<ComPropuesta> propuestaOpt = propuestaService.findById(idPropuesta);
        if (propuestaOpt.isEmpty()) {
            response.put("mensaje", "Propuesta no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ComPropuesta propuesta = propuestaOpt.get();

        // Validar y actualizar producto/servicio si se proporciona
        if (prodServ != null) {
            ComProductoServicio productoServicio = productoServicioDao.findById(prodServ).orElse(null);
            if (productoServicio == null) {
                response.put("mensaje", "No existe el producto/servicio enviado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            propuesta.setProdServ(prodServ);
            datosActualizados = true;
        }

        // Validar y actualizar moneda si se proporciona
        if (monedaId != null) {
            TarifarioMoneda moneda = tarifarioMonedaService.buscaId(monedaId);
            if (moneda == null) {
                response.put("mensaje", "No existe el tipo de moneda enviado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            propuesta.setMoneda(monedaId);
            datosActualizados = true;
        }

        // Actualizar campos opcionales
        if (fecha != null) {
            propuesta.setFecha(fecha);
            datosActualizados = true;
        }

        if (monto != null) {
            propuesta.setMonto(BigDecimal.valueOf(monto));
            datosActualizados = true;
        }

        if (descripcion != null) {
            propuesta.setDescripcion(descripcion);
            datosActualizados = true;
        }

        if (estado != null) {
            propuesta.setEstado(estado);
            datosActualizados = true;
        }

        // Verificar si se han proporcionado datos para actualizar
        if (!datosActualizados) {
            response.put("mensaje", "No se proporcionaron datos para actualizar.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Establecer información de actualización
        propuesta.setUpdateUser(usuario);
        propuesta.setUpdateDate(LocalDateTime.now());

        try {
            // Guardar la propuesta actualizada
            propuestaService.save(propuesta);
        } catch (Exception e) {
            response.put("mensaje", "Se produjo un error en la actualización de la propuesta.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Procesamiento de archivos
        String msgAdjuntos = "";
        if (files != null) {
            List<String> filenames = new ArrayList<>();
            try {
                RepArchivoEtiqueta etiqueta = archivoEtiquetaService.findByNombre("mantenimiento_propuestas")
                        .orElse(null);
                AdmFolder folder = folderService.findById(etiqueta.getIdFolder());
                String pathFile = folder.getPathfolder() + "/" + folder.getNomfolder() + "/"
                        + propuesta.getId().toString();
                ResponseEntity<?> sw = is3Service.readFolder(pathFile);
                if (sw.getStatusCodeValue() != 200) {
                    is3Service.createFolder(folder.getPathfolder() + "/" + folder.getNomfolder() + "/", propuesta.getId().toString());
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
                            RepArchivo adjunto = new RepArchivo();
                            adjunto.setIdFolder(folder.getId());
                            adjunto.setIdModuloFuncionalidad(tipoArchivo);
                            adjunto.setNombre(file.getOriginalFilename());
                            adjunto.setIdArchivoFormato(idFormato);
                            adjunto.setIdtabla(propuesta.getId().longValue());
                            adjunto.setTabla("mantenimiento_propuestas");
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
                response.put("message_files", msgAdjuntos);
            }
        }
        response.put("mensaje", "Se actualizó correctamente la propuesta.");
        response.put("propuesta", propuesta);
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

    @PostMapping("/changeStatus")
    public ResponseEntity<?> cambiarEstadoPropuesta(
            @RequestParam(name = "idPropuesta") Integer idPropuesta,
            @RequestParam(name = "nuevoEstado") Integer nuevoEstado,
            @RequestParam(name = "usuario") String usuario,
            @RequestParam(required = false, name = "motivoRechazo") String motivoRechazo,
            @RequestParam(required = false, name = "fechaRechazo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaRechazo) {
        return propuestaService.cambiarEstadoPropuesta(idPropuesta, nuevoEstado, usuario, motivoRechazo, fechaRechazo);
    }
}
