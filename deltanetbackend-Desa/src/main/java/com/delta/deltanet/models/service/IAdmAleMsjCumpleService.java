package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.models.entity.PerNatSexo;

public interface IAdmAleMsjCumpleService {
    AdmAleMsjCumple findLatestBySexo(PerNatSexo sexo);
}
