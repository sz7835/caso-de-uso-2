package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IAdmCalendarioSemanaDiaDao;
import com.delta.deltanet.models.entity.AdmCalendarioSemanaDia;

@Service
public class AdmCalendarioSemanaDiaServiceImpl implements IAdmCalendarioSemanaDiaService {

    @Autowired
    private IAdmCalendarioSemanaDiaDao calendarioSemanaDiaDao;

    @Override
    @Transactional
    public AdmCalendarioSemanaDia save(AdmCalendarioSemanaDia calendarioSemanaDia) {
        return calendarioSemanaDiaDao.save(calendarioSemanaDia);
    }

    @Override
    @Transactional
    public AdmCalendarioSemanaDia update(AdmCalendarioSemanaDia calendarioSemanaDia) {
        if (!calendarioSemanaDiaDao.existsById(calendarioSemanaDia.getIdCalendarioSemanaDia())) {
            throw new RuntimeException("No se encontró el día de la semana con ID: " + calendarioSemanaDia.getIdCalendarioSemanaDia());
        }
        return calendarioSemanaDiaDao.save(calendarioSemanaDia);
    }

    @Override
    @Transactional
    public AdmCalendarioSemanaDia cambiarEstado(Long id, Integer estado) {
        AdmCalendarioSemanaDia calendario = calendarioSemanaDiaDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el día de la semana con ID: " + id));
        calendario.setEstado(estado);
        return calendarioSemanaDiaDao.save(calendario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioSemanaDia> buscarPorFiltros(String nombre, Integer estado) {
        if (nombre != null && estado != null) {
            return calendarioSemanaDiaDao.findByNombreAndEstado(nombre, estado);
        } else if (nombre != null) {
            return calendarioSemanaDiaDao.findByNombre(nombre);
        } else if (estado != null) {
            return calendarioSemanaDiaDao.findByEstado(estado);
        }
        return calendarioSemanaDiaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioSemanaDia> findAll() {
        return calendarioSemanaDiaDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdmCalendarioSemanaDia> findById(Long id) {
        return calendarioSemanaDiaDao.findById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return calendarioSemanaDiaDao.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsByAcronimo(String acronimo) {
        return calendarioSemanaDiaDao.existsByAcronimoIgnoreCase(acronimo);
    }
}