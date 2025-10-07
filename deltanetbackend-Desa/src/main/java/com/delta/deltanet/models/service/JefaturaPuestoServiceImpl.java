package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IJefaturaPuestoDao;
import com.delta.deltanet.models.entity.JefaturaPuesto;

@Service
public class JefaturaPuestoServiceImpl implements IJefaturaPuestoService {

    @Autowired
    private IJefaturaPuestoDao jefaturaPuestoDao;

    @Override
    public boolean existsNombreActivoPorJefatura(String nombre, Long idJefatura, Long exceptId) {
    if (nombre == null || idJefatura == null) return false;
    String nombreTrim = nombre.trim();
    long count = jefaturaPuestoDao.countByNombreAndJefaturaAndEstadoActivo(nombreTrim, idJefatura, exceptId);
    return count > 0;
    }

    @Override
    public List<JefaturaPuesto> findAll() {
    	return jefaturaPuestoDao.findAll();
    }
    
    @Override
    public JefaturaPuesto findById(Long id) {
    	return jefaturaPuestoDao.findById(id).orElse(null);
    }
    
    @Override
    @Transactional
    public JefaturaPuesto save(JefaturaPuesto jefaturaPuesto) {
        return jefaturaPuestoDao.save(jefaturaPuesto);
    }

    @Override
    @Transactional
    public JefaturaPuesto update(JefaturaPuesto jefaturaPuesto) {
        if (!jefaturaPuestoDao.existsById(jefaturaPuesto.getId())) {
            throw new RuntimeException("No se encontró el puesto de jefatura con ID: " + jefaturaPuesto.getId());
        }
        return jefaturaPuestoDao.save(jefaturaPuesto);
    }

    @Override
    @Transactional
    public JefaturaPuesto changeEstado(Long id, Integer nuevoEstado, String username) {
    	JefaturaPuesto jefaturaPuesto = jefaturaPuestoDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el puesto de jefatura con ID: " + id));
    	
    	jefaturaPuesto.setUpdateUser(username);
    	jefaturaPuesto.setUpdateDate(new Date());
    	jefaturaPuesto.setEstado(nuevoEstado);
    	
        return jefaturaPuestoDao.save(jefaturaPuesto);
    }

}