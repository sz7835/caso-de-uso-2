package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IContratoOutsourcingDao;
import com.delta.deltanet.models.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ContratoOutsourcingServiceImpl implements IContratoOutsourcingService {
    @Autowired
    public IContratoOutsourcingDao contratoDao;
	
    @Override
	public ContratoOutsourcing findById(Long id) {
		return contratoDao.findById(id).orElse(null);
	}

	@Override
	public ContratoOutsourcing save(ContratoOutsourcing contrato) {
		return contratoDao.save(contrato);
	}
	
	@Override
	public ContratoOutsourcing findRelation(Long idRel) {
		return contratoDao.findRelation(idRel);
	}

    
    
}
