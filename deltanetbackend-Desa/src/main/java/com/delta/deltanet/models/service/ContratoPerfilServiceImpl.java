package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ContratoPerfilServiceDao;
import com.delta.deltanet.models.entity.ContratoPerfiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratoPerfilServiceImpl implements ContratoPerfilService {

    @Autowired
    private ContratoPerfilServiceDao contratoPerfilDao;

    @Override
    public ContratoPerfiles save(ContratoPerfiles ContratoPerfiles) {
        return contratoPerfilDao.save(ContratoPerfiles);
    }

    @Override
    public Optional<ContratoPerfiles> busca(Long idContrato, Long idPerfil) {
        return contratoPerfilDao.busca(idContrato, idPerfil);
    }

    @Override
    public void delete(Long id) {
        contratoPerfilDao.deleteById(id);
    }

    @Override
    public List<ContratoPerfiles> getPerfiles(Long idContrato) {
        return contratoPerfilDao.getPerfiles(idContrato);
    }
}
