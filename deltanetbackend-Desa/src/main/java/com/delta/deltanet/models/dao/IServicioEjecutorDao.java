package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.ServicioEjecutor;

import java.util.List;

public interface IServicioEjecutorDao extends JpaRepository<ServicioEjecutor, Long> {

    @Query("from ServicioEjecutor ja where ja.idArea = ?1 And ja.estado = 1")
    ServicioEjecutor buscaServicioEjecutor(Long id_area);

    @Query("from ServicioEjecutor ja where ja.idUsuario = ?1 And ja.estado = ?2")
    List<ServicioEjecutor> buscaEjecutores(Long id_user, Integer estado);

    @Query("from ServicioEjecutor ja")
    List<ServicioEjecutor> buscaEjecutores();

    @Query("from ServicioEjecutor ja where ja.idUsuario = ?1 And ja.estado = 1")
    ServicioEjecutor findStatus(Long id_user);

}
