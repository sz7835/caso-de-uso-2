package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAlertaUsuarioDao;
import com.delta.deltanet.models.entity.AlertaUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaUsuarioServiceImpl implements  IAlertaUsuarioService{

    @Autowired
    private IAlertaUsuarioDao alertaUsuarioDao;

    @Override
    public List<Object> listaEmails(String batch) {
        return alertaUsuarioDao.listaEmails(batch);
    }

    @Override
    public List<AlertaUsuario> listaEmailsv2(String batch) {
        return alertaUsuarioDao.listaEmailsv2(batch);
    }

    @Override
    public AlertaUsuario findById(Long id) {
        return alertaUsuarioDao.findById(id).orElse(null);
    }

    @Override
    public AlertaUsuario save(AlertaUsuario alerta) {
        return alertaUsuarioDao.save(alerta);
    }

    @Override
    public List<AlertaUsuario> findAll() {
        return alertaUsuarioDao.findAll();
    }

    @Override
    public List<AlertaUsuario> findByEstado(Long estado) {
        return alertaUsuarioDao.findByEstadoReg(estado);
    }
}
