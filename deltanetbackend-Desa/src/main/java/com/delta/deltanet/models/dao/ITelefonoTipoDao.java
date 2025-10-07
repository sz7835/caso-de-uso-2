package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TelefonoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ITelefonoTipoDao extends JpaRepository<TelefonoTipo, Long> {
    List<TelefonoTipo> findByDescripcionAndIdTipoPersonaAndEstado(String descripcion, Long idTipoPersona, int estado);
    List<TelefonoTipo> findByEstado(Integer estado);
    List<TelefonoTipo> findByIdTipoPersona(Long idTipoPersona);
}
