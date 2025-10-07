package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.ContratoOutsourcing;

public interface IContratoOutsourcingService {
	
    ContratoOutsourcing findById(Long id);
    
    ContratoOutsourcing save(ContratoOutsourcing contrato);
    
    ContratoOutsourcing findRelation(Long idRel);
}
