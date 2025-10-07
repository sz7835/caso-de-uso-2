package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.SisParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.ActividadRepository;
import com.delta.deltanet.models.dao.TipoActividadRepository;
import com.delta.deltanet.models.entity.Actividad;
import com.delta.deltanet.models.entity.TipoActividad;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

@Service
public class TipoActividadService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private TipoActividadRepository tipoActividadRepository;

    @Autowired
    public ISisParamService parametroService;

    public List<TipoActividad> listarTodasLasActividades() {
        return tipoActividadRepository.findAll();
    }

    @Transactional
    public Map<String, Object> crearActividad(int idTipoAct, int personaId, String fecha, String hora, String detalle,
            String createUser) throws ParseException {
        Map<String, Object> response = new HashMap<>();

        // Verificar y ajustar el valor de detalle
        if (detalle.trim().isEmpty()) {
            detalle = "Detalle no proporcionado";
        }

        String strEtiqueta = "MILISEGUNDOS_REGISTRO_ACTIVIDAD";
        SisParam param = parametroService.buscaEtiqueta(strEtiqueta);
        if (param == null) {
            response.put("mensaje", "no se encuentra el parametro: [" + strEtiqueta + "]");
            return response;
        }

        //Aqui se obtiene el valor del parametro del tiempo de diferencia
        long parActRegMillis = Long.parseLong(param.getValor());

        // Ajustar el formato de la fecha y hora con la zona horaria de Lima
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("es-PE"));
        //formatter.setTimeZone(TimeZone.getTimeZone("America/Lima"));
        //String fechaHoraStr = fecha + " " + hora + ":59";
        //Date fechaHora = formatter.parse(fechaHoraStr);
        
        //Fecha y hora actual obtenida del servidor
        Date fechaHoraActual = new Date();
        
        
        
        Optional<Actividad> ultRegOptional = actividadRepository.findUltimaActividadAntesDe(personaId, fecha);

        if (ultRegOptional.isPresent()) {
            Actividad ultReg = ultRegOptional.get();
            
            //Valida si el tipo del ultimo registro es igual al que se quiere registrar
            if (ultReg.getTipoActividad().getId() == idTipoAct) {
                response.put("error", true);
                String sTipo = (idTipoAct == 1) ? "Entrada" : "Salida";
                response.put("message", "Ya se tiene registrada una " + sTipo + ", no puede crear otra consecutiva");
                return response;
            }

            Date fechaUltimoReg = ultReg.getCreateDate();
            
            long diffInMillis = fechaHoraActual.getTime() - fechaUltimoReg.getTime();
            
            if (diffInMillis < parActRegMillis) {
                response.put("error", true);
                int segundos = (int) (parActRegMillis / 1000);
                response.put("message", "Para crear una nueva actividad, debe transcurrir mínimo " + segundos + " segundos.");
                return response;
            }
        }

        // Proceder a crear la nueva actividad
        TipoActividad tipoActividad = tipoActividadRepository.findById(Long.valueOf(idTipoAct))
                .orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));

        Actividad nuevaActividad = new Actividad();
        nuevaActividad.setTipoActividad(tipoActividad);
        nuevaActividad.setPerPersonaId(personaId);
        nuevaActividad.setFecha(fecha);
        nuevaActividad.setRegistro(fecha + " " + hora + ":00");
        nuevaActividad.setDetalle(detalle);
        nuevaActividad.setCreateUser(createUser);
        nuevaActividad.setCreateDate(new Date());

        Actividad actividadGuardada = actividadRepository.save(nuevaActividad);
        response.put("error", false);
        response.put("message", "Actividad creada exitosamente");
        response.put("actividad", actividadGuardada);
        return response;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> filtrarActividades(Integer idActividad, Integer idPersona, String registroStr) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Actividad> cq = cb.createQuery(Actividad.class);

        Root<Actividad> actividadRoot = cq.from(Actividad.class);
        List<Predicate> predicates = new ArrayList<>();

        if (idActividad != null && idActividad != 9) {
            predicates.add(cb.equal(actividadRoot.get("tipoActividad"), idActividad));
        }

        if (idPersona != null) {
            predicates.add(cb.equal(actividadRoot.get("perPersonaId"), idPersona));
        }

        if (registroStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate registro = LocalDate.parse(registroStr, formatter);

            Predicate fechaInicio = cb.greaterThanOrEqualTo(actividadRoot.get("registro").as(LocalDate.class),
                    registro);
            LocalDate diaSiguiente = registro.plusDays(1);
            Predicate fechaFin = cb.lessThan(actividadRoot.get("registro").as(LocalDate.class), diaSiguiente);

            predicates.add(cb.and(fechaInicio, fechaFin));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Actividad> query = entityManager.createQuery(cq);
        List<Actividad> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return Collections.emptyList();
        }

        // Cambia el nombre de la variable dentro de la lambda para evitar conflictos
        List<Map<String, Object>> formattedResults = resultList.stream().map(actividadResult -> {
            Map<String, Object> formattedResult = new HashMap<>();
            formattedResult.put("id", actividadResult.getId());
            formattedResult.put("idTipoAct", actividadResult.getTipoActividad().getId());
            formattedResult.put("nomTipoAct", actividadResult.getTipoActividad().getNombre());
            formattedResult.put("perPersonaId", actividadResult.getPerPersonaId());
            formattedResult.put("fecha", actividadResult.getFecha());
            formattedResult.put("registro", actividadResult.getRegistro());
            formattedResult.put("detalle", actividadResult.getDetalle());
            formattedResult.put("createUser", actividadResult.getCreateUser());
            formattedResult.put("createDate", actividadResult.getCreateDate());
            formattedResult.put("updateUser", actividadResult.getUpdateUser());
            formattedResult.put("updateDate", actividadResult.getUpdateDate());
            return formattedResult;
        }).collect(Collectors.toList());

        return formattedResults;
    }

}
