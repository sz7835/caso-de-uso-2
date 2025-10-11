package com.delta.deltanet.services.impl;

import com.delta.deltanet.models.dao.IAdmAleMsjCumpleDao;
import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.services.IAdmAleMsjCumpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdmAleMsjCumpleServiceImpl implements IAdmAleMsjCumpleService {

    @Autowired
    private IAdmAleMsjCumpleDao cumpleDao;

    @Override
    @Transactional(readOnly = true)
    public AdmAleMsjCumple findLatestBySexo(Integer sexoId) {
        if (sexoId == null) {
            return null;
        }
        return cumpleDao.findLatestBySexo(sexoId);
    }
}
