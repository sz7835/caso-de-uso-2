package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.FormaPago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IFormaPagoDao extends JpaRepository<FormaPago,Long> {
	List<FormaPago> findByDescripAndEstado(String descrip, Integer estado);
}
