package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ICronogramaModalidadDao;
import com.delta.deltanet.models.entity.CronogramaModalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CronogramaModalidadServiceImpl implements ICronogramaModalidadService{

    @Autowired
    private ICronogramaModalidadDao cronogramaModalidadDao;

    @Override
    public CronogramaModalidad findById(Long id) {
        return cronogramaModalidadDao.findById(id).orElse(null);
    }

    @Override
    public List<CronogramaModalidad> findAll() {
        return cronogramaModalidadDao.findAll();
    }

    @Override
    public CronogramaModalidad save(CronogramaModalidad cronogramaModalidad) {
        return cronogramaModalidadDao.save(cronogramaModalidad);
    }

    @Override
    public List<CronogramaModalidad> findActive() {
        return cronogramaModalidadDao.findActive();
    }

}
