package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IEnfermedadDao;
import com.delta.deltanet.models.entity.Enfermedad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnfermedadServiceImpl {
    @Autowired
    IEnfermedadDao enfermedadDao;

    public List<Enfermedad> getAll(){
        return enfermedadDao.findAll();
    }

    public List<Enfermedad> listaActivas(){
        return enfermedadDao.listaActivas();
    }

    public List<Enfermedad> listaByTipo(Integer tipo){
        return enfermedadDao.listaByTipo(tipo);
    }

    public Optional<Enfermedad> getById(Integer id){
        return enfermedadDao.findById(id);
    }

    public Enfermedad save(Enfermedad enfermedad){
        return enfermedadDao.save(enfermedad);
    }
}
