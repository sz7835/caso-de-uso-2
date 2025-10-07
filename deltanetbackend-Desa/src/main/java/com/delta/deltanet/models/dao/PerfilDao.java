package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfilDao extends JpaRepository<Perfil, Long> {
    @Query("SELECT p FROM Perfil p " +
            "WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:estado = 0 OR p.estado = :estado)")
    List<Perfil> findByNombreAndEstado(
            @Param("nombre") String nombre,
            @Param("estado") Integer estado);

    @Query("from Perfil c where c.id = ?1")
    Perfil buscaId(Long idPer);

}
