package com.delta.deltanet.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.dao.IAdmCalendarioAnioDao;
import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoDao;
import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoHistoricoDao;
import com.delta.deltanet.models.dao.ICalendarioDao;
import com.delta.deltanet.models.dao.ICalendarioHistoricoDao;
import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;
import com.delta.deltanet.models.entity.AdmCalendarioFeriado;
import com.delta.deltanet.models.entity.AdmCalendarioFeriadoHistorico;
import com.delta.deltanet.models.entity.AdmCalendarioMes;
import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;
import com.delta.deltanet.models.entity.AdmTipoDiaSemana;
import com.delta.deltanet.models.entity.Calendario;
import com.delta.deltanet.models.entity.CalendarioHistorico;
import com.delta.deltanet.models.service.IAdmCalendarioAnioEstadoService;
import com.delta.deltanet.models.service.IAdmCalendarioAnioService;
import com.delta.deltanet.models.service.IAdmCalendarioFeriadoService;
import com.delta.deltanet.models.service.IAdmCalendarioMesService;
import com.delta.deltanet.models.service.IAdmCalendarioSemanaDiaService;
import com.delta.deltanet.models.service.IAdmTipoDiaSemanaService;
import com.delta.deltanet.models.service.ICalendarioService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/calendario")
public class AdmCalendarioController {

	@Autowired
	private ICalendarioService calendarioService;

	@Autowired
	private IAdmCalendarioMesService calendarioMesService;

	@Autowired
	private IAdmTipoDiaSemanaService calendarioTipoSemanaService;

	@Autowired
	private IAdmCalendarioFeriadoService calendarioFeriadoService;

	@Autowired
	private IAdmCalendarioAnioService calendarioAnioService;

	@Autowired
	private IAdmCalendarioAnioEstadoService calendarioAnioEstadoService;

	@Autowired
	private IAdmCalendarioSemanaDiaService calendarioSemanaDiaService;

	@Autowired
	public IAdmCalendarioAnioDao admCalendarioAnioDao;

	@Autowired
	public IAdmCalendarioFeriadoDao admCalendarioFeriadoDao;

	@Autowired
	public IAdmCalendarioFeriadoHistoricoDao admCalendarioFeriadoHistoricoDao;

	@Autowired
	public ICalendarioDao calendarioDao;

	@Autowired
	public ICalendarioHistoricoDao calendarioHistoricoDao;

	@GetMapping("/index")
	public ResponseEntity<?> index() {
		List<AdmCalendarioMes> months = calendarioMesService.buscarPorFiltros("", 1);
		List<AdmTipoDiaSemana> types = calendarioTipoSemanaService.buscarPorFiltros("", 1);
		Map<String, Object> response = new HashMap<>();
		response.put("months", months);
		response.put("types", types);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/create")
	public ResponseEntity<?> create() {
		List<AdmCalendarioFeriado> holidays = calendarioFeriadoService.findBySearch("", null, null, 1L);
		List<AdmCalendarioSemanaDia> days = calendarioSemanaDiaService.buscarPorFiltros("", 1);
		List<AdmCalendarioMes> months = calendarioMesService.buscarPorFiltros("", 1);
		List<AdmTipoDiaSemana> types = calendarioTipoSemanaService.buscarPorFiltros("", 1);
		AdmCalendarioAnio year = admCalendarioAnioDao.findByStatus(1L);
		Map<String, Object> response = new HashMap<>();
		response.put("holidays", holidays);
		response.put("year", year);
		response.put("types", types);
		response.put("months", months);
		response.put("days", days);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/store")
	public ResponseEntity<?> store(@RequestParam Long id_anio, @RequestParam Long id_mes,
			@RequestParam Long id_semana_dia, @RequestParam Long id_semana_dia_tipo,
			@RequestParam String es_feriado_empresa, @RequestParam Long contador_dia,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_calendario,
			@RequestParam(required = false) Long id_feriado, @RequestParam String usuCrea) {
		Map<String, Object> response = new HashMap<>();
		// Validación de fecha activa duplicada
		if (calendarioService.existsCalendarioDuplicado(null, null, null, null, null, fecha_calendario, null, null)) {
			response.put("message", "Ya existe un registro activo con la misma fecha");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			Calendario item = new Calendario();
			item.setIdAnio(id_anio);
			item.setIdMes(id_mes);
			item.setIdSemanaDia(id_semana_dia);
			item.setIdSemanaDiaTipo(id_semana_dia_tipo);
			item.setContadorDia(contador_dia);
			item.setFecha(fecha_calendario);
			item.setEsAplicableSectorPrivado(es_feriado_empresa);
			item.setIdFeriado(id_feriado);
			item.setCreateUser(usuCrea);
			item.setCreateDate(new Date());
			item.setEstado(1L);
			calendarioService.save(item);
			response.put("message", "Feriado guardado con éxito");
			response.put("calendario", item);
		} catch (Exception e) {
			response.put("message", "Error al realizar la insercion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/show/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Calendario calendar = calendarioService.find(id);
		if (calendar == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		List<AdmCalendarioFeriado> holidays = calendarioFeriadoService.findBySearch("", null, null, 1L);
		List<AdmCalendarioSemanaDia> days = calendarioSemanaDiaService.buscarPorFiltros("", 1);
		List<AdmCalendarioMes> months = calendarioMesService.buscarPorFiltros("", 1);
		List<AdmTipoDiaSemana> types = calendarioTipoSemanaService.buscarPorFiltros("", 1);
		AdmCalendarioAnio year = admCalendarioAnioDao.findByStatus(1L);
		response.put("holidays", holidays);
		response.put("year", year);
		response.put("types", types);
		response.put("months", months);
		response.put("days", days);
		response.put("calendar", calendar);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestParam Long id_anio, @RequestParam Long id_mes,
			@RequestParam Long id_semana_dia, @RequestParam Long id_semana_dia_tipo,
			@RequestParam String es_feriado_empresa, @RequestParam Long contador_dia,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_calendario,
			@RequestParam(required = false) Long id_feriado, @RequestParam String usuCrea) {
		Map<String, Object> response = new HashMap<>();
		Calendario calendar = calendarioService.find(id);
		if (calendar == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// Validación de fecha activa duplicada
		if (calendarioService.existsCalendarioDuplicado(null, null, null, null, null, fecha_calendario, null, id)) {
			response.put("message", "Ya existe un registro activo con la misma fecha");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			calendar.setIdAnio(id_anio);
			calendar.setIdMes(id_mes);
			calendar.setIdSemanaDia(id_semana_dia);
			calendar.setIdSemanaDiaTipo(id_semana_dia_tipo);
			calendar.setContadorDia(contador_dia);
			calendar.setFecha(fecha_calendario);
			calendar.setEsAplicableSectorPrivado(es_feriado_empresa);
			calendar.setIdFeriado(id_feriado);
			calendar.setCreateUser(usuCrea);
			calendar.setCreateDate(new Date());
			calendar.setEstado(1L);
			calendarioService.save(calendar);
			response.put("message", "Calendario editado con éxito");
			response.put("calendar", calendar);
		} catch (Exception e) {
			response.put("message", "Error al realizar la insercion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
			@RequestParam(required = false) Long idMes, @RequestParam(required = false) Long idSemanaDiaTipo,
			@RequestParam(required = false) Long estado) {
		Map<String, Object> response = new HashMap<>();
		List<Calendario> items = calendarioService.findBySearch(fecha, idMes, idSemanaDiaTipo, estado);
		response.put("data", items);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/status")
	public ResponseEntity<?> actualizarEstado(@RequestParam Long id, @RequestParam String username) {
		Map<String, Object> response = new HashMap<>();
		String message = "Feriado desactivada con éxito";
		try {
			Calendario calendar = calendarioService.find(id);
			if (calendar == null) {
				response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (calendar.getEstado() == 2L) {
				// Validar duplicado de fecha antes de activar
				if (calendarioService.existsCalendarioDuplicado(null, null, null, null, null, calendar.getFecha(), null,
						id)) {
					response.put("message", "Ya existe un registro activo con la misma fecha");
					response.put("data", calendar);
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				calendar.setEstado(1L);
				message = "Feriado activado con éxito";
			} else if (calendar.getEstado() == 1L) {
				calendar.setEstado(2L);
			}
			calendar.setUpdateUser(username);
			calendar.setUpdateDate(new Date());
			calendarioService.save(calendar);
			response.put("message", message);
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Calendario calendar = calendarioService.find(id);
		if (calendar == null) {
			response.put("message", "El feriado con ID: ".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		try {
			calendarioService.delete(calendar);
			response.put("message", "Eliminación exitosa");
		} catch (Exception e) {
			response.put("message", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/calendar")
	public ResponseEntity<?> actualizarEstado(@RequestParam(required = false) Long id) {
		Map<String, Object> response = new HashMap<>();
		List<Calendario> days = calendarioService.findBySearch(null, null, null, 1L);
		List<AdmCalendarioFeriado> holidays = calendarioFeriadoService.findBySearch("", null, null, 1L);
		response.put("holidays", holidays);
		response.put("days", days);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/process")
	public ResponseEntity<?> process(@RequestParam Integer idNuevo,
			@RequestParam String usuario) {
		Map<String, Object> response = new HashMap<>();
		try {
			AdmCalendarioAnio anioActualEntity = admCalendarioAnioDao.findByStatus(1L);
			AdmCalendarioAnio anioNuevoEntity = admCalendarioAnioDao.findByKey(idNuevo);
			if (anioActualEntity == null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				response.put("message", "No existe un año activo vigente");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (anioNuevoEntity == null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				response.put("message", "Año nuevo no encontrado");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (anioNuevoEntity.getAnioEstado().getIdAnioEstado() == 1L) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				response.put("message", "El año seleccionado ya esta vigente");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			// if (Integer.parseInt(anioNuevoEntity.getNombre()) -
			// Integer.parseInt(anioActualEntity.getNombre()) != 1) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// response.put("message", "El año seleccionado no es el que le sigue al año
			// vigente");
			// return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			// }
			int anioActual = LocalDate.now().getYear();
			int anioNuevo = Integer.parseInt(anioNuevoEntity.getNombre());
			if (anioNuevo < (anioActual - 1) || anioNuevo > (anioActual + 1)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				response.put("message",
						"Solo es posible establecer como año vigente el año actual, el anterior o el siguiente");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			// int anioVida = LocalDate.now().getYear();
			// if (anioVida <= Integer.parseInt(anioNuevoEntity.getNombre())) {
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// response.put("message", "El año seleccionado no puede ser mayor al año
			// actual");
			// return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			// }
			Long idAnioActual = anioActualEntity.getIdCalendarioAnio().longValue();
			List<Calendario> registrosActuales = calendarioDao.findYear(idAnioActual);
			List<Calendario> nuevosRegistros = new ArrayList<>();
			List<CalendarioHistorico> historial = new ArrayList<>();

			List<AdmCalendarioFeriado> feriadosActuales = admCalendarioFeriadoDao.findYear(idAnioActual);
			List<AdmCalendarioFeriado> nuevosFeriados = new ArrayList<>();
			List<AdmCalendarioFeriadoHistorico> historialFeriados = new ArrayList<>();
			Date now = new Date();

			// Calcular la diferencia de años
			int anioActualInt = Integer.parseInt(anioActualEntity.getNombre());
			int anioNuevoInt = Integer.parseInt(anioNuevoEntity.getNombre());

			for (AdmCalendarioFeriado original : feriadosActuales) {
				AdmCalendarioFeriadoHistorico backup = new AdmCalendarioFeriadoHistorico();
				backup.setIdAnio(original.getIdAnio());
				backup.setIdFeriadoGeneradoPor(original.getIdFeriadoGeneradoPor());
				backup.setMotivo(original.getMotivo());
				backup.setAplicableSectorPublico(original.getAplicableSectorPublico());
				backup.setAplicableSectorPrivado(original.getAplicableSectorPrivado());
				backup.setFecha(original.getFecha());
				backup.setIdSemanaDia(original.getIdSemanaDia());
				backup.setEstado(original.getEstado());
				backup.setCreateUser(original.getCreateUser());
				backup.setCreateDate(original.getCreateDate());
				backup.setUpdateUser(original.getUpdateUser());
				backup.setUpdateDate(original.getUpdateDate());
				historialFeriados.add(backup);

				// Duplicado para el nuevo año - CORRIGIENDO FECHAS CORRUPTAS
				AdmCalendarioFeriado nuevo = new AdmCalendarioFeriado();
				Calendar cal = Calendar.getInstance();
				cal.setTime(original.getFecha());

				// CORRECCIÓN: Normalizar la fecha al año actual antes de calcular
				// Esto corrige fechas corruptas como 2028, 2048, etc.
				cal.set(Calendar.YEAR, anioActualInt);

				// Ahora sí calcular la diferencia correctamente
				cal.set(Calendar.YEAR, anioNuevoInt);

				int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
				long diaSemanaISO = (long) ((diaSemana == Calendar.SUNDAY) ? 7 : diaSemana - 1);
				nuevo.setIdAnio(anioNuevoEntity.getIdCalendarioAnio().longValue());
				nuevo.setIdFeriadoGeneradoPor(original.getIdFeriadoGeneradoPor());
				nuevo.setMotivo(original.getMotivo());
				nuevo.setAplicableSectorPublico(original.getAplicableSectorPublico());
				nuevo.setAplicableSectorPrivado(original.getAplicableSectorPrivado());
				nuevo.setFecha(cal.getTime());
				nuevo.setIdSemanaDia(diaSemanaISO);
				nuevo.setEstado(original.getEstado());
				nuevo.setCreateUser(usuario);
				nuevo.setCreateDate(now);
				nuevosFeriados.add(nuevo);
			}
			this.admCalendarioFeriadoHistoricoDao.saveAll(historialFeriados);
			this.admCalendarioFeriadoDao.saveAll(nuevosFeriados);
			Long contadorDias = 1L;
			for (Calendario original : registrosActuales) {
				CalendarioHistorico backup = new CalendarioHistorico();
				backup.setIdAnio(original.getIdAnio());
				backup.setIdMes(original.getIdMes());
				backup.setIdSemanaDia(original.getIdSemanaDia());
				backup.setIdSemanaDiaTipo(original.getIdSemanaDiaTipo());
				backup.setContadorDia(original.getContadorDia());
				backup.setFecha(original.getFecha());
				backup.setEsAplicableSectorPrivado(original.getEsAplicableSectorPrivado());
				backup.setEstado(original.getEstado());
				backup.setCreateUser(usuario);
				backup.setCreateDate(original.getCreateDate());
				backup.setUpdateUser(original.getUpdateUser());
				backup.setUpdateDate(original.getUpdateDate());
				List<AdmCalendarioFeriadoHistorico> feriadoTemp = admCalendarioFeriadoHistoricoDao
						.findFecha(original.getFecha());
				AdmCalendarioFeriadoHistorico feriado = null;
				if (feriadoTemp.size() > 0) {
					feriado = feriadoTemp.get(0);
					backup.setIdFeriado(feriado.getIdCalendarioFeriado());
				} else {
					backup.setIdFeriado(null);
				}
				historial.add(backup);
				// Duplicado para el nuevo año - CORRIGIENDO FECHAS CORRUPTAS
				Calendar cal = Calendar.getInstance();
				cal.setTime(original.getFecha());

				// CORRECCIÓN: Normalizar la fecha al año actual antes de calcular
				cal.set(Calendar.YEAR, anioActualInt);

				// Si el actual es bisiesto saltarse el 29 para el siguiente
				if (esBisiesto(cal.get(Calendar.YEAR)) && cal.get(Calendar.MONTH) == 1
						&& cal.get(Calendar.DAY_OF_MONTH) == 29) {
					continue;
				}
				Calendario nuevo = new Calendario();

				// Ahora sí calcular la diferencia correctamente
				cal.set(Calendar.YEAR, anioNuevoInt);
				int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
				long diaSemanaISO = (long) ((diaSemana == Calendar.SUNDAY) ? 7 : diaSemana - 1);
				long numeroMes = (long) cal.get(Calendar.MONTH) + 1;
				long semanaDiaTipo = diaSemanaISO == 6 || diaSemanaISO == 7 ? 2L : 1L;
				nuevo.setIdAnio(anioNuevoEntity.getIdCalendarioAnio().longValue());
				nuevo.setIdMes(numeroMes);
				nuevo.setIdSemanaDia(diaSemanaISO);
				nuevo.setIdSemanaDiaTipo(semanaDiaTipo);
				nuevo.setContadorDia(contadorDias);
				nuevo.setFecha(cal.getTime());
				nuevo.setEsAplicableSectorPrivado(original.getEsAplicableSectorPrivado());
				nuevo.setEstado(original.getEstado());
				nuevo.setCreateUser(usuario);
				nuevo.setCreateDate(now);
				List<AdmCalendarioFeriado> feriadoT = admCalendarioFeriadoDao.findFecha(nuevo.getFecha());
				AdmCalendarioFeriado feriadoH = null;
				if (feriadoT.size() > 0) {
					feriadoH = feriadoT.get(0);
					nuevo.setIdFeriado(feriadoH.getIdCalendarioFeriado());
				} else {
					nuevo.setIdFeriado(null);
				}
				nuevosRegistros.add(nuevo);
				contadorDias += 1;
				// Si el nuevo es bisiesto agregar el 29
				if (esBisiesto(cal.get(Calendar.YEAR)) && cal.get(Calendar.MONTH) == 1
						&& cal.get(Calendar.DAY_OF_MONTH) == 28) {
					nuevo = new Calendario();
					cal.add(Calendar.DAY_OF_MONTH, 1);
					diaSemana = cal.get(Calendar.DAY_OF_WEEK);
					diaSemanaISO = (long) ((diaSemana == Calendar.SUNDAY) ? 7 : diaSemana - 1);
					numeroMes = (long) cal.get(Calendar.MONTH) + 1;
					semanaDiaTipo = diaSemanaISO == 6 || diaSemanaISO == 7 ? 2L : 1L;
					nuevo.setIdAnio(anioNuevoEntity.getIdCalendarioAnio().longValue());
					nuevo.setIdMes(numeroMes);
					nuevo.setIdSemanaDia(diaSemanaISO);
					nuevo.setIdSemanaDiaTipo(semanaDiaTipo);
					nuevo.setContadorDia(contadorDias);
					nuevo.setFecha(cal.getTime());
					nuevo.setEsAplicableSectorPrivado(original.getEsAplicableSectorPrivado());
					nuevo.setEstado(original.getEstado());
					nuevo.setCreateUser(usuario);
					nuevo.setCreateDate(now);
					feriadoT = admCalendarioFeriadoDao.findFecha(nuevo.getFecha());
					feriadoH = null;
					if (feriadoT.size() > 0) {
						feriadoH = feriadoT.get(0);
						nuevo.setIdFeriado(feriadoH.getIdCalendarioFeriado());
					} else {
						nuevo.setIdFeriado(null);
					}
					nuevosRegistros.add(nuevo);
					contadorDias += 1;
				}
			}
			this.calendarioHistoricoDao.saveAll(historial);
			this.calendarioDao.saveAll(nuevosRegistros);
			this.calendarioDao.removeAll(anioActualEntity.getIdCalendarioAnio().longValue());
			this.admCalendarioFeriadoDao.removeAll(anioActualEntity.getIdCalendarioAnio().longValue());
			Optional<AdmCalendarioAnioEstado> opt = calendarioAnioEstadoService.findById(1L); // Actual
			Optional<AdmCalendarioAnioEstado> opt2 = calendarioAnioEstadoService.findById(2L); // Pasado
			Optional<AdmCalendarioAnioEstado> opt3 = calendarioAnioEstadoService.findById(3L); // Futuro
			AdmCalendarioAnioEstado statusActive = opt.get();
			AdmCalendarioAnioEstado statusDisable = opt2.get();
			AdmCalendarioAnioEstado statusFuture = opt3.orElse(null);
			anioActualEntity.setAnioEstado(statusDisable);
			anioNuevoEntity.setAnioEstado(statusActive);
			this.calendarioAnioService.update(anioActualEntity);
			this.calendarioAnioService.update(anioNuevoEntity);
			// Actualizar años activos mayores al nuevo como Futuro
			List<AdmCalendarioAnio> aniosActivos = calendarioAnioService.buscarPorFiltros(null, 1, null);
			int nombreNuevo = Integer.parseInt(anioNuevoEntity.getNombre());
			for (AdmCalendarioAnio anio : aniosActivos) {
				int nombreAnio = Integer.parseInt(anio.getNombre());
				if (anio.getIdCalendarioAnio().longValue() == anioNuevoEntity.getIdCalendarioAnio().longValue()) {
					continue; // Ya actualizado como vigente
				}
				if (nombreAnio > nombreNuevo) {
					if (statusFuture != null) {
						anio.setAnioEstado(statusFuture);
						this.calendarioAnioService.update(anio);
					}
				} else if (nombreAnio < nombreNuevo) {
					if (statusDisable != null) {
						anio.setAnioEstado(statusDisable);
						this.calendarioAnioService.update(anio);
					}
				}
			}
			response.put("message",
					"Se estableció " + anioNuevoEntity.getNombre() + " como nuevo año vigente exitosamente");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.put("message", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private static boolean esBisiesto(int year) {
		return java.time.Year.isLeap(year);
	}
}