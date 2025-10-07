package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAutenticacionClaveDao;
import com.delta.deltanet.models.entity.AutenticacionClave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AutenticacionClaveServiceImpl {
    @Autowired
    IAutenticacionClaveDao autenticacionClaveDao;

    public AutenticacionClave save(AutenticacionClave autenticacionClave){
        return autenticacionClaveDao.save(autenticacionClave);
    }

    public Optional<AutenticacionClave> findById (Long id){
        return autenticacionClaveDao.findById(id);
    }

    public AutenticacionClave buscaUserClave(Long id, Integer secuencia){
        return autenticacionClaveDao.buscaUserClave(id,secuencia);
    }

    public void delete(Long id){
        autenticacionClaveDao.delete(id);
    }

    public void insert(Long id, Integer secPwd, String password, String usrIngreso, Date fecIngreso){
        autenticacionClaveDao.insert(id, secPwd, password, usrIngreso, fecIngreso);
    }
}
