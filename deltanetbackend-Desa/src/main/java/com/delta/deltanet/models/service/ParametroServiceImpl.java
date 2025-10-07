package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IParametroDao;
import com.delta.deltanet.models.entity.Parametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametroServiceImpl implements IParametroService {
    @Autowired
    private IParametroDao parametroDao;
    @Override
    public List<Parametro> findByParams(Long idRubro) {
        return parametroDao.findByRubro(idRubro);
    }

    @Override
    public Parametro findByParam(Long idRubro, Long idItem) {
        return parametroDao.findByRubro(idRubro, idItem);
    }
}
