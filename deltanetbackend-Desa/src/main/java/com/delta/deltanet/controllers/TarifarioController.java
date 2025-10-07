package com.delta.deltanet.controllers;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.CronogramaModalidad;
import com.delta.deltanet.models.entity.Perfil;
import com.delta.deltanet.models.entity.Tarifario;
import com.delta.deltanet.models.entity.TarifarioEstado;
import com.delta.deltanet.models.entity.TarifarioMoneda;
import com.delta.deltanet.models.service.TarifarioService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tariff")
public class TarifarioController {

    @Autowired
    private TarifarioService tarifarioService;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarTarifarios(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(required = false, defaultValue = "0") Integer lugar,
            @RequestParam(required = false, defaultValue = "0") Integer moneda,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {

        Map<String, Object> response = new HashMap<>();
        List<Tarifario> tarifarios = tarifarioService.buscarTarifarios(fechaInicio, fechaFin, lugar, moneda, estado);
        List<CronogramaModalidad> lugares = tarifarioService.listarLugares();
        List<TarifarioMoneda> monedas = tarifarioService.listarMonedas();
        List<TarifarioEstado> estados = tarifarioService.listarEstados();
        List<Perfil> perfiles = tarifarioService.listarPerfiles();

        response.put("tarifarios", tarifarios);
        response.put("lugares", lugares);
        response.put("monedas", monedas);
        response.put("estados", estados);
        response.put("perfiles", perfiles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveTarifario(
            @RequestParam(name = "fechaIni") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaIni,
            @RequestParam(name = "fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(name = "perfil") Integer perfil,
            @RequestParam(name = "lugar") Integer lugar,
            @RequestParam(name = "monto") BigDecimal monto,
            @RequestParam(name = "moneda") Integer moneda,
            @RequestParam(name = "usuario") String usuario) {

        Map<String, Object> response = new HashMap<>();
        try {
            Tarifario tarifario = new Tarifario();
            tarifario.setFechaIni(fechaIni);
            tarifario.setFechaFin(fechaFin);
            tarifario.setPerfil(perfil);
            tarifario.setLugar(lugar);
            tarifario.setMonto(monto);
            tarifario.setMoneda(moneda);
            tarifario.setEstado(1);
            tarifario.setCreateUser(usuario);
            tarifario.setCreateDate(new Date());

            Tarifario savedTarifario = tarifarioService.saveTarifario(tarifario);
            response.put("mensaje", "Tarifario guardado con éxito");
            response.put("tarifario", savedTarifario);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", "Error de validación");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateEstado")
    public ResponseEntity<?> updateEstadoTarifario(
            @RequestParam(name = "id") Long idTarifario,
            @RequestParam(name = "usuario") String usuario) {

        Map<String, Object> response = new HashMap<>();
        try {
            Tarifario tarifario = tarifarioService.findTarifarioById(idTarifario);
            if (tarifario == null) {
                response.put("mensaje", "El tarifario con ID [" + idTarifario + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            String cambio;
            if (tarifario.getEstado() == 1) {
                tarifario.setEstado(2);
                cambio = "desactivó";
            } else if (tarifario.getEstado() == 2) {
                tarifario.setEstado(1);
                cambio = "activó";
            } else {
                response.put("mensaje", "Estado del tarifario no válido.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            tarifario.setUpdateUser(usuario);
            tarifario.setUpdateDate(new Date());

            Tarifario updatedTarifario = tarifarioService.saveT(tarifario);
            response.put("mensaje", "El tarifario se " + cambio + " satisfactoriamente.");
            response.put("tarifario", updatedTarifario);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error al actualizar el estado del tarifario.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTarifario(
            @RequestParam(name = "id") Long idTarifario,
            @RequestParam(name = "fechaIni") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaIni,
            @RequestParam(name = "fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(name = "perfil") Integer perfil,
            @RequestParam(name = "lugar") Integer lugar,
            @RequestParam(name = "monto") BigDecimal monto,
            @RequestParam(name = "moneda") Integer moneda,
            @RequestParam(name = "usuario") String usuario) {

        Map<String, Object> response = new HashMap<>();
        try {
            Tarifario tarifario = tarifarioService.findTarifarioById(idTarifario);
            if (tarifario == null) {
                response.put("mensaje", "El tarifario con ID [" + idTarifario + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            tarifario.setFechaIni(fechaIni);
            tarifario.setFechaFin(fechaFin);
            tarifario.setPerfil(perfil);
            tarifario.setLugar(lugar);
            tarifario.setMonto(monto);
            tarifario.setMoneda(moneda);
            tarifario.setUpdateUser(usuario);
            tarifario.setUpdateDate(new Date());

            Tarifario updatedTarifario = tarifarioService.saveTarifario(tarifario);

            response.put("mensaje", "El tarifario se actualizó satisfactoriamente.");
            response.put("tarifario", updatedTarifario);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error al actualizar el tarifario.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findTarifarioById(@RequestParam(name = "id") Long idTarifario) {
        Map<String, Object> response = new HashMap<>();
        try {
            Tarifario tarifario = tarifarioService.findTarifarioById(idTarifario);

            if (tarifario == null) {
                response.put("mensaje", "El tarifario con ID [" + idTarifario + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("tarifario", tarifario);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("mensaje", "Error al buscar el tarifario.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
