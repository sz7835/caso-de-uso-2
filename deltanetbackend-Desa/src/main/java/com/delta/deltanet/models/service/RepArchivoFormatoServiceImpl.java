package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IRepArchivoFormatoDao;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepArchivoFormatoServiceImpl implements IRepArchivoFormatoService{
    @Override
    public int countByNombreAndEstado(String nombre, int estado) {
        return archivoFormatoDao.countByNombreAndEstado(nombre, estado);
    }

    @Override
    public int countByNombreAndEstadoExcluyendoId(String nombre, int estado, Long id) {
        return archivoFormatoDao.countByNombreAndEstadoExcluyendoId(nombre, estado, id);
    }

    @Autowired
    private IRepArchivoFormatoDao archivoFormatoDao;

    @Override
    public Optional<RepArchivoFormato> buscaExt(String extension) {
        return archivoFormatoDao.buscaExt(extension);
    }

    @Override
    public RepArchivoFormato save(RepArchivoFormato repArchivoFormato) {
        return archivoFormatoDao.save(repArchivoFormato);
    }

    @Override
    public RepArchivoFormato findById(Long idArchivo) {
        return archivoFormatoDao.findById(idArchivo).orElse(null);
    }

    @Override
    public List<RepArchivoFormato> findAll() {
        return archivoFormatoDao.findAll();
    }
}
