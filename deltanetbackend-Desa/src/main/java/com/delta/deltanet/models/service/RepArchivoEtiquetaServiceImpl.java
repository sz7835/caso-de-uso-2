package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IRepArchivoEtiquetaDao;
import com.delta.deltanet.models.entity.RepArchivoEtiqueta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepArchivoEtiquetaServiceImpl implements IRepArchivoEtiquetaService{

    @Autowired
    private IRepArchivoEtiquetaDao archivoEtiquetaDao;


    @Override
    public Optional<RepArchivoEtiqueta> findByNombre(String nombre) {
        return archivoEtiquetaDao.findByNombre(nombre);
    }

    @Override
    public List<RepArchivoEtiqueta> findAll() {
        return archivoEtiquetaDao.findAll();
    }

    @Override
    public Optional<RepArchivoEtiqueta> getById(Long id) {
        return archivoEtiquetaDao.findById(id);
    }

    @Override
    public RepArchivoEtiqueta save(RepArchivoEtiqueta etiqueta) {
        return archivoEtiquetaDao.save(etiqueta);
    }

}
