package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ComProdServTipoDao;
import com.delta.deltanet.models.entity.ComProdServTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComProdServTipoServiceImpl implements ComProdServTipoService {
    @Autowired
    private ComProdServTipoDao tipoDao;

    @Override
    public List<ComProdServTipo> findAll() {
        return tipoDao.findAll();
    }

    @Override
    public Optional<ComProdServTipo> findById(Integer id) {
        return tipoDao.findById(id);
    }

    @Override
    public ComProdServTipo save(ComProdServTipo tipo) {
        return tipoDao.save(tipo);
    }

    @Override
    public boolean existsDescripcionActiva(String descripcion, Integer exceptId) {
        for (ComProdServTipo t : tipoDao.findAll()) {
            if (t.getDescripcion().equalsIgnoreCase(descripcion) && t.getEstado() == 1) {
                if (exceptId == null || t.getId() != exceptId) {
                    return true;
                }
            }
        }
        return false;
    }
}
