package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAdmCalendarioMesDao;
import com.delta.deltanet.models.entity.AdmCalendarioMes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdmCalendarioMesServiceImpl implements IAdmCalendarioMesService {

    @Autowired
    private IAdmCalendarioMesDao calendarioMesDao;

    @Override
    @Transactional
    public AdmCalendarioMes save(AdmCalendarioMes calendarioMes) {
        return calendarioMesDao.save(calendarioMes);
    }

    @Override
    @Transactional
    public AdmCalendarioMes update(AdmCalendarioMes calendarioMes) {
        if (!calendarioMesDao.existsById(calendarioMes.getIdCalendarioMes().longValue())) {
            throw new RuntimeException("No se encontró el mes con ID: " + calendarioMes.getIdCalendarioMes());
        }
        return calendarioMesDao.save(calendarioMes);
    }

    @Override
    @Transactional
    public AdmCalendarioMes cambiarEstado(Long id, Integer estado) {
        AdmCalendarioMes calendarioMes = calendarioMesDao.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el mes con ID: " + id));
        calendarioMes.setEstado(estado);
        return calendarioMesDao.save(calendarioMes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioMes> buscarPorFiltros(String nombre, Integer estado) {
        if (nombre != null && estado != null) {
            return calendarioMesDao.findByNombreAndEstado(nombre, estado);
        } else if (nombre != null) {
            return calendarioMesDao.findByNombre(nombre);
        } else if (estado != null) {
            return calendarioMesDao.findByEstado(estado);
        }
        return calendarioMesDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdmCalendarioMes> findAll() {
        return calendarioMesDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdmCalendarioMes> findById(Long id) {
        return calendarioMesDao.findById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return calendarioMesDao.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsByAcronimo(String acronimo) {
        return calendarioMesDao.existsByAcronimoIgnoreCase(acronimo);
    }

}