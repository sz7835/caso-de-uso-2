package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoContrato2024Dao;
import com.delta.deltanet.models.entity.TipoContrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoContrato2024ServiceImpl implements ITipoContrato2024Service {

    @Autowired
    private ITipoContrato2024Dao tipoContratoDAO;
    @Override
    public TipoContrato findById(Long id) {
        return tipoContratoDAO.findById(id).orElse(null);
    }

    @Override
    public List<TipoContrato> findAll() {
        return tipoContratoDAO.findAll();
    }

    public TipoContrato save(TipoContrato tipoContrato){
        return tipoContratoDAO.save(tipoContrato);
    }

    public Optional<TipoContrato> getById(Long id){
        return tipoContratoDAO.findById(id);
    }

    public List<TipoContrato> findByDescripAndEstado(String descrip, Integer estado) {
        return tipoContratoDAO.findByDescripAndEstado(descrip, estado);
    }
}
