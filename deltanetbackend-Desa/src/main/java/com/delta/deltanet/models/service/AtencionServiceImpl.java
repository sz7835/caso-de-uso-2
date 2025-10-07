package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAtencionDao;
import com.delta.deltanet.models.entity.Atencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtencionServiceImpl implements  IAtencionService{
    @Autowired
    private IAtencionDao atencionDao;

    @Override
    public List<Atencion> findAll() {
        return atencionDao.findAll();
    }

    @Override
    public Optional<Atencion> findById(Integer id) {
        return atencionDao.findById(id);
    }

    @Override
    public List<Atencion> findByEstado(int estado) {
        return atencionDao.findByEstado(estado);
    }

    @Override
    public List<Atencion> findByIdTipoPer(int idTipoPer) {
        return atencionDao.findByIdTipoPer(idTipoPer);
    }

    @Override
    public Atencion save(Atencion atencion) {
        return atencionDao.save(atencion);
    }

    @Override
    public List<Atencion> findByDescripAndIdTipoPerAndEstado(String descrip, int idTipoPer, int estado) {
        return atencionDao.findByDescripAndIdTipoPerAndEstado(descrip, idTipoPer, estado);
    }
}
