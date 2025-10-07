package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITrabFamiliaDao;
import com.delta.deltanet.models.entity.TrabajadorFamilia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabFamiliaServiceImpl implements ITrabFamiliaService{

    @Autowired
    private ITrabFamiliaDao familiaDao;

    @Override
    public void save(TrabajadorFamilia trabajadorFamilia) {
        familiaDao.save(trabajadorFamilia);
    }

    @Override
    public TrabajadorFamilia findById(Long id) {
        return familiaDao.findById(id).orElse(null);
    }

    @Override
    public List<Object> listaFamilia(Long idPerNat) {
        return familiaDao.listaFamilia(idPerNat);
    }
}
