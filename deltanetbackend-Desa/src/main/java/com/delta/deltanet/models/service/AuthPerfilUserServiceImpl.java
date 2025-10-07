package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAuthPerfilUserDao;
import com.delta.deltanet.models.entity.AutorizacionPerfilUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthPerfilUserServiceImpl implements IAuthPerfilUserService {

    @Autowired
    private IAuthPerfilUserDao authPerfilUserDao;

    @Override
    public Optional<AutorizacionPerfilUsuario> findById(Long idUser) {
        return authPerfilUserDao.findById(idUser);
    }

    @Override
    public List<Object> findPerfiles(Long idUser) {
        return authPerfilUserDao.findPerfiles(idUser);
    }

    public List<Integer> findListIdPerfil(Long idUser){
        return authPerfilUserDao.findListIdPerfil(idUser);
    }

    public void delete(Integer id){
        authPerfilUserDao.delete(id);
    }

    public void insert(Long id, Integer idPerf){
        authPerfilUserDao.insert(id, idPerf);
    }

    public Optional<AutorizacionPerfilUsuario> buscaIdUsrIdPerfil(Long idUser, Integer idPerfil){
        return authPerfilUserDao.buscaIdUsrIdPerfil(idUser, idPerfil);
    }
}
