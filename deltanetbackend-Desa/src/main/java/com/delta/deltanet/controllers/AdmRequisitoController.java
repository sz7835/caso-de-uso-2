package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmRequisito;
import com.delta.deltanet.models.service.AdmRequisitoService;

import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/requirements")
public class AdmRequisitoController {

  @Autowired
  private AdmRequisitoService requisitoService;

  @GetMapping("/buscar")
  public ResponseEntity<?> buscarRequisitos(
      @RequestParam(required = false) String descripcion,
      @RequestParam(defaultValue = "0") int estado,
      @RequestParam(required = false, name = "paginado", defaultValue = "0") Integer swPag, Pageable pageable) {
    try {
      Map<String, Object> response = new HashMap<>();

      if (swPag == 0) {
        List<AdmRequisito> requisitos = requisitoService.findByDescripcionAndEstado(descripcion, estado);
        requisitos.sort(Comparator.comparing(AdmRequisito::getId).reversed());
        response.put("data", requisitos);
      } else {
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by("id").descending());
        Page<AdmRequisito> pageRequisitos = requisitoService.findByDescripcionAndEstadoPaginated(descripcion, estado,
            sortedByIdDesc);
        response.put("data", pageRequisitos.getContent());
        response.put("totRegs", pageRequisitos.getTotalElements());
        response.put("totPags", pageRequisitos.getTotalPages());
      }
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/findById")
  public ResponseEntity<?> obtenerRequisito(@RequestParam Integer id) {
    Map<String, Object> response = new HashMap<>();
    AdmRequisito requisito = requisitoService.findById(id);

    if (requisito == null) {
      response.put("message", "Requisito no encontrado");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(requisito, HttpStatus.OK);
  }

  @PostMapping("/create")
  public ResponseEntity<?> nuevoRequisito(
      @RequestParam String descripcion,
      @RequestParam String createUser) {
    Map<String, Object> response = new HashMap<>();
    String descripcionTrim = descripcion != null ? descripcion.trim() : "";
    if (descripcionTrim.isEmpty()) {
      response.put("message", "La descripción es obligatoria y no puede estar vacía");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (descripcionTrim.matches("^[0-9]+$") || descripcionTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
      response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    boolean existe = requisitoService.existsDescripcionActivo(descripcionTrim, null);
    if (existe) {
      response.put("message", "Ya existe otro requisito activo con la misma descripción");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    try {
      AdmRequisito requisito = requisitoService.saveRequisito(descripcionTrim, createUser);
      response.put("requisito", requisito);
      response.put("message", "Requisito creado exitosamente");
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response.put("message", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/update")
  public ResponseEntity<?> actualizarRequisito(
      @RequestParam Integer id,
      @RequestParam String descripcion,
      @RequestParam String updateUser) {
    Map<String, Object> response = new HashMap<>();
    String descripcionTrim = descripcion != null ? descripcion.trim() : "";
    if (descripcionTrim.isEmpty()) {
      response.put("message", "La descripción es obligatoria y no puede estar vacía");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (descripcionTrim.matches("^[0-9]+$") || descripcionTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
      response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    AdmRequisito actual = requisitoService.findById(id);
    if (actual == null) {
      response.put("message", "El requisito con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (!descripcionTrim.equalsIgnoreCase(actual.getDescripcion())) {
      boolean existe = requisitoService.existsDescripcionActivo(descripcionTrim, id);
      if (existe) {
        response.put("message", "Ya existe otro requisito activo con la misma descripción");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    AdmRequisito requisito = requisitoService.updateRequisito(id, descripcionTrim, updateUser);
    response.put("requisito", requisito);
    response.put("message", "Requisito actualizado satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/updEstado")
  public ResponseEntity<?> actualizarEstado(
      @RequestParam Integer id,
      @RequestParam String username) {
    Map<String, Object> response = new HashMap<>();
    AdmRequisito requisito = requisitoService.findById(id);
    if (requisito == null) {
      response.put("message", "El requisito con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // Solo validar duplicados si se va a activar
    if (requisito.getEstado() == 0) {
      String descripcionTrim = requisito.getDescripcion() != null ? requisito.getDescripcion().trim() : "";
      boolean existe = requisitoService.existsDescripcionActivo(descripcionTrim, id);
      if (existe) {
        response.put("message", "Ya existe otro requisito activo con la misma descripción");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    AdmRequisito actualizado = requisitoService.activateRequisito(id, username);
    String accion = (actualizado.getEstado() == 1) ? "activado" : "desactivado";
    response.put("requisito", actualizado);
    response.put("message", "Requisito " + accion + " satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}