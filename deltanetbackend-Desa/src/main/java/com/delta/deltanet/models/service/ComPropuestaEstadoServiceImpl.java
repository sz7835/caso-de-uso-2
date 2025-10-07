package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ComPropuestaEstadoDao;
import com.delta.deltanet.models.entity.ComPropuestaEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComPropuestaEstadoServiceImpl implements ComPropuestaEstadoService {
    @Autowired
    private ComPropuestaEstadoDao estadoDao;

    @Override
    public List<ComPropuestaEstado> findAll() {
        return estadoDao.findAll();
    }

    @Override
    public Optional<ComPropuestaEstado> findById(Integer id) {
        return estadoDao.findById(id);
    }

    @Override
    public ComPropuestaEstado save(ComPropuestaEstado estado) {
        return estadoDao.save(estado);
    }

    @Override
    public boolean existsDescripcionActiva(String descripcion, Integer exceptId) {
        for (ComPropuestaEstado t : estadoDao.findAll()) {
            if (t.getDescripcion().trim().equalsIgnoreCase(descripcion.trim()) && t.getEstado() == 1) {
                if (exceptId == null || !t.getId().equals(exceptId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
