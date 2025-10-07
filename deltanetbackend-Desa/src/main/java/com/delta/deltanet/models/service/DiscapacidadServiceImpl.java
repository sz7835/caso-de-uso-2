package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDiscapacidadDao;
import com.delta.deltanet.models.entity.Discapacidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscapacidadServiceImpl {
    @Autowired
    IDiscapacidadDao discapacidadDao;

    public List<Discapacidad> getAll(){
        return discapacidadDao.findAll();
    }

    List<Discapacidad> listaActivas(){
        return discapacidadDao.listaActivas();
    }

    public List<Discapacidad> listaByTipo(Integer tipo){
        return discapacidadDao.listaByTipo(tipo);
    }

    public Optional<Discapacidad> getById(Integer id){
        return discapacidadDao.findById(id);
    }

    public Discapacidad save(Discapacidad discapacidad){
        return discapacidadDao.save(discapacidad);
    }
}
