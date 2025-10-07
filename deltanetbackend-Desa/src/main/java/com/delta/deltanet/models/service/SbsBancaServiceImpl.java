package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ISbsBancaDao;
import com.delta.deltanet.models.entity.SbsBanca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SbsBancaServiceImpl {
    @Autowired
    ISbsBancaDao sbsBancaDao;

    public int countByNombreAndEstado(String nombre, int estado) {
        return sbsBancaDao.countByNombreAndEstado(nombre, estado);
    }

    public int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id) {
        return sbsBancaDao.countByNombreAndEstadoAndIdNot(nombre, estado, id);
    }
    public int countByCodsbsAndEstado(String codsbs, int estado) {
        return sbsBancaDao.countByCodsbsAndEstado(codsbs, estado);
    }

    public int countByCodsbsAndEstadoAndIdNot(String codsbs, int estado, Integer id) {
        return sbsBancaDao.countByCodsbsAndEstadoAndIdNot(codsbs, estado, id);
    }

    public List<SbsBanca> getBancas(){
        return  sbsBancaDao.findAll();
    }

    public List<SbsBanca> listaCtaSueldo(){
        return sbsBancaDao.listaCtaSueldo();
    }

    public SbsBanca save(SbsBanca sbsBanca){
        return sbsBancaDao.save(sbsBanca);
    }

    public Optional<SbsBanca> getById(Integer id){
        return sbsBancaDao.findById(id);
    }
}
