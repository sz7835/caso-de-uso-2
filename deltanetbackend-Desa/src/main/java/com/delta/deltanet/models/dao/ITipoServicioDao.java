package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoServicio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoServicioDao extends JpaRepository<TipoServicio,Long> {
	List<TipoServicio> findByDescripAndEstado(String descrip, Integer estado);
}
