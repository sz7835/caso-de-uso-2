package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.VentaEventoEstadoDao;
import com.delta.deltanet.models.entity.VentaEventoEstado;

@Service
public class VentaEventoEstadoServiceImpl implements IVentaEventoEstadoService {
    @Autowired
    private VentaEventoEstadoDao ventaEventoEstadoDao;

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long exceptId) {
        if (descripcion == null) return false;
        String descripcionTrim = descripcion.trim();
        long count = ventaEventoEstadoDao.countByDescripcionActivo(descripcionTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<VentaEventoEstado> findAll() {
        return ventaEventoEstadoDao.findAll();
    }

    @Override
    public VentaEventoEstado findById(Long id) {
        return ventaEventoEstadoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public VentaEventoEstado save(VentaEventoEstado ventaEventoEstado) {
        return ventaEventoEstadoDao.save(ventaEventoEstado);
    }

    @Override
    @Transactional
    public VentaEventoEstado update(VentaEventoEstado ventaEventoEstado) {
        if (!ventaEventoEstadoDao.existsById(ventaEventoEstado.getId())) {
            throw new RuntimeException("No se encontró estado de venta evento con ID: " + ventaEventoEstado.getId());
        }
        return ventaEventoEstadoDao.save(ventaEventoEstado);
    }

    @Override
    @Transactional
    public VentaEventoEstado changeEstado(Long id, Integer nuevoEstado, String username) {
        VentaEventoEstado ventaEventoEstado = ventaEventoEstadoDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró estado de venta evento de postulante con ID: " + id));

        ventaEventoEstado.setUpdateUser(username);
        ventaEventoEstado.setUpdateDate(new Date());
        ventaEventoEstado.setEstado(nuevoEstado);

        return ventaEventoEstadoDao.save(ventaEventoEstado);
    }
}
