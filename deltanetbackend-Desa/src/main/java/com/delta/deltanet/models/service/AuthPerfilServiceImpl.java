package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAuthPerfilDao;
import com.delta.deltanet.models.entity.AutorizacionPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthPerfilServiceImpl implements IAuthPerfilService {

    @Autowired
    private IAuthPerfilDao authPerfilDao;

    @Override
    public AutorizacionPerfil findById(int id) {
        return authPerfilDao.findById(id);
    }

    public List<AutorizacionPerfil> findByEstado(Integer estado){
        return authPerfilDao.findByEstado(estado);
    }

    public List<AutorizacionPerfil> getAll(){
        return authPerfilDao.findAll();
    }

    public AutorizacionPerfil getByName(String name){
        return authPerfilDao.findByNombre(name);
    }

    public AutorizacionPerfil save(AutorizacionPerfil autorizacionPerfil){
        return authPerfilDao.save(autorizacionPerfil);
    }

}
