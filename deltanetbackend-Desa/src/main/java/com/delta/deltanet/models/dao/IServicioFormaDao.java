package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.ServicioForma;

public interface IServicioFormaDao extends JpaRepository<ServicioForma, Long> {
	
	@Query("from ServicioForma sf where sf.estado = 1")
	List<ServicioForma> listaFormas();

}
