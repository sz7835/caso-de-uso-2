package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PostulanteNivelesDao;
import com.delta.deltanet.models.entity.PostulanteNiveles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostulanteNivelesServiceImpl implements IPostulanteNivelesService {

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long idExcluir) {
        if (descripcion == null) return false;
        Long count = repository.countByDescripcionActivo(descripcion);
        if (idExcluir != null) {
            PostulanteNiveles existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getDescripcion() != null && existente.getDescripcion().trim().equalsIgnoreCase(descripcion.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Autowired
    private PostulanteNivelesDao repository;
    
    @Override
    public List<PostulanteNiveles> findAll() {
    	return repository.findAll();
    }
    
    @Override
    public PostulanteNiveles findById(Long id) {
    	return repository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public PostulanteNiveles save(PostulanteNiveles postulanteNiveles) {
        return repository.save(postulanteNiveles);
    }

    @Override
    @Transactional
    public PostulanteNiveles update(PostulanteNiveles postulanteNiveles) {
        if (!repository.existsById(postulanteNiveles.getId())) {
            throw new RuntimeException("No se encontró el nivel de postulante con ID: " + postulanteNiveles.getId());
        }
        return repository.save(postulanteNiveles);
    }

    @Override
    @Transactional
    public PostulanteNiveles changeEstado(Long id, Integer nuevoEstado, String username) {
    	PostulanteNiveles postulanteNiveles = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el nivel de postulante con ID: " + id));
    	
    	postulanteNiveles.setUpdateUser(username);
    	postulanteNiveles.setUpdateDate(new Date());
    	postulanteNiveles.setEstado(nuevoEstado);
    	
        return repository.save(postulanteNiveles);
    }
}
