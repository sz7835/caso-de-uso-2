package com.delta.deltanet.models.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IAdmAleMsjCumpleDao;
import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.models.entity.PerNatSexo;
import com.delta.deltanet.models.service.IAdmAleMsjCumpleService;

@Service
public class AdmAleMsjCumpleServiceImpl implements IAdmAleMsjCumpleService {

    @Autowired
    private IAdmAleMsjCumpleDao cumpleDao;

    @Override
    @Transactional(readOnly = true)
    public AdmAleMsjCumple findLatestBySexo(PerNatSexo sexo) {
        if (sexo == null) return null;

        // DAO expects an int (id of PerNatSexo), not the entity itself
        Integer sexoId = sexo.getId(); // If your entity uses a different getter, tell me and I'll switch it.
        if (sexoId == null) return null;

        return cumpleDao.findLatestBySexo(sexoId);
    }
}
