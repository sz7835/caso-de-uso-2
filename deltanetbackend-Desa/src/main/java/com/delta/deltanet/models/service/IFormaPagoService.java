package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.FormaPago;

import java.util.List;

public interface IFormaPagoService {
    FormaPago findById(Long id);
    List<FormaPago> findAll();
    FormaPago save(FormaPago formaPago);
    List<FormaPago> findByDescripAndEstado(String descrip, Integer estado);
}
