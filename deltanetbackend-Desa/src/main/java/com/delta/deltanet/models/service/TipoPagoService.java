package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.TipoPago;

public interface TipoPagoService {
  TipoPago findById(Integer id);
  List<TipoPago> findAll();
}
