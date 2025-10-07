package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.LiquidacionTipoOperacionDao;
import com.delta.deltanet.models.entity.LiquidacionTipoOperacion;

@Service
public class LiquidacionTipoOperacionServiceImpl implements LiquidacionTipoOperacionService {
    @Autowired
    private LiquidacionTipoOperacionDao repository;

    @Override
    public boolean existsNombreActivo(String nombre, Long idExcluir) {
        if (nombre == null) return false;
        Long count = repository.countByNombreActivo(nombre);
        if (idExcluir != null) {
            LiquidacionTipoOperacion existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getNombre() != null && existente.getNombre().trim().equalsIgnoreCase(nombre.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Override
    public List<LiquidacionTipoOperacion> findAll() {
        return repository.findAll();
    }

    @Override
    public LiquidacionTipoOperacion getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public LiquidacionTipoOperacion findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public LiquidacionTipoOperacion save(LiquidacionTipoOperacion liquidacionTipoOperacion) {
        return repository.save(liquidacionTipoOperacion);
    }

    @Override
    @Transactional
    public LiquidacionTipoOperacion update(LiquidacionTipoOperacion liquidacionTipoOperacion) {
        if (!repository.existsById(liquidacionTipoOperacion.getId())) {
            throw new RuntimeException("No se encontró el tipo de liquidación operación con ID: " + liquidacionTipoOperacion.getId());
        }
        return repository.save(liquidacionTipoOperacion);
    }

    @Override
    @Transactional
    public LiquidacionTipoOperacion changeEstado(Long id, Integer nuevoEstado, String username) {
        LiquidacionTipoOperacion liquidacionTipoOperacion = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el tipo de liquidación operación con ID: " + id));

        liquidacionTipoOperacion.setUpdateUser(username);
        liquidacionTipoOperacion.setUpdateDate(new Date());
        liquidacionTipoOperacion.setEstado(nuevoEstado);

        return repository.save(liquidacionTipoOperacion);
    }

}