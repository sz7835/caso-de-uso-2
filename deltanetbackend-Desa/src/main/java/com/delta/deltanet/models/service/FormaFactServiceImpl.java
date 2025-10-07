package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.FormaFactDao;
import com.delta.deltanet.models.entity.FormaFact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FormaFactServiceImpl implements IFormaFactService {
    @Override
    public boolean existsNombreActivo(String nombre, Long exceptId) {
        if (nombre == null) return false;
        String nombreTrim = nombre.trim();
        long count = repository.countByNombreActivo(nombreTrim, exceptId);
        return count > 0;
    }

    @Autowired
    private FormaFactDao repository;
    
    @Override
    public List<FormaFact> findAll() {
    	return repository.findAll();
    }
    
    @Override
    public FormaFact findById(Long id) {
    	return repository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public FormaFact save(FormaFact formaFact) {
        return repository.save(formaFact);
    }

    @Override
    @Transactional
    public FormaFact update(FormaFact formaFact) {
        if (!repository.existsById(formaFact.getId())) {
            throw new RuntimeException("No se encontró el forma fact con ID: " + formaFact.getId());
        }
        return repository.save(formaFact);
    }

    @Override
    @Transactional
    public FormaFact changeEstado(Long id, Integer nuevoEstado, String username) {
    	FormaFact formaFact = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el forma fact con ID: " + id));
    	
    	formaFact.setUpdateUser(username);
    	formaFact.setUpdateDate(new Date());
    	formaFact.setEstado(nuevoEstado);
    	
        return repository.save(formaFact);
    }
}
