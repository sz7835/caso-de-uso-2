package com.delta.deltanet.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.dao.PostulanteDataDao;
import com.delta.deltanet.models.dao.PostulanteExperienciaDao;
import com.delta.deltanet.models.dao.PostulanteFormacionDao;
import com.delta.deltanet.models.dao.PostulanteGradoDao;
import com.delta.deltanet.models.dao.PostulanteNivelesDao;
import com.delta.deltanet.models.dao.PuestoOcupacionalDao;
import com.delta.deltanet.models.dao.VacanteDao;
import com.delta.deltanet.models.dao.VacanteRelacionOfimaticaIdiomasDao;
import com.delta.deltanet.models.entity.PostulanteData;
import com.delta.deltanet.models.entity.PostulanteExperiencia;
import com.delta.deltanet.models.entity.PostulanteFormacion;
import com.delta.deltanet.models.entity.PostulanteGrado;
import com.delta.deltanet.models.entity.PostulanteNiveles;
import com.delta.deltanet.models.entity.PuestoOcupacional;
import com.delta.deltanet.models.entity.Vacante;
import com.delta.deltanet.models.entity.VacanteRelacionOfimaticaIdiomas;
import com.delta.deltanet.models.service.VacanteService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/vacante")
public class VacanteController {

    @Autowired
    private PostulanteFormacionDao postulanteFormacionDao;
    @Autowired
    private PostulanteGradoDao postulanteGradoDao;
    @Autowired
    private PostulanteExperienciaDao postulanteExperienciaDao;
    @Autowired
    private PostulanteNivelesDao postulanteNivelesDao;
    @Autowired
    private PuestoOcupacionalDao puestoOcupacionalDao;
    @Autowired
    private VacanteDao vacanteDao;
    @Autowired
    private VacanteRelacionOfimaticaIdiomasDao vacanteRelacionOfimaticaIdiomasDao;
    @Autowired
    private VacanteService vacanteService;
    @Autowired
    private PostulanteDataDao postulanteDataDao;

    @GetMapping("/indexSearch")
    public ResponseEntity<Map<String, Object>> obtenerDatos() {
        Map<String, Object> catalogo = new HashMap<>();
        List<PuestoOcupacional> puestoOcupacional = puestoOcupacionalDao.findAll();
        catalogo.put("puestoOcupacional", puestoOcupacional);
        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/indexCreate")
    public ResponseEntity<Map<String, Object>> obtenerDatosCreate() {
        Map<String, Object> catalogo = new HashMap<>();
        List<PuestoOcupacional> puestoOcupacional = puestoOcupacionalDao.findAll();
        List<PostulanteFormacion> postulanteFormacion = postulanteFormacionDao.findAll();
        List<PostulanteGrado> postulanteGrado = postulanteGradoDao.findAll();
        List<PostulanteExperiencia> postulanteExperiencia = postulanteExperienciaDao.findAll();
        List<PostulanteNiveles> postulanteNiveles = postulanteNivelesDao.findAll();
        catalogo.put("puestoOcupacional", puestoOcupacional);
        catalogo.put("postulanteFormacion", postulanteFormacion);
        catalogo.put("postulanteGrado", postulanteGrado);
        catalogo.put("postulanteExperiencia", postulanteExperiencia);
        catalogo.put("postulanteNiveles", postulanteNiveles);
        return ResponseEntity.ok(catalogo);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> crearVacante(
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "idPuesto") Integer idPuesto,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecInicio,
            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
            @RequestParam(name = "formacionAcademica") Integer formacionAcademica,
            @RequestParam(name = "gradoAcademico") Integer gradoAcademico,
            @RequestParam(name = "requiereColegiatura") Boolean requiereColegiatura,
            @RequestParam(name = "experienciaGeneral") Integer experienciaGeneral,
            @RequestParam(name = "experienciaEspecifica") Integer experienciaEspecifica,
            @RequestParam(name = "aniosExperiencia") Integer aniosExperiencia,
            @RequestParam(name = "conocimientoTec") String conocimientoTec,
            @RequestParam(name = "programaEspe") String programaEspe,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(name = "conocimientos") List<String> conocimientos) {
        try {
            Vacante vacante = new Vacante();
            vacante.setDescripcion(descripcion);
            vacante.setPuestoId(idPuesto);
            vacante.setFechaIni(fecInicio);
            vacante.setFechaFin(fecFin);
            vacante.setForAcadId(formacionAcademica);
            vacante.setGradSitAcadId(gradoAcademico);
            vacante.setColegiatura(requiereColegiatura);
            vacante.setExpGeneral(experienciaGeneral);
            vacante.setExpEspecId(experienciaEspecifica);
            vacante.setExpReq(aniosExperiencia);
            vacante.setConocimientos(conocimientoTec);
            vacante.setEspecializacion(programaEspe);
            vacante.setCreateUser(createUser);
            vacante.setCreateDate(new Date());
            vacante.setEstado(1);

            // Guardar savedVacante
            Vacante savedVacante = vacanteDao.save(vacante);

            // Procesar Conocimientos
            List<VacanteRelacionOfimaticaIdiomas> relacionConocimientos = new ArrayList<>();
            if (conocimientos != null && conocimientos.size() >= 2) {
                for (int i = 0; i < conocimientos.size(); i += 2) {
                    VacanteRelacionOfimaticaIdiomas relacionConocimiento = new VacanteRelacionOfimaticaIdiomas();
                    relacionConocimiento.setVacanteId(Integer.parseInt(savedVacante.getId().toString()));
                    relacionConocimiento.setOfimaticaIdiomasId(Integer.parseInt(conocimientos.get(i)));
                    relacionConocimiento.setNivelId(Integer.parseInt(conocimientos.get(i + 1)));
                    relacionConocimientos.add(relacionConocimiento);
                }
                vacanteRelacionOfimaticaIdiomasDao.saveAll(relacionConocimientos);
            }

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("savedVacante", savedVacante.getId());
            response.put("message", "Vacante creado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear postulante: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<Vacante>> buscarVacantes(
            @RequestParam(value = "id", required = false, defaultValue = "0") Long id,
            @RequestParam(value = "puesto", required = false, defaultValue = "0") Integer puestoId,
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(value = "estado", required = false, defaultValue = "0") Integer estado) {

        List<Vacante> vacantes;

        if (id != 0) {
            // Si se proporciona un ID específico, buscar solo por ese ID.
            vacantes = vacanteDao.findById(id).map(List::of).orElse(new ArrayList<>());
        } else {
            // Filtrar por puestoId, fechas y estado.
            vacantes = vacanteDao.findAll().stream()
                    .filter(v -> puestoId == 0 || v.getPuestoId() == puestoId)
                    .filter(v -> (fechaInicio == null || !v.getFechaIni().before(fechaInicio)) &&
                            (fechaFin == null || !v.getFechaFin().after(fechaFin)))
                    .filter(v -> estado == 0 || v.getEstado() == estado)
                    .collect(Collectors.toList());;
        }

        return ResponseEntity.ok(vacantes);
    }

    @GetMapping("/show")
    public ResponseEntity<Map<String, Object>> showVacante(@RequestParam(required = true) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar que el ID no sea nulo
            if (id == null) {
                response.put("error", "Falta el parámetro 'id'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Buscar postulante por ID
            Optional<Vacante> vacanteDataOpt = vacanteDao.findById(id);
            if (vacanteDataOpt.isEmpty()) {
                response.put("error", "No se encontró la vacante con el ID proporcionado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Obtener datos del postulante
            Vacante vacanteData = vacanteDataOpt.get();

            // Clasificar datos en categorías
            Map<String, Object> data = new HashMap<>();
            data.put("id", vacanteData.getId());
            data.put("descripcion", vacanteData.getDescripcion());
            data.put("puestoId", vacanteData.getPuestoId());
            data.put("fechaInicio", vacanteData.getFechaIni());
            data.put("fechaFin", vacanteData.getFechaFin());
            data.put("formacionAcademicaId", vacanteData.getForAcadId());
            data.put("gradoAcademicoId", vacanteData.getGradSitAcadId());
            data.put("colegiatura", vacanteData.isColegiatura());
            data.put("experienciaGeneral", vacanteData.getExpGeneral());
            data.put("experienciaEspecificaId", vacanteData.getExpEspecId());
            data.put("experienciaRequerida", vacanteData.getExpReq());
            data.put("conocimientos", vacanteData.getConocimientos());
            data.put("especializacion", vacanteData.getEspecializacion());
            // Agregar datos a la respuesta
            response.put("data", data);

            // Agregar conocimientos
            List<VacanteRelacionOfimaticaIdiomas> conocimientos = vacanteRelacionOfimaticaIdiomasDao
                    .findByVacanteId(vacanteData.getId().intValue());
            List<Map<String, Object>> conocimientosResponse = conocimientos.stream().map(conocimiento -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", conocimiento.getId());
                map.put("vacanteId", conocimiento.getVacanteId());
                map.put("ofimaticaIdiomasId", conocimiento.getOfimaticaIdiomasId());
                map.put("descripcion",
                        conocimiento.getOfimaticaIdiomas() != null ? conocimiento.getOfimaticaIdiomas().getDescripcion()
                                : null);
                map.put("nivelId", conocimiento.getNivelId());
                return map;
            }).collect(Collectors.toList());
            response.put("conocimientos", conocimientosResponse);

            List<PuestoOcupacional> puestoOcupacional = puestoOcupacionalDao.findAll();
            response.put("puestoOcupacional", puestoOcupacional);

            List<PostulanteFormacion> postulanteFormacion = postulanteFormacionDao.findAll();
            response.put("postulanteFormacion", postulanteFormacion);

            List<PostulanteGrado> postulanteGrado = postulanteGradoDao.findAll();
            response.put("postulanteGrado", postulanteGrado);

            List<PostulanteExperiencia> postulanteExperiencia = postulanteExperienciaDao.findAll();
            response.put("postulanteExperiencia", postulanteExperiencia);

            List<PostulanteNiveles> postulanteNiveles = postulanteNivelesDao.findAll();
            response.put("postulanteNiveles", postulanteNiveles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error inesperado al recuperar postulante: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> actualizarPostulante(
            @RequestParam(name = "vacancyId") Long vacancyId,
            @RequestParam(name = "descripcion") String descripcion,
            @RequestParam(name = "idPuesto") Integer idPuesto,
            @RequestParam(name = "fecInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecInicio,
            @RequestParam(name = "fecFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
            @RequestParam(name = "formacionAcademica") Integer formacionAcademica,
            @RequestParam(name = "gradoAcademico") Integer gradoAcademico,
            @RequestParam(name = "requiereColegiatura") Boolean requiereColegiatura,
            @RequestParam(name = "experienciaGeneral") Integer experienciaGeneral,
            @RequestParam(name = "experienciaEspecifica") Integer experienciaEspecifica,
            @RequestParam(name = "aniosExperiencia") Integer aniosExperiencia,
            @RequestParam(name = "conocimientoTec") String conocimientoTec,
            @RequestParam(name = "programaEspe") String programaEspe,
            @RequestParam(name = "updateUser") String updateUser,
            @RequestParam(name = "conocimientos") List<String> conocimientos) {
        try {
            // Find existing PostulanteData
            Vacante existingVacante = vacanteDao.findById(vacancyId)
                    .orElseThrow(() -> new RuntimeException("Vacante no encontrada"));

            existingVacante.setDescripcion(descripcion);
            existingVacante.setPuestoId(idPuesto);
            existingVacante.setFechaIni(fecInicio);
            existingVacante.setFechaFin(fecFin);
            existingVacante.setForAcadId(formacionAcademica);
            existingVacante.setGradSitAcadId(gradoAcademico);
            existingVacante.setColegiatura(requiereColegiatura);
            existingVacante.setExpGeneral(experienciaGeneral);
            existingVacante.setExpEspecId(experienciaEspecifica);
            existingVacante.setExpReq(aniosExperiencia);
            existingVacante.setConocimientos(conocimientoTec);
            existingVacante.setEspecializacion(programaEspe);
            existingVacante.setUpdateUser(updateUser);
            existingVacante.setUpdateDate(new Date());

            Vacante updatedVacante = vacanteDao.save(existingVacante);

            // Procesar Requisitos
            List<VacanteRelacionOfimaticaIdiomas> relacionConocimientos = new ArrayList<>();

            if (conocimientos != null && conocimientos.size() % 3 == 0) {
                for (int i = 0; i < conocimientos.size(); i += 3) {
                    VacanteRelacionOfimaticaIdiomas relacionConocimiento = new VacanteRelacionOfimaticaIdiomas();

                    Long primerValor = Long.parseLong(conocimientos.get(i));
                    Long segundoValor = Long.parseLong(conocimientos.get(i + 1));
                    Long tercerValor = Long.parseLong(conocimientos.get(i + 2));

                    if (primerValor == 0) {
                        relacionConocimiento.setVacanteId(updatedVacante.getId().intValue());
                        relacionConocimiento.setOfimaticaIdiomasId(segundoValor.intValue());
                        relacionConocimiento.setNivelId(tercerValor.intValue());
                    } else {
                        // Buscar la entidad existente
                        Optional<VacanteRelacionOfimaticaIdiomas> existente = vacanteRelacionOfimaticaIdiomasDao
                                .findById(primerValor);
                        if (existente.isPresent()) {
                            relacionConocimiento = existente.get(); // Tomar entidad existente
                        } else {
                            throw new RuntimeException("El ID " + primerValor + " no existe en la base de datos.");
                        }
                        relacionConocimiento.setOfimaticaIdiomasId(segundoValor.intValue());
                        relacionConocimiento.setNivelId(tercerValor.intValue());
                    }
                    relacionConocimientos.add(relacionConocimiento);
                }
                // Guardar los registros, ya sean nuevos o actualizados
                vacanteRelacionOfimaticaIdiomasDao.saveAll(relacionConocimientos);
            }

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("vacanteId", updatedVacante.getId());
            response.put("message", "Vacante actualizada exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al actualizar la vacante: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/deleteConocimiento")
    public ResponseEntity<?> deleteConocimiento(@RequestParam(name = "idConocimiento") Long idConocimiento,
            @RequestParam(name = "idPostulante") Long idPostulante) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<VacanteRelacionOfimaticaIdiomas> cc = vacanteRelacionOfimaticaIdiomasDao.busca(
                    idPostulante.intValue(), idConocimiento);
            if (cc.isEmpty()) {
                response.put("message", "El conocimiento [" + idConocimiento + "] del postulante [" + idPostulante
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            vacanteRelacionOfimaticaIdiomasDao.deleteById(cc.get().getId());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar el conocimiento [" + idConocimiento + "] del postulante [" + idPostulante + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino el conocimiento [" + idConocimiento + "] del postulante [" + idPostulante
                        + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cambiarEstado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoVacante(
            @RequestParam Long id,
            @RequestParam String usuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            String mensaje = vacanteService.cambiarEstadoVacante(id, usuario);
            response.put("message", mensaje);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("message", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/reporte-index")
    public ResponseEntity<Map<String, Object>> obtenerDatosReporteIndex() {
        Map<String, Object> catalogo = new HashMap<>();
        List<PuestoOcupacional> puestoOcupacional = puestoOcupacionalDao.findAll();
        List<PostulanteFormacion> postulanteFormacion = postulanteFormacionDao.findAll();
        List<PostulanteGrado> postulanteGrado = postulanteGradoDao.findAll();
        catalogo.put("puesto", puestoOcupacional);
        catalogo.put("formacionAcademica", postulanteFormacion);
        catalogo.put("gradoSituacionAcad", postulanteGrado);
        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/reporte-search")
    public ResponseEntity<List<Vacante>> searchVacantes(
            @RequestParam(required = false, name = "puesto") Integer puestoId,
            @RequestParam(required = false, name = "experienciaGeneral") Integer expGeneral,
            @RequestParam(required = false, name = "fechaPubli") String createDate,
            @RequestParam(required = false, name = "estado") Integer estado,
            @RequestParam(required = false, name = "formacionAcademica") Integer forAcadId,
            @RequestParam(required = false, name = "gradoAcademico") Integer gradSitAcadId,
            @RequestParam(required = false, name = "requiereColegiatura") Boolean colegiatura) {
        List<Vacante> vacantes = vacanteService.searchVacantes(
                puestoId, expGeneral, createDate, estado, forAcadId, gradSitAcadId, colegiatura);
        return ResponseEntity.ok(vacantes);
    }

    @GetMapping("/filter-postulantes")
    public ResponseEntity<?> filterPostulantesByVacancy(@RequestParam(required = true) Long id) {
        try {
            Optional<Vacante> optionalVacante = vacanteDao.findById(id);
            if (!optionalVacante.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Vacante vacante = optionalVacante.get();

            List<PostulanteData> allPostulantes = postulanteDataDao.findAll();

            // Filter postulantes based on vacancy requirements
            List<PostulanteData> filteredPostulantes = allPostulantes.stream()
                    .filter(postulante -> {
                        // Check formación académica
                        if (postulante.getFormacionAcademica() == null ||
                                postulante.getFormacionAcademica() != vacante.getForAcadId()) {
                            return false;
                        }

                        // Check grado académico
                        if (postulante.getGradoAcademico() == null ||
                                postulante.getGradoAcademico() != vacante.getGradSitAcadId()) {
                            return false;
                        }

                        // Check colegiatura
                        boolean vacanteRequiereColegiatura = vacante.isColegiatura();
                        boolean postulanteTieneColegiatura = postulante.getColegiatura() != null
                                && postulante.getColegiatura() == 1;
                        if (vacanteRequiereColegiatura && !postulanteTieneColegiatura) {
                            return false;
                        }

                        // Check experiencia general
                        // Postulante debe tener experiencia igual o mayor a la requerida
                        if (postulante.getExperienciaGeneral() == null ||
                                vacante.getExpGeneral() == null ||
                                postulante.getExperienciaGeneral() < vacante.getExpGeneral()) {
                            return false;
                        }

                        return true;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("vacante", vacante);
            response.put("postulantes", filteredPostulantes);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al filtrar los postulantes");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}