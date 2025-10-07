package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import com.delta.deltanet.models.service.AdmFolderServiceImpl;
import com.delta.deltanet.models.service.RepArchivoFuncServiceImpl;
import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/submodulo-funcionality")
public class RepArchivoFuncController {

    @Autowired
    private RepArchivoFuncServiceImpl subfuncService;

    @Autowired
    private AdmFolderServiceImpl folderService;

    @GetMapping("/list")
    public ResponseEntity<?> getSubmoduloFunc() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<RepArchivoFuncionalidad> registros = subfuncService.findAll();
            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error inesperado en la funcion list de submodulo-funcionality");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> create() {
        Map<String, Object> response = new HashMap<>();
        List<AdmFolder> folders = folderService.findAll();
        response.put("folders", folders);
        return new ResponseEntity<>(response, HttpStatus.OK);
        
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSubFunc(@RequestParam(name = "name") String nombre,
            @RequestParam(name = "id_folder") Long id_folder,
            @RequestParam(name = "key") String key,
            @RequestParam(name = "username") String createUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validación para id_submodulo
        	AdmFolder folder = null;
            if (id_folder != null) {
                folder = folderService.findById(id_folder);
                if (folder == null) {
                    response.put("message", "El id del folder: [" + id_folder + "] no existe");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            RepArchivoFuncionalidad funcionalidad = new RepArchivoFuncionalidad();
            funcionalidad.setFolder(folder);
            funcionalidad.setNombre(nombre);
            funcionalidad.setClave(key);
            funcionalidad.setUsrCreate(createUser);
            funcionalidad.setFecCreate(new Date());
            funcionalidad.setEstado(1);
            RepArchivoFuncionalidad saveFunc = subfuncService.save(funcionalidad);
            response.put("subfuncionalidad", saveFunc);
            response.put("message", "Funcionalidad creada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error al crear la funcionalidad");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/show")
    public ResponseEntity<?> getSubFunc(@RequestParam(name = "id") Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivoFuncionalidad> subFuncOpt = subfuncService.busca(id);
            if (!subFuncOpt.isPresent()) {
                response.put("message", "La sub_func con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoFuncionalidad sub_func = subFuncOpt.get();
            Map<String, Object> sub_funcMap = new HashMap<>();
            sub_funcMap.put("id", sub_func.getIdArchivoFunc());
            sub_funcMap.put("folder", sub_func.getFolder());
            sub_funcMap.put("sub_func_nombre", sub_func.getNombre());
            sub_funcMap.put("key", sub_func.getClave());
            sub_funcMap.put("create_user", sub_func.getUsrCreate());
            sub_funcMap.put("create_date", sub_func.getFecCreate());
            sub_funcMap.put("update_user", sub_func.getUsrUpdate());
            sub_funcMap.put("update_date", sub_func.getFecUpdate());
            response.put("sub_func", sub_funcMap);
            response.put("message", "Búsqueda exitosa");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error inesperado en la función show de sub_func");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSubFunc(@RequestParam(name = "id") Long id,
            @RequestParam(required = true, name = "name") String nombre,
            @RequestParam(required = true, name = "id_folder") Long id_folder,
            @RequestParam(required = true, name = "key") String key,
            @RequestParam(name = "username") String updateUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<RepArchivoFuncionalidad> subFuncOpt = subfuncService.busca(id);
            if (!subFuncOpt.isPresent()) {
                response.put("message", "La sub funcionalidad con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoFuncionalidad sub_func = subFuncOpt.get();
            AdmFolder folder = folderService.findById(id_folder);
            if (folder == null) {
                response.put("message", "El folder con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            sub_func.setNombre(nombre);
            sub_func.setFolder(folder);
            sub_func.setClave(key);
            sub_func.setUsrUpdate(updateUser);
            sub_func.setFecUpdate(new Date());
            subfuncService.save(sub_func);
            response.put("message", "La sub funcionalidad fue actualizada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al actualizar la sub funcionalidad");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteEtiqueta(@RequestParam(name = "id") Long id,
            @RequestParam(name = "username") String username) {
        Map<String, Object> response = new HashMap<>();
        String accion = "";
        try {
            Optional<RepArchivoFuncionalidad> subFuncOpt = subfuncService.busca(id);
            if (!subFuncOpt.isPresent()) {
                response.put("message", "La subFunc con ID [" + id + "] no se encuentra.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RepArchivoFuncionalidad subFunc = subFuncOpt.get();
            if (subFunc.getEstado() == 0) {
                subFunc.setEstado(1);
                accion = "activado";
            } else {
                subFunc.setEstado(0);
                accion = "desactivado";
            }
            subFunc.setUsrUpdate(username);
            subFunc.setFecUpdate(new Date());
            RepArchivoFuncionalidad deletedEtiqueta = subfuncService.save(subFunc);
            response.put("subFunc", deletedEtiqueta);
            response.put("message", "Funcionalidad " + accion + " satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error al eliminar la subFunc");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
