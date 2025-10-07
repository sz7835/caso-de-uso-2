package com.delta.deltanet.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.dao.PostulanteNivelesDao;
import com.delta.deltanet.models.dao.PostulanteRequisitoDao;
import com.delta.deltanet.models.dao.PostulanteSkillDao;
import com.delta.deltanet.models.dao.PuestoOcupacionalDao;
import com.delta.deltanet.models.dao.PuestoOcupacionalRelacionRequisitoDao;
import com.delta.deltanet.models.dao.PuestoOcupacionalRelacionSkillDao;
import com.delta.deltanet.models.entity.PostulanteNiveles;
import com.delta.deltanet.models.entity.PuestoOcupacional;
import com.delta.deltanet.models.entity.PuestoOcupacionalRelacionRequisito;
import com.delta.deltanet.models.entity.PuestoOcupacionalRelacionSkill;
import com.delta.deltanet.models.service.PuestoOcupacionalService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/puesto-ocupacional")
public class PuestoOcupacionalController {
    @Autowired
    private PuestoOcupacionalService puestoService;
    @Autowired
    private PuestoOcupacionalDao puestoDao;
    @Autowired
    private PuestoOcupacionalRelacionRequisitoDao requisitoService;
    @Autowired
    private PuestoOcupacionalRelacionSkillDao skillService;
    @Autowired
    private PostulanteRequisitoDao postulanteRequisitoDao;
    @Autowired
    private PostulanteSkillDao postulanteSkillDao;
    @Autowired
    private PostulanteNivelesDao postulanteNivelesDao;

    @GetMapping("/search")
    public ResponseEntity<List<PuestoOcupacional>> filtrarPuestos(
            @RequestParam(name = "nombre", required = false) String nombrePuesto,
            @RequestParam(name = "estado", required = false, defaultValue = "0") Integer estado) {
        List<PuestoOcupacional> puestos = puestoService.filtrarPuestoOcupacional(nombrePuesto, estado);
        return ResponseEntity.ok(puestos);
    }

    @PostMapping("/createPuesto")
    public ResponseEntity<Map<String, Object>> crearPuesto(
            @RequestParam(name = "unidadOrganica") String unidadOrganica,
            @RequestParam(name = "nombrePuesto") String nombrePuesto,
            @RequestParam(name = "dependenciaFuncional") String dependenciaFuncional,
            @RequestParam(name = "puestoQueSupervisa", defaultValue = "No aplica", required = false) String puestoQueSupervisa,
            @RequestParam(name = "misionPuesto") String misionPuesto,
            @RequestParam(name = "funcionesPuesto") String funcionesPuesto,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(name = "requisitos") List<String> requisitos,
            @RequestParam(name = "habilidades") List<String> habilidades) {
        try {
            // Crear Puesto
            PuestoOcupacional puesto = new PuestoOcupacional();
            puesto.setUnidadOrganica(unidadOrganica);
            puesto.setNombrePuesto(nombrePuesto);
            puesto.setDependenciaJerarquicaFuncional(dependenciaFuncional);
            puesto.setPuestoQueSupervisa(puestoQueSupervisa);
            puesto.setMisionDelPuesto(misionPuesto);
            puesto.setFuncionesDelPuesto(funcionesPuesto);
            puesto.setEstado(1);
            puesto.setCreateUser(createUser);
            puesto.setCreateDate(new Date());

            PuestoOcupacional savedPuesto = puestoDao.save(puesto);

            // Validar y Asociar Requisitos
            List<PuestoOcupacionalRelacionRequisito> relacionRequisitos = new ArrayList<>();

            if (requisitos.size() % 2 != 0) {
                throw new IllegalArgumentException(
                        "La lista de requisitos debe contener pares de valores (requisitoId, nivelId).");
            }

            for (int i = 0; i < requisitos.size(); i += 2) {
                Long requisitoId = Long.parseLong(requisitos.get(i));
                Long nivelId = Long.parseLong(requisitos.get(i + 1));

                // Validar existencia de requisitoId
                if (postulanteRequisitoDao.buscaId(requisitoId) == null) {
                    throw new IllegalArgumentException(
                            "No se pudo asociar el requisito con ID " + requisitoId + " al puesto.");
                }

                // Validar existencia de nivelId
                if (postulanteNivelesDao.buscaId(nivelId) == null) {
                    throw new IllegalArgumentException(
                            "No se pudo asociar el nivel con ID " + nivelId + " al requisito " + requisitoId + ".");
                }

                // Crear relación si ambos IDs son válidos
                PuestoOcupacionalRelacionRequisito relacionRequisito = new PuestoOcupacionalRelacionRequisito();
                relacionRequisito.setPuestoId(savedPuesto.getId());
                relacionRequisito.setRequisitoId(requisitoId);
                relacionRequisito.setNivelId(nivelId);
                relacionRequisitos.add(relacionRequisito);
            }
            // Guardar todas las relaciones
            requisitoService.saveAll(relacionRequisitos);

            // Validar y Asociar Habilidades
            List<PuestoOcupacionalRelacionSkill> relacionSkills = new ArrayList<>();
            for (String idSkill : habilidades) {
                if (postulanteSkillDao.buscaId(Long.parseLong(idSkill)) == null) {
                    throw new IllegalArgumentException(
                            "No se pudo asociar la habilidad con ID " + idSkill + " al puesto.");
                }
                PuestoOcupacionalRelacionSkill relacionSkill = new PuestoOcupacionalRelacionSkill();
                relacionSkill.setPuestoId(savedPuesto.getId());
                relacionSkill.setSkillId(Long.parseLong(idSkill));
                relacionSkills.add(relacionSkill);
            }
            skillService.saveAll(relacionSkills);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("puestoId", savedPuesto.getId());
            response.put("message", "Puesto creado exitosamente");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear puesto: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/updEstado")
    public ResponseEntity<?> actualizarEstado(
            @RequestParam Long id,
            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        PuestoOcupacional puestoServicio = puestoService.updateStatus(id, username);
        if (puestoServicio == null) {
            response.put("message", "El puesto con id [" + id + "] no se encuentra");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String accion = (puestoServicio.getEstado() == 1) ? "activado" : "desactivado";
        response.put("puesto", puestoServicio);
        response.put("message", "Puesto " + accion + " satisfactoriamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<Map<String, Object>> showPuesto(@RequestParam(required = true) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar que el ID no sea nulo
            if (id == null) {
                response.put("error", "Falta el parámetro 'id'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Buscar puesto por ID
            Optional<PuestoOcupacional> puestoOcupacionalOptional = puestoDao.findById(id);
            if (puestoOcupacionalOptional.isEmpty()) {
                response.put("error", "No se encontró un puesto con el ID proporcionado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Obtener datos del puesto
            PuestoOcupacional puestoOcupacional = puestoOcupacionalOptional.get();

            // Clasificar datos en categorías
            Map<String, Object> data = new HashMap<>();
            data.put("id", puestoOcupacional.getId());
            data.put("unidadOrganica", puestoOcupacional.getUnidadOrganica());
            data.put("nombrePuesto", puestoOcupacional.getNombrePuesto());
            data.put("dependenciaJerarquicaFuncional", puestoOcupacional.getDependenciaJerarquicaFuncional());
            data.put("puestoQueSupervisa", puestoOcupacional.getPuestoQueSupervisa());
            data.put("misionDelPuesto", puestoOcupacional.getMisionDelPuesto());
            data.put("funcionesDelPuesto", puestoOcupacional.getFuncionesDelPuesto());
            data.put("estado", puestoOcupacional.getEstado());
            // Agregar datos a la respuesta
            response.put("data", data);

            // Agregar requisitos
            List<PuestoOcupacionalRelacionRequisito> requisitos = requisitoService
                    .findByPuestoRequisito(puestoOcupacional.getId());

            List<Map<String, Object>> requisitosResponse = requisitos.stream().map(req -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", req.getId());
                map.put("puestoId", req.getPuestoId());
                map.put("requisitoId", req.getRequisitoId());
                map.put("descripcion",
                        req.getRequisito() != null ? req.getRequisito().getDescripcion() : null);
                map.put("nivelId", req.getNivelId());
                return map;
            }).collect(Collectors.toList());
            response.put("requisitos", requisitosResponse);

            // Agregar habilidades
            List<PuestoOcupacionalRelacionSkill> skills = skillService
                    .findByPuestoId(puestoOcupacional.getId());
            List<Map<String, Object>> skillsResponse = skills.stream().map(skill -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", skill.getId());
                map.put("puestoId", skill.getPuestoId());
                map.put("skillId", skill.getSkillId());
                map.put("descripcion", skill.getSkill() != null ? skill.getSkill().getDescripcion() : null);
                return map;
            }).collect(Collectors.toList());
            response.put("skills", skillsResponse);

            List<PostulanteNiveles> postulanteNiveles = postulanteNivelesDao.findAll();
            response.put("postulanteNiveles", postulanteNiveles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error inesperado al recuperar puesto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/deleteRequisito")
    public ResponseEntity<?> deleteRequisito(@RequestParam(name = "idRequisito") Long idRequisito,
            @RequestParam(name = "idPuesto") Long idPuesto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<PuestoOcupacionalRelacionRequisito> cc = requisitoService.busca(
                    idPuesto, idRequisito);
            if (cc.isEmpty()) {
                response.put("message", "El requisito [" + idRequisito + "] del puesto [" + idPuesto
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            requisitoService.deleteById(cc.get().getId());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar el requisito [" + idRequisito + "] del puesto [" + idPuesto + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino el requisito [" + idRequisito + "] del puesto [" + idPuesto
                        + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteSkill")
    public ResponseEntity<?> deleteSkill(@RequestParam(name = "idSkill") Long idSkill,
            @RequestParam(name = "idPuesto") Long idPuesto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<PuestoOcupacionalRelacionSkill> cc = skillService.busca(
                    idPuesto, idSkill);
            if (cc.isEmpty()) {
                response.put("message", "La habilidad [" + idSkill + "] del puesto [" + idPuesto
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            skillService.deleteById(cc.get().getId());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar la habilidad [" + idSkill + "] del puesto [" + idPuesto + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino la habilidad [" + idSkill + "] del puesto [" + idPuesto
                        + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> actualizarPostulante(
            @RequestParam(name = "puestoId") Long puestoId,
            @RequestParam(name = "unidadOrganica") String unidadOrganica,
            @RequestParam(name = "nombrePuesto") String nombrePuesto,
            @RequestParam(name = "dependenciaFuncional") String dependenciaFuncional,
            @RequestParam(name = "puestosSupervisa", defaultValue = "No aplica", required = false) String puestoQueSupervisa,
            @RequestParam(name = "misionPuesto") String misionPuesto,
            @RequestParam(name = "funcionesPuesto") String funcionesPuesto,
            @RequestParam(name = "updateUser") String updateUser,
            @RequestParam(name = "requisitos") List<String> requisitos,
            @RequestParam(name = "habilidades") List<String> habilidades) {
        try {
            // Find existing PostulanteData
            PuestoOcupacional existingPostulanteData = puestoDao.findById(puestoId)
                    .orElseThrow(() -> new RuntimeException("Puesto no encontrado"));

            // Update PostulanteData fields
            existingPostulanteData.setUnidadOrganica(unidadOrganica);
            existingPostulanteData.setNombrePuesto(nombrePuesto);
            existingPostulanteData.setDependenciaJerarquicaFuncional(dependenciaFuncional);
            existingPostulanteData.setPuestoQueSupervisa(puestoQueSupervisa);
            existingPostulanteData.setMisionDelPuesto(misionPuesto);
            existingPostulanteData.setFuncionesDelPuesto(funcionesPuesto);
            existingPostulanteData.setUpdateUser(updateUser);
            existingPostulanteData.setUpdateDate(new Date());
            PuestoOcupacional updatedPostulanteData = puestoDao.save(existingPostulanteData);

            // Procesar Requisitos
            List<PuestoOcupacionalRelacionRequisito> relacionRequisitos = new ArrayList<>();

            if (requisitos != null && requisitos.size() % 3 == 0) {
                for (int i = 0; i < requisitos.size(); i += 3) {
                    PuestoOcupacionalRelacionRequisito relacionRequisito = new PuestoOcupacionalRelacionRequisito();

                    Long primerValor = Long.parseLong(requisitos.get(i));
                    Long segundoValor = Long.parseLong(requisitos.get(i + 1));
                    Long tercerValor = Long.parseLong(requisitos.get(i + 2));

                    if (primerValor == 0) {
                        relacionRequisito.setPuestoId(updatedPostulanteData.getId());
                        relacionRequisito.setRequisitoId(segundoValor);
                        relacionRequisito.setNivelId(tercerValor);
                    } else {
                        // Buscar la entidad existente
                        Optional<PuestoOcupacionalRelacionRequisito> existente = requisitoService
                                .findById(primerValor);
                        if (existente.isPresent()) {
                            relacionRequisito = existente.get(); // Tomar entidad existente
                        } else {
                            throw new RuntimeException("El ID " + primerValor + " no existe en la base de datos.");
                        }
                        relacionRequisito.setRequisitoId(segundoValor);
                        relacionRequisito.setNivelId(tercerValor);
                    }
                    relacionRequisitos.add(relacionRequisito);
                }
                // Guardar los registros, ya sean nuevos o actualizados
                requisitoService.saveAll(relacionRequisitos);
            }

            // Procesar Habilidades
            List<PuestoOcupacionalRelacionSkill> relacionSkills = new ArrayList<>();
            if (habilidades != null && !habilidades.isEmpty()) {
                PuestoOcupacionalRelacionSkill relacionSkill = new PuestoOcupacionalRelacionSkill();
                relacionSkill.setPuestoId(updatedPostulanteData.getId());
                relacionSkill.setSkillId(Long.parseLong(habilidades.get(0)));
                relacionSkills.add(relacionSkill);
                skillService.saveAll(relacionSkills);
            }

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("postulanteDataId", updatedPostulanteData.getId());
            response.put("mensaje", "Postulante actualizado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al actualizar postulante: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
