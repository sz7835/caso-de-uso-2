package com.delta.deltanet.models.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IAdmCalendarioAnioEstadoDao;
import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;

@Service
public class AdmCalendarioAnioEstadoServiceImpl implements IAdmCalendarioAnioEstadoService {
    @Autowired
    private IAdmCalendarioAnioEstadoDao calendarioAnioEstadoDao;

    @Override
    public Map<String, Boolean> obtenerEstadosPrincipalesActivos() {
        Map<String, Boolean> principalEstados = new HashMap<>();
        principalEstados.put("Actual", calendarioAnioEstadoDao.existsActualActivo());
        principalEstados.put("Pasado", calendarioAnioEstadoDao.existsPasadoActivo());
        principalEstados.put("Futuro", calendarioAnioEstadoDao.existsFuturoActivo());
        return principalEstados;
    }

    @Override
    public boolean existeEstadoPrincipalActivo() {
        return calendarioAnioEstadoDao.countPrincipalActivo() > 0;
    }

    @Override
    @Transactional
    public AdmCalendarioAnioEstado save(AdmCalendarioAnioEstado calendarioAnioEstado) {
        return calendarioAnioEstadoDao.save(calendarioAnioEstado);
    }

    @Override
    @Transactional
    public AdmCalendarioAnioEstado update(AdmCalendarioAnioEstado calendarioAnioEstado) {
        if (!calendarioAnioEstadoDao.existsById(calendarioAnioEstado.getIdAnioEstado().longValue())) {
            throw new RuntimeException("No se encontró el calendario año estado con ID: " + calendarioAnioEstado.getIdAnioEstado());
        }
        return calendarioAnioEstadoDao.save(calendarioAnioEstado);
    }

    @Override
    @Transactional
    public AdmCalendarioAnioEstado cambiarEstado(Long id, Integer nuevoEstado) {
        AdmCalendarioAnioEstado calendario = calendarioAnioEstadoDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el calendario año estado con ID: " + id));
        calendario.setEstado(nuevoEstado);
        return calendarioAnioEstadoDao.save(calendario);
    }

    @Override
    public List<AdmCalendarioAnioEstado> buscarPorFiltros(String nombre, Integer estado) {
        if (nombre != null && estado != null) {
            return calendarioAnioEstadoDao.findByNombreAndEstado(nombre, estado);
        } else if (nombre != null) {
            return calendarioAnioEstadoDao.findByNombre(nombre);
        } else if (estado != null) {
            return calendarioAnioEstadoDao.findByEstado(estado);
        }
        return calendarioAnioEstadoDao.findAll();
    }

    @Override
    public Optional<AdmCalendarioAnioEstado> findById(Long id) {
        return calendarioAnioEstadoDao.findById(id);
    }

    @Override
    public List<AdmCalendarioAnioEstado> findAll() {
        return calendarioAnioEstadoDao.findAll();
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return calendarioAnioEstadoDao.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsByAcronimo(String acronimo) {
        return calendarioAnioEstadoDao.existsByAcronimoIgnoreCase(acronimo);
    }
}