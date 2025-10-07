package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioMes;
import com.delta.deltanet.models.entity.AdmTipoDiaSemana;
import com.delta.deltanet.models.entity.CalendarioHistorico;
import com.delta.deltanet.models.service.AdmCalendarioHistoricoService;
import com.delta.deltanet.models.service.IAdmCalendarioAnioService;
import com.delta.deltanet.models.service.IAdmCalendarioMesService;
import com.delta.deltanet.models.service.IAdmTipoDiaSemanaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/calendario-historico")
public class CalendarioHistoricoController {

    @Autowired
    private AdmCalendarioHistoricoService hisCalendarService;
    @Autowired
	private IAdmCalendarioMesService calendarioMesService;
	@Autowired
	private IAdmTipoDiaSemanaService calendarioTipoSemanaService;
    @Autowired
	private IAdmCalendarioAnioService calendarioAnioService;

    @GetMapping("/search")
    public List<CalendarioHistorico> buscarPorFiltros(
            @RequestParam(required = false) Long idAnio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            @RequestParam(required = false) Long idMes,
            @RequestParam(required = false) Long idSemanaDiaTipo,
            @RequestParam(required = false) Long estado
    ) {
        return hisCalendarService.buscarPorFiltros(idAnio, fecha, idMes, idSemanaDiaTipo, estado);
    }

    @GetMapping("/index")
	public ResponseEntity<?> index() {
        List<AdmCalendarioAnio> years = calendarioAnioService.buscarPorFiltros("", 1, 2L);
		List<AdmCalendarioMes> months = calendarioMesService.buscarPorFiltros("", 1);
		List<AdmTipoDiaSemana> types = calendarioTipoSemanaService.buscarPorFiltros("", 1);
		Map<String, Object> response = new HashMap<>();
        response.put("years", years);
		response.put("months", months);
		response.put("types", types);
		return ResponseEntity.ok(response);
	}

    @PostMapping("/calendar")
    public ResponseEntity<?> calendarView(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        List<CalendarioHistorico> days = hisCalendarService.buscarPorFiltros(null, null, null, null, 1L);
        List<Map<String, Object>> holidays = days.stream()
                .filter(d -> d.getIdFeriado() != null)
                .map((d) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idFeriado", d.getIdFeriado());
                    map.put("fecha", d.getFecha());
                    map.put("calendarioFeriado", d.getCalendarioFeriado());
                    return map;
                })
               .collect(Collectors.toList());
        List<Map<String, Object>> years = days.stream()
                .filter(d -> d.getIdAnio() != null && d.getCalendarioAnio() != null)
                .collect(java.util.stream.Collectors.toMap(
                        d -> d.getIdAnio(),
                        d -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("idAnio", d.getIdAnio());
                            map.put("calendarioAnio", d.getCalendarioAnio());
                            return map;
                        },
                        (a, b) -> a
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
        response.put("holidays", holidays);
        response.put("days", days);
        response.put("years", years);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> show(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        CalendarioHistorico calendario = hisCalendarService.buscarPorFiltros(null, null, null, null, null)
            .stream()
            .filter(c -> c.getId() != null && c.getId().equals(id))
            .findFirst()
            .orElse(null);
        if (calendario == null) {
            response.put("message", "El id " + id + " no existe o no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("calendarioHistorico", calendario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}