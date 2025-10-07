package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.JefeArea;

import java.util.List;

public interface IJefeAreaDao extends JpaRepository<JefeArea, Long> {

    @Query("from JefeArea ja where ja.idArea = ?1 and ja.estado = 1")
    List<JefeArea> buscaJefeArea(Long idJefeArea);

    @Query("from JefeArea ja where ja.idArea = ?1 and ja.estado = 1")
    List<JefeArea> buscaJefes(Long idArea);

    @Query("from JefeArea ja where ja.idUsuario = ?1 and ja.estado = ?2")
    List<JefeArea> buscaJefes(Long idUser, Integer estado);
    
    @Query("from JefeArea ja where ja.idUsuario = ?1 and ja.idArea = ?2")
    JefeArea buscaBoss(Long idUser, Long idArea);
}
