package com.delta.deltanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.AdmNivel;
import com.delta.deltanet.models.service.AdmNivelService;

import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/levels")
public class AdmNivelController {

  @Autowired
  private AdmNivelService nivelService;

  @GetMapping("/buscar")
  public ResponseEntity<?> buscarNiveles(
      @RequestParam(required = false) String descripcion,
      @RequestParam(defaultValue = "0") int estado,
      @RequestParam(required = false, name = "paginado", defaultValue = "0") Integer swPag, Pageable pageable) {
    try {
      Map<String, Object> response = new HashMap<>();

      if (swPag == 0) {
        List<AdmNivel> niveles = nivelService.findByDescripcionAndEstado(descripcion, estado);
        niveles.sort(Comparator.comparing(AdmNivel::getId).reversed());
        response.put("data", niveles);
      } else {
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by("id").descending());
        Page<AdmNivel> pageNiveles = nivelService.findByDescripcionAndEstadoPaginated(descripcion, estado,
            sortedByIdDesc);
        response.put("data", pageNiveles.getContent());
        response.put("totRegs", pageNiveles.getTotalElements());
        response.put("totPags", pageNiveles.getTotalPages());
      }
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> nuevoNivel(
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
    boolean existe = nivelService.existsDescripcionActivo(descripcionTrim, null);
    if (existe) {
      response.put("message", "Ya existe otro nivel activo con la misma descripción");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    try {
      AdmNivel nivel = nivelService.saveNivel(descripcionTrim, createUser);
      response.put("nivel", nivel);
      response.put("message", "Nivel creado exitosamente");
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response.put("message", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/findById")
  public ResponseEntity<?> obtenerNivel(@RequestParam Integer id) {
    Map<String, Object> response = new HashMap<>();
    AdmNivel nivel = nivelService.findById(id);

    if (nivel == null) {
      response.put("message", "Nivel no encontrado");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(nivel, HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<?> actualizarNivel(
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
    AdmNivel actual = nivelService.findById(id);
    if (actual == null) {
      response.put("message", "El nivel con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // Si la descripción no cambia, permite actualizar
    if (!descripcionTrim.equalsIgnoreCase(actual.getDescripcion())) {
      boolean existe = nivelService.existsDescripcionActivo(descripcionTrim, id);
      if (existe) {
        response.put("message", "Ya existe otro nivel activo con la misma descripción");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    AdmNivel nivel = nivelService.updateNivel(id, descripcionTrim, updateUser);
    response.put("nivel", nivel);
    response.put("message", "Nivel actualizado satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/updEstado")
  public ResponseEntity<?> actualizarEstado(
      @RequestParam Integer id,
      @RequestParam String username) {
    Map<String, Object> response = new HashMap<>();
    AdmNivel nivel = nivelService.findById(id);
    if (nivel == null) {
      response.put("message", "El nivel con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // Solo validar duplicados si se va a activar
    if (nivel.getEstado() == 2) {
      String descripcionTrim = nivel.getDescripcion() != null ? nivel.getDescripcion().trim() : "";
      boolean existe = nivelService.existsDescripcionActivo(descripcionTrim, id);
      if (existe) {
        response.put("message", "Ya existe otro nivel activo con la misma descripción");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    // Switch para cambiar estado
    switch (nivel.getEstado()) {
      case 0:
        nivel.setEstado(1);
        break;
      case 1:
        nivel.setEstado(2);
        break;
      default:
        nivel.setEstado(2);
        break;
    }
    nivel.setUpdateUser(username);
    nivel.setUpdateDate(new Date());
    nivelService.save(nivel);
    String accion = (nivel.getEstado() == 1) ? "activado" : "desactivado";
    response.put("nivel", nivel);
    response.put("message", "Nivel " + accion + " satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}