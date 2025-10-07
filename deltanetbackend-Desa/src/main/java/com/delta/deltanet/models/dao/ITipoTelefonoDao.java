package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TelefonoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITipoTelefonoDao extends JpaRepository<TelefonoTipo,Long> {
	List<TelefonoTipo> findAllByIdTipoPersona(Long idTipoPersona);
	List<TelefonoTipo> findAll();
}
