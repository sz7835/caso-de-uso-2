package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AlertaUsuario;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.EMail;
import com.delta.deltanet.models.service.IAlertaUsuarioService;
import com.delta.deltanet.models.service.IAutenticacionUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/alerta-usuario")
public class AlertaUsuarioController {

    @Autowired
    private IAlertaUsuarioService alertaUsuarioService;
    @Autowired
    private IAutenticacionUsuarioService autenticacionUsuarioService;

    // 1. Validar usuario
    @GetMapping("/validar")
    public ResponseEntity<?> validarUsuario(@RequestParam String user) {
        Map<String, Object> response = new HashMap<>();
        AutenticacionUsuario auth = autenticacionUsuarioService.findByUsuario(user);
        if (auth == null) {
            response.put("message", "Usuario no encontrado");
            response.put("error", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Persona persona = auth.getPersona();
        if (persona == null) {
            response.put("message", "No se encontró persona asociada");
            response.put("error", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Long idTipoPersona = persona.getTipoPer() != null ? persona.getTipoPer().getIdTipoPer() : null;
        Long idArea = persona.getArea() != null ? persona.getArea().getId() : null;
        String email = null;
        for (EMail correo : persona.getCorreos()) {
            if (correo.getEstado() == 1) {
                email = correo.getCorreo();
                break;
            }
        }
        response.put("message", "Validación exitosa");
        response.put("error", false);
        response.put("idPersona", persona.getId());
        response.put("idTipoPersona", idTipoPersona);
        response.put("idArea", idArea);
        response.put("email", email);
        return ResponseEntity.ok(response);
    }

    // 2. Crear alerta usuario
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam String batch,
            @RequestParam String nomUsuario,
            @RequestParam String correoDelta,
            @RequestParam Long tipoUsuario,
            @RequestParam Long areaUsuario,
            @RequestParam String username,
            @RequestParam(required = false) String descripcion) {
        Map<String, Object> response = new HashMap<>();
        // Validar batch duplicado activo para el username
        List<AlertaUsuario> existentes = alertaUsuarioService.listaEmailsv2(batch);
        for (AlertaUsuario a : existentes) {
            if (a.getNomUsuario().equals(nomUsuario) && a.getEstadoReg() == 1) {
                response.put("message", "Ya existe un registro activo con ese batch para este usuario");
                response.put("error", true);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }
        AlertaUsuario alerta = new AlertaUsuario();
        alerta.setBatch(batch);
        alerta.setNomUsuario(nomUsuario);
        alerta.setCorreoDelta(correoDelta);
        alerta.setTipoUsuario(tipoUsuario);
        alerta.setAreaUsuario(areaUsuario);
        alerta.setEstadoReg(1L);
        alerta.setCreateDate(new Date());
        alerta.setCreateUser(username);
        if (descripcion != null) {
            alerta.setDescripcion(descripcion);
        }
        alertaUsuarioService.save(alerta);
        response.put("message", "Registro creado exitosamente");
        response.put("error", false);
        return ResponseEntity.ok(response);
    }

    // 3. Update
    @PostMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam Long id,
            @RequestParam String batch,
            @RequestParam String nomUsuario,
            @RequestParam String correoDelta,
            @RequestParam Long tipoUsuario,
            @RequestParam Long areaUsuario,
            @RequestParam String username,
            @RequestParam(required = false) String descripcion) {
        Map<String, Object> response = new HashMap<>();
        AlertaUsuario actual = alertaUsuarioService.findById(id);
        if (actual == null) {
            response.put("message", "No existe el registro");
            response.put("error", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        // Validar batch duplicado activo para el username, omitiendo el propio
        List<AlertaUsuario> existentes = alertaUsuarioService.listaEmailsv2(batch);
        for (AlertaUsuario a : existentes) {
            if (!a.getId().equals(id) && a.getNomUsuario().equals(nomUsuario) && a.getEstadoReg() == 1) {
                response.put("message", "Ya existe un registro activo con ese batch para este usuario");
                response.put("error", true);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }
        actual.setBatch(batch);
        actual.setNomUsuario(nomUsuario);
        actual.setCorreoDelta(correoDelta);
        actual.setTipoUsuario(tipoUsuario);
        actual.setAreaUsuario(areaUsuario);
        actual.setUpdateUser(username);
        actual.setUpdateDate(new Date());
        if (descripcion != null) {
            actual.setDescripcion(descripcion);
        }
        alertaUsuarioService.save(actual);
        response.put("message", "Registro actualizado exitosamente");
        response.put("error", false);
        return ResponseEntity.ok(response);
    }

    // 4. Cambiar estado
    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(
            @RequestParam Long id,
            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        AlertaUsuario actual = alertaUsuarioService.findById(id);
        if (actual == null) {
            response.put("message", "No existe el registro");
            response.put("error", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Long nuevoEstado = (actual.getEstadoReg() != null && actual.getEstadoReg() == 1) ? 0L : 1L;
        // Si se va a activar, validar que no haya otro batch igual activo para el usuario
        if (nuevoEstado == 1) {
            List<AlertaUsuario> existentes = alertaUsuarioService.listaEmailsv2(actual.getBatch());
            for (AlertaUsuario a : existentes) {
                if (!a.getId().equals(id) && a.getNomUsuario().equals(actual.getNomUsuario()) && a.getEstadoReg() == 1) {
                    response.put("message", "No se puede activar: ya existe un registro activo con ese batch para este usuario");
                    response.put("error", true);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }
        }
        actual.setEstadoReg(nuevoEstado);
        actual.setUpdateUser(username);
        actual.setUpdateDate(new Date());
        alertaUsuarioService.save(actual);
        if (nuevoEstado == 1) {
            response.put("message", "Registro activado con éxito");
        } else {
            response.put("message", "Registro desactivado con éxito");
        }
        response.put("error", false);
        return ResponseEntity.ok(response);
    }

    // 5. Index por id y estado (ambos opcionales)
    @GetMapping("/index")
    public ResponseEntity<?> index(@RequestParam(required = false) Long id, @RequestParam(required = false) Long estado) {
        Map<String, Object> response = new HashMap<>();
        // Si no se envía ninguno, retorna todos
        if (id == null && estado == null) {
            List<AlertaUsuario> todos = alertaUsuarioService.findAll();
            response.put("error", false);
            response.put("data", todos);
            return ResponseEntity.ok(response);
        }
        // Si solo se envía id
        if (id != null && estado == null) {
            AlertaUsuario alerta = alertaUsuarioService.findById(id);
            if (alerta == null) {
                response.put("error", true);
                response.put("message", "No existe el registro");
                return ResponseEntity.ok(response);
            }
            if (alerta.getEstadoReg() == 0) {
                response.put("error", true);
                response.put("message", "El registro existe pero está en estado desactivado");
                response.put("data", alerta);
                return ResponseEntity.ok(response);
            }
            response.put("error", false);
            response.put("data", alerta);
            return ResponseEntity.ok(response);
        }
        // Si solo se envía estado
        if (id == null && estado != null) {
            List<AlertaUsuario> filtrados = alertaUsuarioService.findByEstado(estado);
            response.put("error", false);
            response.put("data", filtrados);
            return ResponseEntity.ok(response);
        }
        // Si se envían ambos
        AlertaUsuario alerta = alertaUsuarioService.findById(id);
        if (alerta == null) {
            response.put("error", true);
            response.put("message", "No existe el registro");
            return ResponseEntity.ok(response);
        }
        if (!alerta.getEstadoReg().equals(estado)) {
            if (alerta.getEstadoReg() == 0) {
                response.put("error", true);
                response.put("message", "Acceso denegado, este registro está en estado desactivado");
                response.put("data", alerta);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", true);
                response.put("message", "El estado no coincide con el registro");
                response.put("data", alerta);
                return ResponseEntity.ok(response);
            }
        }
        response.put("error", false);
        response.put("data", alerta);
        return ResponseEntity.ok(response);
    }
}
