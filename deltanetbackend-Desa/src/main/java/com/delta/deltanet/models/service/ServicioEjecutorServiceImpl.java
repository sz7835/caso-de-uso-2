package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IServicioEjecutorDao;
import com.delta.deltanet.models.entity.ServicioEjecutor;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioEjecutorServiceImpl implements IServicioEjecutorService {

    @Autowired 
    private IServicioEjecutorDao serviceEjecutorDao;

    public Optional<ServicioEjecutor> getById(Long id){
        return serviceEjecutorDao.findById(id);
    }

    public ServicioEjecutor findStatus(Long id){
        return serviceEjecutorDao.findStatus(id);
    }

    @Override
    public ServicioEjecutor buscaServicioEjecutor(Long id_area) {
        return serviceEjecutorDao.buscaServicioEjecutor(id_area);
    }

    @Override
    public List<ServicioEjecutor> buscaEjecutores(Long id_user, Integer estado) {
        return serviceEjecutorDao.buscaEjecutores(id_user, estado);
    }

    @Override
    public List<ServicioEjecutor> buscaEjecutores() {
        return serviceEjecutorDao.buscaEjecutores();
    }

    public ServicioEjecutor save(ServicioEjecutor ejecutor){
        return serviceEjecutorDao.save(ejecutor);
    }
    
}
