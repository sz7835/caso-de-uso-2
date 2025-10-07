package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITrabajadorSueldoDao;
import com.delta.deltanet.models.entity.TrabajadorSueldo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrabSueldoServiceImpl implements ITrabSueldoService{

    @Autowired
    private ITrabajadorSueldoDao sueldoDao;

    @Override
    public TrabajadorSueldo save(TrabajadorSueldo trabajadorSueldo) {
        return sueldoDao.save(trabajadorSueldo);
    }

    @Override
    public TrabajadorSueldo findById(Long id) {
        return sueldoDao.findById(id).orElse(null);
    }

    @Override
    public TrabajadorSueldo buscaByPerNat(Long idPerNat) {
        return sueldoDao.buscaByPerNat(idPerNat);
    }
}
