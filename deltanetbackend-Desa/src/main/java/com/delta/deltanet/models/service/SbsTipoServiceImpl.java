package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ISbsTipoDao;
import com.delta.deltanet.models.entity.SbsTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SbsTipoServiceImpl {
    public int countByNombreAndEstado(String nombre, int estado) {
        return sbsTipoDao.countByNombreAndEstado(nombre, estado);
    }

    public int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id) {
        return sbsTipoDao.countByNombreAndEstadoAndIdNot(nombre, estado, id);
    }
    @Autowired
    ISbsTipoDao sbsTipoDao;

    public List<SbsTipo> getTipos(){
        return sbsTipoDao.findAll();
    }

    public Optional<SbsTipo> getById(Integer id){
        return sbsTipoDao.findById(id);
    }

    public SbsTipo save(SbsTipo sbsTipo){
        return sbsTipoDao.save(sbsTipo);
    }
}
