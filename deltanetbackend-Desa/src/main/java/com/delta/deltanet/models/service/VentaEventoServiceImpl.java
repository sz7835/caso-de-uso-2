package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.TipoVentaEventoDao;
import com.delta.deltanet.models.dao.VentaEventoDAO;
import com.delta.deltanet.models.entity.TipoVentaEvento;
import com.delta.deltanet.models.entity.VentaEvento;

@Service
public class VentaEventoServiceImpl implements VentaEventoService {

    @Autowired
    private VentaEventoDAO ventaEventoDao;

    @Autowired
    private TipoVentaEventoDao tipoVentaEventoDao;

    @Override
    public VentaEvento buscarPorId(Integer id) {
        return ventaEventoDao.findById(id).orElse(null);
    }

    @Override
    public VentaEvento guardar(VentaEvento ventaEvento) {
        return ventaEventoDao.save(ventaEvento);
    }

    @Override
    public TipoVentaEvento buscarTipoEvento(Integer id) {
        return tipoVentaEventoDao.findById(id).orElse(null);
    }

    @Override
    public List<VentaEvento> buscarTipoVentaEventoConFechas(
            Integer id, String descripcion, Integer estado, Integer tipo, Date fechaInicio, Date fechaFin) {
        return ventaEventoDao.findWithFilters(id, descripcion, estado, tipo, fechaInicio, fechaFin);
    }

    @Override
    public List<TipoVentaEvento> listarTiposVentaEvento() {
        return tipoVentaEventoDao.findAll();
    }
}
