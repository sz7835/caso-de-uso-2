package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IAdmCalendarioAnioDao;
import com.delta.deltanet.models.dao.IAdmCalendarioAnioEstadoDao;
import com.delta.deltanet.models.entity.AdmCalendarioAnio;
import com.delta.deltanet.models.entity.AdmCalendarioAnioEstado;

@Service
public class AdmCalendarioAnioServiceImpl implements IAdmCalendarioAnioService {

    @Autowired
    private IAdmCalendarioAnioDao calendarioAnioDao;

    @Autowired
    private IAdmCalendarioAnioEstadoDao calendarioAnioEstadoDao;

    @Override
    public AdmCalendarioAnio save(AdmCalendarioAnio calendarioAnio) {
        return calendarioAnioDao.save(calendarioAnio);
    }

    @Override
    public AdmCalendarioAnio update(AdmCalendarioAnio calendarioAnio) {
        Optional<AdmCalendarioAnio> existingCalendario = calendarioAnioDao.findById(calendarioAnio.getIdCalendarioAnio());
        if (!existingCalendario.isPresent()) {
            throw new RuntimeException("No se encontró el calendario año");
        }
        return calendarioAnioDao.saveAndFlush(calendarioAnio);
    }

    @Override
    public AdmCalendarioAnio cambiarEstado(Integer id, Integer estado) {
        Optional<AdmCalendarioAnio> calendario = calendarioAnioDao.findById(id);
        if (!calendario.isPresent()) {
            throw new RuntimeException("No se encontró el calendario año");
        }
        AdmCalendarioAnio calendarioAnio = calendario.get();
        calendarioAnio.setEstado(estado);
        return calendarioAnioDao.save(calendarioAnio);
    }

    @Override
    public List<AdmCalendarioAnio> buscarPorFiltros(String nombre, Integer estado, Long idAnioEstado) {
        if (nombre != null && !nombre.isEmpty() && estado != null && idAnioEstado != null) {
            return calendarioAnioDao.findByNombreContainingIgnoreCaseAndAnioEstado_IdAnioEstadoAndEstado(nombre, idAnioEstado, estado);
        } else if (nombre != null && !nombre.isEmpty() && idAnioEstado != null) {
            return calendarioAnioDao.findByNombreContainingIgnoreCaseAndAnioEstado_IdAnioEstado(nombre, idAnioEstado);
        } else if (estado != null && idAnioEstado != null) {
            return calendarioAnioDao.findByAnioEstado_IdAnioEstadoAndEstado(idAnioEstado, estado);
        } else if (idAnioEstado != null) {
            return calendarioAnioDao.findByAnioEstado_IdAnioEstado(idAnioEstado);
        } else if (nombre != null && !nombre.isEmpty() && estado != null) {
            return calendarioAnioDao.findByNombreContainingIgnoreCaseAndEstado(nombre, estado);
        } else if (nombre != null && !nombre.isEmpty()) {
            return calendarioAnioDao.findByNombreContainingIgnoreCase(nombre);
        } else if (estado != null) {
            return calendarioAnioDao.findByEstado(estado);
        }
        return calendarioAnioDao.findAll();
    }

    @Override
    public Optional<AdmCalendarioAnio> findById(Integer id) {
        return calendarioAnioDao.findById(id);
    }

    @Override
    public List<AdmCalendarioAnioEstado> index() {
        return calendarioAnioEstadoDao.findAll();
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return calendarioAnioDao.existsByNombreIgnoreCase(nombre);
    }
}