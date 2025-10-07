package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IRepArchivoFuncionalidadDao;
import com.delta.deltanet.models.entity.RepArchivoFuncionalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepArchivoFuncServiceImpl implements IRepArchivoFuncService{

    @Autowired
    private IRepArchivoFuncionalidadDao archivoFuncionalidadDao;

    @Override
    public List<RepArchivoFuncionalidad> getRepFuncionalidades(Long idSubModulo) {
        return archivoFuncionalidadDao.getRepFuncionalidades(idSubModulo);
    }

    @Override
    public Optional<RepArchivoFuncionalidad> busca(Long idFunc) {
        return archivoFuncionalidadDao.busca(idFunc);
    }

    @Override
    public List<RepArchivoFuncionalidad> findAll() {
        return archivoFuncionalidadDao.findAll();
    }

    @Override
    public RepArchivoFuncionalidad save(RepArchivoFuncionalidad funcionalidad) {
        return archivoFuncionalidadDao.save(funcionalidad);
    }

}
