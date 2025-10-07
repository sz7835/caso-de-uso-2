package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Areas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAreasDao extends JpaRepository<Areas,Integer> {

    @Query("from Areas a where a.idGerencia = ?1")
    List<Areas> getAreasGer(Integer id);
}
