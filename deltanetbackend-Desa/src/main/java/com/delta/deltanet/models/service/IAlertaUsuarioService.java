package com.delta.deltanet.models.service;


import java.util.List;

import com.delta.deltanet.models.entity.AlertaUsuario;

public interface IAlertaUsuarioService {
    public List<Object> listaEmails(String batch);
    List<AlertaUsuario> listaEmailsv2(String batch);
    AlertaUsuario findById(Long id);
    AlertaUsuario save(AlertaUsuario alerta);
    List<AlertaUsuario> findAll();
    List<AlertaUsuario> findByEstado(Long estado);
}
