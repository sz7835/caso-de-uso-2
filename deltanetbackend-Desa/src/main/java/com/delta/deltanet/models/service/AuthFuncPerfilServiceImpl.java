package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAuthFuncPerfilDao;
import com.delta.deltanet.models.entity.AuthFuncPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthFuncPerfilServiceImpl {

    @Autowired
    private IAuthFuncPerfilDao authFuncPerfilDao;

    public List<Object> listaFunciones(Integer idPerfil, Integer idApp){
        return authFuncPerfilDao.listaFunciones(idPerfil, idApp);
    }

    public Optional<AuthFuncPerfil> busca(Integer idPerfil, Long idFuncion){
        return authFuncPerfilDao.busca(idPerfil, idFuncion);
    }

    public List<AuthFuncPerfil> busca(Iterable<Integer> idPerfil){
        return authFuncPerfilDao.findAllById(idPerfil);
    }

    public List<AuthFuncPerfil> listaFunciones(Integer idPerfil){
        return authFuncPerfilDao.findAllById(Collections.singleton(idPerfil));
    }

    public List<Long> listaFuncionalidades(Integer idPerfil){
        return authFuncPerfilDao.listaFuncionalidades(idPerfil);
    }

    public void delete(AuthFuncPerfil authFuncPerfil){
        authFuncPerfilDao.delete(authFuncPerfil);
    }

    public void deleteAll(List<AuthFuncPerfil> registros){
        authFuncPerfilDao.deleteAll(registros);
    }

    public void deleteFunc(Integer id){
        authFuncPerfilDao.deleteFunc(id);
    }

    public void insertFunc(Integer id, Long idFun){
        authFuncPerfilDao.insertFunc(id, idFun);
    }

    public void save(AuthFuncPerfil authFuncPerfil){
        authFuncPerfilDao.save(authFuncPerfil);
    }

}
