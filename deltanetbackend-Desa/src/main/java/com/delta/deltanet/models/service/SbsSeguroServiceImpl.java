package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ISbsSeguroDao;
import com.delta.deltanet.models.entity.SbsSeguro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SbsSeguroServiceImpl {
    @Autowired
    ISbsSeguroDao sbsSeguroDao;

    public List<SbsSeguro> getSeguros(){
        return  sbsSeguroDao.findAll();
    }

    public SbsSeguro save(SbsSeguro sbsSeguro){
        return sbsSeguroDao.save(sbsSeguro);
    }

    public Optional<SbsSeguro> getById(Integer id){
        return sbsSeguroDao.findById(id);
    }

    public int countByNombreAndEstado(String nombre, int estado) {
        return sbsSeguroDao.countByNombreAndEstado(nombre, estado);
    }

    public int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id) {
        return sbsSeguroDao.countByNombreAndEstadoAndIdNot(nombre, estado, id);
    }

    public int countByCodsbsAndEstado(String codsbs, int estado) {
        return sbsSeguroDao.countByCodsbsAndEstado(codsbs, estado);
    }

    public int countByCodsbsAndEstadoAndIdNot(String codsbs, int estado, Integer id) {
        return sbsSeguroDao.countByCodsbsAndEstadoAndIdNot(codsbs, estado, id);
    }
}
