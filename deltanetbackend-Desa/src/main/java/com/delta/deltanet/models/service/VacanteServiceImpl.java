package com.delta.deltanet.models.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.VacanteDao;
import com.delta.deltanet.models.entity.Vacante;

@Service
public class VacanteServiceImpl implements VacanteService {

    @Autowired
    private EntityManager entityManager;

    private final VacanteDao vacanteDao;

    public VacanteServiceImpl(VacanteDao vacanteDao) {
        this.vacanteDao = vacanteDao;
    }

    @Override
    @Transactional
    public String cambiarEstadoVacante(Long id, String usuario) {
        Vacante vacante = vacanteDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vacante no encontrada"));

        String mensaje;
        int nuevoEstado;

        switch (vacante.getEstado()) {
            case 1:
                nuevoEstado = 2;
                mensaje = "Desactivado exitosamente";
                break;
            case 2:
                nuevoEstado = 1;
                mensaje = "Activado exitosamente";
                break;
            default:
                throw new IllegalStateException("Estado desconocido");
        }

        vacante.setEstado(nuevoEstado);
        vacante.setUpdateUser(usuario);
        vacante.setUpdateDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));

        vacanteDao.save(vacante);

        return mensaje;
    }

    @Override
    @Transactional
    public List<Vacante> searchVacantes(
            Integer puestoId,
            Integer expGeneral,
            String createDate,
            Integer estado,
            Integer forAcadId,
            Integer gradSitAcadId,
            Boolean colegiatura) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vacante> query = cb.createQuery(Vacante.class);
        Root<Vacante> vacante = query.from(Vacante.class);
        List<Predicate> predicates = new ArrayList<>();

        if (puestoId != null && puestoId != 0) {
            predicates.add(cb.equal(vacante.get("puestoId"), puestoId));
        }

        if (expGeneral != null && expGeneral != 0) {
            predicates.add(cb.equal(vacante.get("expGeneral"), expGeneral));
        }

        if (createDate != null && !createDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = sdf.parse(createDate);

                // Create end date as the start of the next day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date endDate = calendar.getTime();

                // Search for dates between start of day and start of next day
                predicates.add(cb.between(
                        vacante.get("createDate"),
                        startDate,
                        endDate));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format, expected yyyy-MM-dd", e);
            }
        }

        if (estado != null && estado != 0) {
            predicates.add(cb.equal(vacante.get("estado"), estado));
        }

        if (forAcadId != null && forAcadId != 0) {
            predicates.add(cb.equal(vacante.get("forAcadId"), forAcadId));
        }

        if (gradSitAcadId != null && gradSitAcadId != 0) {
            predicates.add(cb.equal(vacante.get("gradSitAcadId"), gradSitAcadId));
        }

        if (colegiatura != null) {
            predicates.add(cb.equal(vacante.get("colegiatura"), colegiatura));
        }
        query.where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(vacante.get("createDate")));

        return entityManager.createQuery(query).getResultList();
    }
}
