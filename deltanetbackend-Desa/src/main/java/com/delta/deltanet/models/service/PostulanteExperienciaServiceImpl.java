package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PostulanteExperienciaDao;
import com.delta.deltanet.models.entity.PostulanteExperiencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostulanteExperienciaServiceImpl implements IPostulanteExperienciaService {
    @Autowired
    private PostulanteExperienciaDao repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long exceptId) {
        if (nombre == null) return false;
        String nombreTrim = nombre.trim();
        long count = repository.countByNombreActivo(nombreTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<PostulanteExperiencia> findAll() {
        return repository.findAll();
    }

    @Override
    public PostulanteExperiencia findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PostulanteExperiencia save(PostulanteExperiencia postulanteExperiencia) {
        return repository.save(postulanteExperiencia);
    }

    @Override
    @Transactional
    public PostulanteExperiencia update(PostulanteExperiencia postulanteExperiencia) {
        if (!repository.existsById(postulanteExperiencia.getId())) {
            throw new RuntimeException("No se encontró la experiencia de postulante con ID: " + postulanteExperiencia.getId());
        }
        return repository.save(postulanteExperiencia);
    }

    @Override
    @Transactional
    public PostulanteExperiencia changeEstado(Long id, Integer nuevoEstado, String username) {
        PostulanteExperiencia postulanteExperiencia = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró la experiencia de postulante con ID: " + id));

        postulanteExperiencia.setUpdateUser(username);
        postulanteExperiencia.setUpdateDate(new Date());
        postulanteExperiencia.setEstado(nuevoEstado);

        return repository.save(postulanteExperiencia);
    }
}
