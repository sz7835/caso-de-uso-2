package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.CatalogoServicio;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogoServicioCustomServiceImpl implements ICatalogoServicioCustomService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CatalogoServicio> findByFilters(Long areaId, String nombre, String estado) {
        StringBuilder sb = new StringBuilder("SELECT c FROM CatalogoServicio c WHERE 1=1");
        List<Object> params = new ArrayList<>();
        int idx = 1;
        if (areaId != null) {
            sb.append(" AND c.area.id = ?" + idx);
            params.add(areaId);
            idx++;
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            sb.append(" AND LOWER(c.nombre) LIKE ?" + idx);
            params.add("%" + nombre.toLowerCase() + "%");
            idx++;
        }
        if (estado != null && !estado.trim().isEmpty()) {
            sb.append(" AND c.estadoRegistro = ?" + idx);
            params.add(estado);
            idx++;
        }
        TypedQuery<CatalogoServicio> query = entityManager.createQuery(sb.toString(), CatalogoServicio.class);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i + 1, params.get(i));
        }
        return query.getResultList();
    }
}
