package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.RepArchivoEtiquetaServiceImpl;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/etiqueta")
public class EtiquetaController {

  @Autowired
  private RepArchivoEtiquetaServiceImpl etiquetaService;

  @Autowired
  private AdmFolderServiceImpl folderService;

    @GetMapping("/list")
    public ResponseEntity<?> getEtiquetas(){
      Map<String, Object> response = new HashMap<>();
      try {
        List<RepArchivoEtiqueta> registros = etiquetaService.findAll();
        return new ResponseEntity<>(registros, HttpStatus.OK);
      } catch (Exception e){
        response.put("mensaje", "Error inesperado en la funcion list de etiquetas");
        response.put("debug", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    
    @GetMapping("/index")
    public ResponseEntity<?> listarModulosYSubmodulos() {
      Map<String, Object> response = new HashMap<>();
      try {
          List<AdmFolder> folders = folderService.findAll();
          response.put("folders", folders);
          return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
          response.put("mensaje", "Error al listar módulos y submódulos");
          response.put("error", e.getMessage());
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @PostMapping("/show")
    public ResponseEntity<?> getEtiqueta(@RequestParam(name = "id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivoEtiqueta> etiquetaOpt = etiquetaService.getById(id);
            if (!etiquetaOpt.isPresent()) {
                response.put("message", "La etiqueta con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoEtiqueta etiqueta = etiquetaOpt.get();
            Map<String, Object> etiquetaMap = new HashMap<>();
            etiquetaMap.put("id", etiqueta.getIdArchivoEtiqueta());
            etiquetaMap.put("folder", etiqueta.getFolder());
            etiquetaMap.put("etiqueta_nombre", etiqueta.getNombre());
            etiquetaMap.put("etiqueta_descripcion", etiqueta.getDescripcion());
            etiquetaMap.put("etiqueta_estado", etiqueta.getEstado());
            etiquetaMap.put("create_user", etiqueta.getUsrCreate());
            etiquetaMap.put("create_date", etiqueta.getFecCreate());
            etiquetaMap.put("update_user", etiqueta.getUsrUpdate());
            etiquetaMap.put("update_date", etiqueta.getFecUpdate());

            response.put("etiqueta", etiquetaMap);
            response.put("message", "Búsqueda exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la función show de etiquetas");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteEtiqueta(@RequestParam(name = "id") Long id,
                                            @RequestParam(name = "username") String username) {
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<RepArchivoEtiqueta> etiquetaOpt = etiquetaService.getById(id);
            if (!etiquetaOpt.isPresent()) {
                response.put("message", "La etiqueta con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoEtiqueta etiqueta = etiquetaOpt.get();
            if (etiqueta.getEstado()==0) {
                etiqueta.setEstado(1);
                accion = "activado";
            } else {
                etiqueta.setEstado(0);
                accion = "desactivado";
            }
            etiqueta.setUsrUpdate(username);
            etiqueta.setFecUpdate(new Date());
            RepArchivoEtiqueta deletedEtiqueta = etiquetaService.save(etiqueta);
            response.put("etiqueta", deletedEtiqueta);
            response.put("message","Registro " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al eliminar la etiqueta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEtiqueta(@RequestParam(name = "name") String nombre,
                                            @RequestParam(name = "description") String descripcion,
                                            @RequestParam(name = "id_folder") Long id_folder,
                                            @RequestParam(name = "username") String createUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validación para id_modulo
            AdmFolder folder = folderService.findById(id_folder);
            if (folder == null) {
                response.put("message", "El id del módulo: [" + id_folder + "] no existe");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            RepArchivoEtiqueta etiqueta = new RepArchivoEtiqueta();
            etiqueta.setNombre(nombre);
            etiqueta.setDescripcion(descripcion);
            etiqueta.setFolder(folder);
            etiqueta.setEstado(1);
            etiqueta.setUsrCreate(createUser);
            etiqueta.setFecCreate(new Date());
            RepArchivoEtiqueta savedEtiqueta = etiquetaService.save(etiqueta);
            response.put("etiqueta", savedEtiqueta);
            response.put("message", "Etiqueta creada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error al crear la etiqueta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateEtiqueta(@RequestParam(name = "id") Long id,
                                            @RequestParam(required = false, name = "name") String nombre,
                                            @RequestParam(required = false, name = "description") String descripcion,
                                            @RequestParam(required = false, name = "id_folder") Long id_folder,
                                            @RequestParam(name = "username") String updateUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivoEtiqueta> etiquetaOpt = etiquetaService.getById(id);
            if (!etiquetaOpt.isPresent()) {
                response.put("message", "La etiqueta con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoEtiqueta etiqueta = etiquetaOpt.get();
            etiqueta.setNombre(nombre);
            etiqueta.setDescripcion(descripcion);
            AdmFolder folder = folderService.findById(id_folder);
            if (folder == null) {
                response.put("message", "El folder con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            etiqueta.setFolder(folder);
            etiqueta.setUsrUpdate(updateUser);
            etiqueta.setFecUpdate(new Date());
            RepArchivoEtiqueta updatedEtiqueta = etiquetaService.save(etiqueta);
            response.put("etiqueta", updatedEtiqueta);
            response.put("message", "Etiqueta actualizada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al actualizar la etiqueta");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
