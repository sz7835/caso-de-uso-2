package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PostulanteGradoDao;
import com.delta.deltanet.models.entity.PostulanteGrado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostulanteGradoServiceImpl implements IPostulanteGradoService {
    @Autowired
    private PostulanteGradoDao repository;
    @Override
    public boolean existsNombreActivo(String nombre, Long exceptId) {
        if (nombre == null) return false;
        String nombreTrim = nombre.trim();
        long count = repository.countByNombreActivo(nombreTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<PostulanteGrado> findAll() {
        return repository.findAll();
    }

    @Override
    public PostulanteGrado findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PostulanteGrado save(PostulanteGrado postulanteGrado) {
        return repository.save(postulanteGrado);
    }

    @Override
    @Transactional
    public PostulanteGrado update(PostulanteGrado postulanteGrado) {
        if (!repository.existsById(postulanteGrado.getId())) {
            throw new RuntimeException("No se encontró el grado de postulante con ID: " + postulanteGrado.getId());
        }
        return repository.save(postulanteGrado);
    }

    @Override
    @Transactional
    public PostulanteGrado changeEstado(Long id, Integer nuevoEstado, String username) {
        PostulanteGrado postulanteGrado = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el grado de postulante con ID: " + id));

        postulanteGrado.setUpdateUser(username);
        postulanteGrado.setUpdateDate(new Date());
        postulanteGrado.setEstado(nuevoEstado);

        return repository.save(postulanteGrado);
    }
}
