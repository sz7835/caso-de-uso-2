package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IEjecutivosDao;
import com.delta.deltanet.models.entity.Ejecutivos;

@Service
public class EjecutivosServiceImpl implements IEjecutivosService {
    @Override
    public boolean existsNombrePuestoActivoPorPersona(String nombrePuesto, Long idPersona, Long exceptId) {
        if (nombrePuesto == null || idPersona == null) return false;
        String nombrePuestoTrim = nombrePuesto.trim();
        long count = ejecutivosDao.countByNombrePuestoAndPersonaActivo(nombrePuestoTrim, idPersona, exceptId);
        return count > 0;
    }

    @Autowired
    private IEjecutivosDao ejecutivosDao;

    @Override
    public List<Ejecutivos> findAll() {
    	return ejecutivosDao.findAll();
    }
    
    @Override
    public Ejecutivos findById(Long id) {
    	return ejecutivosDao.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public Ejecutivos save(Ejecutivos ejecutivo) {
        return ejecutivosDao.save(ejecutivo);
    }

    @Override
    @Transactional
    public Ejecutivos update(Ejecutivos ejecutivo) {
        if (!ejecutivosDao.existsById(ejecutivo.getId())) {
            throw new RuntimeException("No se encontró el ejecutivo con ID: " + ejecutivo.getId());
        }
        return ejecutivosDao.save(ejecutivo);
    }

    @Override
    @Transactional
    public Ejecutivos changeEstado(Long id, Integer nuevoEstado, String username) {
    	Ejecutivos ejecutivo = ejecutivosDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el ejecutivo con ID: " + id));
    	
        ejecutivo.setUpdateUser(username);
        ejecutivo.setUpdateDate(new Date());
    	ejecutivo.setEstado(nuevoEstado);
    	
        return ejecutivosDao.save(ejecutivo);
    }

}