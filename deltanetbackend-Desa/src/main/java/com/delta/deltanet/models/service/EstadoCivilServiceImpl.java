package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.EstadoCivilDao;
import com.delta.deltanet.models.entity.EstadoCivil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EstadoCivilServiceImpl implements IEstadoCivilService {
    @Autowired
    private EstadoCivilDao repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long idExcluir) {
        if (nombre == null) return false;
        Long count = repository.countByNombreActivo(nombre);
        if (idExcluir != null) {
            EstadoCivil existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getNombre() != null && existente.getNombre().trim().equalsIgnoreCase(nombre.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Override
    public List<EstadoCivil> findAll() {
        return repository.findAll();
    }

    @Override
    public EstadoCivil findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public EstadoCivil save(EstadoCivil estadoCivil) {
        return repository.save(estadoCivil);
    }

    @Override
    @Transactional
    public EstadoCivil update(EstadoCivil estadoCivil) {
        if (!repository.existsById(estadoCivil.getId())) {
            throw new RuntimeException("No se encontró el Estado Civil con ID: " + estadoCivil.getId());
        }
        return repository.save(estadoCivil);
    }

    @Override
    @Transactional
    public EstadoCivil changeEstado(Long id, Integer nuevoEstado, String username) {
        EstadoCivil estadoCivil = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el Estado Civil con ID: " + id));

        estadoCivil.setUpdateUser(username);
        estadoCivil.setUpdateDate(new Date());
        estadoCivil.setEstado(nuevoEstado);

        return repository.save(estadoCivil);
    }
    }
