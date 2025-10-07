package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ITarifarioMonedaDao;
import com.delta.deltanet.models.entity.TarifarioMoneda;

@Service
public class TarifarioMonedaServiceImpl implements TarifarioMonedaService {

    @Autowired
    private ITarifarioMonedaDao relacionDao;

    @Override
    public TarifarioMoneda buscaId(Long id) {
        return relacionDao.buscaId(id);
    }

    @Override
    public List<TarifarioMoneda> findAll() {
        return relacionDao.findAll();
    }

    @Override
    public TarifarioMoneda save(TarifarioMoneda moneda) {
        return relacionDao.save(moneda);
    }

    @Override
    public List<TarifarioMoneda> findByNombreAndEstado(String nombre, int estado) {
        return relacionDao.findByNombreAndEstado(nombre, estado);
    }
}
