package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ICorreoTipoDao;
import com.delta.deltanet.models.entity.CorreoTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CorreoTipoServiceImpl implements CorreoTipoService {
    @Autowired
    private ICorreoTipoDao correoTipoDao;

    @Override
    public List<CorreoTipo> findByEstado(Integer estado) {
        return correoTipoDao.findByEstado(estado);
    }

    @Override
    public List<CorreoTipo> findByIdTipoPersona(Long idTipoPersona) {
        return correoTipoDao.findByIdTipoPersona(idTipoPersona);
    }

    @Override
    public List<CorreoTipo> findAll() {
        return correoTipoDao.findAll();
    }

    @Override
    public CorreoTipo findById(Long id) {
        return correoTipoDao.findById(id).orElse(null);
    }

    @Override
    public CorreoTipo save(CorreoTipo correoTipo) {
        return correoTipoDao.save(correoTipo);
    }

    @Override
    public List<CorreoTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado) {
        return correoTipoDao.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, estado);
    }
}
