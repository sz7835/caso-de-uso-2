package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoServicioDao;
import com.delta.deltanet.models.entity.TipoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoServicioServiceImpl implements ITipoServicioService {

    @Autowired
    private ITipoServicioDao tipoServicioDAO;
    @Override
    public TipoServicio findById(Long id) {
        return tipoServicioDAO.findById(id).orElse(null);
    }

    @Override
    public List<TipoServicio> findAll() {
        return tipoServicioDAO.findAll();
    }

    @Override
    public TipoServicio save(TipoServicio tipoServicio) {
        return tipoServicioDAO.save(tipoServicio);
    }

    @Override
    public List<TipoServicio> findByDescripAndEstado(String descrip, Integer estado) {
        return tipoServicioDAO.findByDescripAndEstado(descrip, estado);
    }
}
