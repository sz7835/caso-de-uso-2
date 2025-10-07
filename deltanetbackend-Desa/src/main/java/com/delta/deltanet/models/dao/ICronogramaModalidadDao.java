package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.CronogramaModalidad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICronogramaModalidadDao extends JpaRepository<CronogramaModalidad, Long> {

	@Query("from CronogramaModalidad where estado = 1")
	List<CronogramaModalidad> findActive();

	List<CronogramaModalidad> findAll();
}
