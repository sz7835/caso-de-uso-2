package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.LiquidacionPagoDao;
import com.delta.deltanet.models.entity.LiquidacionPago;

@Service
public class LiquidacionPagoServiceImpl implements LiquidacionPagoService {

    @Autowired
    private LiquidacionPagoDao liquidacionPagoDao;

    @Override
    @Transactional
    public LiquidacionPago save(LiquidacionPago liquidacion) {
        return liquidacionPagoDao.save(liquidacion);
    }

}