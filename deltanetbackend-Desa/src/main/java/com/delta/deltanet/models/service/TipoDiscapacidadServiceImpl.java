package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoDiscapacidadDao;
import com.delta.deltanet.models.entity.TipoDiscapacidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDiscapacidadServiceImpl {
    @Autowired
    ITipoDiscapacidadDao tipoDiscapacidadDao;

    public List<TipoDiscapacidad> getAll(){
        return tipoDiscapacidadDao.findAll();
    }

    public Optional<TipoDiscapacidad> getById(Integer id){
        return tipoDiscapacidadDao.findById(id);
    }

    public TipoDiscapacidad save(TipoDiscapacidad tipoDiscapacidad){
        return tipoDiscapacidadDao.save(tipoDiscapacidad);
    }
}
