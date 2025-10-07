package com.delta.deltanet.models.service;

import java.util.List;

public interface IAuthFuncService {
    public List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion, Long idFunc);
    public List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion);
}
