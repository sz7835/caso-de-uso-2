package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IFormaPagoDao;
import com.delta.deltanet.models.entity.FormaPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormaPagoServiceImpl implements IFormaPagoService {

    @Autowired
    IFormaPagoDao formaPagoDAO;
    @Override
    public FormaPago findById(Long id) {
        return formaPagoDAO.findById(id).orElse(null);
    }

    @Override
    public List<FormaPago> findAll() {
        return formaPagoDAO.findAll();
    }

    @Override
    public FormaPago save(FormaPago formaPago) {
        return formaPagoDAO.save(formaPago);
    }

    @Override
    public List<FormaPago> findByDescripAndEstado(String descrip, Integer estado) {
        return formaPagoDAO.findByDescripAndEstado(descrip, estado);
    }
}
