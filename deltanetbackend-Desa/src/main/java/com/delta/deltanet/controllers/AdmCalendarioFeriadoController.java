package com.delta.deltanet.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioFeriado;
import com.delta.deltanet.models.entity.AdmCalendarioFeriadoGeneradoPor;
import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;
import com.delta.deltanet.models.service.IAdmCalendarioAnioService;
import com.delta.deltanet.models.service.IAdmCalendarioFeriadoGeneradoPorService;
import com.delta.deltanet.models.service.IAdmCalendarioFeriadoService;
import com.delta.deltanet.models.service.IAdmCalendarioSemanaDiaService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/calendario-holiday")
public class AdmCalendarioFeriadoController {

	@Autowired
	private IAdmCalendarioSemanaDiaService calendarioSemanaDiaService;

	@Autowired
	private IAdmCalendarioFeriadoService calendarioFeriadoService;

	@Autowired
	private IAdmCalendarioAnioService calendarioAnioService;

	@Autowired
	private IAdmCalendarioFeriadoGeneradoPorService calendarioFeriadoGeneradoPorService;

	@GetMapping("/index")
	public ResponseEntity<?> index() {
		List<AdmCalendarioFeriadoGeneradoPor> generates = calendarioFeriadoGeneradoPorService.findAll().stream()
				.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
		List<AdmCalendarioAnio> list = calendarioAnioService.buscarPorFiltros("", 1, 1L);
		AdmCalendarioAnio anio = list.stream().filter(item -> item.getEstado() == 1).findFirst().orElse(null);
		Map<String, Object> response = new HashMap<>();
		response.put("generates", generates);
		response.put("anio", anio);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/create")
	public ResponseEntity<?> create() {
		Map<String, Object> response = new HashMap<>();
		List<AdmCalendarioFeriadoGeneradoPor> generates = calendarioFeriadoGeneradoPorService.findAll().stream()
				.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
		List<AdmCalendarioAnio> years = calendarioAnioService.buscarPorFiltros("", 1, 1L).stream()
				.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
		List<AdmCalendarioSemanaDia> days = calendarioSemanaDiaService.findAll().stream()
				.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
		response.put("days", days);
		response.put("generates", generates);
		response.put("years", years);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/store")
	public ResponseEntity<?> store(@RequestParam Long idAnio,
								   @RequestParam Long idGeneradoPor,
								   @RequestParam String motivo,
								   @RequestParam String aplicableSectorPublico,
								   @RequestParam String aplicableSectorPrivado,
								   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
								   @RequestParam Long idDiaSemana,
								   @RequestParam String createUser) {
		Map<String, Object> response = new HashMap<>();
		motivo = motivo == null ? "" : motivo.trim();
		if (motivo.isEmpty()) {
			response.put("message", "El campo 'motivo' es obligatorio");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!motivo.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
			response.put("message", "El campo 'motivo' no debe contener caracteres especiales");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (motivo.matches("^\\d+$")) {
			response.put("message", "El campo 'motivo' no puede ser completamente numérico");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// Validar duplicado exacto
		List<AdmCalendarioFeriado> duplicados = calendarioFeriadoService.findByMotivoAndCamposClave(
			motivo, idAnio, idGeneradoPor, aplicableSectorPublico, aplicableSectorPrivado, fecha, idDiaSemana);
		if (!duplicados.isEmpty()) {
			response.put("message", "Ya existe un feriado con el mismo motivo y combinación de datos");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			AdmCalendarioFeriado item = new AdmCalendarioFeriado();
			item.setIdAnio(idAnio);
			item.setIdFeriadoGeneradoPor(idGeneradoPor);
			item.setMotivo(motivo);
			item.setAplicableSectorPublico(aplicableSectorPublico);
			item.setAplicableSectorPrivado(aplicableSectorPrivado);
			item.setFecha(fecha);
			item.setIdSemanaDia(idDiaSemana);
			item.setCreateUser(createUser);
			item.setCreateDate(new Date());
			item.setEstado(1L);
			calendarioFeriadoService.save(item);
			response.put("message", "Feriado guardado con éxito");
			response.put("feriado", item);
		} catch (Exception e) {
			response.put("message", "Error al realizar la insercion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/show/{id}")
	public ResponseEntity<?> show(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		AdmCalendarioFeriado feriado = calendarioFeriadoService.find(id);
		if (feriado == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		List<AdmCalendarioFeriadoGeneradoPor> generates = calendarioFeriadoGeneradoPorService.findAll();
		AdmCalendarioAnio year = calendarioAnioService.findById(feriado.getIdAnio().intValue()).orElse(null);
		List<AdmCalendarioSemanaDia> days = calendarioSemanaDiaService.findAll().stream()
				.filter(item -> item.getEstado() == 1).collect(Collectors.toList());
		response.put("days", days);
		response.put("generates", generates);
		response.put("year", year);
		response.put("feriado", feriado);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestParam Long idAnio, @RequestParam Long idGeneradoPor,
									@RequestParam String motivo, @RequestParam String aplicableSectorPublico,
									@RequestParam String aplicableSectorPrivado,
									@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha, @RequestParam Long idDiaSemana,
									@RequestParam String createUser) {
		Map<String, Object> response = new HashMap<>();
		AdmCalendarioFeriado feriado = calendarioFeriadoService.find(id);
		if (feriado == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		motivo = motivo == null ? "" : motivo.trim();
		if (motivo.isEmpty()) {
			response.put("message", "El campo 'motivo' es obligatorio");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!motivo.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 _-]+$")) {
			response.put("message", "El campo 'motivo' no debe contener caracteres especiales");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (motivo.matches("^\\d+$")) {
			response.put("message", "El campo 'motivo' no puede ser completamente numérico");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// Validar duplicado exacto (ignorando el propio id)
		List<AdmCalendarioFeriado> duplicados = calendarioFeriadoService.findByMotivoAndCamposClave(
			motivo, idAnio, idGeneradoPor, aplicableSectorPublico, aplicableSectorPrivado, fecha, idDiaSemana);
		boolean existeDuplicado = duplicados.stream().anyMatch(f -> !f.getIdCalendarioFeriado().equals(id));
		if (existeDuplicado) {
			response.put("message", "Ya existe un feriado con el mismo motivo y combinación de datos");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			feriado.setIdFeriadoGeneradoPor(idGeneradoPor);
			feriado.setMotivo(motivo);
			feriado.setAplicableSectorPublico(aplicableSectorPublico);
			feriado.setAplicableSectorPrivado(aplicableSectorPrivado);
			feriado.setFecha(fecha);
			feriado.setIdSemanaDia(idDiaSemana);
			feriado.setUpdateUser(createUser);
			feriado.setUpdateDate(new Date());
			calendarioFeriadoService.save(feriado);
			response.put("message", "Feriado actualizado con éxito");
			response.put("feriado", feriado);
		} catch (Exception e) {
			response.put("message", "Error al realizar la insercion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam(required = false) String motivo,
			@RequestParam(required = false) Long generado, @RequestParam(required = false) Long anio,
			@RequestParam(required = false) Long estado,
			@RequestParam(required = false, name = "paginado", defaultValue = "0") Integer swPag, Pageable pageable) {
		Map<String, Object> response = new HashMap<>();
		if (swPag == 0) {
			List<AdmCalendarioFeriado> items = calendarioFeriadoService.findBySearch(motivo, generado, anio, estado);
			response.put("data", items);
		} else {
			Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
					Sort.by("idCalendarioFeriado").descending());
			Page<AdmCalendarioFeriado> pageRequisitos = calendarioFeriadoService.findBySearchPaginate(motivo, generado,
					anio, estado, sortedByIdDesc);
			response.put("data", pageRequisitos.getContent());
			response.put("totRegs", pageRequisitos.getTotalElements());
			response.put("totPags", pageRequisitos.getTotalPages());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/status")
	public ResponseEntity<?> actualizarEstado(@RequestParam Long id, @RequestParam String username) {
		Map<String, Object> response = new HashMap<>();
		String message = "Feriado desactivada con éxito";
		try {
			AdmCalendarioFeriado feriado = calendarioFeriadoService.find(id);
			if (feriado == null) {
				response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (feriado.getEstado() == 2L) {
				// Validar duplicado exacto antes de activar
				List<AdmCalendarioFeriado> duplicados = calendarioFeriadoService.findByMotivoAndCamposClave(
					feriado.getMotivo(), feriado.getIdAnio(), feriado.getIdFeriadoGeneradoPor(),
					feriado.getAplicableSectorPublico(), feriado.getAplicableSectorPrivado(),
					feriado.getFecha(), feriado.getIdSemanaDia());
				boolean existeDuplicado = duplicados.stream().anyMatch(f -> !f.getIdCalendarioFeriado().equals(id) && f.getEstado() == 1L);
				if (existeDuplicado) {
					response.put("message", "Ya existe un feriado activo con el mismo motivo y combinación de datos");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				feriado.setEstado(1L);
				message = "Feriado activado con éxito";
			} else if (feriado.getEstado() == 1L) {
				feriado.setEstado(2L);
			}
			feriado.setUpdateUser(username);
			feriado.setUpdateDate(new Date());
			calendarioFeriadoService.save(feriado);
			response.put("message", message);
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		AdmCalendarioFeriado feriado = calendarioFeriadoService.find(id);
		if (feriado == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			calendarioFeriadoService.delete(feriado);
			response.put("message", "Eliminación exitosa");
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}