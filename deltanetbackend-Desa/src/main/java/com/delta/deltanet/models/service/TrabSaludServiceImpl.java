package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITrabajadorSaludDao;
import com.delta.deltanet.models.entity.TrabajadorSalud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrabSaludServiceImpl implements ITrabSaludService{

    @Autowired
    private ITrabajadorSaludDao saludDao;

    @Override
    public TrabajadorSalud save(TrabajadorSalud trabajadorSalud) {
        return saludDao.save(trabajadorSalud);
    }

    @Override
    public TrabajadorSalud findById(Long id) {
        return saludDao.findById(id).orElse(null);
    }

    @Override
    public TrabajadorSalud buscaByPerNat(Long idPerNat) {
        return saludDao.buscaByPerNat(idPerNat);
    }
}
