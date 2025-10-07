package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IAdmTipoDiaSemanaDao;
import com.delta.deltanet.models.entity.AdmTipoDiaSemana;

@Service
public class AdmTipoDiaSemanaServiceImpl implements IAdmTipoDiaSemanaService {

    @Autowired
    private IAdmTipoDiaSemanaDao tipoDiaSemanaDao;

    @Override
    @Transactional
    public AdmTipoDiaSemana save(AdmTipoDiaSemana tipoDiaSemana) {
        return tipoDiaSemanaDao.save(tipoDiaSemana);
    }

    @Override
    @Transactional
    public AdmTipoDiaSemana update(AdmTipoDiaSemana tipoDiaSemana) {
        if (!tipoDiaSemanaDao.existsById(tipoDiaSemana.getIdCalendarioSemana().longValue())) {
            throw new RuntimeException("No se encontró el tipo día semana con ID: " + tipoDiaSemana.getIdCalendarioSemana());
        }
        return tipoDiaSemanaDao.save(tipoDiaSemana);
    }

    @Override
    @Transactional
    public AdmTipoDiaSemana cambiarEstado(Long id, Integer nuevoEstado) {
        AdmTipoDiaSemana calendario = tipoDiaSemanaDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró el tipo día semana con ID: " + id));
        calendario.setEstado(nuevoEstado);
        return tipoDiaSemanaDao.save(calendario);
    }

    @Override
    public List<AdmTipoDiaSemana> buscarPorFiltros(String nombre, Integer estado) {
        if (nombre != null && estado != null) {
            return tipoDiaSemanaDao.findByNombreAndEstado(nombre, estado);
        } else if (nombre != null) {
            return tipoDiaSemanaDao.findByNombre(nombre);
        } else if (estado != null) {
            return tipoDiaSemanaDao.findByEstado(estado);
        }
        return tipoDiaSemanaDao.findAll();
    }

    @Override
    public Optional<AdmTipoDiaSemana> findById(Long id) {
        return tipoDiaSemanaDao.findById(id);
    }

    @Override
    public List<AdmTipoDiaSemana> findAll() {
        return tipoDiaSemanaDao.findAll();
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return tipoDiaSemanaDao.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsByAcronimo(String acronimo) {
        return tipoDiaSemanaDao.existsByAcronimoIgnoreCase(acronimo);
    }
}