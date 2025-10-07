package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITarifarioEstadoDao;
import com.delta.deltanet.models.entity.TarifarioEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifarioEstadoServiceImpl implements TarifarioEstadoService {
    @Autowired
    private ITarifarioEstadoDao estadoDao;

    @Override
    public List<TarifarioEstado> findAll() {
        return estadoDao.findAll();
    }

    @Override
    public Optional<TarifarioEstado> findById(Long id) {
        return estadoDao.findById(id);
    }

    @Override
    public TarifarioEstado save(TarifarioEstado estado) {
        return estadoDao.save(estado);
    }

    @Override
    public boolean existsDescripcionActiva(String descripcion, Long exceptId) {
        for (TarifarioEstado t : estadoDao.findAll()) {
            if (t.getDescripcion().trim().equalsIgnoreCase(descripcion.trim()) && t.getEstado() == 1) {
                if (exceptId == null || !t.getId().equals(exceptId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
