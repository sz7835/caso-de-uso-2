package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.ITipoRelacionDao;
import com.delta.deltanet.models.entity.TipoRelacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoRelacionServiceImpl implements ITipoRelacionService {

    @Autowired
    private ITipoRelacionDao tipoRelacionDao;

    @Override
    public List<TipoRelacion> Lista() {
        return tipoRelacionDao.Lista();
    }

    @Override
    public List<TipoRelacion> findAll() {
        return tipoRelacionDao.findAll();
    }

    @Override
    public List<Object> listaRelaciones() {
        return tipoRelacionDao.listaRelaciones();
    }

    @Override
    public TipoRelacion buscaId(Long idTipo) {
        return tipoRelacionDao.buscaId(idTipo);
    }

	@Override
	public List<TipoRelacion> findByTipo(List<Integer> tipos, Long destino) {
		return tipoRelacionDao.findByTipo(tipos, destino);
	}

    @Override
    public List<Long> findIdsByNodoTipoOrigenAndTipoIn(Long nodoTipoOrigenId, List<Integer> tipos) {
        return tipoRelacionDao.findIdsByNodoTipoOrigenAndTipoIn(nodoTipoOrigenId, tipos);
    }

    @Override
    public TipoRelacion save(TipoRelacion tipoRelacion) {
        return tipoRelacionDao.save(tipoRelacion);
    }

    @Override
    public List<TipoRelacion> findByDescripAndOrigenAndDestinoAndTipo(String nombre, Long origen, Long destino, Integer tipo) {
        return tipoRelacionDao.findByDescripAndOrigenAndDestinoAndTipo(nombre, origen, destino, tipo);
    }
}
