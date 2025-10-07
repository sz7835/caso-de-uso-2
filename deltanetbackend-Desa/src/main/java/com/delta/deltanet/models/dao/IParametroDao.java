package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IParametroDao extends JpaRepository<Parametro, Long> {
    @Query("from Parametro where idRubro = ?1")
    public List<Parametro> findByRubro(Long idRubro);

    @Query("from Parametro where idRubro = ?1 and idItem = ?2")
    public Parametro findByRubro(Long idRubro,Long idTem);
}
