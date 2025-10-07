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
import com.delta.deltanet.models.dao.PostulanteRelacionOfimaticaIdiomasDao;
import com.delta.deltanet.models.dao.PostulanteRelacionRequisitosDao;
import com.delta.deltanet.models.dao.PostulanteRelacionSkillDao;
import com.delta.deltanet.models.entity.PostulanteData;
import com.delta.deltanet.models.entity.PostulanteExperiencia;
import com.delta.deltanet.models.entity.PostulanteFormacion;
import com.delta.deltanet.models.entity.PostulanteGrado;
import com.delta.deltanet.models.entity.PostulanteNiveles;
import com.delta.deltanet.models.entity.PostulanteOfimaticaIdiomas;
import com.delta.deltanet.models.entity.PostulanteRelacionOfimaticaIdiomas;
import com.delta.deltanet.models.entity.PostulanteRelacionRequisitos;
import com.delta.deltanet.models.entity.PostulanteRelacionSkill;
import com.delta.deltanet.models.entity.PostulanteRequisito;
import com.delta.deltanet.models.entity.PostulanteSkill;
import com.delta.deltanet.models.service.PostulanteDataService;
import com.delta.deltanet.models.service.PostulanteOfimaticaIdiomasServiceImpl;
import com.delta.deltanet.models.service.PostulanteRequisitoServiceImpl;
import com.delta.deltanet.models.service.PostulanteSkillServiceImpl;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/postulanteData")
public class PostulanteDataController {

    @Autowired
    private PostulanteFormacionDao postulanteFormacionDao;
    @Autowired
    private PostulanteGradoDao postulanteGradoDao;
    @Autowired
    private PostulanteExperienciaDao postulanteExperienciaDao;
    @Autowired
    private PostulanteNivelesDao postulanteNivelesDao;
    @Autowired
    private PostulanteDataDao postulanteDataDao;
    @Autowired
    private PostulanteRelacionOfimaticaIdiomasDao postulanteRelacionOfimaticaIdiomasDao;
    @Autowired
    private PostulanteRelacionSkillDao postulanteRelacionSkillDao;
    @Autowired
    private PostulanteRelacionRequisitosDao postulanteRelacionRequisitosDao;
    @Autowired
    private PostulanteRequisitoServiceImpl requisitoService;
    @Autowired
    private PostulanteSkillServiceImpl postulanteSkillService;
    @Autowired
    private PostulanteOfimaticaIdiomasServiceImpl postulanteOfimaticaIdiomasService;
    @Autowired
    private PostulanteDataService postulanteDataService;

    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> obtenerDatos() {
        Map<String, Object> catalogo = new HashMap<>();

        List<PostulanteFormacion> postulanteFormacion = postulanteFormacionDao.findAll();
        List<PostulanteGrado> postulanteGrado = postulanteGradoDao.findAll();
        List<PostulanteExperiencia> postulanteExperiencia = postulanteExperienciaDao.findAll();
        List<PostulanteNiveles> postulanteNiveles = postulanteNivelesDao.findAll();
        catalogo.put("postulanteFormacion", postulanteFormacion);
        catalogo.put("postulanteGrado", postulanteGrado);
        catalogo.put("postulanteExperiencia", postulanteExperiencia);
        catalogo.put("postulanteNiveles", postulanteNiveles);

        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/indexSearch")
    public ResponseEntity<Map<String, Object>> obtenerDatosBusqueda() {
        Map<String, Object> catalogo = new HashMap<>();

        List<PostulanteFormacion> postulanteFormacion = postulanteFormacionDao.findAll();
        List<PostulanteGrado> postulanteGrado = postulanteGradoDao.findAll();
        catalogo.put("postulanteFormacion", postulanteFormacion);
        catalogo.put("postulanteGrado", postulanteGrado);

        return ResponseEntity.ok(catalogo);
    }

    @GetMapping("/searchApplicant")
    public ResponseEntity<Map<String, Object>> searchPostulantes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer formacionAcademica,
            @RequestParam(required = false) Integer gradoAcademico,
            @RequestParam(required = false) Integer estado) {
        Map<String, Object> data = new HashMap<>();
        List<PostulanteData> resultados = postulanteDataService.searchPostulantes(nombre, formacionAcademica,
                gradoAcademico, estado);
        data.put("data", resultados);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> crearPostulante(
            @RequestParam(name = "nombres") String nombreCompleto,
            @RequestParam(name = "apellidoPaterno") String apePaterno,
            @RequestParam(name = "apellidoMaterno") String apeMaterno,
            @RequestParam(name = "fechaNacimiento") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNac,
            @RequestParam(name = "domicilio") String domicilio,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "telefono") String telefono,
            @RequestParam(name = "formacionAcademica") Integer formacionAcademica,
            @RequestParam(name = "gradoAcademico") Integer situacionAcademica,
            @RequestParam(name = "requiereColegiatura") Integer colegiatura,
            @RequestParam(name = "experienciaGeneral") Integer experiencia,
            @RequestParam(name = "experienciaEspecifica") Integer experienciaEspecifica,
            @RequestParam(name = "aniosExperiencia") Integer aniosExperiencia,
            @RequestParam(name = "createUser") String createUser,
            @RequestParam(name = "requisitos") List<String> requisitos,
            @RequestParam(name = "habilidades") List<String> habilidades,
            @RequestParam(name = "conocimientos") List<String> conocimientos) {
        try {
            PostulanteData postulanteData = new PostulanteData();
            postulanteData.setNombre(nombreCompleto);
            postulanteData.setApellidoPat(apePaterno);
            postulanteData.setApellidoMat(apeMaterno);
            postulanteData.setFechaNac(fechaNac);
            postulanteData.setDomicilio(domicilio);
            postulanteData.setEmail(email);
            postulanteData.setNroTelefono(telefono);
            postulanteData.setFormacionAcademica(formacionAcademica);
            postulanteData.setGradoAcademico(situacionAcademica);
            postulanteData.setColegiatura(colegiatura);
            postulanteData.setExperienciaGeneral(experiencia);
            postulanteData.setExperienciaEspecifica(experienciaEspecifica);
            postulanteData.setAniosExperiencia(aniosExperiencia);
            postulanteData.setCreateUser(createUser);
            postulanteData.setCreateDate(new Date());
            postulanteData.setEstado(1);

            // Guardar PostulanteData
            PostulanteData savedPostulanteData = postulanteDataDao.save(postulanteData);

            // Procesar Requisitos
            List<PostulanteRelacionRequisitos> relacionRequisitos = new ArrayList<>();
            if (requisitos != null && requisitos.size() >= 2) {
                for (int i = 0; i < requisitos.size(); i += 2) {
                    PostulanteRelacionRequisitos relacionRequisito = new PostulanteRelacionRequisitos();
                    relacionRequisito.setPostulanteDataId(savedPostulanteData.getId().intValue());
                    relacionRequisito.setRequisitoId(Integer.parseInt(requisitos.get(i)));
                    relacionRequisito.setNivelId(Integer.parseInt(requisitos.get(i + 1)));
                    relacionRequisitos.add(relacionRequisito);
                }
                postulanteRelacionRequisitosDao.saveAll(relacionRequisitos);
            }

            // Procesar Habilidades
            List<PostulanteRelacionSkill> relacionSkills = new ArrayList<>();
            if (habilidades != null && !habilidades.isEmpty()) {
                PostulanteRelacionSkill relacionSkill = new PostulanteRelacionSkill();
                relacionSkill.setPostulanteDataId(savedPostulanteData.getId().intValue());
                relacionSkill.setSkillId(Integer.parseInt(habilidades.get(0)));
                relacionSkills.add(relacionSkill);
                postulanteRelacionSkillDao.saveAll(relacionSkills);
            }

            // Procesar Conocimientos
            List<PostulanteRelacionOfimaticaIdiomas> relacionConocimientos = new ArrayList<>();
            if (conocimientos != null && conocimientos.size() >= 2) {
                for (int i = 0; i < conocimientos.size(); i += 2) {
                    PostulanteRelacionOfimaticaIdiomas relacionConocimiento = new PostulanteRelacionOfimaticaIdiomas();
                    relacionConocimiento.setPostulanteDataId(Integer.parseInt(savedPostulanteData.getId().toString()));
                    relacionConocimiento.setOfimaticaIdiomasId(Integer.parseInt(conocimientos.get(i)));
                    relacionConocimiento.setNivelId(Integer.parseInt(conocimientos.get(i + 1)));
                    relacionConocimientos.add(relacionConocimiento);
                }
                postulanteRelacionOfimaticaIdiomasDao.saveAll(relacionConocimientos);
            }

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("postulanteDataId", savedPostulanteData.getId());
            response.put("mensaje", "Postulante creado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear postulante: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/show")
    public ResponseEntity<Map<String, Object>> showPostulante(@RequestParam(required = true) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validar que el ID no sea nulo
            if (id == null) {
                response.put("error", "Falta el parámetro 'id'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Buscar postulante por ID
            Optional<PostulanteData> postulanteDataOptional = postulanteDataDao.findById(id);
            if (postulanteDataOptional.isEmpty()) {
                response.put("error", "No se encontró un postulante con el ID proporcionado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Obtener datos del postulante
            PostulanteData postulanteData = postulanteDataOptional.get();

            // Clasificar datos en categorías
            Map<String, Object> data = new HashMap<>();
            data.put("id", postulanteData.getId());
            data.put("nombre", postulanteData.getNombre());
            data.put("apellidoPaterno", postulanteData.getApellidoPat());
            data.put("apellidoMaterno", postulanteData.getApellidoMat());
            data.put("fechaNacimiento", postulanteData.getFechaNac());
            data.put("domicilio", postulanteData.getDomicilio());
            data.put("email", postulanteData.getEmail());
            data.put("telefono", postulanteData.getNroTelefono());
            data.put("formacionAcademica", postulanteData.getFormacionAcademica());
            data.put("gradoAcademico", postulanteData.getGradoAcademico());
            data.put("colegiatura", postulanteData.getColegiatura());
            data.put("experienciaGeneral", postulanteData.getExperienciaGeneral());
            data.put("experienciaEspecifica", postulanteData.getExperienciaEspecifica());
            data.put("aniosExperiencia", postulanteData.getAniosExperiencia());

            // Agregar datos a la respuesta
            response.put("data", data);

            // Agregar requisitos
            List<PostulanteRelacionRequisitos> requisitos = postulanteRelacionRequisitosDao
                    .findByPostulanteDataIdWithRequisito(postulanteData.getId().intValue());

            List<Map<String, Object>> requisitosResponse = requisitos.stream().map(req -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", req.getId());
                map.put("postulanteDataId", req.getPostulanteDataId());
                map.put("requisitoId", req.getRequisitoId());
                map.put("descripcion",
                        req.getRequisito() != null ? req.getRequisito().getDescripcion() : null);
                map.put("nivelId", req.getNivelId());
                return map;
            }).collect(Collectors.toList());
            response.put("requisitos", requisitosResponse);

            // Agregar habilidades
            List<PostulanteRelacionSkill> skills = postulanteRelacionSkillDao
                    .findByPostulanteDataId(postulanteData.getId().intValue());
            List<Map<String, Object>> skillsResponse = skills.stream().map(skill -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", skill.getId());
                map.put("postulanteDataId", skill.getPostulanteDataId());
                map.put("skillId", skill.getSkillId());
                map.put("descripcion", skill.getSkill() != null ? skill.getSkill().getDescripcion() : null);
                return map;
            }).collect(Collectors.toList());
            response.put("skills", skillsResponse);

            // Agregar conocimientos
            List<PostulanteRelacionOfimaticaIdiomas> conocimientos = postulanteRelacionOfimaticaIdiomasDao
                    .findByPostulanteDataId(postulanteData.getId().intValue());
            List<Map<String, Object>> conocimientosResponse = conocimientos.stream().map(conocimiento -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", conocimiento.getId());
                map.put("postulanteDataId", conocimiento.getPostulanteDataId());
                map.put("ofimaticaIdiomasId", conocimiento.getOfimaticaIdiomasId());
                map.put("descripcion",
                        conocimiento.getOfimaticaIdiomas() != null ? conocimiento.getOfimaticaIdiomas().getDescripcion()
                                : null);
                map.put("nivelId", conocimiento.getNivelId());
                return map;
            }).collect(Collectors.toList());
            response.put("conocimientos", conocimientosResponse);

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

    @GetMapping("/searchRequest")
    public Map<String, Object> buscarRequisito(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {
        List<PostulanteRequisito> resultado = requisitoService.buscarPorNombreYEstado(nombre, estado);

        Map<String, Object> response = new HashMap<>();
        response.put("data", resultado);

        return response;
    }

    @GetMapping("/searchSkill")
    public Map<String, Object> buscarSkill(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {
        List<PostulanteSkill> resultado = postulanteSkillService.buscarPorNombreYEstado(nombre, estado);

        Map<String, Object> response = new HashMap<>();
        response.put("data", resultado);

        return response;
    }

    @GetMapping("/searchConocimiento")
    public Map<String, Object> buscarConocimiento(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false, defaultValue = "0") Integer estado) {
        List<PostulanteOfimaticaIdiomas> resultado = postulanteOfimaticaIdiomasService.buscarPorNombreYEstado(nombre,
                estado);

        Map<String, Object> response = new HashMap<>();
        response.put("data", resultado);

        return response;
    }

    @PostMapping("/deleteRequisito")
    public ResponseEntity<?> deleteRequisito(@RequestParam(name = "idRequisito") Long idRequisito,
            @RequestParam(name = "idPostulante") Long idPostulante) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<PostulanteRelacionRequisitos> cc = postulanteRelacionRequisitosDao.busca(
                    idPostulante.intValue(), idRequisito);
            if (cc.isEmpty()) {
                response.put("message", "El requisito [" + idRequisito + "] del postulante [" + idPostulante
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            postulanteRelacionRequisitosDao.deleteById(cc.get().getId());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar el requisito [" + idRequisito + "] del postulante [" + idPostulante + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino el requisito [" + idRequisito + "] del postulante [" + idPostulante
                        + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteSkill")
    public ResponseEntity<?> deleteSkill(@RequestParam(name = "idSkill") Long idSkill,
            @RequestParam(name = "idPostulante") Long idPostulante) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<PostulanteRelacionSkill> cc = postulanteRelacionSkillDao.busca(
                    idPostulante.intValue(), idSkill);
            if (cc.isEmpty()) {
                response.put("message", "La habilidad [" + idSkill + "] del postulante [" + idPostulante
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            postulanteRelacionSkillDao.deleteById(cc.get().getId());
        } catch (Exception e) {
            response.put("message",
                    "Error al eliminar la habilidad [" + idSkill + "] del postulante [" + idPostulante + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("message",
                "Se elimino la habilidad [" + idSkill + "] del postulante [" + idPostulante
                        + "] satisfactoriamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteConocimiento")
    public ResponseEntity<?> deleteConocimiento(@RequestParam(name = "idConocimiento") Long idConocimiento,
            @RequestParam(name = "idPostulante") Long idPostulante) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<PostulanteRelacionOfimaticaIdiomas> cc = postulanteRelacionOfimaticaIdiomasDao.busca(
                    idPostulante.intValue(), idConocimiento);
            if (cc.isEmpty()) {
                response.put("message", "El conocimiento [" + idConocimiento + "] del postulante [" + idPostulante
                        + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            postulanteRelacionOfimaticaIdiomasDao.deleteById(cc.get().getId());
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

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> actualizarPostulante(
            @RequestParam(name = "postulanteDataId") Long postulanteDataId,
            @RequestParam(name = "nombres") String nombreCompleto,
            @RequestParam(name = "apellidoPaterno") String apePaterno,
            @RequestParam(name = "apellidoMaterno") String apeMaterno,
            @RequestParam(name = "fechaNacimiento") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNac,
            @RequestParam(name = "domicilio") String domicilio,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "telefono") String telefono,
            @RequestParam(name = "formacionAcademica") Integer formacionAcademica,
            @RequestParam(name = "gradoAcademico") Integer situacionAcademica,
            @RequestParam(name = "requiereColegiatura") Integer colegiatura,
            @RequestParam(name = "experienciaGeneral") Integer experiencia,
            @RequestParam(name = "experienciaEspecifica") Integer experienciaEspecifica,
            @RequestParam(name = "aniosExperiencia") Integer aniosExperiencia,
            @RequestParam(name = "updateUser") String updateUser,
            @RequestParam(name = "requisitos") List<String> requisitos,
            @RequestParam(name = "habilidades") List<String> habilidades,
            @RequestParam(name = "conocimientos") List<String> conocimientos) {
        try {
            // Find existing PostulanteData
            PostulanteData existingPostulanteData = postulanteDataDao.findById(postulanteDataId)
                    .orElseThrow(() -> new RuntimeException("Postulante no encontrado"));

            // Update PostulanteData fields
            existingPostulanteData.setNombre(nombreCompleto);
            existingPostulanteData.setApellidoPat(apePaterno);
            existingPostulanteData.setApellidoMat(apeMaterno);
            existingPostulanteData.setFechaNac(fechaNac);
            existingPostulanteData.setDomicilio(domicilio);
            existingPostulanteData.setEmail(email);
            existingPostulanteData.setNroTelefono(telefono);
            existingPostulanteData.setFormacionAcademica(formacionAcademica);
            existingPostulanteData.setGradoAcademico(situacionAcademica);
            existingPostulanteData.setColegiatura(colegiatura);
            existingPostulanteData.setExperienciaGeneral(experiencia);
            existingPostulanteData.setExperienciaEspecifica(experienciaEspecifica);
            existingPostulanteData.setAniosExperiencia(aniosExperiencia);
            existingPostulanteData.setUpdateUser(updateUser);
            existingPostulanteData.setUpdateDate(new Date());
            PostulanteData updatedPostulanteData = postulanteDataDao.save(existingPostulanteData);

            // Procesar Requisitos
            List<PostulanteRelacionRequisitos> relacionRequisitos = new ArrayList<>();

            if (requisitos != null && requisitos.size() % 3 == 0) {
                for (int i = 0; i < requisitos.size(); i += 3) {
                    PostulanteRelacionRequisitos relacionRequisito = new PostulanteRelacionRequisitos();

                    int primerValor = Integer.parseInt(requisitos.get(i));
                    int segundoValor = Integer.parseInt(requisitos.get(i + 1));
                    int tercerValor = Integer.parseInt(requisitos.get(i + 2));

                    if (primerValor == 0) {
                        relacionRequisito.setPostulanteDataId(updatedPostulanteData.getId().intValue());
                        relacionRequisito.setRequisitoId(segundoValor);
                        relacionRequisito.setNivelId(tercerValor);
                    } else {
                        // Buscar la entidad existente
                        Optional<PostulanteRelacionRequisitos> existente = postulanteRelacionRequisitosDao
                                .findById((long) primerValor);
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
                postulanteRelacionRequisitosDao.saveAll(relacionRequisitos);
            }

            // Procesar Habilidades
            List<PostulanteRelacionSkill> relacionSkills = new ArrayList<>();
            if (habilidades != null && !habilidades.isEmpty()) {
                PostulanteRelacionSkill relacionSkill = new PostulanteRelacionSkill();
                relacionSkill.setPostulanteDataId(updatedPostulanteData.getId().intValue());
                relacionSkill.setSkillId(Integer.parseInt(habilidades.get(0)));
                relacionSkills.add(relacionSkill);
                postulanteRelacionSkillDao.saveAll(relacionSkills);
            }

            // Procesar Conocimientos

            List<PostulanteRelacionOfimaticaIdiomas> relacionConocimientos = new ArrayList<>();

            if (conocimientos != null && conocimientos.size() % 3 == 0) {
                for (int i = 0; i < conocimientos.size(); i += 3) {
                    PostulanteRelacionOfimaticaIdiomas relacionConocimiento = new PostulanteRelacionOfimaticaIdiomas();

                    int primerValor = Integer.parseInt(conocimientos.get(i));
                    int segundoValor = Integer.parseInt(conocimientos.get(i + 1));
                    int tercerValor = Integer.parseInt(conocimientos.get(i + 2));

                    if (primerValor == 0) {
                        relacionConocimiento.setPostulanteDataId(updatedPostulanteData.getId().intValue());
                        relacionConocimiento.setOfimaticaIdiomasId(segundoValor);
                        relacionConocimiento.setNivelId(tercerValor);
                    } else {
                        relacionConocimiento.setId((long) primerValor);
                        relacionConocimiento.setPostulanteDataId(postulanteDataId.intValue());
                        relacionConocimiento.setOfimaticaIdiomasId(segundoValor);
                        relacionConocimiento.setNivelId(tercerValor);
                    }

                    relacionConocimientos.add(relacionConocimiento);
                }

                postulanteRelacionOfimaticaIdiomasDao.saveAll(relacionConocimientos);
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

    @PostMapping("/cambiarEstado")
    public ResponseEntity<?> cambiarEstadoContratoMacro(
            @RequestParam Long id,
            @RequestParam String usuario,
            @RequestParam int estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            String mensaje = postulanteDataService.cambiarEstado(id, usuario, estado);
            response.put("message", mensaje);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            response.put("error", "Postulante no encontrado");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            response.put("error", "Estado no válido");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("error", "Error inesperado");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
