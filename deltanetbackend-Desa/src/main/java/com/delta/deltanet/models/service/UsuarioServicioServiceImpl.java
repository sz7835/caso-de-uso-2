package com.delta.deltanet.models.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IUsuarioServicioDao;
import com.delta.deltanet.models.entity.UsuarioServicio;

@Service
public class UsuarioServicioServiceImpl implements IUsuarioServicioService {
	
	@Autowired
	private IUsuarioServicioDao usuarioServicioDao;
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<UsuarioServicio> findAll() {
		return usuarioServicioDao.findAll();
	}

	@Override
	public UsuarioServicio findById(Long id) {
		return usuarioServicioDao.findById(id).orElse(null);
	}

	@Override
	public UsuarioServicio save(UsuarioServicio UsuarioServicio) {
		return usuarioServicioDao.save(UsuarioServicio);
	}

	@Override
	public void delete(Long id) {
		usuarioServicioDao.deleteById(id);
	}

	@Override
	public UsuarioServicio findByIdAndEstado(Long id, String estado) {
		return usuarioServicioDao.findByIdAndEstadoRegistro(id, estado);
	}

	@Override
	public List<UsuarioServicio> listado() {
		return usuarioServicioDao.findByEstadoRegistro("A");
	}

	@Override
	public UsuarioServicio findByUsuario(String usuario) {
		return usuarioServicioDao.findByUsuario(usuario);
	}

	@Override
	public Map<String, Object> findAllFiltroPaginado(Long idArea, String usuario, String nombre, String apellidos,
			int length, int start) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UsuarioServicio> query = cb.createQuery(UsuarioServicio.class);
        Root<UsuarioServicio> usuarioServicio = query.from(UsuarioServicio.class);
        
        Map<String, Object> objects = new HashMap<>();
        boolean allNull = true;
        
        List<Predicate> andPredicates = new ArrayList<>();
        
        if(idArea!=null) {
        	andPredicates.add(cb.equal(usuarioServicio.get("area").get("id"), idArea));
        	allNull = false;
        }
        if(usuario!=null) {
        	andPredicates.add(cb.like(usuarioServicio.get("usuario"), "%"+usuario+"%"));
        	allNull = false;
        }
        if(nombre!=null) {
        	andPredicates.add(cb.like(usuarioServicio.get("nombre"), "%"+nombre+"%"));
        	allNull = false;
        }
        if(apellidos!=null) {
        	andPredicates.add(cb.like(usuarioServicio.get("apellidos"), "%"+apellidos+"%"));
        	allNull = false;
        }

        if(allNull) {
        	query.select(usuarioServicio);
        }else {
    		query.select(usuarioServicio).where(cb.and(andPredicates.toArray(new Predicate[andPredicates.size()])));        	   
        }
        
        Long total = Long.valueOf(entityManager.createQuery(query).getResultList().size());
        List<UsuarioServicio> usuarioServicios = entityManager.createQuery(query).setMaxResults(length).setFirstResult(start).getResultList();
        
        objects.put("usuarios", usuarioServicios);
        objects.put("total", total);
        
        return objects;
	}


}
