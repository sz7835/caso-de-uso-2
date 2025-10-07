package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAuthHomeDao;
import com.delta.deltanet.models.entity.AuthHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthHomeServiceImpl {

    @Autowired
    private IAuthHomeDao authHomeDao;

    public Optional<AuthHome> findById(Long id){
        return authHomeDao.findById(id);
    }

    public void save(AuthHome authHome){
        authHomeDao.save(authHome);
    }
}
