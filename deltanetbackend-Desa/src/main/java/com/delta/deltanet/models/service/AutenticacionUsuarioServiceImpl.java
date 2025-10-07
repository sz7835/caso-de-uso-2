package com.delta.deltanet.models.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.delta.deltanet.models.entity.Relacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IAutenticacionUsuarioDao;
import com.delta.deltanet.models.entity.AutenticacionUsuario;

@Service
public class AutenticacionUsuarioServiceImpl implements IAutenticacionUsuarioService {
	
	@Autowired private IAutenticacionUsuarioDao autenticacionUsuarioDao;
	@PersistenceContext private EntityManager entityManager;

	@Override
	public List<AutenticacionUsuario> findAll() {
		return autenticacionUsuarioDao.findAll();
	}

	@Override
	public AutenticacionUsuario buscaUserDelta2(Long id) {
		return autenticacionUsuarioDao.buscaUserDelta2(id);
	}

	@Override
	public AutenticacionUsuario buscaUserDelta(Long id) {
		return autenticacionUsuarioDao.buscaUserDelta(id);
	}

	public List<Object> buscaUsuarioIdArea(Long idArea){
		return autenticacionUsuarioDao.buscaUsuarioIdArea(idArea);
	}

	@Override
	public Optional<AutenticacionUsuario> findById(Long id) {
		return autenticacionUsuarioDao.findById(id);
	}

	@Override
	public AutenticacionUsuario save(AutenticacionUsuario autenticacionUsuario) {
		return autenticacionUsuarioDao.save(autenticacionUsuario);
	}

	@Override
	public void delete(Long id) {
		autenticacionUsuarioDao.deleteById(id);
	}

	@Override
	public AutenticacionUsuario findByUsuario(String usuario) {
		return autenticacionUsuarioDao.findByUsuario(usuario);
	}

	@Override
	public Map<String, Object> findAllFiltroPaginado(Long idArea, String usuario, String nombre, String apellidos,
			int length, int start) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutenticacionUsuario> query = cb.createQuery(AutenticacionUsuario.class);
        Root<AutenticacionUsuario> AutenticacionUsuario = query.from(AutenticacionUsuario.class);
        
        Map<String, Object> objects = new HashMap<>();
        boolean allNull = true;
        
        List<Predicate> andPredicates = new ArrayList<>();
        
        if(idArea!=null) {
        	andPredicates.add(cb.equal(AutenticacionUsuario.get("area").get("id"), idArea));
        	allNull = false;
        }
        if(usuario!=null) {
        	andPredicates.add(cb.like(AutenticacionUsuario.get("usuario"), "%"+usuario+"%"));
        	allNull = false;
        }
        if(nombre!=null) {
        	andPredicates.add(cb.like(AutenticacionUsuario.get("nombre"), "%"+nombre+"%"));
        	allNull = false;
        }
        if(apellidos!=null) {
        	andPredicates.add(cb.like(AutenticacionUsuario.get("apellidos"), "%"+apellidos+"%"));
        	allNull = false;
        }

        if(allNull) {
        	query.select(AutenticacionUsuario);
        }else {
    		query.select(AutenticacionUsuario).where(cb.and(andPredicates.toArray(new Predicate[andPredicates.size()])));        	   
        }
        
        Long total = Long.valueOf(entityManager.createQuery(query).getResultList().size());
        List<AutenticacionUsuario> AutenticacionUsuarios = entityManager.createQuery(query).setMaxResults(length).setFirstResult(start).getResultList();
        
        objects.put("usuarios", AutenticacionUsuarios);
        objects.put("total", total);
        
        return objects;
	}

	@Override
	public List<Object> buscaUsuarioAutenticado(String username) {
		return autenticacionUsuarioDao.buscaUsuarioAutenticado(username);
	}

	public List<Object> listaUsuariosDelta(Integer typeCategory, Integer type){
		return autenticacionUsuarioDao.listaUsuariosDelta(typeCategory, type);
	}

	public List<Relacion> listaRelaciones(){
		return autenticacionUsuarioDao.listaRelaciones();
	}

	public List<Object> listaUsuarios(){
		return autenticacionUsuarioDao.listaUsuarios();
	}

	@Override
	public List<Object> listaFullUsuarios() {
		return autenticacionUsuarioDao.listaFullUsuarios();
	}

	@Override
	public AutenticacionUsuario buscaById(Long id) {
		return autenticacionUsuarioDao.buscaById(id);
	}

	@Override
	public List<Object> listaRecursos() {
		return autenticacionUsuarioDao.listaRecursos();
	}


}
