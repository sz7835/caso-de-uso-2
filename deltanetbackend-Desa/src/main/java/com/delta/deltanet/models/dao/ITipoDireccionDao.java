package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.DireccionTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITipoDireccionDao extends JpaRepository<DireccionTipo,Long> {
	List<DireccionTipo> findAllByIdTipoPersona(Long idTipoPersona);
	List<DireccionTipo> findAll();
}
