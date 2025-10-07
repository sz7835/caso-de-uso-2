package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IEmailDao;
import com.delta.deltanet.models.entity.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private IEmailDao emailDao;


    @Override
    public List<EMail> findAllEmail(Long id) {
        return emailDao.findAllEmail(id);
    }

    @Override
    public EMail findById(Long idEMail) {
        return emailDao.findById(idEMail);
    }

    @Override
    public List<EMail> findByCorreo(String correo) {
        return emailDao.findByCorreo(correo);
    }

    @Override
    public EMail save(EMail email) {
        return emailDao.save(email);
    }

    @Override
    public List<Object> findAllAct() {
        return emailDao.findAllAct();
    }

    @Override
    public List<Object> findAllPer(Long idPersona) {
        return emailDao.findAllPer(idPersona);
    }

    @Override
    public List<Object> findByCorreosPer(Long tipo, Long idPer) {
        return emailDao.findByCorreosPer(tipo, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(Long tipo, String correo, Long idPer) {
        return emailDao.findByCorreosPer(tipo, correo, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(Long tipo, String correo, Integer estado, Long idPer) {
        return emailDao.findByCorreosPer(tipo, correo, estado, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(Long tipo, Integer estado, Long idPer) {
        return emailDao.findByCorreosPer(tipo, estado, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(String correo, Long idPer) {
        return emailDao.findByCorreosPer(correo, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(String correo, Integer estado, Long idPer) {
        return emailDao.findByCorreosPer(correo, estado, idPer);
    }

    @Override
    public List<Object> findByCorreosPer(Integer estado, Long idPer) {
        return emailDao.findByCorreosPer(estado, idPer);
    }

}
