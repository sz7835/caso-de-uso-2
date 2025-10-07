package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.TarifarioMoneda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITarifarioMonedaDao extends JpaRepository<TarifarioMoneda, Long> {

  List<TarifarioMoneda> findAll();

  @Query("from TarifarioMoneda where id = ?1")
  TarifarioMoneda buscaId(Long id);

  List<TarifarioMoneda> findByNombreAndEstado(String nombre, int estado);
}
