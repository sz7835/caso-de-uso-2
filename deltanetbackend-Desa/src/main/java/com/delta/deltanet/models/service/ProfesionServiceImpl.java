package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ProfesionDao;
import com.delta.deltanet.models.entity.Profesion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProfesionServiceImpl implements IProfesionService {

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long idExcluir) {
        if (descripcion == null) return false;
        Long count = repository.countByDescripcionActivo(descripcion);
        if (idExcluir != null) {
            Profesion existente = repository.findById(idExcluir).orElse(null);
            if (existente != null && existente.getDescripcion() != null && existente.getDescripcion().trim().equalsIgnoreCase(descripcion.trim()) && existente.getEstado() == 1) {
                // El único duplicado es el mismo registro que estamos editando
                return count > 1;
            }
        }
        return count > 0;
    }

    @Autowired
    private ProfesionDao repository;
    
    @Override
    public List<Profesion> findAll() {
    	return repository.findAll();
    }
    
    @Override
    public Profesion findById(Long id) {
    	return repository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public Profesion save(Profesion profesion) {
        return repository.save(profesion);
    }

    @Override
    @Transactional
    public Profesion update(Profesion profesion) {
        if (!repository.existsById(profesion.getId())) {
            throw new RuntimeException("No se encontró profesión con ID: " + profesion.getId());
        }
        return repository.save(profesion);
    }

    @Override
    @Transactional
    public Profesion changeEstado(Long id, Integer nuevoEstado, String username) {
    	Profesion profesion = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró profesión con ID: " + id));
    	
    	profesion.setUpdateUser(username);
    	profesion.setUpdateDate(new Date());
    	profesion.setEstado(nuevoEstado);
    	
        return repository.save(profesion);
    }
}
