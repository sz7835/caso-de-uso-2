package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.LiquidacionFacturaDao;
import com.delta.deltanet.models.entity.LiquidacionFactura;

@Service
public class LiquidacionFacturaServiceImpl implements LiquidacionFacturaService {

    @Autowired
    private LiquidacionFacturaDao liquidacionFacturaDAO;

    @Override
    @Transactional
    public LiquidacionFactura save(LiquidacionFactura liquidacion) {
        return liquidacionFacturaDAO.save(liquidacion);
    }

}