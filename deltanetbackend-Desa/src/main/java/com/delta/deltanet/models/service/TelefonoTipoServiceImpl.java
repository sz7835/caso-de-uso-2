package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITelefonoTipoDao;
import com.delta.deltanet.models.entity.TelefonoTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TelefonoTipoServiceImpl implements TelefonoTipoService {

    @Override
    public List<TelefonoTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado) {
        return telefonoTipoDao.findByDescripcionAndIdTipoPersonaAndEstado(descripcion, idTipoPersona, estado);
    }
    @Autowired
    private ITelefonoTipoDao telefonoTipoDao;

    @Override
    public List<TelefonoTipo> findByEstado(Integer estado) {
        return telefonoTipoDao.findByEstado(estado);
    }

    @Override
    public List<TelefonoTipo> findByIdTipoPersona(Long idTipoPersona) {
        return telefonoTipoDao.findByIdTipoPersona(idTipoPersona);
    }

    @Override
    public List<TelefonoTipo> findAll() {
        return telefonoTipoDao.findAll();
    }

    @Override
    public TelefonoTipo findById(Long id) {
        return telefonoTipoDao.findById(id).orElse(null);
    }

    @Override
    public TelefonoTipo save(TelefonoTipo telefonoTipo) {
        return telefonoTipoDao.save(telefonoTipo);
    }
}
