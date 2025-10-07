package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoEnfermedadDao;
import com.delta.deltanet.models.entity.TipoEnfermedad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEnfermedadServiceImpl {
    @Autowired
    ITipoEnfermedadDao tipoEnfermedadDao;

    public List<TipoEnfermedad> getAll(){
        return tipoEnfermedadDao.findAll();
    }

    public Optional<TipoEnfermedad> getById(Integer id){
        return tipoEnfermedadDao.findById(id);
    }

    public TipoEnfermedad save(TipoEnfermedad tipoEnfermedad){
        return tipoEnfermedadDao.save(tipoEnfermedad);
    }
}
