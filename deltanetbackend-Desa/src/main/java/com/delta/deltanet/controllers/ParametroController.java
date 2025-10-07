package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.Parametro;
import com.delta.deltanet.models.service.IDistritoService;
import com.delta.deltanet.models.service.IParametroService;
import com.delta.deltanet.models.service.IProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/parametro")
public class ParametroController {
    @Autowired
    private IParametroService parametroService;

    @Autowired
    private IProvinciaService provinciaService;

    @Autowired
    private IDistritoService distritoService;

    public ParametroController(IParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> ReadAllParams(@RequestParam(name="idRubro") Long idRubro){
        Map<String, Object> response = new HashMap<>();

        if (idRubro == null){
            response.put("mensaje", "Error se esperaba el parametro idRubro");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            List<Parametro> parametros = parametroService.findByParams(idRubro);
            return new ResponseEntity<List<Parametro>>(parametros,HttpStatus.OK);
        }catch (Exception e){
            response.put("mensaje", "Error al realizar la busqueda de parametros");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/lstProvincias")
    public ResponseEntity<?> lstProvincias(@RequestParam(name="idDpto") Long idDpto){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> provincias = provinciaService.lstPorDpto(idDpto);
            return new ResponseEntity<List<Object>>(provincias,HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al listar provincias");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/lstDistritos")
    public ResponseEntity<?> lstDistritos(@RequestParam(name="idProv") Long idProv){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> distritos = distritoService.lstPorProv(idProv);
            return new ResponseEntity<List<Object>>(distritos,HttpStatus.OK);
        } catch (Exception e){
            response.put("mensaje", "Error al listar distritos");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
