package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDireccionTipoDao;
import com.delta.deltanet.models.entity.DireccionTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DireccionTipoServiceImpl implements DireccionTipoService {
    @Autowired
    private IDireccionTipoDao direccionTipoDao;

    @Override
    public List<DireccionTipo> findByEstado(Integer estado) {
        return direccionTipoDao.findByEstado(estado);
    }

    @Override
    public List<DireccionTipo> findByIdTipoPersona(Long idTipoPersona) {
        return direccionTipoDao.findByIdTipoPersona(idTipoPersona);
    }

    @Override
    public List<DireccionTipo> findAll() {
        return direccionTipoDao.findAll();
    }

    @Override
    public DireccionTipo findById(Long id) {
        return direccionTipoDao.findById(id).orElse(null);
    }

    @Override
    public DireccionTipo save(DireccionTipo direccionTipo) {
        return direccionTipoDao.save(direccionTipo);
    }

    @Override
    public List<DireccionTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado) {
        return direccionTipoDao.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, estado);
    }
}
