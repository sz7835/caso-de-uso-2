package com.delta.deltanet.services.impl;

import com.delta.deltanet.models.dao.IAdmAleMsjCumpleDao;
import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.services.IAdmAleMsjCumpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdmAleMsjCumpleServiceImpl implements IAdmAleMsjCumpleService {

    @Autowired
    private IAdmAleMsjCumpleDao cumpleDao;

    @Override
    public AdmAleMsjCumple findLatestBySexo(Integer sexoId) {
        // estadoReg = 1 means active
        return cumpleDao.findTopBySexoIdAndEstadoRegOrderByIdDesc(sexoId, 1);
    }
}
