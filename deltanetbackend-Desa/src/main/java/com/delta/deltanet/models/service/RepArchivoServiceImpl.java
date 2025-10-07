package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IRepArchivoDao;
import com.delta.deltanet.models.entity.RepArchivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepArchivoServiceImpl implements IRepArchivoService{

    @Autowired
    public IRepArchivoDao iRepArchivoDao;

    @Override
    public List<RepArchivo> getRepArchivos(Long idContrato) {
        return iRepArchivoDao.getRepArchivos(idContrato);
    }

    @Override
    public List<RepArchivo> getRepArchivos(Long idContrato, String table) {
        return iRepArchivoDao.getRepArchivos(idContrato, table);
    }

    @Override
    public RepArchivo save(RepArchivo repArchivo) {
        return iRepArchivoDao.save(repArchivo);
    }

    @Override
    public void delete(Long idArchivo) {
        iRepArchivoDao.deleteById(idArchivo);
    }

    @Override
    public Optional<RepArchivo> getArchivo(Long idArchivo) {
        return iRepArchivoDao.getArchivo(idArchivo);
    }


}
