package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.RepArchivoFormato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IRepArchivoFormatoDao extends JpaRepository<RepArchivoFormato,Long> {
    @Query("SELECT COUNT(a) FROM RepArchivoFormato a WHERE a.nombre = ?1 AND a.estado = ?2")
    int countByNombreAndEstado(String nombre, int estado);

    @Query("SELECT COUNT(a) FROM RepArchivoFormato a WHERE a.nombre = ?1 AND a.estado = ?2 AND a.id <> ?3")
    int countByNombreAndEstadoExcluyendoId(String nombre, int estado, Long id);
    @Query("from RepArchivoFormato a where a.nombre = ?1")
    Optional<RepArchivoFormato> buscaExt(String extension);

    @Query("from RepArchivoFormato a where a.estado = 1")
    List<RepArchivoFormato> lista();
}
