package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ISbsAfpDao;
import com.delta.deltanet.models.entity.SbsAfp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SbsAfpServiceImpl {
    @Autowired
    ISbsAfpDao sbsAfpDao;

    public List<SbsAfp> getAfps(){
        return  sbsAfpDao.findAll();
    }

    public SbsAfp save(SbsAfp sbsAfp){
        return sbsAfpDao.save(sbsAfp);
    }

    public Optional<SbsAfp> getById(Integer id){
        return sbsAfpDao.findById(id);
    }

    public int countByNombreAndEstado(String nombre, int estado) {
        return sbsAfpDao.countByNombreAndEstado(nombre, estado);
    }

    public int countByNombreAndEstadoAndIdNot(String nombre, int estado, Integer id) {
        return sbsAfpDao.countByNombreAndEstadoAndIdNot(nombre, estado, id);
    }

    public int countByCodsbsAndEstado(String codsbs, int estado) {
        return sbsAfpDao.countByCodsbsAndEstado(codsbs, estado);
    }

    public int countByCodsbsAndEstadoAndIdNot(String codsbs, int estado, Integer id) {
        return sbsAfpDao.countByCodsbsAndEstadoAndIdNot(codsbs, estado, id);
    }
}
