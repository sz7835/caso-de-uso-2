package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDistritoDao;
import com.delta.deltanet.models.entity.Distrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistritoServiceImpl implements IDistritoService {

    @Autowired
    private IDistritoDao distritoDao;
    @Override
    public List<Distrito> findAll() {
        return distritoDao.findAll();
    }

    @Override
    public Distrito finddByIdDist(Long id) {
        return distritoDao.findByIdDist(id);
    }

    @Override
    public List<Object> lstPorProv(Long idProv) {
        return distritoDao.lstPorProv(idProv);
    }
}
