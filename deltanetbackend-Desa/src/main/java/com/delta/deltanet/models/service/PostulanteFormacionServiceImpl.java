package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PostulanteFormacionDao;
import com.delta.deltanet.models.entity.PostulanteFormacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PostulanteFormacionServiceImpl implements IPostulanteFormacionService {
    @Autowired
    private PostulanteFormacionDao repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long exceptId) {
        if (nombre == null) return false;
        String nombreTrim = nombre.trim();
        long count = repository.countByNombreActivo(nombreTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<PostulanteFormacion> findAll() {
        return repository.findAll();
    }

    @Override
    public PostulanteFormacion findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PostulanteFormacion save(PostulanteFormacion postulanteFormacion) {
        return repository.save(postulanteFormacion);
    }

    @Override
    @Transactional
    public PostulanteFormacion update(PostulanteFormacion postulanteFormacion) {
        if (!repository.existsById(postulanteFormacion.getId())) {
            throw new RuntimeException("No se encontró la formacion de postulante con ID: " + postulanteFormacion.getId());
        }
        return repository.save(postulanteFormacion);
    }

    @Override
    @Transactional
    public PostulanteFormacion changeEstado(Long id, Integer nuevoEstado, String username) {
        PostulanteFormacion postulanteFormacion = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró la formacion de postulante con ID: " + id));

        postulanteFormacion.setUpdateUser(username);
        postulanteFormacion.setUpdateDate(new Date());
        postulanteFormacion.setEstado(nuevoEstado);

        return repository.save(postulanteFormacion);
    }
}
