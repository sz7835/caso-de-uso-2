package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDireccionDao;
import com.delta.deltanet.models.entity.Direccion;
import com.delta.deltanet.models.entity.lstDireccion1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

@Service
public class DireccionServiceImpl implements IDireccionService  {

    @Autowired
    public IDireccionDao direccionDao;

    @Override
    public Direccion save(Direccion direccion) {
        return direccionDao.save(direccion);
    }

    @Override
    public Direccion buscaDir(Long idDireccion) {
        return direccionDao.buscaDir(idDireccion);
    }

    @Override
    public List<Object> findAllPer(Long idPer) {
        return direccionDao.findAllPer(idPer);
    }
    @Override
    public List<Object> findByDirPer(Long tipo, String dire, int estado, Long idPer) {
        return direccionDao.findByDirPer(tipo, dire, estado, idPer);
    }
    @Override
    public List<Object> findByDirPer(Long tipo, Long idPer) {
        return direccionDao.findByDirPer(tipo, idPer);
    }
    @Override
    public List<Object> findByDirPer(Long tipo, String dire, Long idPer) {
        return direccionDao.findByDirPer(tipo, dire, idPer);
    }
    @Override
    public List<Object> findByDirPer(Long tipo, int estado, Long idPer) {
        return direccionDao.findByDirPer(tipo, estado, idPer);
    }
    @Override
    public List<Object> findByDirPer(String dire, Long idPer) {
        return direccionDao.findByDirPer(dire, idPer);
    }
    @Override
    public List<Object> findByDirPer(String dire, int estado, Long idPer) {
        return direccionDao.findByDirPer(dire, estado, idPer);
    }
    @Override
    public List<Object> findByDirPer2(int estado, Long idPer) {
        return direccionDao.findByDirPer2(estado, idPer);
    }

    @PersistenceContext
    private EntityManager entityManager;

    // @Transactional(readOnly = true)
    public List<lstDireccion1> search(Long idPersona, Long tipo, String direccion, String tipoUbigeo, Integer estado) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Direccion> cq = cb.createQuery(Direccion.class);
        Root<Direccion> direccionRoot = cq.from(Direccion.class);
        List<Predicate> predicates = new ArrayList<>();

        // Filtro por idPersona
        predicates.add(cb.equal(direccionRoot.get("persona").get("id"), idPersona));

        // Filtro por tipo, ignorando si es 9
        if (tipo != null) {
            predicates.add(cb.equal(direccionRoot.get("tipo"), tipo));
        }

        // Filtro opcional por dirección
        if (direccion != null && !direccion.isEmpty()) {
            predicates.add(cb.like(direccionRoot.get("direccion"), "%" + direccion + "%"));
        }

        // Filtro por estado, ignorando si es 9
        if (estado != 9) {
            predicates.add(cb.equal(direccionRoot.get("estado"), estado));
        }

        if (tipoUbigeo != null && !tipoUbigeo.equals("999999")) {
            String pattern = tipoUbigeo.replace("9999", "%").replace("99", "%");
            predicates.add(cb.like(direccionRoot.get("distrito").get("id").as(String.class), pattern));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Direccion> result = entityManager.createQuery(cq).getResultList();

        return result.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private lstDireccion1 convertToDto(Direccion direccion) {
        lstDireccion1 dto = new lstDireccion1();
        dto.setId(direccion.getId());
        dto.setIdPersona(direccion.getPersona() != null ? direccion.getPersona().getId() : null);
        dto.setTipo(direccion.getTipo());
        dto.setDireccion(direccion.getDireccion());
        dto.setIdDistrito(direccion.getDistrito() != null ? direccion.getDistrito().getId() : null);
        dto.setDistrito(direccion.getDistrito());
        dto.setEstado(direccion.getEstado());
        dto.setCreateUser(direccion.getCreateUser());
        dto.setCreateDate(direccion.getCreateDate());
        dto.setUpdateUser(direccion.getUpdateUser());
        dto.setUpdateDate(direccion.getUpdateDate());
        return dto;
    }

    @Override
    @Transactional
    public List<lstDireccion1> buscarDirecciones(Long idPersona, Long tipo, String direccion, Integer estado) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Direccion> cq = cb.createQuery(Direccion.class);
        Root<Direccion> root = cq.from(Direccion.class);

        List<Predicate> predicates = new ArrayList<>();

        if (idPersona != null) {
            predicates.add(cb.equal(root.get("persona").get("id"), idPersona));
        }
        if (tipo != null) {
            predicates.add(cb.equal(root.get("tipo"), tipo));
        }
        if (direccion != null && !direccion.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("direccion")), "%" + direccion.toLowerCase() + "%"));
        }
        if (estado != null && estado != 9) {
            predicates.add(cb.equal(root.get("estado"), estado));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<Direccion> result = entityManager.createQuery(cq).getResultList();

        return result.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<Direccion> findByPersonaAndDireccion(Long idPersona, String direccion) {
        return direccionDao.findByPersonaAndDireccion(idPersona, direccion);
    }
}
