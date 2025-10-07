package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.ServicioEjecutor;

import java.util.List;

public interface IServicioEjecutorService {
    public ServicioEjecutor buscaServicioEjecutor(Long id_area);
    public ServicioEjecutor findStatus(Long id_user);
    public List<ServicioEjecutor> buscaEjecutores(Long id_user, Integer estado);
    public List<ServicioEjecutor> buscaEjecutores();
}
