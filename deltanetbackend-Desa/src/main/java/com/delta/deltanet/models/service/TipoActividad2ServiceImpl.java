package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.TipoActividadRepository;
import com.delta.deltanet.models.entity.TipoActividad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TipoActividad2ServiceImpl implements ITipoActividad2Service {

    @Autowired
    private TipoActividadRepository repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long exceptId) {
        if (nombre == null) return false;
        String nombreTrim = nombre.trim();
        long count = repository.countByNombreActivo(nombreTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<TipoActividad> findAll() {
        return repository.findAll();
    }

    @Override
    public TipoActividad findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public TipoActividad save(TipoActividad TipoActividad) {
        return repository.save(TipoActividad);
    }

    @Override
    @Transactional
    public TipoActividad update(TipoActividad TipoActividad) {
        if (!repository.existsById(TipoActividad.getId())) {
            throw new RuntimeException("No se encontró el TipoActividad con ID: " + TipoActividad.getId());
        }
        return repository.save(TipoActividad);
    }

    @Override
    @Transactional
    public TipoActividad changeEstado(Long id, Integer nuevoEstado, String username) {
        TipoActividad TipoActividad = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el TipoActividad con ID: " + id));

        TipoActividad.setUpdateUser(username);
        TipoActividad.setUpdateDate(new Date());
        TipoActividad.setEstado(nuevoEstado);

        return repository.save(TipoActividad);
    }
}
