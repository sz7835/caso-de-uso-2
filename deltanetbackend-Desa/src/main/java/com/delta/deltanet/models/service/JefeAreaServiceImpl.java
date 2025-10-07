package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IJefeAreaDao;
import com.delta.deltanet.models.entity.JefeArea;

import java.util.List;
import java.util.Optional;

@Service
public class JefeAreaServiceImpl implements IJefeAreaService {

    @Autowired private IJefeAreaDao jefeAreaDao;

    @Override
    public JefeArea buscaJefeArea(Long idJefeArea) {
        List<JefeArea> resultados = jefeAreaDao.buscaJefeArea(idJefeArea);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public List<JefeArea> buscaJefes(Long idArea) {
        return jefeAreaDao.buscaJefes(idArea);
    }

    @Override
    public List<JefeArea> buscaJefes(Long idUser, Integer estado) {
        return jefeAreaDao.buscaJefes(idUser, estado);
    }

    public List<JefeArea> getAll(){
        return jefeAreaDao.findAll();
    }

    public Optional<JefeArea> getById(Long id){
        return jefeAreaDao.findById(id);
    }

    public JefeArea save(JefeArea jefeArea){
        return jefeAreaDao.save(jefeArea);
    }

    @Override
    public JefeArea buscaBoss(Long idUser, Long idArea) {
        return jefeAreaDao.buscaBoss(idUser, idArea);
    }

}
