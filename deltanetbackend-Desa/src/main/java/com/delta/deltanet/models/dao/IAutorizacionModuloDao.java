package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AutorizacionModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAutorizacionModuloDao extends JpaRepository<AutorizacionModulo,Integer> {
    Optional<AutorizacionModulo> findByNombre(String nombre);

   @Query("select a.id, a.nombre from AutorizacionModulo a where a.estado = 1")
    List<Object> listaAplicaciobnes();
}
