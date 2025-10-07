package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoPersonaDao;
import com.delta.deltanet.models.entity.TipoPersona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoPersonaServiceImpl implements ITipoPersonaService {

    @Autowired
    private ITipoPersonaDao tipoPersonaDao;

    @Override
    public List<TipoPersona> findAll() {
        return tipoPersonaDao.findAll();
    }

    @Override
    public TipoPersona buscaTipoPer(Long idTipoPer) {
        return tipoPersonaDao.buscaTipoPer(idTipoPer);
    }
}
