package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoContrato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ITipoContrato2024Dao extends JpaRepository<TipoContrato,Long> {
	List<TipoContrato> findByDescripAndEstado(String descrip, Integer estado);
}
