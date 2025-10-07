package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import com.delta.deltanet.models.entity.ClienteSolicitud;

public interface ClienteSolicitudService {

    ClienteSolicitud buscarPorId(Integer id);

    ClienteSolicitud guardar(ClienteSolicitud clienteSolicitud);

    List<ClienteSolicitud> buscarClienteSolicitudConFechas(
            Integer id, String descripcion, Date fechaInicio, Date fechaFin, Integer estado);
}
