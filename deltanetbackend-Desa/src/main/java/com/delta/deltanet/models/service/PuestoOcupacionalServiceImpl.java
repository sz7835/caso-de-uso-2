package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.PuestoOcupacionalDao;
import com.delta.deltanet.models.entity.PuestoOcupacional;

import java.util.Date;
import java.util.List;

@Service
public class PuestoOcupacionalServiceImpl implements PuestoOcupacionalService {

    @Autowired
    private PuestoOcupacionalDao puestoDao;

    @Override
    public List<PuestoOcupacional> filtrarPuestoOcupacional(String nombrePuesto, Integer estado) {
        return puestoDao.filtrarPuestos(nombrePuesto, estado);
    }

    @Override
    public PuestoOcupacional updateStatus(Long id, String username) {
        PuestoOcupacional puestoServicio = puestoDao.findById(id).orElse(null);
        if (puestoServicio == null) {
            return null;
        }
        if (puestoServicio.getEstado() == 2) {
            puestoServicio.setEstado(1); // Activar
        } else {
            puestoServicio.setEstado(2); // Desactivar
        }
        puestoServicio.setUpdateUser(username);
        puestoServicio.setUpdateDate(new Date());
        return puestoDao.save(puestoServicio);
    }
}
