package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.DireccionTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IDireccionTipoDao extends JpaRepository<DireccionTipo, Long> {
    List<DireccionTipo> findByEstado(Integer estado);
    List<DireccionTipo> findByIdTipoPersona(Long idTipoPersona);
    List<DireccionTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado);
}
