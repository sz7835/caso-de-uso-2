package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.ActivarRegistroHorasRequest;
import com.delta.deltanet.models.entity.Contrato2024;
import com.delta.deltanet.models.entity.CreateRegistroHorasRequest;
import com.delta.deltanet.models.entity.OutRegistroHoras;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.RegistroHorasResponse;
import com.delta.deltanet.models.entity.RegistroProyecto;
import com.delta.deltanet.models.entity.Relacion;
import com.delta.deltanet.models.entity.ShowDataResponse;
import com.delta.deltanet.models.service.IContrato2024Service;
import com.delta.deltanet.models.service.IPersonaService;
import com.delta.deltanet.models.service.IRelacionService;
import com.delta.deltanet.models.service.OutRegistroHorasService;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/registro-horas")
public class RegistroHorasController {

    private final OutRegistroHorasService registroHorasService;

    public RegistroHorasController(OutRegistroHorasService registroHorasService) {
        this.registroHorasService = registroHorasService;
    }
    
    @Autowired
    public IRelacionService relService;

    @Autowired
    public IContrato2024Service contratoService;
    
    @Autowired
    public IPersonaService perService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createRegistroHoras(@RequestBody CreateRegistroHorasRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Llama al servicio para crear el registro de horas usando el idCronograma
            List<OutRegistroHoras> registros = registroHorasService.createRegistroHoras(
                    request.getIdProyecto(),
                    request.getIdPersona(),
                    request.getDetalle(),
                    request.getDia(),
                    request.getCreateUser());

            response.put("list", registros);
            response.put("message", "Horas registradas exitosamente en el sistema");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("error", "Error al crear el registro de horas: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRegistroHoras(
            @RequestParam int id,
            @RequestParam(required = false) String actividad,
            @RequestParam(required = false) Integer horas,
            @RequestParam String update_user) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<OutRegistroHoras> updatedRegistro = registroHorasService.updateRegistroHoras(id, actividad, horas,
                    update_user);

            if (updatedRegistro.isPresent()) {
                response.put("message", "Se actualizó el registro correctamente.");
                response.put("registro", updatedRegistro.get());
                return ResponseEntity.ok().body(response);
            } else {
                response.put("message", "Error! Registro no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error! No se pudo actualizar el registro de horas");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<List<Map<String, Object>>> activarRegistroHoras(
            @RequestBody ActivarRegistroHorasRequest request) {
        List<Map<String, Object>> responseList = new ArrayList<>();

        try {
            List<Map<String, Integer>> registros = request.getRegistro();
            String updateUser = request.getUpdateUser();

            List<Map<String, Object>> resultados = registroHorasService.activarRegistroHoras(
                    registros.stream()
                            .map(registro -> registro.get("id"))
                            .collect(Collectors.toList()),
                    updateUser);

            // Asociar resultados con el formato del message
            for (Map<String, Object> resultado : resultados) {
                Map<String, Object> response = new HashMap<>();
                Integer id = (Integer) resultado.get("id");

                if (resultado.containsKey("message")) {
                    response.put("message", resultado.get("message"));

                    if (resultado.containsKey("registro")) {
                        response.put("registro", resultado.get("registro"));
                    }
                } else {
                    response.put("message", "Error! Registro con id \"" + id + "\" no encontrado.");
                }

                responseList.add(response);
            }

            return ResponseEntity.ok().body(responseList);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error! No se pudo activar el registro de horas");
            responseList.add(errorResponse);
            return ResponseEntity.badRequest().body(responseList);
        }
    }

    @PostMapping("/mostrarProyecto")
    public ResponseEntity<Object> mostrarProyecto(@RequestParam("idPersona") Long idPersona) {
        try {
        	List<RegistroProyecto> resultado = registroHorasService.mostrarProyecto(idPersona);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            // Manejo de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el cronograma");
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> showRegistroHoras(@RequestParam int id) {
        // Llama al servicio para obtener los datos y construir la respuesta
        ShowDataResponse response = registroHorasService.show(id);

        // Puedes manejar la respuesta según tus necesidades y retornar el código de
        // estado correspondiente
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteRegistroHoras(@RequestParam int id) {
        Map<String, Object> response = registroHorasService.deleteRegistroHoras(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> index(
            @RequestParam(name = "idPersona", required = true) Integer idPersona,
            @RequestParam(name = "idRel", required = false) Long idRel,
            @RequestParam(name = "meses", required = false) Integer meses,
            @RequestParam(name = "estado", required = true) Integer estado,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        Map<String, Object> response = new HashMap<>();
        Persona per = null;
            // Llamar al servicio registroHoras con el idCronograma obtenido
        if(idRel > 0) {
        	Contrato2024 contract = contratoService.findByRelacion2(idRel);
            if(contract != null) {
            	per = perService.buscarId(contract.getIdCliente());
            }
        }
        List<OutRegistroHoras> resultados = registroHorasService.index(idPersona, meses, estado,
                    fechaInicio, fechaFin);
        response.put("data", resultados);
        response.put("client", per);
        return ResponseEntity.ok(response);
    }
}
