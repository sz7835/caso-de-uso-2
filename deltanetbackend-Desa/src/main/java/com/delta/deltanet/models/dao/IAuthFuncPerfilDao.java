package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AuthFuncPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IAuthFuncPerfilDao extends JpaRepository<AuthFuncPerfil,Integer> {

    @Query("select af.idFunc from AuthFuncPerfil a " +
            "inner join a.authFunc af " +
            "where a.idPerfil = ?1 and af.idApp = ?2")
    List<Object> listaFunciones(Integer idPerfil, Integer idApp);

    @Query("from AuthFuncPerfil a " +
            "inner join a.authFunc af " +
            "where a.idPerfil = ?1 and af.idFunc = ?2")
    Optional<AuthFuncPerfil> busca(Integer idPerfil, Long idFuncion);

    @Query("select af.idFunc from AuthFuncPerfil a " +
            "inner join a.authFunc af " +
            "where a.idPerfil = ?1")
    List<Long> listaFuncionalidades(Integer idPerfil);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sec_autorizacion_funcionalidad_perfil WHERE ID_PERFIL = :id",nativeQuery = true)
    void deleteFunc(Integer id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sec_autorizacion_funcionalidad_perfil (ID_PERFIL, ID_FUNCIONALIDAD) " +
            "VALUES (:id, :idFun);",nativeQuery = true)
    void insertFunc(Integer id, Long idFun);


}
