package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoFuncionalidadDao extends JpaRepository<RepArchivoFuncionalidad,Long> {
    @Query("from RepArchivoFuncionalidad a where a.idFolder = ?1")
    List<RepArchivoFuncionalidad> getRepFuncionalidades(Long idSubModulo);

    @Query("from RepArchivoFuncionalidad a "
            + "inner join a.folder r " 
    		+ "where a.idArchivoFunc = ?1")
    Optional<RepArchivoFuncionalidad> busca(Long idFunc);
}
