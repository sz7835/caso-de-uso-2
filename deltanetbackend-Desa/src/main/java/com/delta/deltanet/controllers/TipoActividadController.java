package com.delta.deltanet.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.dao.ActividadRepository;
import com.delta.deltanet.models.entity.Actividad;
import com.delta.deltanet.models.entity.TipoActividad;
import com.delta.deltanet.models.service.TipoActividadService;

@SuppressWarnings("all")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/actividades")
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;
    private final ActividadRepository actividadRepository;

    @Autowired
    public TipoActividadController(TipoActividadService tipoActividadService, ActividadRepository actividadRepository) {
        this.tipoActividadService = tipoActividadService;
        this.actividadRepository = actividadRepository;
    }

    @GetMapping("/tipoActividad")
    public List<TipoActividad> listarTodasLasActividades() {
        return tipoActividadService.listarTodasLasActividades();
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> crearActividad(
            @RequestParam(required = true, name = "idTipoAct") int idTipoAct,
            @RequestParam(required = true, name = "personaId") int personaId,
            @RequestParam(required = true, name = "fecha") String fecha,
            @RequestParam(required = true, name = "hora") String hora,
            @RequestParam(required = false, defaultValue = "", name = "detalle") String detalle,
            @RequestParam(required = true, name = "createUser") String createUser) throws ParseException {

        Map<String, Object> response = tipoActividadService.crearActividad(idTipoAct, personaId, fecha, hora, detalle,
                createUser);
        if (response.get("error").equals(true)) {
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Map<String, Object>>> filtrarActividades(
            @RequestParam(required = true, name = "idActividad") Integer idActividad,
            @RequestParam(required = true, name = "idPersona") Integer idPersona,
            @RequestParam(required = true, name = "registro") String registroStr) {

        List<Map<String, Object>> actividades = tipoActividadService.filtrarActividades(idActividad, idPersona,
                registroStr);

        if (actividades.isEmpty() || (actividades.size() == 1 && actividades.get(0).isEmpty())) {
            return ResponseEntity.ok(actividades);
        }

        return ResponseEntity.ok(actividades);
    }
}
