package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.SisParam;
import com.delta.deltanet.models.service.ISisParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@SuppressWarnings("all")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/sisparam")
public class SisParamController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ISisParamService sisparamService;

    @GetMapping("/index")
    public ResponseEntity<?> listadoParams(@RequestParam(required = false,name="etiqueta") String etiqueta,
                                            @RequestParam(required = false,name="descrip") String descrip,
                                            @RequestParam(required = false,name="valor") String valor,
                                            @RequestParam(required = false,name="estado") Long estado){
        List<Object> respuesta = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SisParam> cq = cb.createQuery(SisParam.class).distinct(true); // Asegurar resultados únicos
        Root<SisParam> sisParamRoot = cq.from(SisParam.class);
        List<Predicate> predicates = new ArrayList<>();

        if (estado!=null){
            predicates.add(cb.equal(sisParamRoot.get("estado"), estado));
        }
        if (etiqueta!=null){
            predicates.add(cb.like(sisParamRoot.get("etiqueta"), "%" + etiqueta + "%"));
        }
        if (descrip!=null){
            predicates.add(cb.like(sisParamRoot.get("descripcion"), "%" + descrip + "%"));
        }
        if (valor!=null){
            predicates.add(cb.like(sisParamRoot.get("valor"), "%" + valor + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<SisParam> sisParam = entityManager.createQuery(cq).getResultList();
        respuesta.addAll(sisParam);

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> updEstado(@RequestParam(name="idParam") Long idParam,
                                       @RequestParam(name="updUsuario") String updUser){
        Map<String, Object> response = new HashMap<>();
        String message = "Registro desactivado con éxito";
        try {
            SisParam param = sisparamService.findById(idParam);
            if (param==null){
                response.put("mensaje", "No existe parametro con el ID: [" + idParam + "]");
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (param.getEstado() == 0){
                param.setEstado(1);
                message="Registro activado con éxito";
            } else if (param.getEstado() == 1) {
                param.setEstado(0);
            }
            param.setUpdDate(new Date());
            param.setUpdUser(updUser);
            sisparamService.save(param);
            response.put("mensaje", message);
        } catch (Exception e){
            response.put("mensaje", "Error al realizar cambio de estado del registro");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> grabar(@RequestParam(name="etiqueta") String etiqueta,
                                      @RequestParam(name = "descrip") String descrip,
                                      @RequestParam(name = "valor") String valor,
                                      @RequestParam(name = "createUser") String createUser) {
        try {
            SisParam param = new SisParam();
            param.setEtiqueta(etiqueta);
            param.setDescripcion(descrip);
            param.setValor(valor);
            param.setEstado(1);
            param.setCreUser(createUser);
            param.setCreDate(new Date());
            param = sisparamService.save(param);
            return ResponseEntity.ok(Map.of("mensaje", "Registro completo con éxito."));
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/show")
    public ResponseEntity<?> verRegistro(@RequestParam(name="idParam") Long idParam){
        Map<String, Object> response = new HashMap<>();

        try {
            SisParam param = sisparamService.findById(idParam);
            response.put("parametro",param);
        } catch (Exception e){
            response.put("mensaje", "Error al consultar el parametro: [" + idParam + "]");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestParam(name ="idParam") Long idParam,
                                    @RequestParam(required=false,name ="etiqueta") String etiqueta,
                                    @RequestParam(required=false,name = "descrip") String descrip,
                                    @RequestParam(required=false,name = "valor") String valor,
                                    @RequestParam(name="updUsuario") String updUser){
        Map<String, Object> response = new HashMap<>();
        if (etiqueta==null && descrip==null && valor==null){
            response.put("mensaje", "No se ha enviado valores a actualizar");
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            SisParam param = sisparamService.findById(idParam);
            if (param==null){
                response.put("mensaje", "No existe parametro con el ID: [" + idParam + "]");
                return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (etiqueta!=null) param.setEtiqueta(etiqueta);
            if (descrip!=null) param.setDescripcion(descrip);
            if (valor!=null) param.setValor(valor);

            param.setUpdDate(new Date());
            param.setUpdUser(updUser);
            sisparamService.save(param);
            response.put("mensaje", "Actualizacion aplicada satisfactoriamente");
        } catch (Exception e){
            response.put("mensaje", "Error al realizar la actuaización del registro");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
