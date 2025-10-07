package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.PuestoOcupacionalDao;
import com.delta.deltanet.models.entity.PuestoOcupacional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuestoOcupacionalEspecialServiceImpl implements PuestoOcupacionalEspecialService {
    @Autowired
    private PuestoOcupacionalDao puestoDao;

    @Override
    public boolean existsNombreUnidadOrganicaActivo(String nombre, String unidadOrganica, Long exceptId) {
        if (nombre == null || unidadOrganica == null) return false;
        String nombreTrim = nombre.trim();
        String unidadTrim = unidadOrganica.trim();
        PuestoOcupacional existente;
        if (exceptId != null) {
            existente = puestoDao.findByNombrePuestoIgnoreCaseAndUnidadOrganicaIgnoreCaseAndEstadoAndIdNot(nombreTrim, unidadTrim, 1, exceptId);
        } else {
            existente = puestoDao.findByNombrePuestoIgnoreCaseAndUnidadOrganicaIgnoreCaseAndEstado(nombreTrim, unidadTrim, 1);
        }
        return existente != null;
    }

    @Override
    public List<PuestoOcupacional> findAll() {
        return puestoDao.findAll();
    }

    @Override
    public PuestoOcupacional findById(Long id) {
        return puestoDao.findById(id).orElse(null);
    }

    @Override
    public PuestoOcupacional save(PuestoOcupacional puesto) {
        return puestoDao.save(puesto);
    }
}
