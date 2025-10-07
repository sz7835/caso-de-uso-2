package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.PostulanteOfimaticaIdiomasDao;
import com.delta.deltanet.models.entity.PostulanteOfimaticaIdiomas;

@Service
public class PostulanteOfimaticaIdiomasServiceImpl implements PostulanteOfimaticaIdiomasService, IPostulanteOfimaticaIdiomasService {
    @Autowired
    private PostulanteOfimaticaIdiomasDao postulanteOfimaticaIdiomasDao;
    @Autowired
    private PostulanteOfimaticaIdiomasDao repository;

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long idExcluir) {
        if (descripcion == null) return false;
        Long count = postulanteOfimaticaIdiomasDao.countByDescripcionActivo(descripcion);
        if (idExcluir != null) {
            PostulanteOfimaticaIdiomas existente = postulanteOfimaticaIdiomasDao.findById(idExcluir).orElse(null);
            if (existente != null && existente.getDescripcion() != null && existente.getDescripcion().trim().equalsIgnoreCase(descripcion.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Override
    public List<PostulanteOfimaticaIdiomas> buscarPorNombreYEstado(String descripcion, Integer estado) {
        return postulanteOfimaticaIdiomasDao.findByDescripcionAndEstado(descripcion, estado);
    }

    @Override
    public PostulanteOfimaticaIdiomas PostulanteOfimaticaIdiomas(Long idPer) {
        return postulanteOfimaticaIdiomasDao.findById(idPer).orElse(null);
    }
    
    
    @Override
    public List<PostulanteOfimaticaIdiomas> findAll() {
    	return repository.findAll();
    }
    
    @Override
    public PostulanteOfimaticaIdiomas findById(Long id) {
    	return repository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public PostulanteOfimaticaIdiomas save(PostulanteOfimaticaIdiomas postulanteOfimaticaIdiomas) {
        return repository.save(postulanteOfimaticaIdiomas);
    }

    @Override
    @Transactional
    public PostulanteOfimaticaIdiomas update(PostulanteOfimaticaIdiomas postulanteOfimaticaIdiomas) {
        if (!repository.existsById(postulanteOfimaticaIdiomas.getId())) {
            throw new RuntimeException("No se encontró ofimática idioma de postulante con ID: " + postulanteOfimaticaIdiomas.getId());
        }
        return repository.save(postulanteOfimaticaIdiomas);
    }

    @Override
    @Transactional
    public PostulanteOfimaticaIdiomas changeEstado(Long id, Integer nuevoEstado, String username) {
    	PostulanteOfimaticaIdiomas postulanteOfimaticaIdiomas = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró ofimática idioma de postulante con ID: " + id));
    	
    	postulanteOfimaticaIdiomas.setUpdateUser(username);
    	postulanteOfimaticaIdiomas.setUpdateDate(new Date());
    	postulanteOfimaticaIdiomas.setEstado(nuevoEstado);
    	
        return repository.save(postulanteOfimaticaIdiomas);
    }

}
