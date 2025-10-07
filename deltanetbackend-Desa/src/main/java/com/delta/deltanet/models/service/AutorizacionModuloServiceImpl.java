package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAutorizacionModuloDao;
import com.delta.deltanet.models.entity.AutorizacionModulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorizacionModuloServiceImpl {
    @Autowired
    IAutorizacionModuloDao autorizacionModuloDao;

    public Optional<AutorizacionModulo> findById(Integer id){
        return autorizacionModuloDao.findById(id);
    }

    public List<AutorizacionModulo> getAll(){
        return autorizacionModuloDao.findAll();
    }

    public List<Object> listaAplicaciones(){
        return autorizacionModuloDao.listaAplicaciobnes();
    }

    public Optional<AutorizacionModulo> getById(Integer id){
        return autorizacionModuloDao.findById(id);
    }

    public Optional<AutorizacionModulo> getByNombre(String name){
        return autorizacionModuloDao.findByNombre(name);
    }

    public AutorizacionModulo save(AutorizacionModulo autorizacionModulo){
        return autorizacionModuloDao.save(autorizacionModulo);
    }
}
