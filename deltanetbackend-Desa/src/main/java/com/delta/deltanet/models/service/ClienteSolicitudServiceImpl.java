package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ClienteSolicitudDao;
import com.delta.deltanet.models.entity.ClienteSolicitud;

@Service
public class ClienteSolicitudServiceImpl implements ClienteSolicitudService {

    @Autowired
    private ClienteSolicitudDao clienteSolicitudDao;

    @Override
    public ClienteSolicitud buscarPorId(Integer id) {
        return clienteSolicitudDao.findById(id).orElse(null);
    }

    @Override
    public ClienteSolicitud guardar(ClienteSolicitud clienteSolicitud) {
        return clienteSolicitudDao.save(clienteSolicitud);
    }

    @Override
    public List<ClienteSolicitud> buscarClienteSolicitudConFechas(
            Integer id, String descripcion, Date fechaInicio, Date fechaFin, Integer estado) {
        return clienteSolicitudDao.findWithFilters(id, descripcion, fechaInicio, fechaFin, estado);
    }
}
