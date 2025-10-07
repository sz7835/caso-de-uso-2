package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDepartamentoDao;
import com.delta.deltanet.models.entity.Departamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService {

    @Autowired
    IDepartamentoDao departamentoDao;
    @Override
    public List<Departamento> findByAll() {
        return departamentoDao.findAll();
    }
}
