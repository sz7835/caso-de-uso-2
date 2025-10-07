package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TipoEnfermedad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoEnfermedadDao extends JpaRepository<TipoEnfermedad,Integer> {
}
