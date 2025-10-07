package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AutenticacionClave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface IAutenticacionClaveDao extends JpaRepository<AutenticacionClave,Long> {
    @Query("from AutenticacionClave a " +
            "where a.id = ?1 and a.secPassword = ?2")
    AutenticacionClave buscaUserClave(Long id, Integer secuencia);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sec_autenticacion_clave WHERE ID_USUARIO = :id",nativeQuery = true)
    void delete(Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sec_autenticacion_clave (ID_USUARIO,SEC_PASSWORD,DSC_PASSWORD,USR_INGRESO,FEC_INGRESO) " +
            "VALUES (:id, :secPwd, :password, :usrIngreso, :fecIngreso);",nativeQuery = true)
    void insert(Long id, Integer secPwd, String password, String usrIngreso, Date fecIngreso);
}
