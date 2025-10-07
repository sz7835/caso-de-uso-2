package com.delta.deltanet.controllers;

import com.delta.deltanet.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class S3Controller {

    @Autowired
    IS3Service is3Service;

    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadFiles(@RequestParam(name="path") String path,
                                         @RequestParam(name="file") String file){
        return is3Service.downloadFile(path,file);
    }

    @PostMapping("/copyFile")
    public ResponseEntity<?> copyFile(@RequestParam(name = "file") String file,
                                      @RequestParam(name = "pathSource") String source,
                                      @RequestParam(name = "pathTarget") String target){
        return is3Service.copyFile(file, source, target);
    }

    @PostMapping("/moveFile")
    public ResponseEntity<?> moveFile(@RequestParam(name = "file") String file,
                                      @RequestParam(name = "pathSource") String source,
                                      @RequestParam(name = "pathTarget") String target){
        return is3Service.moveFile(file, source, target);
    }

    @PostMapping("/renameFile")
    public ResponseEntity<?> renameFile(@RequestParam(name = "path") String path,
                                      @RequestParam(name = "filenameOri") String fileOri,
                                      @RequestParam(name = "filenameNew") String fileNew){
        return is3Service.renameFile(path, fileOri, fileNew);
    }

    @PostMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam(name = "file") String file,
                                      @RequestParam(name = "pathSource") String source){
        return is3Service.deleteFile(file, source);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(name="files") MultipartFile file,
                                        @RequestParam(name="path") String path,
                                        @RequestParam(name="nameFile") String nameFile){
    	return is3Service.uploadFile(file,path,nameFile);
    }



    /*@PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(name="files") MultipartFile[] files,
                                        @RequestParam(name="path") String path){
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> filenames = new ArrayList<>();
            Arrays.asList(files).stream().forEach(file -> {
                ResponseEntity<?> ret = is3Service.uploadFile(file,path);
                if(ret.getStatusCodeValue()==200)
                    filenames.add(file.getOriginalFilename() + " subido correctamente.");
                else
                    filenames.add(file.getOriginalFilename() + " presento error al subirlo.");
            });
            response.put("message","Subida de archivos terminada satisfactoriamente");
            response.put("archivos",filenames);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Se presento problemas al subir los archivos.");
            response.put("Error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }*/

    @PostMapping("/createFolder")
    public ResponseEntity<?> createFolder(@RequestParam(name="path") String ruta,
                                          @RequestParam(name = "directory") String carpeta,
                                          @RequestParam(name = "user") String usuario){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(ruta.isEmpty()){
            response.put("message","El parametro PATH no tiene valor.");
            valida=false;
        }

        if(carpeta.isEmpty()){
            response.put("message","El parametro DIRECTORY no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        return is3Service.createFolder(ruta, carpeta, usuario);
    }

    @GetMapping("/readFolder")
    public ResponseEntity<?> readFolder(@RequestParam(name = "path") String ruta){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(ruta.isEmpty()){
            response.put("message","El parametro PATH no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        return is3Service.readFolder(ruta);
    }
    
    @GetMapping("/listado")
    public ResponseEntity<?> listContenido(@RequestParam(name = "path") String ruta){
    	Map<String, Object> response = new HashMap<>();
        boolean valida = true;
        
        if(ruta.isEmpty()){
            response.put("message","El parametro PATH no tiene valor.");
            valida=false;
        }
        
        if (!valida) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return is3Service.listado(ruta);
    }
    
    @GetMapping("/lstcarpetas")
    public ResponseEntity<?> listCarpetas(@RequestParam(name = "path") String ruta){
    	Map<String, Object> response = new HashMap<>();
        boolean valida = true;
        
        if(ruta.isEmpty()){
            response.put("message","El parametro PATH no tiene valor.");
            valida=false;
        }
        
        if (!valida) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return is3Service.lstcarpetas(ruta);
    }

    @PostMapping("/updateFolder")
    public ResponseEntity<?> updateFolder(@RequestParam(name = "path") String path,
                                          @RequestParam(name = "oldDirectory") String oldDirectory,
                                          @RequestParam(name = "newDirectory") String newDirectory,
                                          @RequestParam(name = "user") String usuario){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(path.isEmpty()){
            response.put("message","El parametro path no tiene valor.");
            valida=false;
        }
        if(oldDirectory.isEmpty()){
            response.put("message","El parametro oldDirectory no tiene valor.");
            valida=false;
        }
        if(newDirectory.isEmpty()){
            response.put("message","El parametro newDirectory no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        return is3Service.updateFolder(path, oldDirectory, newDirectory, usuario);
    }

    @PostMapping("/deleteFolder")
    public ResponseEntity<?> deleteFolder(@RequestParam(name = "path") String path,
                                          @RequestParam(name = "directory") String directory,
                                          @RequestParam(name = "user") String usuario){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(path.isEmpty()){
            response.put("message","El parametro path no tiene valor.");
            valida=false;
        }
        if(directory.isEmpty()){
            response.put("message","El parametro directory no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        return is3Service.deleteFolder(path, directory, usuario);
    }

    @PostMapping("/moveFolder")
    public ResponseEntity<?> moveFolder(@RequestParam(name = "directory") String directory,
                                          @RequestParam(name = "oldDirectory") String oldDirectory,
                                          @RequestParam(name = "newDirectory") String newDirectory){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(directory.isEmpty()){
            response.put("message","El parametro directory no tiene valor.");
            valida=false;
        }
        if(oldDirectory.isEmpty()){
            response.put("message","El parametro oldDirectory no tiene valor.");
            valida=false;
        }
        if(newDirectory.isEmpty()){
            response.put("message","El parametro newDirectory no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        return is3Service.moveFolder(directory, oldDirectory, newDirectory);
    }

    @GetMapping("/openFolder")
    public ResponseEntity<?> openFolder(@RequestParam(name = "path") String ruta,
                                        @RequestParam(name = "directory") String direc){
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        if(ruta.isEmpty()){
            response.put("message","El parametro PATH no tiene valor.");
            valida=false;
        }
        if(direc.isEmpty()){
            response.put("message","El parametro DIRECTORY no tiene valor.");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        return is3Service.openFolder(ruta,direc);
    }
}

