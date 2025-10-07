package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.SisParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISisParamDao extends JpaRepository<SisParam,Long> {
    @Query("from SisParam where etiqueta = ?1")
    public SisParam buscaEtiqueta(String etiqueta);
}
