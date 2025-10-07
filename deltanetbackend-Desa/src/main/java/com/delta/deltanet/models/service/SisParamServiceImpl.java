package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ISisParamDao;
import com.delta.deltanet.models.entity.SisParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SisParamServiceImpl implements ISisParamService {

    @Autowired
    private ISisParamDao sisparamDao;

    @Override
    public List<SisParam> findAll() { return sisparamDao.findAll(); }

    @Override
    public SisParam findById(Long id) { return sisparamDao.findById(id).orElse(null); }

    @Override
    public SisParam save(SisParam sisparam) { return sisparamDao.save(sisparam); }

    @Override
    public void delete(Long id) { sisparamDao.deleteById(id); }

    @Override
    public SisParam buscaEtiqueta(String etiqueta) {
        return sisparamDao.buscaEtiqueta(etiqueta);
    }
}
