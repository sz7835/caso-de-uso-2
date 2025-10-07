package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.RepArchivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoDao extends JpaRepository<RepArchivo,Long> {
    @Query("from RepArchivo a where a.idtabla = ?1")
    List<RepArchivo> getRepArchivos(Long idContrato);

    @Query("from RepArchivo a where a.idtabla = ?1 and a.tabla = ?2")
    List<RepArchivo> getRepArchivos(Long idContrato, String table);

    @Query("from RepArchivo a where a.idArchivo = ?1")
    Optional<RepArchivo> getArchivo(Long idArchivo);

}
