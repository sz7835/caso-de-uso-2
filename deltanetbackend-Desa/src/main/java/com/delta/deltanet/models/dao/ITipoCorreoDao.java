package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.CorreoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITipoCorreoDao extends JpaRepository<CorreoTipo,Long> {
	List<CorreoTipo> findAllByIdTipoPersona(Long idTipoPersona);
	List<CorreoTipo> findAll();
}
