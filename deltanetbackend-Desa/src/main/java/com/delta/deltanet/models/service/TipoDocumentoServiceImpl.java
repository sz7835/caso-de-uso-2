package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoDocumentoDao;
import com.delta.deltanet.models.entity.TipoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class TipoDocumentoServiceImpl implements ITipoDocumentoService {

    @Autowired
    private ITipoDocumentoDao tipoDocumentoDao;

    @Override
    public List<TipoDocumento> findAll() {
        return tipoDocumentoDao.findAll();
    }

    @Override
    public TipoDocumento buscaTipDoc(Long idTipoDoc) {
        return tipoDocumentoDao.buscaTipoDoc(idTipoDoc);
    }

    @Override
    public Integer buscarLongitud(Long idTipoDoc) {
        return tipoDocumentoDao.findLongitudByIdTipDoc(idTipoDoc);
    }
}
