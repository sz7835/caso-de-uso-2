package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IAdmCalendarioFeriadoGeneradoPorDao;
import com.delta.deltanet.models.entity.AdmCalendarioFeriadoGeneradoPor;

@Service
public class AdmCalendarioFeriadoGeneradoPorServiceImpl implements IAdmCalendarioFeriadoGeneradoPorService {

    @Autowired
    private IAdmCalendarioFeriadoGeneradoPorDao calendarioFeriadoGeneradoPorDao;

    @Override
    @Transactional
    public AdmCalendarioFeriadoGeneradoPor save(AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor) {
        return calendarioFeriadoGeneradoPorDao.save(calendarioFeriadoGeneradoPor);
    }

    @Override
    @Transactional
    public AdmCalendarioFeriadoGeneradoPor update(AdmCalendarioFeriadoGeneradoPor calendarioFeriadoGeneradoPor) {
        if (!calendarioFeriadoGeneradoPorDao.existsById(calendarioFeriadoGeneradoPor.getIdCalendarioFeriadoGeneradoPor())) {
            throw new RuntimeException("No se encontró el feriado generado por con ID: " + calendarioFeriadoGeneradoPor.getIdCalendarioFeriadoGeneradoPor());
        }
        return calendarioFeriadoGeneradoPorDao.save(calendarioFeriadoGeneradoPor);
    }

    @Override
    @Transactional
    public AdmCalendarioFeriadoGeneradoPor cambiarEstado(Long id, Integer estado) {
        AdmCalendarioFeriadoGeneradoPor calendario = calendarioFeriadoGeneradoPorDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el feriado generado por con ID: " + id));
        calendario.setEstado(estado);
        return calendarioFeriadoGeneradoPorDao.save(calendario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioFeriadoGeneradoPor> buscarPorFiltros(String descripcion, Integer estado) {
        if (descripcion != null && estado != null) {
            return calendarioFeriadoGeneradoPorDao.findByDescripcionAndEstado(descripcion, estado);
        } else if (descripcion != null) {
            return calendarioFeriadoGeneradoPorDao.findByDescripcion(descripcion);
        } else if (estado != null) {
            return calendarioFeriadoGeneradoPorDao.findByEstado(estado);
        }
        return calendarioFeriadoGeneradoPorDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioFeriadoGeneradoPor> findAll() {
        return calendarioFeriadoGeneradoPorDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdmCalendarioFeriadoGeneradoPor> findById(Long id) {
        return calendarioFeriadoGeneradoPorDao.findById(id);
    }

    @Override
    public boolean existsByAcronimo(String acronimo) {
        return calendarioFeriadoGeneradoPorDao.existsByAcronimoIgnoreCase(acronimo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDescripcion(String descripcion) {
        return calendarioFeriadoGeneradoPorDao.existsByDescripcionIgnoreCase(descripcion);
    }
}