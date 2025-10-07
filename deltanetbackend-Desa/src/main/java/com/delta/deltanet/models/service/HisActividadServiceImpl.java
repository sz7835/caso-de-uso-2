package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IHisActividadesDao;
import com.delta.deltanet.models.entity.HisActividades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HisActividadServiceImpl implements IHisActividadService{

    @Autowired
    private IHisActividadesDao hisActividadesDao;

    @Override
    public void save(HisActividades hisActividades) {
        hisActividadesDao.save(hisActividades);
    }
}
