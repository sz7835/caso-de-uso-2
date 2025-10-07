package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.ITrabajadorParentescoDao;
import com.delta.deltanet.models.entity.TrabajadorParentesco;

@Service
public class TrabajadorParentescoServiceImpl implements ITrabajadorParentescoService {

    @Autowired
    private ITrabajadorParentescoDao trabajadorParentestoDao;

    @Override
    public List<TrabajadorParentesco> findAll() {
    	return trabajadorParentestoDao.findAll();
    }
    
    @Override
    public TrabajadorParentesco findById(Long id) {
    	return trabajadorParentestoDao.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public TrabajadorParentesco save(TrabajadorParentesco trabajadorParentesco) {
        return trabajadorParentestoDao.save(trabajadorParentesco);
    }

    @Override
    @Transactional
    public TrabajadorParentesco update(TrabajadorParentesco trabajadorParentesco) {
        if (!trabajadorParentestoDao.existsById(trabajadorParentesco.getId())) {
            throw new RuntimeException("No se encontró el parentesco de trabajador con ID: " + trabajadorParentesco.getId());
        }
        return trabajadorParentestoDao.save(trabajadorParentesco);
    }

    @Override
    @Transactional
    public TrabajadorParentesco changeEstado(Long id, Integer nuevoEstado, String username) {
    	TrabajadorParentesco trabajadorParentesco = trabajadorParentestoDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el parentesco de trabajador con ID: " + id));
    	
    	trabajadorParentesco.setUpdateUser(username);
    	trabajadorParentesco.setUpdateDate(new Date());
    	trabajadorParentesco.setEstado(nuevoEstado);
    	
        return trabajadorParentestoDao.save(trabajadorParentesco);
    }

}