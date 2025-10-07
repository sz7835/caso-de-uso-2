package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IFuncionalidadDao;
import com.delta.deltanet.models.entity.Funcionalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionalidadServiceImpl {
    @Autowired
    IFuncionalidadDao funcionalidadDao;

    public List<Funcionalidad> getAll(){
        return  funcionalidadDao.findAll();
    }

    public List<Object> listaFunciones() { return  funcionalidadDao.listaFuncionalidades(); }

    public Optional<Funcionalidad> getById(Long id){
        return funcionalidadDao.findById(id);
    }

    public Funcionalidad save(Funcionalidad funcionalidad){
        return funcionalidadDao.save(funcionalidad);
    }

    public Funcionalidad getByRoute(String route) {
        return funcionalidadDao.findByRoute(route);
    }

    public List<Funcionalidad> getByPadreId(Long idPadre) {
        return funcionalidadDao.findByPadreId(idPadre);
    }
}
