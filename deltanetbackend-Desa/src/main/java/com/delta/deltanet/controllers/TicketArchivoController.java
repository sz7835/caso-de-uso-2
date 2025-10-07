package com.delta.deltanet.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.delta.deltanet.models.entity.Archivo;
import com.delta.deltanet.models.entity.Comentario;
import com.delta.deltanet.models.entity.Historial;
import com.delta.deltanet.models.entity.SisParam;
import com.delta.deltanet.models.service.IArchivoService;
import com.delta.deltanet.models.service.IComentarioService;
import com.delta.deltanet.models.service.IHistorialService;
import com.delta.deltanet.models.service.ITipoAccionService;
import com.delta.deltanet.models.service.SisParamServiceImpl;
import com.delta.deltanet.services.IS3Service;


@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/ticket")
public class TicketArchivoController {

	@Autowired
	private IArchivoService archivoService;

	@Autowired
	private ITipoAccionService tipoAccionService;
	@Autowired
	private IHistorialService historialService;
	@Autowired
	private IComentarioService comentarioService;
	@Autowired
    private SisParamServiceImpl sisParamService;
	@Autowired
    IS3Service is3Service;
	//VariableEntorno
	@Value("#{${tablas}}")
	private Map<String,String> tablas;
	@Value("#{${acciones}}")
	private Map<String,String> acciones;
	@Value("#{${accionesNew}}")
	private Map<String, String> accionesNew;

    //ARCHIVO
	@PostMapping("/archivo/CreateArchivo")
	public ResponseEntity<?> uploadFiles(
			@RequestParam("files") MultipartFile[] files,
			@RequestParam("IdTabla") Long idTabla,
			@RequestParam("Tabla") String tabla,
			@RequestParam("usuario") String usuario) {

		Map<String, Object> response = new HashMap<>();

		// Verificar si ya existen archivos para esta tabla y ID
		List<Archivo> lstFiles = archivoService.findByTablaAndTablaId(tabla, idTabla);
		if (!lstFiles.isEmpty()) {
			response.put("mensaje", "Ya existen archivos para la tabla: " + tabla + ", TablaId: " + idTabla);
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		try {
			// Obtener el bucket S3
			SisParam param = sisParamService.buscaEtiqueta("S3_BUCKETNAME");
			String bucketName = param.getValor();

			String folderName;
			switch (tabla.toLowerCase()) {
				case "ticket":
					folderName = "rep_archivo_ticket";
					break;
				case "comentario":
					folderName = "rep_archivo_comentario";
					break;
				default:
					folderName = "rep_archivo_otros";
					break;
			}

			String pathFile = bucketName + "/" + folderName;

			// Verificar si la carpeta ya existe en S3, si no, crearla
			ResponseEntity<?> sw = is3Service.readFolder(pathFile);
			if (sw.getStatusCodeValue() != 200) {
				is3Service.createFolder(bucketName, folderName);
			}

			List<String> fileNames = new ArrayList<>();

			// Subir archivos y guardarlos en la base de datos
			for (MultipartFile file : files) {
				// Generar un nombre único para evitar sobrescribir archivos
				String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
				String key = String.format("%s/%s.%s", folderName, UUID.randomUUID(), extension);

				ResponseEntity<?> ret = is3Service.uploadFile(file, pathFile, key);
				if (ret.getStatusCodeValue() == 200) {
					HashMap<String, String> result = (HashMap<String, String>) ret.getBody();
					String url = result.get("url");

					// Guardar en la BD
					Archivo archivo = new Archivo();
					archivo.setTabla(tabla);
					archivo.setTablaId(idTabla);
					archivo.setNombre(file.getOriginalFilename());
					archivo.setUrl(url);
					archivoService.save(archivo);

					fileNames.add(file.getOriginalFilename());
				} else {
					fileNames.add(file.getOriginalFilename() + " presentó un error al subir.");
				}
			}

			// Registrar en historial
			Historial historial = new Historial();
			String messageHistory = "";

			switch (tabla.toLowerCase()) {
				case "ticket":
					historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ADJUNTAR_ARCHIVO"))));
					historial.setTabla(tablas.get("TICKET"));
					messageHistory = usuario + " adjuntó uno o más archivos en el ticket ";
					historial.setAccion(messageHistory);
					historial.setUsuCreado(usuario);
					historial.setFechaCreado(new Date());
					historial.setTablaId(idTabla);
					historialService.save(historial);
					break;

				case "comentario":
					Comentario comment = comentarioService.findById(idTabla);
					historial.setTipoAccionId(tipoAccionService.findById(Long.valueOf(accionesNew.get("ADJUNTAR_ARCHIVO"))));
					historial.setTabla(tablas.get("TICKET"));
					messageHistory = usuario + " adjuntó uno o más archivos en un comentario del ticket ";
					historial.setAccion(messageHistory);
					historial.setUsuCreado(usuario);
					historial.setFechaCreado(new Date());
					historial.setTablaId(comment.getTicket().getId());
					historialService.save(historial);
					break;
			}

			response.put("mensaje", "Archivos subidos correctamente: " + fileNames);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.put("mensaje", "Falla al subir archivos...");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/archivo/ReadAllArchivo")
	public ResponseEntity<?> readFiles(@RequestParam("IdTabla") Long idTabla,
                                         @RequestParam("Tabla") String Tabla
                                        ){
		
		Map<String, Object> response = new HashMap<>();
		try {
			List<Archivo> archivos = archivoService.findByTablaAndTablaId(Tabla, idTabla);
			if (archivos==null) {
				response.put("mensaje", "No se encontraron archivos.");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			response.put("archivos", archivos);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "No se obtuvo listado de archivos");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		}
	}

	@GetMapping("/archivo/download/file/{nombre}")
	public ResponseEntity<?> downloadFiles(@PathVariable String nombre){
		Map<String, Object> response = new HashMap<>();

		try {
			Resource resource = archivoService.load(nombre);
			return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
		} catch(Exception e) {
			response.put("mensaje", "No se pudo acceder al archivo.");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value="/archivo/read/{nombre}")
	public ResponseEntity<Object> loadFile(@PathVariable String nombre){
		try {
			Resource resource = archivoService.load(nombre);
			MediaType mediaType;
			
			InputStreamResource in = new InputStreamResource(resource.getInputStream());
			
			String mime = archivoService.getType(nombre);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			
			if(mime.contains("image")) {
				mediaType = MediaType.IMAGE_JPEG;
			}else{
				mediaType = MediaType.parseMediaType("application/txt");
				headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", nombre));
			}
			
			ResponseEntity<Object> result = ResponseEntity.ok().headers(headers).contentLength(resource.getFile().length()).contentType(mediaType).body(in);
			
			return result;			
		} catch (Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(),HttpStatus.MULTI_STATUS);
		}
	}
	
	
	
	
	@DeleteMapping("/archivo/DeleteArchivo/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable Long id){
		Map<String, Object> response = new HashMap<>();
		try {
			archivoService.delete(id);
			response.put("mensaje","Archivo eliminado satisfactoriamente.");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje","No se elimino el archivo.");
			response.put("error",e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}


}