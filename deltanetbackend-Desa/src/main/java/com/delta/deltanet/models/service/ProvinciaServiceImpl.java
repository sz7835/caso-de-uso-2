package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IProvinciaDao;
import com.delta.deltanet.models.entity.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaServiceImpl implements IProvinciaService {

    @Autowired
    public IProvinciaDao provinciaDao;
    @Override
    public List<Provincia> listadoFull() {
        return provinciaDao.listadoFull();
    }

    @Override
    public List<Object> lstPorDpto(Long idDpto) {
        return provinciaDao.lstPorDpto(idDpto);
    }
}
