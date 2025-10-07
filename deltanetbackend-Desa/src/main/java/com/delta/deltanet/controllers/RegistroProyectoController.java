package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.delta.deltanet.models.entity.AdmCronograma;
import com.delta.deltanet.models.entity.OutRegistroHoras;
import com.delta.deltanet.models.entity.RegistroProyecto;
import com.delta.deltanet.models.service.OutRegistroHorasService;
import com.delta.deltanet.models.service.RegistroProyectoService;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/registro-proyecto")
public class RegistroProyectoController {

    private final RegistroProyectoService registroProyectoService;

    public RegistroProyectoController(RegistroProyectoService registroProyectoService) {
        this.registroProyectoService = registroProyectoService;
    }

	@Autowired
    private OutRegistroHorasService registroHorasService;
    // @PostMapping("/save")
    // public ResponseEntity<Map<String, Object>> saveRegistroProyecto(
    // @RequestParam("idCronograma") int idCronograma,
    // @RequestParam("proyectoDescripcion") String proyectoDescripcion,
    // @RequestParam("createUser") String createUser) {

    // RegistroProyecto nuevoRegistro = registroProyectoService.save(idCronograma,
    // proyectoDescripcion, createUser);

    // Map<String, Object> response = new HashMap<>();
    // response.put("message", "Guardado con éxito");
    // response.put("registro", nuevoRegistro);

    // return ResponseEntity.ok(response);
    // }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveRegistroProyecto(
            @RequestParam("idConsultor") Long idConsultor,
            @RequestParam("codigo") String codigo,
            @RequestParam("proyectoDescripcion") String proyectoDescripcion,
            @RequestParam("createUser") String createUser) {

        Map<String, Object> response = new HashMap<>();

        // Guarda el nuevo registro con el idCronograma encontrado
        RegistroProyecto nuevoRegistro = registroProyectoService.save(idConsultor, codigo, proyectoDescripcion,
                createUser);

        response.put("message", "Guardado con éxito");
        response.put("registro", nuevoRegistro);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRegistroProyecto(@RequestParam("idProyecto") Long idProyecto,
            @RequestParam(value = "proyectoDescripcion", required = false) String proyectoDescripcion,
            @RequestParam("updateUser") String updateUser) {

        Optional<RegistroProyecto> registroActualizado = registroProyectoService.updateRegistroProyecto(idProyecto,
                proyectoDescripcion, updateUser);

        if (registroActualizado.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Actualizado con éxito");
            response.put("registro", registroActualizado.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // @PostMapping("/change-status")
    // public ResponseEntity<Map<String, Object>> changeStatus(
    // @RequestParam("idCronograma") int idCronograma,
    // @RequestParam("idProyecto") int idProyecto,
    // @RequestParam("updateUser") String updateUser) {

    // Map<String, Object> response =
    // registroProyectoService.changeStatus(idCronograma, idProyecto, updateUser);

    // return ResponseEntity.ok(response);
    // }

    @PutMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteRegistroProyecto(@RequestParam("idProyecto") Long idProyecto) {
    	List<OutRegistroHoras> list = registroHorasService.findByIdProyecto(idProyecto.intValue());
    	if(list.size() > 0) {
    		Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se puede eliminar el registro por que tiene acciones asociadas.");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.UNPROCESSABLE_ENTITY);
    	}
        Map<String, Object> response = registroProyectoService.delete(idProyecto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activarRegistros(
            @RequestParam(value = "proyects") List<Long> requestData,
            @RequestParam("updateUser") String updateUser) {
        return registroProyectoService.activate(requestData, updateUser);
    }

    @GetMapping("/listCronograma")
    public ResponseEntity<List<AdmCronograma>> findCronogramas(@RequestParam("idConsultor") Integer idConsultor) {
        List<AdmCronograma> cronogramas = registroProyectoService.findCronograma(idConsultor);
        return ResponseEntity.ok(cronogramas.isEmpty() ? Collections.emptyList() : cronogramas);
    }

    // @GetMapping("/index")
    // public ResponseEntity<List<RegistroProyecto>>
    // index(@RequestParam("idConsultor") int idConsultor) {
    // List<RegistroProyecto> registros =
    // registroProyectoService.index(idConsultor);
    // return ResponseEntity.ok(registros);
    // }

    @GetMapping("/index")
    public ResponseEntity<List<Map<String, Object>>> index(
            @RequestParam("idConsultor") Long idConsultor,
            @RequestParam(value = "proyectoDescripcion", required = false) Optional<String> proyectoDescripcion,
            @RequestParam(value = "estado", required = false) Optional<Integer> estado) {

        List<Map<String, Object>> registros = registroProyectoService.index(idConsultor, proyectoDescripcion, estado);
        return ResponseEntity.ok(registros);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> changeStatus(@RequestParam("idProyecto") Long idProyecto,
            @RequestParam("updateUser") String updateUser) {
        Map<String, Object> response = registroProyectoService.changeStatus(idProyecto, updateUser);
        return ResponseEntity.ok(response);
    }
}