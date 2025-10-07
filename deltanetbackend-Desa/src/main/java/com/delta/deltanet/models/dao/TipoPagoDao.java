package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.TipoContrato;
import com.delta.deltanet.models.entity.TipoPago;

public interface TipoPagoDao extends JpaRepository<TipoPago, Integer> {

  TipoPago save(TipoContrato tipoPago);
}
