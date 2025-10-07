package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAdmCronoHisDao;
import com.delta.deltanet.models.entity.AdmCronogramaHist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdmCronoHisServiceImpl implements IAdmCronoHisService{

    @Autowired
    private IAdmCronoHisDao cronoHisDao;

    @Override
    public AdmCronogramaHist save(AdmCronogramaHist cronoHist) {
        return cronoHisDao.save(cronoHist);
    }

    @Override
    public void delete(Long id) {
        cronoHisDao.deleteById(id);
    }
}
