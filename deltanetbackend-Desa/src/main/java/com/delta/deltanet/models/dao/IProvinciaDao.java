package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProvinciaDao extends JpaRepository<Provincia, Long> {
    @Query("from Provincia")
    List<Provincia> listadoFull();

    @Query("select p.id, p.descripcion from Provincia p inner join p.dpto d where d.id = ?1")
    List<Object> lstPorDpto(Long idDpto);
}
