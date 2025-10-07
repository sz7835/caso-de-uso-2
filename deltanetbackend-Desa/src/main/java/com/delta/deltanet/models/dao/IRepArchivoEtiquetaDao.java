package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRepArchivoEtiquetaDao extends JpaRepository<RepArchivoEtiqueta,Long> {
    @Query("from RepArchivoEtiqueta a where a.nombre = ?1")
    Optional<RepArchivoEtiqueta> findByNombre(String nombre);
}
