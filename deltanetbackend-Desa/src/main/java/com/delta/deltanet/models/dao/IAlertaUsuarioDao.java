package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AlertaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAlertaUsuarioDao extends JpaRepository<AlertaUsuario,Long> {

    @Query("select a.correoDelta, a.nomUsuario, a.tipoUsuario, a.areaUsuario" +
            " from AlertaUsuario a" +
            " where a.estadoReg = 1 and lower(a.batch) = lower(?1)")
    public List<Object> listaEmails(String batch);

    @Query("SELECT a FROM AlertaUsuario a WHERE a.batch = :batch")
    List<AlertaUsuario> listaEmailsv2(@Param("batch") String batch);

    List<AlertaUsuario> findByEstadoReg(Long estadoReg);
}
