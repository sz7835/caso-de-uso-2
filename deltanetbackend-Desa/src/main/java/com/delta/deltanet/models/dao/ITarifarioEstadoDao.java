package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TarifarioEstado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITarifarioEstadoDao extends JpaRepository<TarifarioEstado, Long> {

  List<TarifarioEstado> findAll();
}
