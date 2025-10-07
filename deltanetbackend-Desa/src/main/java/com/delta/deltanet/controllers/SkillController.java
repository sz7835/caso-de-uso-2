package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.ISkillService;
import com.delta.deltanet.models.service.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/skill")
public class SkillController {

    @Autowired
    private ISkillService skillService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonaServiceImpl personaService;

    @GetMapping("/blandaClas/index")
    public ResponseEntity<?> blandaClasIndex(@RequestParam(required = false,name="descrip") String descrip,
                                             @RequestParam(required = false,name = "paginado", defaultValue = "0") Integer swPag,
                                             Pageable pageable){
        Map<String, Object> response = new HashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillBlandaClas> cq = cb.createQuery(SkillBlandaClas.class);//.distinct(true); // Asegurar resultados únicos
        Root<SkillBlandaClas> blandaRoot = cq.from(SkillBlandaClas.class);
        List<Predicate> predicates = new ArrayList<>();

        if(descrip!=null) predicates.add(cb.like(blandaRoot.get("descripcion"),"%" + descrip + "%"));

        cq.where(predicates.toArray(new Predicate[0]));
        if(swPag==0){
            List<SkillBlandaClas> listado = entityManager.createQuery(cq).getResultList();
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<SkillBlandaClas> listado = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
            Page<SkillBlandaClas> result = new PageImpl<>(listado,pageable,total);
            response.put("data",result.getContent());
            response.put("totRegs",result.getTotalElements());
            response.put("totPags", result.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/blandaClas/show")
    public ResponseEntity<?> blandaClasShow(@RequestParam( name="id") Long idReg) {
        Map<String, Object> response = new HashMap<>();
        SkillBlandaClas reg = skillService.findBlandaClasById(idReg);
        if (reg==null){
            response.put("message","El id [" + idReg + "] no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("data",reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/blandaClas/create")
    public ResponseEntity<?> blandaClasCreate(@RequestParam(name="descrip") String descrip,
                                              @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        String descripcion = descrip != null ? descrip.trim() : "";
        // Validaciones
        if (descripcion.isEmpty()) {
            response.put("message", "La descripción es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descripcion.matches("\\d+")) {
            response.put("message", "La descripción no puede estar compuesta únicamente por números");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            response.put("message", "La descripción solo puede incluir letras, números y espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (skillService.existsDescripcionActivoBlandaClas(descripcion, null)) {
            response.put("message", "Ya existe un registro activo con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            SkillBlandaClas reg = new SkillBlandaClas();
            reg.setDescripcion(descripcion);
            reg.setEstado(1);
            reg.setCreateFec(new Date());
            reg.setCreateUsr(username);
            skillService.saveBlandaClas(reg);
            response.put("message","Se creo el registro satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al crear el registro");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blandaClas/update")
    public ResponseEntity<?> blandaClasUpdate(@RequestParam(name="id") Long idReg,
                                              @RequestParam(name="descrip") String descrip,
                                              @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        String descripcion = descrip != null ? descrip.trim() : "";
        if (descripcion.isEmpty()) {
            response.put("message", "La descripción es obligatoria");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (descripcion.matches("\\d+")) {
            response.put("message", "La descripción no puede estar compuesta únicamente por números\"");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!descripcion.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            response.put("message", "La descripción solo puede incluir letras, números y espacios");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (skillService.existsDescripcionActivoBlandaClas(descripcion, idReg)) {
            response.put("message", "Ya existe un registro activo con la misma descripción");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            SkillBlandaClas reg = skillService.findBlandaClasById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDescripcion(descripcion);
            reg.setUpdateFec(new Date());
            reg.setUpdteUsr(username);
            skillService.saveBlandaClas(reg);
            response.put("message","Se actualizó el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blandaClas/updEstado")
    public ResponseEntity<?> blandaClasUpdEstado(@RequestParam(name="id") Long idReg,
                                                 @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillBlandaClas reg = skillService.findBlandaClasById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = reg.getEstado()==1?"desactivó":"activó";
            reg.setEstado(reg.getEstado()==1?0:1);
            reg.setUpdteUsr(username);
            reg.setUpdateFec(new Date());
            skillService.saveBlandaClas(reg);
            response.put("message","El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el estado del registro.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/blanda/list")
    public ResponseEntity<?> blandaList(@RequestParam(required = true,name="idclas") Long idClas){
        Map<String, Object> response = new HashMap<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillBlanda> cq = cb.createQuery(SkillBlanda.class);
        Root<SkillBlanda> blandaRoot = cq.from(SkillBlanda.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(blandaRoot.get("idClas"),idClas));
        cq.where(predicates.toArray(new Predicate[0]));
        List<SkillBlanda> listado = entityManager.createQuery(cq).getResultList();
        response.put("data",listado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/blanda/index")
    public ResponseEntity<?> blandaIndex(@RequestParam(required = false,name="idclas") Long idClas,
    									 @RequestParam(required = false,name="idhab") Long idhab,
                                         @RequestParam(required = false,name="descrip") String descrip,
                                         @RequestParam(required = false,name = "paginado", defaultValue = "0") Integer swPag,
                                         Pageable pageable){
        Map<String, Object> response = new HashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillBlanda> cq = cb.createQuery(SkillBlanda.class);
        Root<SkillBlanda> blandaRoot = cq.from(SkillBlanda.class);
        List<Predicate> predicates = new ArrayList<>();

        if(idClas!=null && idClas>0) predicates.add(cb.equal(blandaRoot.get("idClas"),idClas));
        if(descrip!=null) predicates.add(cb.like(blandaRoot.get("descripcion"),"%" + descrip + "%"));
        if(idhab!=null && idhab>0) predicates.add(cb.equal(blandaRoot.get("id"),idhab));
        
        cq.where(predicates.toArray(new Predicate[0]));
        if(swPag==0){
            List<SkillBlanda> listado = entityManager.createQuery(cq).getResultList();
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<SkillBlanda> listado = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
            Page<SkillBlanda> result = new PageImpl<>(listado,pageable,total);
            response.put("data",result.getContent());
            response.put("totRegs",result.getTotalElements());
            response.put("totPags", result.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/blanda/show")
    public ResponseEntity<?> blandaShow(@RequestParam( name="id") Long idReg){
        Map<String, Object> response = new HashMap<>();
        SkillBlanda reg = skillService.findBlandaById(idReg);
        if (reg==null){
            response.put("message","El id [" + idReg + "] no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillBlandaClas> cq = cb.createQuery(SkillBlandaClas.class);
        Root<SkillBlandaClas> blandaRoot = cq.from(SkillBlandaClas.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(blandaRoot.get("estado"),1));
        cq.where(predicates.toArray(new Predicate[0]));

        List<SkillBlandaClas> listado = entityManager.createQuery(cq).getResultList();;
        response.put("clas",listado);

        response.put("data",reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/blanda/create")
    public ResponseEntity<?> blandaLstClas(){
        Map<String, Object> response = new HashMap<>();
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SkillBlandaClas> cq = cb.createQuery(SkillBlandaClas.class);//.distinct(true); // Asegurar resultados únicos
            Root<SkillBlandaClas> blandaRoot = cq.from(SkillBlandaClas.class);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(blandaRoot.get("estado"),1));
            cq.where(predicates.toArray(new Predicate[0]));

            List<SkillBlandaClas> listado = entityManager.createQuery(cq).getResultList();;
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al obtener la información");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blanda/create")
    public ResponseEntity<?> blandaCreate(@RequestParam(name="idclas") Long idClas,
                                              @RequestParam(name="descrip") String descrip,
                                              @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillBlandaClas clas = skillService.findBlandaClasById(idClas);
            if(clas==null){
                response.put("message","El id [" + idClas + "] de habilidad blanda no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicados usando el servicio
            boolean existe = skillService.existsBlandaActivoByDescripcionAndIdClas(descripTrim, idClas, null);
            if (existe) {
                response.put("message", "Ya existe otro registro con esa descripción en la misma clasificación");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillBlanda reg = new SkillBlanda();
            reg.setBlandaClas(clas);
            reg.setDescripcion(descripTrim);
            reg.setEstado(1);
            reg.setCreateFec(new Date());
            reg.setCreateUsr(username);

            skillService.saveBlanda(reg);
            response.put("message","Se creo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al crear el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blanda/update")
    public ResponseEntity<?> blandaUpdate(@RequestParam(name="id") Long idReg,
                                          @RequestParam(name="idClas") Long idClas,
                                          @RequestParam(name="descrip") String descrip,
                                          @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillBlanda reg = skillService.findBlandaById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            SkillBlandaClas clas = skillService.findBlandaClasById(idClas);
            if(clas==null){
                response.put("message","El id [" + idClas + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicados usando el servicio
            boolean existe = skillService.existsBlandaActivoByDescripcionAndIdClas(descripTrim, idClas, idReg);
            if (existe) {
                response.put("message", "Ya existe otro registro con esa descripción en la misma clasificación");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setBlandaClas(clas);
            reg.setDescripcion(descripTrim);
            reg.setUpdateFec(new Date());
            reg.setUpdteUsr(username);

            skillService.saveBlanda(reg);
            response.put("message","Se actualizo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blanda/updEstado")
    public ResponseEntity<?> blandaUpdEstado(@RequestParam(name="id") Long idReg,
                                             @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillBlanda reg = skillService.findBlandaById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Solo permitir activar si no hay otro igual con estado 1 y misma clasificación
            if (reg.getEstado() == 0) {
                String descripTrim = reg.getDescripcion() != null ? reg.getDescripcion().trim() : "";
                // Usar el servicio para validar duplicados directamente
                boolean existe = skillService.existsBlandaActivoByDescripcionAndIdClas(descripTrim, reg.getBlandaClas().getId(), idReg);
                if (existe) {
                    response.put("message", "Ya existe otro registro activo con esa descripción en la misma clasificación");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            String cambio = reg.getEstado()==1?"desactivó":"activó";
            reg.setEstado(reg.getEstado()==1?0:1);
            reg.setUpdteUsr(username);
            reg.setUpdateFec(new Date());
            skillService.saveBlanda(reg);
            response.put("message","El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el estado del registro.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping("/duraClas/index")
    public ResponseEntity<?> duraClasIndex(@RequestParam(required = false,name="descrip") String descrip,
                                           @RequestParam(required = false,name = "paginado", defaultValue = "0") Integer swPag,
                                           Pageable pageable){
        Map<String, Object> response = new HashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDuraClas> cq = cb.createQuery(SkillDuraClas.class);
        Root<SkillDuraClas> duraRoot = cq.from(SkillDuraClas.class);
        List<Predicate> predicates = new ArrayList<>();

        if(descrip!=null) predicates.add(cb.like(duraRoot.get("descripcion"),"%" + descrip + "%"));

        cq.where(predicates.toArray(new Predicate[0]));
        if(swPag==0){
            List<SkillDuraClas> listado = entityManager.createQuery(cq).getResultList();
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<SkillDuraClas> listado = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
            Page<SkillDuraClas> result = new PageImpl<>(listado,pageable,total);
            response.put("data",result.getContent());
            response.put("totRegs",result.getTotalElements());
            response.put("totPags", result.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/duraClas/show")
    public ResponseEntity<?> duraClasShow(@RequestParam( name="id") Long idReg) {
        Map<String, Object> response = new HashMap<>();
        SkillDuraClas reg = skillService.findDuraClasById(idReg);
        if (reg==null){
            response.put("message","El id [" + idReg + "] no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.put("data",reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/duraClas/create")
    public ResponseEntity<?> duraClasCreate(@RequestParam(name="descrip") String descrip,
                                            @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicados solo por descripción
            boolean existe = skillService.existsDuraClasActivoByDescripcion(descripTrim, null);
            if (existe) {
                response.put("message", "Ya existe otro registro activo con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillDuraClas reg = new SkillDuraClas();
            reg.setDescripcion(descripTrim);
            reg.setEstado(1);
            reg.setCreateFec(new Date());
            reg.setCreateUsr(username);
            skillService.saveDuraClas(reg);
            response.put("message","Se creo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al crear el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/duraClas/update")
    public ResponseEntity<?> duraClasUpdate(@RequestParam(name="id") Long idReg,
                                            @RequestParam(name="descrip") String descrip,
                                            @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            boolean existe = skillService.existsDuraClasActivoByDescripcion(descripTrim, idReg);
            if (existe) {
                response.put("message", "Ya existe otro registro activo con esa descripción");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillDuraClas reg = skillService.findDuraClasById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDescripcion(descripTrim);
            reg.setUpdateFec(new Date());
            reg.setUpdteUsr(username);
            skillService.saveDuraClas(reg);
            response.put("message","Se actualizo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/duraClas/updEstado")
    public ResponseEntity<?> duraClasUpdEstado(@RequestParam(name="id") Long idReg,
                                               @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDuraClas reg = skillService.findDuraClasById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = reg.getEstado()==1?"desactivó":"activó";
            reg.setEstado(reg.getEstado()==1?0:1);
            reg.setUpdteUsr(username);
            reg.setUpdateFec(new Date());
            skillService.saveDuraClas(reg);
            response.put("message","El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el estado del registro.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/dura/list")
    public ResponseEntity<?> duraList(@RequestParam(required = true,name="idclas") Long idClas){
        Map<String, Object> response = new HashMap<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDura> cq = cb.createQuery(SkillDura.class);
        Root<SkillDura> blandaRoot = cq.from(SkillDura.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(blandaRoot.get("idClas"),idClas));
        cq.where(predicates.toArray(new Predicate[0]));
        List<SkillDura> listado = entityManager.createQuery(cq).getResultList();
        response.put("data",listado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dura/index")
    public ResponseEntity<?> duraIndex(@RequestParam(required = false,name="idclas") Long idClas,
                                       @RequestParam(required = false,name="descrip") String descrip,
                                       @RequestParam(required = false,name="idhab") Long idhab,
                                       @RequestParam(required = false,name = "paginado", defaultValue = "0") Integer swPag,
                                       Pageable pageable){
        Map<String, Object> response = new HashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDura> cq = cb.createQuery(SkillDura.class);
        Root<SkillDura> duraRoot = cq.from(SkillDura.class);
        List<Predicate> predicates = new ArrayList<>();

        if(idClas!=null && idClas>0) predicates.add(cb.equal(duraRoot.get("idClas"),idClas));
        if(descrip!=null) predicates.add(cb.like(duraRoot.get("descripcion"),"%" + descrip + "%"));
        if(idhab!=null && idhab>0) predicates.add(cb.equal(duraRoot.get("id"),idhab));

        cq.where(predicates.toArray(new Predicate[0]));
        if(swPag==0){
            List<SkillDura> listado = entityManager.createQuery(cq).getResultList();
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<SkillDura> listado = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
            Page<SkillDura> result = new PageImpl<>(listado,pageable,total);
            response.put("data",result.getContent());
            response.put("totRegs",result.getTotalElements());
            response.put("totPags", result.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/dura/show")
    public ResponseEntity<?> duraShow(@RequestParam( name="id") Long idReg){
        Map<String, Object> response = new HashMap<>();
        SkillDura reg = skillService.findDuraById(idReg);
        if (reg==null){
            response.put("message","El id [" + idReg + "] no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDuraClas> cq = cb.createQuery(SkillDuraClas.class);
        Root<SkillDuraClas> duraRoot = cq.from(SkillDuraClas.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(duraRoot.get("estado"),1));
        cq.where(predicates.toArray(new Predicate[0]));
        List<SkillDuraClas> listado = entityManager.createQuery(cq).getResultList();;
        response.put("clas",listado);
        response.put("data",reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dura/create")
    public ResponseEntity<?> duraLstClas(){
        Map<String, Object> response = new HashMap<>();
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SkillDuraClas> cq = cb.createQuery(SkillDuraClas.class);
            Root<SkillDuraClas> duraRoot = cq.from(SkillDuraClas.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(duraRoot.get("estado"),1));
            cq.where(predicates.toArray(new Predicate[0]));
            List<SkillDuraClas> listado = entityManager.createQuery(cq).getResultList();;
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al obtener la información.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/dura/create")
    public ResponseEntity<?> duraCreate(@RequestParam(name="idclas") Long idClas,
                                        @RequestParam(name="descrip") String descrip,
                                        @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDuraClas clas = skillService.findDuraClasById(idClas);
            if(clas==null){
                response.put("message","El id [" + idClas + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Validar duplicados por descripción + idClas
            boolean existe = skillService.existsDuraActivoByDescripcionAndIdClas(descripTrim, idClas, null);
            if (existe) {
                response.put("message", "Ya existe otro registro con esa descripción en la misma clasificación");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillDura reg = new SkillDura();
            reg.setDuraClas(clas);
            reg.setDescripcion(descripTrim);
            reg.setEstado(1);
            reg.setCreateFec(new Date());
            reg.setCreateUsr(username);
            skillService.saveDura(reg);
            /* inserta framework ---*/
            SkillDuraFrameWork fw = new SkillDuraFrameWork();
            fw.setDura(reg);
            fw.setDescripcion("---");
            fw.setEstado(1);
            fw.setCreateFec(new Date());
            fw.setCreateUsr(username);
            skillService.saveDuraFW(fw);
            response.put("message","Se creo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al crear el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/dura/update")
    public ResponseEntity<?> duraUpdate(@RequestParam(name="id") Long idReg,
                                        @RequestParam(name="idClas") Long idClas,
                                        @RequestParam(name="descrip") String descrip,
                                        @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDura reg = skillService.findDuraById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillDuraClas clas = skillService.findDuraClasById(idClas);
            if(clas==null){
                response.put("message","El idClas [" + idClas + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            boolean existe = skillService.existsDuraActivoByDescripcionAndIdClas(descripTrim, idClas, idReg);
            if (existe) {
                response.put("message", "Ya existe otro registro con esa descripción en la misma clasificación");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            reg.setDuraClas(clas);
            reg.setDescripcion(descripTrim);
            reg.setUpdateFec(new Date());
            reg.setUpdteUsr(username);
            skillService.saveDura(reg);
            response.put("message","Se actualizo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/dura/updEstado")
    public ResponseEntity<?> duraUpdEstado(@RequestParam(name="id") Long idReg,
                                           @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDura reg = skillService.findDuraById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String cambio = reg.getEstado()==1?"desactivó":"activó";
            reg.setEstado(reg.getEstado()==1?0:1);
            reg.setUpdteUsr(username);
            reg.setUpdateFec(new Date());
            skillService.saveDura(reg);
            response.put("message","El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el estado del registro.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @GetMapping("/duraFW/index")
    public ResponseEntity<?> duraFWIndex(@RequestParam(required = false,name="idDura") Long idDura,
                                         @RequestParam(required = false,name="descrip") String descrip,
                                         @RequestParam(required = false,name = "paginado", defaultValue = "0") Integer swPag,
                                         Pageable pageable){
        Map<String, Object> response = new HashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDuraFrameWork> cq = cb.createQuery(SkillDuraFrameWork.class);
        Root<SkillDuraFrameWork> duraRoot = cq.from(SkillDuraFrameWork.class);
        List<Predicate> predicates = new ArrayList<>();

        if(idDura!=null && idDura>0) predicates.add(cb.equal(duraRoot.get("idDura"),idDura));
        if(descrip!=null) predicates.add(cb.like(duraRoot.get("descripcion"),"%" + descrip + "%"));

        cq.where(predicates.toArray(new Predicate[0]));
        if(swPag==0){
            List<SkillDuraFrameWork> listado = entityManager.createQuery(cq).getResultList();
            response.put("data",listado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            List<SkillDuraFrameWork> listado = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
            Page<SkillDuraFrameWork> result = new PageImpl<>(listado,pageable,total);
            response.put("data",result.getContent());
            response.put("totRegs",result.getTotalElements());
            response.put("totPags", result.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/duraFW/show")
    public ResponseEntity<?> duraFWShow(@RequestParam( name="id") Long idReg){
        Map<String, Object> response = new HashMap<>();
        SkillDuraFrameWork reg = skillService.findDuraFWById(idReg);
        if (reg==null){
            response.put("message","El id [" + idReg + "] no se encuentra registrado.");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        response.put("data",reg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/duraFW/create")
    public ResponseEntity<?> duraFWLst(){
        Map<String, Object> response = new HashMap<>();
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SkillDuraClas> cq = cb.createQuery(SkillDuraClas.class);
            Root<SkillDuraClas> duraRoot = cq.from(SkillDuraClas.class);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(duraRoot.get("estado"),1));
            cq.where(predicates.toArray(new Predicate[0]));

            List<SkillDuraClas> listado = entityManager.createQuery(cq).getResultList();

            CriteriaBuilder cb1 = entityManager.getCriteriaBuilder();
            CriteriaQuery<SkillDura> cq1 = cb1.createQuery(SkillDura.class);
            Root<SkillDura> dura1Root = cq1.from(SkillDura.class);
            List<Predicate> predicates1 = new ArrayList<>();

            predicates1.add(cb1.equal(dura1Root.get("estado"),1));
            cq1.where(predicates1.toArray(new Predicate[0]));

            List<SkillDura> listado1 = entityManager.createQuery(cq1).getResultList();
            response.put("duraClas",listado);
            response.put("dura",listado1);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al obtener la información.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/duraFW/create")
    public ResponseEntity<?> duraFWCreate(@RequestParam(name="idDura") Long idDura,
                                          @RequestParam(name="descrip") String descrip,
                                          @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDura dura = skillService.findDuraById(idDura);
            if(dura==null){
                response.put("message","El id [" + idDura + "] de Dura no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!"---".equals(descripTrim)) {
                if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                    response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                // Validar duplicados por descripción y estado 1
                boolean existe = skillService.existsDuraFWActivoByDescripcion(descripTrim, null);
                if (existe) {
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            SkillDuraFrameWork reg = new SkillDuraFrameWork();
            reg.setDura(dura);
            reg.setDescripcion(descripTrim);
            reg.setEstado(1);
            reg.setCreateFec(new Date());
            reg.setCreateUsr(username);
            skillService.saveDuraFW(reg);
            response.put("message","Se creo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al crear el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/duraFW/update")
    public ResponseEntity<?> duraFWUpdate(@RequestParam(name="id") Long idReg,
                                        @RequestParam(name="idDura") Long idDura,
                                        @RequestParam(name="descrip") String descrip,
                                        @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDuraFrameWork reg = skillService.findDuraFWById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            SkillDura dura = skillService.findDuraById(idDura);
            if(dura==null){
                response.put("message","El idDura [" + idDura + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String descripTrim = descrip != null ? descrip.trim() : "";
            if (descripTrim.isEmpty()) {
                response.put("message", "La descripción es obligatoria y no puede estar vacía");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // Si la descripción es '---', permitir actualizar sin validaciones
            if (!"---".equals(descripTrim)) {
                if (descripTrim.matches("^[0-9]+$") || descripTrim.matches(".*[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s].*")) {
                    response.put("message", "La descripción no puede contener solo números ni caracteres especiales");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                boolean existe = skillService.existsDuraFWActivoByDescripcion(descripTrim, idReg);
                if (existe) {
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            reg.setDura(dura);
            reg.setDescripcion(descripTrim);
            reg.setUpdateFec(new Date());
            reg.setUpdteUsr(username);
            skillService.saveDuraFW(reg);
            response.put("message","Se actualizo el registro satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/duraFW/updEstado")
    public ResponseEntity<?> duraFWUpdEstado(@RequestParam(name="id") Long idReg,
                                             @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            SkillDuraFrameWork reg = skillService.findDuraFWById(idReg);
            if (reg == null) {
                response.put("message","El id [" + idReg + "] no se encuentra registrado.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            // No permitir activar si hay otro con la misma descripción y estado 1, excepto si es '---'
            if (reg.getEstado() == 0 && !"---".equals(reg.getDescripcion())) {
                boolean existe = skillService.existsDuraFWActivoByDescripcion(reg.getDescripcion(), idReg);
                if (existe) {
                    response.put("message", "Ya existe otro registro activo con esa descripción");
                    return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            String cambio = reg.getEstado()==1?"desactivó":"activó";
            reg.setEstado(reg.getEstado()==1?0:1);
            reg.setUpdteUsr(username);
            reg.setUpdateFec(new Date());
            skillService.saveDuraFW(reg);
            response.put("message","El registro se " + cambio + " satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al actualizar el estado del registro.");
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @PostMapping("/blanda/insper")
    public ResponseEntity<?> insPerBlanda(@RequestParam(name="idPer") Long idPer,
                                          @RequestParam(name="data") String[] data,
                                          @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {
            for(String row : data){
                Long idHab = Long.valueOf(row);
                Persona regPer = personaService.buscarId(idPer);
                SkillBlanda regBlanda = skillService.findBlandaById(idHab);

                if(regPer!=null && regBlanda !=null) {
                    SkillBlandaPers registro = new SkillBlandaPers();
                    registro.setPersona(regPer);
                    registro.setHabBlanda(regBlanda);
                    skillService.saveBlandaPers(registro);
                }
            }
            List<Object> listado = skillService.listaHabBlandas(idPer);
            List<lstBlanda> lst = new ArrayList<>();
            Iterator<Object> ilst = listado != null ? listado.iterator() : null;
            while (ilst != null && ilst.hasNext()) {
                Object[] colLst = (Object[]) ilst.next();
                lstBlanda x = new lstBlanda();
                x.setId((Long) colLst[0]);
                x.setIdPer((Long) colLst[1]);
                x.setIdHab((Long) colLst[2]);
                x.setDesClas((String) colLst[3]);
                x.setDescrip((String) colLst[4]);
                x.setEstado((Integer) colLst[5]);
                lst.add(x);
            }
            response.put("dataBlanda",lst);
            response.put("message","Se realizo el registro de las habilidades");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al insertar las habilidades.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/blanda/delper")
    public ResponseEntity<?> delPerBlanda(@RequestParam(name="idHabPerson") Long idHab) {
        Map<String, Object> response = new HashMap<>();
        try {
            skillService.deleteBlandaPers(idHab);
            response.put("message","Se realizo la eliminación de la habilidad");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al eliminar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/blanda/lstper")
    public ResponseEntity<?> lisPerBlanda(@RequestParam(name="idPer") Long idPer) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> listado = skillService.listaHabBlandas(idPer);
            List<lstBlanda> lst = new ArrayList<>();
            Iterator<Object> ilst = listado != null ? listado.iterator() : null;
            while (ilst != null && ilst.hasNext()) {
                Object[] colLst = (Object[]) ilst.next();
                lstBlanda x = new lstBlanda();
                x.setId((Long) colLst[0]);
                x.setIdPer((Long) colLst[1]);
                x.setIdHab((Long) colLst[2]);
                x.setDesClas((String) colLst[3]);
                x.setDescrip((String) colLst[4]);
                x.setEstado((Integer) colLst[5]);
                lst.add(x);
            }

            List<Object> lstSrv = skillService.listaHabDuras(idPer);
            List<lstDura> lstDura = new ArrayList<>();
            Iterator<Object> dlst = lstSrv != null ? lstSrv.iterator() : null;
            while (dlst != null && dlst.hasNext()) {
                Object[] colLst = (Object[]) dlst.next();
                lstDura x = new lstDura();
                x.setId((Long) colLst[0]);
                x.setIdPer((Long) colLst[1]);
                x.setIdHab((Long) colLst[2]);
                x.setDesClas((String) colLst[3]);
                x.setDesDura((String) colLst[4]);
                x.setDesFrame((String) colLst[5]);
                x.setEstado((Integer) colLst[6]);
                x.setIdFrame((Long) colLst[7]);
                lstDura.add(x);
            }
            response.put("message","Se obtuvo el listado de habilidades de [" + idPer + "]");
            response.put("dataBlanda",lst);
            response.put("dataDura",lstDura);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al listar las habilidades blandas de [" + idPer + "].");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/dura/insper")
    public ResponseEntity<?> insPerDura(@RequestParam(name="idPer") Long idPer,
                                         @RequestParam(name="idHab") String[] idHab,
                                         @RequestParam(name="idFrame") Long[] idFrame,
                                         @RequestParam(name="usuario") String username){
        Map<String, Object> response = new HashMap<>();
        try {

            Integer i = 0;
            for(String row : idHab) {
                Long idDura = Long.valueOf(row);
                Long idDuraFw = idFrame[i];

                Persona per = personaService.buscarId(idPer);
                SkillDura habDura = skillService.findDuraById(idDura);
                SkillDuraFrameWork habDuraFw = skillService.findDuraFWById(idDuraFw);
                if(habDura!=null && habDuraFw!=null && per!=null){
                    SkillDuraPers reg = new SkillDuraPers();
                    reg.setPersona(per);
                    reg.setHabDura(habDura);
                    reg.setHabDuraFw(habDuraFw);
                    skillService.saveDuraPers(reg);
                }
                i++;
            }
            List<Object> lstSrv = skillService.listaHabDuras(idPer);
            List<lstDura> lstDura = new ArrayList<>();
            Iterator<Object> dlst = lstSrv != null ? lstSrv.iterator() : null;
            while (dlst != null && dlst.hasNext()) {
                Object[] colLst = (Object[]) dlst.next();
                lstDura x = new lstDura();
                x.setId((Long) colLst[0]);
                x.setIdPer((Long) colLst[1]);
                x.setIdHab((Long) colLst[2]);
                x.setDesClas((String) colLst[3]);
                x.setDesDura((String) colLst[4]);
                x.setDesFrame((String) colLst[5]);
                x.setEstado((Integer) colLst[6]);
                x.setIdFrame((Long) colLst[7]);
                lstDura.add(x);
            }
            response.put("dataDura",lstDura);
            response.put("message","Se realizo el registro de las habilidades");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al insertar las habilidades.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/dura/delper")
    public ResponseEntity<?> delPerDura(@RequestParam(name="idHabPerson") Long idHab) {
        Map<String, Object> response = new HashMap<>();
        try {
            skillService.deleteDuraPers(idHab);
            response.put("message","Se realizo la eliminación de la habilidad");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al eliminar el registro.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/dura/filter")
    public ResponseEntity<?> duraFilter(@RequestParam(required = false, name="dscClas") String dscClas,
                                        @RequestParam(required = false, name="dscDura") String dscDura,
                                        @RequestParam(required = false, name="idclas") Long idclas,
                                        @RequestParam(required = false, name="idhab") Long idhab,
                                        @RequestParam(required = false, name="idfram") Long idfram,
                                        @RequestParam(required = false, name="dscFrame") String dscFrame){
        Map<String, Object> response = new HashMap<>();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SkillDuraFrameWork> cq = cb.createQuery(SkillDuraFrameWork.class);
            Root<SkillDuraFrameWork> frameRoot = cq.from(SkillDuraFrameWork.class);

            Join<SkillDuraFrameWork, SkillDura> duraJoin = frameRoot.join("dura", JoinType.INNER);
            Join<SkillDura, SkillDuraClas> clasJoin = duraJoin.join("duraClas", JoinType.INNER);
            /*
            cq.multiselect(frameRoot.get("id"),frameRoot.get("descripcion"),
                           duraJoin.get("id"), duraJoin.get("descripcion"),
                           clasJoin.get("id"), clasJoin.get("descripcion"));*/
            List<Predicate> predicates = new ArrayList<>();
            if(dscClas!=null) predicates.add(cb.like(clasJoin.get("descripcion"),"%" + dscClas + "%"));
            if(dscDura!=null) predicates.add(cb.like(duraJoin.get("descripcion"),"%" + dscDura + "%"));
            if(dscFrame!=null) predicates.add(cb.like(frameRoot.get("descripcion"),"%" + dscFrame + "%"));
            if(idclas!=null && idclas>0) predicates.add(cb.equal(duraJoin.get("idClas"),idclas));
            if(idhab!=null && idhab>0) predicates.add(cb.equal(frameRoot.get("idDura"),idhab));
            if(idfram!=null && idfram>0) predicates.add(cb.equal(frameRoot.get("id"),idfram));
            cq.where(predicates.toArray(new Predicate[0]));
            List<SkillDuraFrameWork> listado = entityManager.createQuery(cq).getResultList();
            List<lstFiltro> filtrado = new ArrayList<>();
            for(SkillDuraFrameWork reg : listado){
                lstFiltro x = new lstFiltro();
                x.setIdClas(reg.getDura().getIdClas());
                x.setDscClas(reg.getDura().getDuraClas().getDescripcion());
                x.setIdDura(reg.getDura().getId());
                x.setDscDura(reg.getDura().getDescripcion());
                x.setIdFrame(reg.getId());
                x.setDscFrame(reg.getDescripcion());
                x.setEstado(reg.getEstado());
                filtrado.add(x);
            }

            response.put("data",filtrado);
            response.put("message","Se realizo satisfactoriamente el filtrado de registros.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al filtrar los registros.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/framework/list")
    public ResponseEntity<?> frameworkList(@RequestParam(required = true,name="idhab") Long idHab){
        Map<String, Object> response = new HashMap<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SkillDuraFrameWork> cq = cb.createQuery(SkillDuraFrameWork.class);
        Root<SkillDuraFrameWork> blandaRoot = cq.from(SkillDuraFrameWork.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(blandaRoot.get("idDura"),idHab));
        cq.where(predicates.toArray(new Predicate[0]));
        List<SkillDuraFrameWork> listado = entityManager.createQuery(cq).getResultList();
        response.put("data",listado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

class lstFiltro{
    private Long idClas;
    private String dscClas;
    private Long idDura;
    private String dscDura;
    private Long idFrame;
    private String dscFrame;
    private Integer estado;

    public Long getIdClas() {
        return idClas;
    }

    public void setIdClas(Long idClas) {
        this.idClas = idClas;
    }

    public String getDscClas() {
        return dscClas;
    }

    public void setDscClas(String dscClas) {
        this.dscClas = dscClas;
    }

    public Long getIdDura() {
        return idDura;
    }

    public void setIdDura(Long idDura) {
        this.idDura = idDura;
    }

    public String getDscDura() {
        return dscDura;
    }

    public void setDscDura(String dscDura) {
        this.dscDura = dscDura;
    }

    public Long getIdFrame() {
        return idFrame;
    }

    public void setIdFrame(Long idFrame) {
        this.idFrame = idFrame;
    }

    public String getDscFrame() {
        return dscFrame;
    }

    public void setDscFrame(String dscFrame) {
        this.dscFrame = dscFrame;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

class lstDura{
    private Long id;
    private Long idPer;
    private String desClas;
    private Long idHab;
    private String desDura;
    private Long idFrame;
    private String desFrame;
    private Integer estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPer() {
        return idPer;
    }

    public void setIdPer(Long idPer) {
        this.idPer = idPer;
    }

    public String getDesClas() {
        return desClas;
    }

    public void setDesClas(String desClas) {
        this.desClas = desClas;
    }

    public Long getIdHab() {
        return idHab;
    }

    public void setIdHab(Long idHab) {
        this.idHab = idHab;
    }

    public String getDesDura() {
        return desDura;
    }

    public void setDesDura(String desDura) {
        this.desDura = desDura;
    }

    public Long getIdFrame() {
        return idFrame;
    }

    public void setIdFrame(Long idFrame) {
        this.idFrame = idFrame;
    }

    public String getDesFrame() {
        return desFrame;
    }

    public void setDesFrame(String desFrame) {
        this.desFrame = desFrame;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

class lstBlanda{
    private Long id;
    private Long idPer;
    private String desClas;
    private Long idHab;
    private String descrip;
    private Integer estado;

    public String getDesClas() {
        return desClas;
    }

    public void setDesClas(String desClas) {
        this.desClas = desClas;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPer() {
        return idPer;
    }

    public void setIdPer(Long idPer) {
        this.idPer = idPer;
    }

    public Long getIdHab() {
        return idHab;
    }

    public void setIdHab(Long idHab) {
        this.idHab = idHab;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
