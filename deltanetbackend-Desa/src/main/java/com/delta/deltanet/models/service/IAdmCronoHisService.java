package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmCronogramaHist;

public interface IAdmCronoHisService {
    public AdmCronogramaHist save(AdmCronogramaHist cronoHist);
    void delete(Long id);
}
