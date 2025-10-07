package com.delta.deltanet.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delta.deltanet.models.entity.ComProdServEstado;
import com.delta.deltanet.models.entity.ComProdServTipo;
import com.delta.deltanet.models.entity.ComProductoServicio;
import com.delta.deltanet.models.service.ComProductoServicioService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/products-services")
public class ComProductoServicioController {

  @Autowired
  private ComProductoServicioService productoServicioService;

  @GetMapping("/buscar")
  public ResponseEntity<?> buscarProductosServicios(
      @RequestParam(required = false) String descripcion,
      @RequestParam(defaultValue = "0") int estado,
      @RequestParam(defaultValue = "0") int idTipo,
      @RequestParam(required = false, name = "paginado", defaultValue = "0") Integer swPag,
      Pageable pageable) {
    try {
      Map<String, Object> response = new HashMap<>();

      if (swPag == 0) {
        List<ComProductoServicio> productosServicios = productoServicioService.findByParameters(idTipo,
            estado, descripcion);
        productosServicios.sort(Comparator.comparing(ComProductoServicio::getId).reversed());
        response.put("data", productosServicios);
      } else {
        Pageable sortedByIdDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by("id").descending());
        Page<ComProductoServicio> pageProductosServicios = productoServicioService
            .findByParametersPaginated(idTipo, estado, descripcion, sortedByIdDesc);
        response.put("data", pageProductosServicios.getContent());
        response.put("totRegs", pageProductosServicios.getTotalElements());
        response.put("totPags", pageProductosServicios.getTotalPages());
      }
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<?> nuevoProductoServicio(
      @RequestParam String descripcion,
      @RequestParam String createUser,
      @RequestParam Integer idTipo) {
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
    boolean existe = productoServicioService.existsDescripcionTipoActivo(descripcionTrim, idTipo, null);
    if (existe) {
      response.put("message", "Ya existe un registro activo con la misma descripción y tipo");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    try {
      ComProductoServicio productoServicio = productoServicioService.saveProductoServicio(descripcionTrim, createUser,
          idTipo);
      response.put("productoServicio", productoServicio);
      response.put("message", "Producto o servicio creado exitosamente");
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      response.put("message", e.getMessage());
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/findById")
  public ResponseEntity<?> obtenerProductoServicio(@RequestParam Integer id) {
    Map<String, Object> response = new HashMap<>();
    ComProductoServicio productoServicio = productoServicioService.findById(id);
    List<ComProdServTipo> tipos = productoServicioService.findAllTipos();
    List<ComProdServEstado> estados = productoServicioService.findAllEstados();

    response.put("data", productoServicio);
    response.put("tipos", tipos);
    response.put("estados", estados);

    if (productoServicio == null) {
      response.put("message", "Producto o servicio no encontrado");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update")
  public ResponseEntity<?> actualizarProductoServicio(
      @RequestParam Integer id,
      @RequestParam String descripcion,
      @RequestParam String updateUser,
      @RequestParam Integer idTipo) {
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
    ComProductoServicio actual = productoServicioService.findById(id);
    if (actual == null) {
      response.put("message", "El producto o servicio con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (!descripcionTrim.equalsIgnoreCase(actual.getDescripcion()) || !idTipo.equals(actual.getIdTipo())) {
      boolean existe = productoServicioService.existsDescripcionTipoActivo(descripcionTrim, idTipo, id);
      if (existe) {
        response.put("message", "Ya existe otro registro activo con la misma descripción y tipo");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    ComProductoServicio productoServicio = productoServicioService.updateProductoServicio(id, descripcionTrim, updateUser,
        idTipo);
    response.put("productoServicio", productoServicio);
    response.put("message", "Producto o servicio actualizado satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/updEstado")
  public ResponseEntity<?> actualizarEstado(
      @RequestParam Integer id,
      @RequestParam String username) {
    Map<String, Object> response = new HashMap<>();
    ComProductoServicio productoServicio = productoServicioService.findById(id);
    if (productoServicio == null) {
      response.put("message", "El producto o servicio con id [" + id + "] no se encuentra");
      return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // Solo validar duplicados si se va a activar
    if (productoServicio.getEstado() == 2) {
      boolean existe = productoServicioService.existsDescripcionTipoActivo(productoServicio.getDescripcion(), productoServicio.getIdTipo(), id);
      if (existe) {
        response.put("message", "Ya existe otro registro activo con la misma descripción y tipo");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    ComProductoServicio actualizado = productoServicioService.activateProductoServicio(id, username);
    String accion = (actualizado.getEstado() == 1) ? "activado" : "desactivado";
    response.put("productoServicio", actualizado);
    response.put("message", "Producto o servicio " + accion + " satisfactoriamente");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/listTiposEstados")
  public ResponseEntity<?> getTiposYEstados() {
    // Obtener los tipos y estados desde el servicio
    List<ComProdServTipo> tipos = productoServicioService.findAllTipos();
    List<ComProdServEstado> estados = productoServicioService.findAllEstados();

    // Crear la respuesta en un Map
    Map<String, Object> response = new HashMap<>();
    response.put("tipos", tipos);
    response.put("estados", estados);

    // Devolver la respuesta
    return ResponseEntity.ok(response);
  }

  @GetMapping("/indexEspecial")
  public ResponseEntity<?> indexEspecial(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false) Integer estado) {
      Map<String, Object> response = new HashMap<>();
      List<ComProductoServicio> registros = new ArrayList<>();
      if (id != null) {
          ComProductoServicio reg = productoServicioService.findById(id);
          if (reg == null) {
              response.put("error", true);
              response.put("message", "No existe un registro con el id proporcionado");
              return new ResponseEntity<>(response, HttpStatus.OK);
          }
          if (reg.getEstado() != 1) {
              response.put("error", true);
              response.put("message", "Acceso denegado, no se puede acceder a un registro con estado Desactivado");
              response.put("data", Collections.singletonList(reg));
              return new ResponseEntity<>(response, HttpStatus.OK);
          }
          if (estado == null || estado == 0 || reg.getEstado() == estado) {
              registros.add(reg);
          }
      } else if (estado != null) {
          for (ComProductoServicio reg : productoServicioService.findByParameters(0, 0, null)) {
              if (reg.getEstado() == estado) {
                  registros.add(reg);
              }
          }
      } else {
          registros = productoServicioService.findByParameters(0, 0, null);
      }
      response.put("data", registros);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/createEspecial")
  public ResponseEntity<?> createEspecial(@RequestParam String descripcion,
                                          @RequestParam String username,
                                          @RequestParam Integer idTipo) {
      Map<String, Object> response = new HashMap<>();
      descripcion = descripcion.trim();
      List<ComProductoServicio> existentes = productoServicioService.findByParameters(0, 1, null);
      for (ComProductoServicio reg : existentes) {
          if (reg.getDescripcion().equalsIgnoreCase(descripcion) && reg.getEstado() == 1) {
              response.put("error", true);
              response.put("message", "Ya existe un registro activo con esa descripción");
              return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
          }
      }
      ComProductoServicio nuevo = new ComProductoServicio();
      nuevo.setDescripcion(descripcion);
      nuevo.setEstado(1);
      nuevo.setCreateUser(username);
      nuevo.setCreateDate(new Date());
      nuevo.setIdTipo(idTipo);
      productoServicioService.saveProductoServicio(descripcion, username, idTipo);
      response.put("message", "Registro creado correctamente");
      response.put("registro", nuevo);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/updateEspecial")
  public ResponseEntity<?> updateEspecial(@RequestParam Integer id,
                                          @RequestParam(required = false) String descripcion,
                                          @RequestParam String username,
                                          @RequestParam(required = false) Integer idTipo) {
      Map<String, Object> response = new HashMap<>();
      ComProductoServicio reg = productoServicioService.findById(id);
      if (reg == null) {
          response.put("error", true);
          response.put("message", "No existe un registro con el id proporcionado");
          return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      if (descripcion != null) {
          descripcion = descripcion.trim();
          List<ComProductoServicio> existentes = productoServicioService.findByParameters(0, 1, null);
          for (ComProductoServicio r : existentes) {
              if (r.getId() != id && r.getDescripcion().equalsIgnoreCase(descripcion) && r.getEstado() == 1) {
                  response.put("error", true);
                  response.put("message", "Ya existe otro registro activo con esa descripción");
                  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
              }
          }
          reg.setDescripcion(descripcion);
      }
      if (idTipo != null) {
          reg.setIdTipo(idTipo);
      }
      reg.setUpdateUser(username);
      reg.setUpdateDate(new Date());
      productoServicioService.updateProductoServicio(id, reg.getDescripcion(), username, reg.getIdTipo());
      response.put("message", "Registro actualizado correctamente");
      response.put("registro", reg);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/changeStatusEspecial")
  public ResponseEntity<?> changeStatusEspecial(@RequestParam Integer id,
                                                @RequestParam String username) {
      Map<String, Object> response = new HashMap<>();
      ComProductoServicio reg = productoServicioService.findById(id);
      if (reg == null) {
          response.put("error", true);
          response.put("message", "No existe un registro con el id proporcionado");
          return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
      if (reg.getEstado() == 1) {
          reg.setEstado(0);
          reg.setUpdateUser(username);
          reg.setUpdateDate(new Date());
          productoServicioService.updateProductoServicio(id, reg.getDescripcion(), username, reg.getIdTipo());
          response.put("message", "Registro desactivado correctamente");
          response.put("registro", reg);
          return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
          // Verificar que no haya otro activo con la misma descripción
          List<ComProductoServicio> existentes = productoServicioService.findByParameters(0, 1, null);
          for (ComProductoServicio r : existentes) {
              if (r.getId() != id && r.getDescripcion().equalsIgnoreCase(reg.getDescripcion()) && r.getEstado() == 1) {
                  response.put("error", true);
                  response.put("message", "Ya existe otro registro activo con esa descripción");
                  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
              }
          }
          reg.setEstado(1);
          reg.setUpdateUser(username);
          reg.setUpdateDate(new Date());
          productoServicioService.updateProductoServicio(id, reg.getDescripcion(), username, reg.getIdTipo());
          response.put("message", "Registro activado correctamente");
          response.put("registro", reg);
          return new ResponseEntity<>(response, HttpStatus.OK);
      }
  }
}
