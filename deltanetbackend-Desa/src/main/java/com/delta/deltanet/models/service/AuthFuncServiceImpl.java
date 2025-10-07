package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAuthFuncDao;
import com.delta.deltanet.models.entity.AuthFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthFuncServiceImpl implements IAuthFuncService {

    @Autowired
    private IAuthFuncDao authFuncDao;

    @Override
    public List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion, Long idFunc) {
        return authFuncDao.ListaFuncionalidades(perfiles,aplicacion, idFunc);
    }

    @Override
    public List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion) {
        return authFuncDao.ListaFuncionalidades(perfiles,aplicacion);
    }

    public List<AuthFunc> listaApp(Integer idApp){
        return authFuncDao.listaFuncApp(idApp);
    }

    public List<AuthFunc> listaApp(Integer idApp, Long padre){
        return authFuncDao.listaFuncApp(idApp,padre);
    }

    public AuthFunc buscaFuncApp(Long idFunc, Integer idApp){
        return authFuncDao.buscaFuncApp(idFunc, idApp);
    }

    public AuthFunc buscaFuncApp(Long idFunc){
        return authFuncDao.buscaFuncApp(idFunc);
    }

    public List<AuthFunc> listaFuncs(List<Long> lista, Integer idApp){
        return authFuncDao.listaFuncs(lista, idApp);
    }

    public List<AuthFunc> listaFuncs(Long idPadre, List<Long> lista, Integer idApp){
        return authFuncDao.listaFuncs(idPadre, lista, idApp);
    }

}
