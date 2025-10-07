package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Modalidad;

public interface IModalidadDao extends JpaRepository<Modalidad, Long>{

	List<Modalidad> findByEstado(int estado);

	List<Modalidad> findByNombreIgnoreCaseAndEstado(String nombre, int estado);

	List<Modalidad> findByNombreContainingIgnoreCaseAndEstado(String nombre, int estado);

}
