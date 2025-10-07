package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.ServicioTipo;

public interface IServicioTipoDao extends JpaRepository<ServicioTipo, Long> {
	@Query("from ServicioTipo st where st.estado = 1")
	List<ServicioTipo> listaTipos();

	List<ServicioTipo> findByEstado(int estado);

	List<ServicioTipo> findByNombreAndEstado(String nombre, int estado);
}
