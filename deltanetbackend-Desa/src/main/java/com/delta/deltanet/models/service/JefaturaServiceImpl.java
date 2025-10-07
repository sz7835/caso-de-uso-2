package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IJefaturaDao;
import com.delta.deltanet.models.entity.Jefatura;

@Service
public class JefaturaServiceImpl implements IJefaturaService {

    @Autowired
    private IJefaturaDao jefaturaDao;

    @Override
    public boolean existsCombinacionActiva(String nombre, String nombrePuesto, Long idGerencia, Long idPersona, Long exceptId) {
        if (nombre == null || nombrePuesto == null || idGerencia == null || idPersona == null) return false;
        String nombreTrim = nombre.trim();
        String nombrePuestoTrim = nombrePuesto.trim();
        long count = jefaturaDao.countByUniqueFieldsActivo(nombreTrim, nombrePuestoTrim, idGerencia, idPersona, exceptId);
        return count > 0;
    }

    @Override
    public List<Jefatura> findAll() {
        return jefaturaDao.findAll();
    }

    @Override
    public Jefatura findById(Long id) {
        return jefaturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Jefatura save(Jefatura jefatura) {
        return jefaturaDao.save(jefatura);
    }

    @Override
    @Transactional
    public Jefatura update(Jefatura jefatura) {
        if (!jefaturaDao.existsById(jefatura.getId())) {
            throw new RuntimeException("No se encontró la jefatura con ID: " + jefatura.getId());
        }
        return jefaturaDao.save(jefatura);
    }

    @Override
    @Transactional
    public Jefatura changeEstado(Long id, Integer nuevoEstado, String username) {
        Jefatura jefatura = jefaturaDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró la jefatura con ID: " + id));

        jefatura.setUpdateUser(username);
        jefatura.setUpdateDate(new Date());
        jefatura.setEstado(nuevoEstado);

        return jefaturaDao.save(jefatura);
    }

}