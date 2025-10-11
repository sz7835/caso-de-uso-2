package com.delta.deltanet.services.impl;

import com.delta.deltanet.models.dao.IPerNatSexoDao;
import com.delta.deltanet.models.entity.PerNatSexo;
import com.delta.deltanet.models.service.IPerNatSexoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerNatSexoServiceImpl implements IPerNatSexoService {

    @Autowired
    private IPerNatSexoDao perNatSexoDao;

    @Override
    public List<PerNatSexo> findAll() {
        return perNatSexoDao.findAll();
    }

    @Override
    public PerNatSexo findById(Integer id) {
        return perNatSexoDao.findById(id).orElse(null);
    }
}
