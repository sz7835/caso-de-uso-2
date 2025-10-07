package com.delta.deltanet.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.TipoPagoDao;
import com.delta.deltanet.models.entity.TipoPago;

@Service
public class TipoPagoServiceImpl implements TipoPagoService {

  @Autowired
  private TipoPagoDao tipoPagoDao;

  @Override
  public TipoPago findById(Integer id) {
    return tipoPagoDao.findById(id).orElse(null);
  }

  @Override
  public List<TipoPago> findAll() {
    return tipoPagoDao.findAll();
  }

  public TipoPago save(TipoPago TipoPago){
    return tipoPagoDao.save(TipoPago);
  }

  public Optional<TipoPago> getById(Integer id){
    return tipoPagoDao.findById(id);
  }
}
